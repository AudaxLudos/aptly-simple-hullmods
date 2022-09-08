package data.hullmods;

import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.impl.hullmods.BaseLogisticsHullMod;

public class ASH_OverloadedDriveField extends BaseLogisticsHullMod {
    public static final float MAX_BURN_LEVEL_MODIFIER = 4f;
    public static final float FUEL_USE_MODIFIER = 2f;
    public static final float SENSOR_PROFILE_MODIFIER = 100f;
    public static final float SENSOR_STRENGTH_MODIFIER = 50f;

    @Override
    public void applyEffectsBeforeShipCreation(ShipAPI.HullSize hullSize, MutableShipStatsAPI stats, String id) {
        stats.getMaxBurnLevel().modifyFlat(id, MAX_BURN_LEVEL_MODIFIER);
        stats.getFuelUseMod().modifyMult(id, FUEL_USE_MODIFIER);
        stats.getSensorProfile().modifyPercent(id, SENSOR_PROFILE_MODIFIER);
        stats.getSensorStrength().modifyPercent(id, -SENSOR_STRENGTH_MODIFIER);
    }

    @Override
    public String getDescriptionParam(int index, HullSize hullSize) {
        if (index == 0)
            return Math.round(MAX_BURN_LEVEL_MODIFIER) + "";
        if (index == 1)
            return Math.round(FUEL_USE_MODIFIER) + "";
        if (index == 2)
            return Math.round(SENSOR_PROFILE_MODIFIER) + "%";
        if (index == 3)
            return Math.round(SENSOR_STRENGTH_MODIFIER) + "%";
        return null;
    }
}