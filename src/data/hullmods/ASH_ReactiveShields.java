package data.hullmods;

import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;

import data.ASH_Utils;

public class ASH_ReactiveShields extends BaseHullMod {
    public static final float SHIELD_STRENGTH_MODIFIER = 15f;
    public static final float MINIMUM_SHIELD_ARC = 30F;
    public float shipShieldArc;

    @Override
    public void applyEffectsAfterShipCreation(ShipAPI ship, String id) {
        shipShieldArc = ship.getShield().getArc();
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
    public String getDescriptionParam(int index, HullSize hullSize) {
        if (index == 0)
            return Math.round(SHIELD_STRENGTH_MODIFIER) + "%";
        if (index == 1)
            return Math.round(80f) + "%";
        if (index == 2)
            return Math.round(MINIMUM_SHIELD_ARC) + " degrees";
        if (index == 3)
            return Math.round(80f) + "%";
        return null;
    }

    @Override
    public void advanceInCombat(ShipAPI ship, float amount) {
        if (!ship.isAlive())
            return;

        MutableShipStatsAPI stats = ship.getMutableStats();

        float computedShieldStrength = ASH_Utils.getValueWithinMax(SHIELD_STRENGTH_MODIFIER * (ship.getHardFluxLevel() * 0.30f + ship.getHardFluxLevel()), 0, SHIELD_STRENGTH_MODIFIER);
        float computedShieldArc = ASH_Utils.getValueWithinRange(1 - ship.getHardFluxLevel(), MINIMUM_SHIELD_ARC, shipShieldArc);

        stats.getShieldAbsorptionMult().modifyPercent(spec.getId(), -computedShieldStrength);
        ship.getShield().setArc(computedShieldArc);
    }
}