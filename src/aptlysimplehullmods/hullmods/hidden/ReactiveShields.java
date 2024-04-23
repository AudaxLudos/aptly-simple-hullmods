package aptlysimplehullmods.hullmods.hidden;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShieldAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;

import java.awt.*;

public class ReactiveShields extends BaseHullMod {
    public static final float SHIELD_STRENGTH_MULT = 0.15f;
    public static final float SHIELD_ARC_MOD = 60f;
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
        if (ship.getShield() == null)
            return;

        MutableShipStatsAPI stats = ship.getMutableStats();
        float shipShieldArc = ship.getHullSpec().getShieldSpec().getArc();
        Color defaultShieldColor = ship.getHullSpec().getShieldSpec().getInnerColor();

        if (Global.getCombatEngine().getCustomData().get("ASH_ShieldArc_" + spec.getId()) instanceof Float)
            shipShieldArc = (float) Global.getCombatEngine().getCustomData().get("ASH_ShieldArc_" + spec.getId());

        float selectedFluxLevel = isSMod(stats) ? ship.getFluxLevel() : ship.getHardFluxLevel();
        float computedShieldStrength = getValueWithinMax((SHIELD_STRENGTH_MULT * 0.30f + SHIELD_STRENGTH_MULT) * selectedFluxLevel, 0, SHIELD_STRENGTH_MULT);
        float computedShieldArc = getValueWithinRange(ship.getHardFluxLevel(), 0, SHIELD_ARC_MOD);

        if (ship.getSystem() != null && ship.getSystem().getId().equals("fortressshield") && ship.getSystem().isActive())
            defaultShieldColor = ship.getShield().getInnerColor();

        int red = (int) Math.min(Math.abs((ship.getFluxLevel() * CUSTOM_SHIELD_COLOR.getRed()) + ((1 - ship.getFluxLevel()) * defaultShieldColor.getRed())), 255);
        int green = (int) Math.min(Math.abs((ship.getFluxLevel() * CUSTOM_SHIELD_COLOR.getGreen()) + ((1 - ship.getFluxLevel()) * defaultShieldColor.getGreen())), 255);
        int blue = (int) Math.min(Math.abs((ship.getFluxLevel() * CUSTOM_SHIELD_COLOR.getBlue()) + ((1 - ship.getFluxLevel()) * defaultShieldColor.getBlue())), 255);

        ShieldAPI shield = ship.getShield();
        shield.setInnerColor(new Color(red, green, blue, ship.getShield().getInnerColor().getAlpha()));
        if (shield.getArc() > 30)
            shield.setArc(shipShieldArc - computedShieldArc);
        else
            shield.setArc(30);
        stats.getShieldDamageTakenMult().modifyMult(spec.getId(), 1 - computedShieldStrength);

        if (ship == Global.getCombatEngine().getPlayerShip() && computedShieldStrength * 100f >= 1f) {
            Global.getCombatEngine().maintainStatusForPlayerShip("ASH_ReactiveShields", "graphics/icons/hullsys/fortress_shield.png", "Shield Strength Boost",
                    Math.round(computedShieldStrength * 100f) + "%", false);
        }
    }

    @Override
    public void addPostDescriptionSection(TooltipMakerAPI tooltip, HullSize hullSize, ShipAPI ship, float width, boolean isForModSpec) {
        float pad = 3f;
        float oPad = 10f;
        Color b = Misc.getHighlightColor();
        Color bad = Misc.getNegativeHighlightColor();
        Color good = Misc.getPositiveHighlightColor();

        tooltip.setBulletedListMode("");
        tooltip.addPara("As %s flux levels rise:", oPad, b, "hard");
        tooltip.setBulletedListMode(" ^ ");
        tooltip.addPara("Increases the ship's shield strength up to %s", pad, good, Math.round(SHIELD_STRENGTH_MULT * 100f) + "%");
        tooltip.addPara("Decreases the ship's shield arc by up to %s", pad, bad, Math.round(SHIELD_ARC_MOD) + " degrees");
        tooltip.setBulletedListMode(" - ");
        tooltip.addPara("The ship's shield arc can never go below %s", oPad, b, Math.round(SHIELD_ARC_MOD * 0.5f) + " degrees");
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
        tooltip.addPara("The increase in shield strength scales with soft flux", oPad);
        tooltip.setBulletedListMode(null);
    }

    @Override
    public boolean hasSModEffect() {
        return true;
    }

    public float getValueWithinMax(float value, float min, float max) {
        return Math.min(Math.max(value, min), max);
    }

    public float getValueWithinRange(float percentage, float min, float max) {
        return percentage * (max - min) + min;
    }
}