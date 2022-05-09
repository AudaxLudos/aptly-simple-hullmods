package data.hullmods;

import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;

public class ASH_TestHullmod extends BaseHullMod {
    public static final float ENERGY_FIRE_RATE_MODIFIER = 20f;
    public static final float ENERGY_AMMO_REGEN_MODIFIER = 20f;
    public static final float ENERGY_AMMO_CAPACITY_MODIFIER = 20f;
    public static final float ENERGY_FLUX_COST_MODIFIER = 10f;
    public static final float ENERGY_DAMAGE_MODIFIER = 10f;
    
    @Override
    public void applyEffectsBeforeShipCreation(ShipAPI.HullSize hullSize, MutableShipStatsAPI stats, String id) {
        stats.getEnergyRoFMult().modifyPercent(id, ENERGY_FIRE_RATE_MODIFIER);
        stats.getEnergyAmmoRegenMult().modifyPercent(id, ENERGY_AMMO_REGEN_MODIFIER);
        stats.getEnergyAmmoBonus().modifyPercent(id, ENERGY_AMMO_CAPACITY_MODIFIER);
        stats.getEnergyWeaponFluxCostMod().modifyPercent(id, ENERGY_FLUX_COST_MODIFIER);
        stats.getEnergyWeaponDamageMult().modifyPercent(id, -ENERGY_DAMAGE_MODIFIER);
    }

    @Override
    public String getDescriptionParam(int index, HullSize hullSize) {
        if (index == 0)
            return Math.round(ENERGY_FIRE_RATE_MODIFIER) + "%";
        if (index == 1)
            return Math.round(ENERGY_DAMAGE_MODIFIER) + "%";
        return null;
    }
}