package data.hullmods;

import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;

public class ASH_EnhancedAutoloaders extends BaseHullMod {
    public static final float BALLISTIC_STATS_MODIFIER = 100f;

    @Override
    public void applyEffectsBeforeShipCreation(ShipAPI.HullSize hullSize, MutableShipStatsAPI stats, String id) {
        stats.getBallisticRoFMult().modifyPercent(id, BALLISTIC_STATS_MODIFIER);
        stats.getBallisticAmmoRegenMult().modifyPercent(id, BALLISTIC_STATS_MODIFIER);
        stats.getBallisticAmmoBonus().modifyPercent(id, BALLISTIC_STATS_MODIFIER);
    }

    @Override
    public String getDescriptionParam(int index, HullSize hullSize) {
        if (index == 0)
            return Math.round(BALLISTIC_STATS_MODIFIER) + "%";
        return null;
    }
}