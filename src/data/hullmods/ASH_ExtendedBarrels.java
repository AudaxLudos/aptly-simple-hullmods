package data.hullmods;

import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;

public class ASH_ExtendedBarrels extends BaseHullMod {
    public static final float BALLISTIC_BULLET_SPEED_MODIFIER = 20f;
    
    @Override
    public void applyEffectsBeforeShipCreation(ShipAPI.HullSize hullSize, MutableShipStatsAPI stats, String id) {
        stats.getBallisticProjectileSpeedMult().modifyPercent(id, BALLISTIC_BULLET_SPEED_MODIFIER);
    }

    @Override
    public String getDescriptionParam(int index, HullSize hullSize) {
        if (index == 0)
            return Math.round(BALLISTIC_BULLET_SPEED_MODIFIER) + "%";
        return null;
    }
}