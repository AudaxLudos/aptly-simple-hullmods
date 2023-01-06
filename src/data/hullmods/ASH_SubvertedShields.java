package data.hullmods;

import java.awt.Color;

import org.lwjgl.input.Keyboard;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.impl.campaign.ids.Stats;
import com.fs.starfarer.api.ui.Alignment;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;

import data.ASH_Utils;

public class ASH_SubvertedShields extends BaseHullMod {
    public static final float HARD_FLUX_DISSIPATION_MODIFIER = 0.15f;
    public static final float SHIELD_UPKEEP_MULTIPLIER = 0.30f;
    public static final float SHIELD_STRENGTH_MULTIPLIER = 0.30f;
    public static final float SHIELD_PIERCED_MULTIPLIER = 0.50f;

    @Override
    public void applyEffectsBeforeShipCreation(ShipAPI.HullSize hullSize, MutableShipStatsAPI stats, String id) {
        stats.getShieldDamageTakenMult().modifyMult(id, 1f + SHIELD_STRENGTH_MULTIPLIER);
        stats.getShieldUpkeepMult().modifyMult(id, 1f + -SHIELD_UPKEEP_MULTIPLIER);

        stats.getDynamic().getStat(Stats.SHIELD_PIERCED_MULT).modifyMult(id, 1f + SHIELD_PIERCED_MULTIPLIER);

        if (stats.getVariant().getSMods().contains(id) && ASH_Utils.isModEnabled())
            stats.getDynamic().getStat(Stats.SHIELD_PIERCED_MULT).unmodifyMult(id);
    }

    @Override
    public void advanceInCombat(ShipAPI ship, float amount) {
        if (!ship.isAlive())
            return;

        MutableShipStatsAPI stats = ship.getMutableStats();
        Color shieldInnerColor = ship.getShield().getInnerColor();
        float shieldInnerAlpha = 0f;
        float shieldPulse = 0f;

        if (Global.getCombatEngine().getCustomData().get("ASH_ShieldPulse_" + ship.getId()) instanceof Float)
            shieldPulse = (float) Global.getCombatEngine().getCustomData().get("ASH_ShieldPulse_" + ship.getId());

        shieldPulse += amount / 2f;
        float pulse = (float) (Math.sin(2f * 3.14f * shieldPulse) * 0.20f + 0.80f);
        shieldInnerAlpha = pulse * ship.getHullSpec().getShieldSpec().getInnerColor().getAlpha();

        ship.getShield().setInnerColor(new Color(
                shieldInnerColor.getRed(),
                shieldInnerColor.getGreen(),
                shieldInnerColor.getBlue(),
                Math.round(shieldInnerAlpha)));
        stats.getHardFluxDissipationFraction().modifyFlat(spec.getId(), HARD_FLUX_DISSIPATION_MODIFIER);

        Global.getCombatEngine().getCustomData().put("ASH_ShieldPulse_" + ship.getId(), shieldPulse);
    }

    @Override
    public void addPostDescriptionSection(TooltipMakerAPI tooltip, HullSize hullSize, ShipAPI ship, float width, boolean isForModSpec) {
        float pad = 3f;
        float opad = 10f;
        Color good = Misc.getPositiveHighlightColor();
        Color bad = Misc.getNegativeHighlightColor();
        Color story = Misc.getStoryOptionColor();

        if (ship == null || !ship.getVariant().getSMods().contains(spec.getId()) || !ASH_Utils.isModEnabled()) {
            tooltip.addSectionHeading("Effects:", Alignment.MID, opad);
            tooltip.setBulletedListMode(" - ");
            tooltip.addPara("Dissipates hard flux while shields are active by %s", opad, good, Math.round(HARD_FLUX_DISSIPATION_MODIFIER * 100f) + "%");
            tooltip.addPara("Reduces the shield upkeep by %s", pad, good, Math.round(SHIELD_UPKEEP_MULTIPLIER * 100f) + "%");
            tooltip.addPara("Reduces the shield strength by %s", pad, bad, Math.round(SHIELD_STRENGTH_MULTIPLIER * 100f) + "%");
            tooltip.addPara("Increases the chance of EMP arcing by %s", pad, bad, Math.round(SHIELD_PIERCED_MULTIPLIER * 100f) + "%");
            tooltip.setBulletedListMode(null);

            if (!ASH_Utils.isModEnabled())
                return;

            if (ship == null || !Keyboard.isKeyDown(Keyboard.getKeyIndex("F1"))) {
                tooltip.addPara("Press F1 to show S-mod effects", Misc.getGrayColor(), opad);
                return;
            }
        }

        tooltip.addSectionHeading("S-Mod Effects:", story, Misc.setAlpha(story, 110), Alignment.MID, opad);
        tooltip.setBulletedListMode(" - ");
        tooltip.addPara("Dissipates hard flux while shields are active by %s", opad, good, Math.round(HARD_FLUX_DISSIPATION_MODIFIER * 100f) + "%");
        tooltip.addPara("Reduces the shield upkeep by %s", pad, good, Math.round(SHIELD_UPKEEP_MULTIPLIER * 100f) + "%");
        tooltip.addPara("Reduces the shield strength by %s", pad, bad, Math.round(SHIELD_STRENGTH_MULTIPLIER * 100f) + "%");
        tooltip.setBulletedListMode(null);
    }

    @Override
    public String getUnapplicableReason(ShipAPI ship) {
        if (ship == null || ship.getShield() == null)
            return "Ship has no shields";
        return null;
    }

    @Override
    public boolean isApplicableToShip(ShipAPI ship) {
        return ship != null && ship.getShield() != null;
    }
}