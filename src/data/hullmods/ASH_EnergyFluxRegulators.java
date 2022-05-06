package data.hullmods;

import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;

import data.ASH_Utils;

public class ASH_EnergyFluxRegulators extends BaseHullMod {
    public static final float ENERGY_COST_MODIFIER = 15f;
    public static final float ENERGY_DMG_MODIFIER = 15f;

    @Override
    public String getDescriptionParam(int index, HullSize hullSize) {
        if (index == 0)
            return "+" + Math.round(ENERGY_DMG_MODIFIER) + "%";
        if (index == 1)
            return "-" + Math.round(ENERGY_COST_MODIFIER) + "%";
        return null;
    }

    @Override
    public void advanceInCombat(ShipAPI ship, float amount) {
        if (!ship.isAlive())
            return;

        MutableShipStatsAPI stats = ship.getMutableStats();

        float computedEnergyDmg = ASH_Utils.getValueWithinRange(1 - ship.getFluxLevel(), -ENERGY_DMG_MODIFIER, ENERGY_DMG_MODIFIER);
        float computedEnergyCost = ASH_Utils.getValueWithinRange(1 - ship.getFluxLevel(), -ENERGY_COST_MODIFIER, ENERGY_COST_MODIFIER);

        stats.getEnergyWeaponFluxCostMod().modifyPercent(spec.getId(), computedEnergyCost);
        stats.getEnergyWeaponDamageMult().modifyPercent(spec.getId(), computedEnergyDmg);
    }
}