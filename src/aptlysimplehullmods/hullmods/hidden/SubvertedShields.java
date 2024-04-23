package aptlysimplehullmods.hullmods.hidden;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.impl.campaign.ids.Stats;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;

import java.awt.*;

public class SubvertedShields extends BaseHullMod {
    public static final float HARD_FLUX_DISSIPATION_MOD = 0.15f;
    public static final float SHIELD_UPKEEP_MULT = 0.15f;
    public static final float SHIELD_STRENGTH_MULT = 0.15f;
    public static final float SHIELD_PIERCED_MULT = 0.25f;

    @Override
    public void applyEffectsBeforeShipCreation(ShipAPI.HullSize hullSize, MutableShipStatsAPI stats, String id) {
        stats.getShieldDamageTakenMult().modifyMult(id, 1f + SHIELD_STRENGTH_MULT);
        stats.getShieldUpkeepMult().modifyMult(id, 1f - SHIELD_UPKEEP_MULT);
        stats.getDynamic().getStat(Stats.SHIELD_PIERCED_MULT).modifyMult(id, 1f + SHIELD_PIERCED_MULT);

        if (isSMod(stats))
            stats.getDynamic().getStat(Stats.SHIELD_PIERCED_MULT).unmodifyMult(id);
    }

    @Override
    public void advanceInCombat(ShipAPI ship, float amount) {
        if (!ship.isAlive())
            return;

        MutableShipStatsAPI stats = ship.getMutableStats();
        Color shieldInnerColor = ship.getShield().getInnerColor();
        float shieldInnerAlpha;
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
        stats.getHardFluxDissipationFraction().modifyFlat(spec.getId(), HARD_FLUX_DISSIPATION_MOD);

        Global.getCombatEngine().getCustomData().put("ASH_ShieldPulse_" + ship.getId(), shieldPulse);
    }

    @Override
    public void addPostDescriptionSection(TooltipMakerAPI tooltip, HullSize hullSize, ShipAPI ship, float width, boolean isForModSpec) {
        float pad = 3f;
        float oPad = 10f;
        Color good = Misc.getPositiveHighlightColor();
        Color bad = Misc.getNegativeHighlightColor();

        tooltip.setBulletedListMode(" - ");
        tooltip.addPara("Dissipates hard flux while shields are active by %s", oPad, good, Math.round(HARD_FLUX_DISSIPATION_MOD * 100f) + "%");
        tooltip.addPara("Reduces the shield's upkeep by %s", pad, good, Math.round(SHIELD_UPKEEP_MULT * 100f) + "%");
        tooltip.addPara("Reduces the shield's strength by %s", pad, bad, Math.round(SHIELD_STRENGTH_MULT * 100f) + "%");
        tooltip.addPara("Increases the chance of EMP arcing by %s", pad, bad, Math.round(SHIELD_PIERCED_MULT * 100f) + "%");
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

    @Override
    public void addSModEffectSection(TooltipMakerAPI tooltip, ShipAPI.HullSize hullSize, ShipAPI ship, float width, boolean isForModSpec, boolean isForBuildInList) {
        float oPad = 10f;

        tooltip.setBulletedListMode(" - ");
        tooltip.addPara("Fully negates the emp arcing penalty", oPad);
        tooltip.setBulletedListMode(null);
    }

    @Override
    public boolean hasSModEffect() {
        return true;
    }
}