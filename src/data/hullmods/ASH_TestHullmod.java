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
    public final float MODIFIER = 15f;

    @Override
    public void applyEffectsBeforeShipCreation(ShipAPI.HullSize hullSize, MutableShipStatsAPI stats, String id) {
        stats.getMissileRoFMult().modifyMult(id, MODIFIER);
    }

    @Override
    public String getDescriptionParam(int index, HullSize hullSize) {
        if (index == 0)
            return (int) MODIFIER + "";
        return null;
    }

    @Override
    public void advanceInCombat(ShipAPI ship, float amount) {
        if (!ship.isAlive())
            return;

        MutableShipStatsAPI stats = ship.getMutableStats();

        List<WeaponAPI> weapons = ship.getAllWeapons();
        for (WeaponAPI weapon : weapons) {
            if (weapon.getType() == WeaponType.MISSILE && weapon.usesAmmo() && weapon.getAmmo() <= 0f && weapon.getAmmoPerSecond() == 0f) {
                float ammoReloadSize = (float) Math.ceil(weapon.getMaxAmmo() * 0.1f);
                float ammoPerSecond = 0.01666666666666666666666666666667f * ammoReloadSize;
                weapon.getAmmoTracker().setReloadSize(ammoReloadSize);
                weapon.getAmmoTracker().setAmmoPerSecond(ammoPerSecond);
            }
        }
    }
}