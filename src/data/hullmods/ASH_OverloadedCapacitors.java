package data.hullmods;

import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;

public class ASH_OverloadedCapacitors extends BaseHullMod {
    public static final float ENERGY_DAMAGE_MODIFIER = 25f;
    public static final float ENERGY_FLUX_COST_MODIFIER = 1.25f;
    public static final float ENERGY_FIRE_RATE_MODIFIER = 0.75f;

    @Override
    public void applyEffectsBeforeShipCreation(ShipAPI.HullSize hullSize, MutableShipStatsAPI stats, String id) {
        stats.getEnergyWeaponDamageMult().modifyPercent(id, ENERGY_DAMAGE_MODIFIER);
        stats.getEnergyWeaponFluxCostMod().modifyMult(id, ENERGY_FLUX_COST_MODIFIER);
        stats.getEnergyRoFMult().modifyMult(id, ENERGY_FIRE_RATE_MODIFIER);
    }

    @Override
    public String getDescriptionParam(int index, HullSize hullSize) {
        if (index == 0)
            return Math.round(ENERGY_DAMAGE_MODIFIER) + "%";
        if (index == 1)
            return ENERGY_FIRE_RATE_MODIFIER + "";
        if (index == 2)
            return ENERGY_FLUX_COST_MODIFIER + "";
        return null;
    }
}