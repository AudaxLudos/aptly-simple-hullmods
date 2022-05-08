package data.hullmods;

import java.util.List;

import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.WeaponAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.combat.WeaponAPI.WeaponType;

import data.ASH_Utils;

public class ASH_TestHullmod extends BaseHullMod {
    public static final float BALLISTIC_FIRE_RATE_MOD = 20f;
    public static final float BALLISTIC_AMMO_REGEN_MOD = 20f;
    public static final float BALLISTIC_AMMO_CAPACITY_MOD = 20f;
    public static final float BALLISTIC_FLUX_COST_MOD = 15f;
    public static final float BALLISTIC_DAMAGE_MOD = 10f;

    @Override
    public void applyEffectsBeforeShipCreation(ShipAPI.HullSize hullSize, MutableShipStatsAPI stats, String id) {
        stats.getBallisticRoFMult().modifyPercent(id, BALLISTIC_FIRE_RATE_MOD);
        stats.getBallisticAmmoRegenMult().modifyPercent(id, BALLISTIC_AMMO_REGEN_MOD);
        stats.getBallisticAmmoBonus().modifyPercent(id, BALLISTIC_AMMO_CAPACITY_MOD);
        stats.getBallisticWeaponFluxCostMod().modifyPercent(id, BALLISTIC_FLUX_COST_MOD);
        stats.getBallisticWeaponDamageMult().modifyPercent(id, -BALLISTIC_DAMAGE_MOD);
    }

    @Override
    public String getDescriptionParam(int index, HullSize hullSize) {
        if (index == 0)
            return (int) BALLISTIC_FIRE_RATE_MOD + "";
        if (index == 1)
            return (int) BALLISTIC_FLUX_COST_MOD + "";
        return null;
    }
}