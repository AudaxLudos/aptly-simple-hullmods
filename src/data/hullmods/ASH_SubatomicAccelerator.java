package data.hullmods;

import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;

public class ASH_SubatomicAccelerator extends BaseHullMod {
    public static final float ENERGY_BULLET_SPEED_MODIFIER = 20f;

    @Override
    public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {
        stats.getEnergyProjectileSpeedMult().modifyPercent(id, ENERGY_BULLET_SPEED_MODIFIER);
    }

    @Override
    public String getDescriptionParam(int index, HullSize hullSize) {
        if (index == 0)
            return Math.round(ENERGY_BULLET_SPEED_MODIFIER) + "%";
        return null;
    }
}