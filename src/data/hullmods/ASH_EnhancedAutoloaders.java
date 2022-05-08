package data.hullmods;

import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;

public class ASH_EnhancedAutoloaders extends BaseHullMod {
    public static final float BALLISTIC_STATS_MODIFIER = 20f;
    public static final float BALLISTIC_FLUX_COST_MODIFIER = 20f;

    @Override
    public void applyEffectsBeforeShipCreation(ShipAPI.HullSize hullSize, MutableShipStatsAPI stats, String id) {
        stats.getBallisticRoFMult().modifyPercent(id, BALLISTIC_STATS_MODIFIER);
        stats.getBallisticAmmoRegenMult().modifyPercent(id, BALLISTIC_STATS_MODIFIER);
        stats.getBallisticAmmoBonus().modifyPercent(id, BALLISTIC_STATS_MODIFIER);
        stats.getBallisticWeaponFluxCostMod().modifyPercent(id, BALLISTIC_FLUX_COST_MODIFIER);
    }

    @Override
    public String getDescriptionParam(int index, HullSize hullSize) {
        if (index == 0)
            return Math.round(BALLISTIC_STATS_MODIFIER) + "%";
        if (index == 1)
            return Math.round(BALLISTIC_FLUX_COST_MODIFIER) + "%";
        return null;
    }
}