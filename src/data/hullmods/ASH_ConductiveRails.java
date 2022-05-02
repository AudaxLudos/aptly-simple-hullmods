package data.hullmods;

import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;

import data.ASH_Utils;

public class ASH_ConductiveRails extends BaseHullMod {
    public final float BALLISTIC_STATS_MODIFIER = 20f;
    public final float BALLISTIC_COST_MODIFIER = 50f;

    @Override
    public String getDescriptionParam(int index, HullSize hullSize) {
        if (index == 0)
            return (int) BALLISTIC_STATS_MODIFIER + "%";
        if (index == 1)
            return (int) BALLISTIC_COST_MODIFIER + "%";
        if (index == 2)
            return 20 + "%";
        return null;
    }

    @Override
    public void advanceInCombat(ShipAPI ship, float amount) {
        if (!ship.isAlive())
            return;

        MutableShipStatsAPI stats = ship.getMutableStats();
        float shipRemainingFlux = 1 - ship.getFluxLevel();
        float computedBallisticStats = ASH_Utils.getValueWithinMax((BALLISTIC_STATS_MODIFIER * 0.20f + BALLISTIC_STATS_MODIFIER) * shipRemainingFlux, 0f, BALLISTIC_STATS_MODIFIER);
        float computedBallisticCost = ASH_Utils.getValueWithinMax((BALLISTIC_COST_MODIFIER * 0.20f + BALLISTIC_COST_MODIFIER) * shipRemainingFlux, 0f, BALLISTIC_COST_MODIFIER);

        stats.getBallisticWeaponDamageMult().modifyPercent(spec.getId(), computedBallisticStats);
        stats.getBallisticWeaponRangeBonus().modifyPercent(spec.getId(), computedBallisticStats);
        stats.getBallisticProjectileSpeedMult().modifyPercent(spec.getId(), computedBallisticStats);
        stats.getBallisticWeaponFluxCostMod().modifyPercent(spec.getId(), computedBallisticCost);
    }
}