package data.hullmods;

import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;

public class ASH_EnergizedCapacitors extends BaseHullMod {
    public static final float ENERGY_STATS_MODIFIER = 100f;
    public static final float ENERGY_DAMAGE_MODIFIER = 25f;
    
    @Override
    public void applyEffectsBeforeShipCreation(ShipAPI.HullSize hullSize, MutableShipStatsAPI stats, String id) {
        stats.getEnergyRoFMult().modifyPercent(id, ENERGY_STATS_MODIFIER);
        stats.getEnergyAmmoRegenMult().modifyPercent(id, ENERGY_STATS_MODIFIER);
        stats.getEnergyAmmoBonus().modifyPercent(id, ENERGY_STATS_MODIFIER);
        stats.getEnergyWeaponDamageMult().modifyPercent(id, -ENERGY_DAMAGE_MODIFIER);
    }

    @Override
    public String getDescriptionParam(int index, HullSize hullSize) {
        if (index == 0)
            return Math.round(ENERGY_STATS_MODIFIER) + "%";
        if (index == 1)
            return Math.round(ENERGY_DAMAGE_MODIFIER) + "%";
        return null;
    }
}