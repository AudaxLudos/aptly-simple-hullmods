package data.hullmods;

import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;

public class ASH_MaximizedDriveField extends BaseHullMod {
    public static final float MAX_BURN_LEVEL_MODIFIER = 3f;
    public static final float FUEL_USE_MODIFIER = 2f;

    @Override
    public void applyEffectsBeforeShipCreation(ShipAPI.HullSize hullSize, MutableShipStatsAPI stats, String id) {
        stats.getMaxBurnLevel().modifyFlat(id, MAX_BURN_LEVEL_MODIFIER);
        stats.getFuelUseMod().modifyMult(id, FUEL_USE_MODIFIER);
    }

    @Override
    public String getDescriptionParam(int index, HullSize hullSize) {
        if (index == 0)
            return Math.round(MAX_BURN_LEVEL_MODIFIER) + "";
        if (index == 1)
            return Math.round(FUEL_USE_MODIFIER) + "";
        return null;
    }
}