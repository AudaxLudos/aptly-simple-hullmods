package data.hullmods;

import java.awt.Color;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.ui.Alignment;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;

import data.ASH_Utils;

public class ASH_ReactiveShields extends BaseHullMod {
    public static final float SHIELD_STRENGTH_MULTIPLIER = 0.15f;
    public static final float MINIMUM_SHIELD_ARC = 30F;
    public static final Color CUSTOM_SHIELD_COLOR = new Color(255, 100, 255, 75);

    @Override
    public void applyEffectsAfterShipCreation(ShipAPI ship, String id) {
        if (!(Global.getCombatEngine().getCustomData().get("ASH_ShieldArc_" + id) instanceof Float))
            Global.getCombatEngine().getCustomData().put("ASH_ShieldArc_" + id, ship.getShield().getArc());
    }

    @Override
    public void advanceInCombat(ShipAPI ship, float amount) {
        if (!ship.isAlive())
            return;

        MutableShipStatsAPI stats = ship.getMutableStats();
        float shipShieldArc = ship.getHullSpec().getShieldSpec().getArc();
        Color defaultShieldColor = ship.getHullSpec().getShieldSpec().getInnerColor();

        if (Global.getCombatEngine().getCustomData().get("ASH_ShieldArc_" + spec.getId()) instanceof Float)
            shipShieldArc = (float) Global.getCombatEngine().getCustomData().get("ASH_ShieldArc_" + spec.getId());

        float selectedFluxLevel = stats.getVariant().getSMods().contains(spec.getId()) ? ship.getFluxLevel() : ship.getHardFluxLevel();
        float computedShieldStrength = ASH_Utils.getValueWithinMax((SHIELD_STRENGTH_MULTIPLIER * 0.30f + SHIELD_STRENGTH_MULTIPLIER) * selectedFluxLevel, 0, SHIELD_STRENGTH_MULTIPLIER);
        float minShieldArc = (shipShieldArc <= MINIMUM_SHIELD_ARC) ? shipShieldArc : MINIMUM_SHIELD_ARC;
        float computedShieldArc = ASH_Utils.getValueWithinRange(1 - ship.getHardFluxLevel(), minShieldArc, shipShieldArc);

        if (ship.getSystem().getId().equals("fortressshield") && ship.getSystem().isActive())
            defaultShieldColor = ship.getShield().getInnerColor();

        int red = (int) Math.min(Math.abs((ship.getFluxLevel() * CUSTOM_SHIELD_COLOR.getRed()) + ((1 - ship.getFluxLevel()) * defaultShieldColor.getRed())), 255);
        int green = (int) Math.min(Math.abs((ship.getFluxLevel() * CUSTOM_SHIELD_COLOR.getGreen()) + ((1 - ship.getFluxLevel()) * defaultShieldColor.getGreen())), 255);
        int blue = (int) Math.min(Math.abs((ship.getFluxLevel() * CUSTOM_SHIELD_COLOR.getBlue()) + ((1 - ship.getFluxLevel()) * defaultShieldColor.getBlue())), 255);

        ship.getShield().setInnerColor(new Color(red, green, blue, ship.getShield().getInnerColor().getAlpha()));
        ship.getShield().setArc(computedShieldArc);
        stats.getShieldDamageTakenMult().modifyMult(spec.getId(), 1 + -computedShieldStrength);

        if (ship == Global.getCombatEngine().getPlayerShip())
            Global.getCombatEngine().maintainStatusForPlayerShip("ASH_ReactiveShields", "graphics/icons/hullsys/fortress_shield.png", "Shield Strength Boost",
                    Math.round(computedShieldStrength * 100f) + "%", false);
    }

    @Override
    public void addPostDescriptionSection(TooltipMakerAPI tooltip, HullSize hullSize, ShipAPI ship, float width, boolean isForModSpec) {
        float pad = 3f;
        float opad = 10f;
        Color b = Misc.getHighlightColor();
        Color bad = Misc.getNegativeHighlightColor();
        Color good = Misc.getPositiveHighlightColor();

        tooltip.addSectionHeading("Effects:", Alignment.MID, opad);
        tooltip.setBulletedListMode("");
        tooltip.addPara("As %s flux levels rise:", opad, b, "hard");
        tooltip.setBulletedListMode(" ^ ");
        tooltip.addPara("Increases the shield strength by up to %s", pad, good, Math.round(SHIELD_STRENGTH_MULTIPLIER * 100f) + "%");
        tooltip.addPara("Lowers the shield arc down to %s", pad, bad, "30 degrees");
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
        float opad = 10.0F;

        tooltip.setBulletedListMode(" - ");
        tooltip.addPara("The increase in shield strength scales with soft flux", opad);
        tooltip.setBulletedListMode(null);
    }

    @Override
    public boolean hasSModEffect() {
        return true;
    }
}