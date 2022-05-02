package data.hullmods;

import java.util.List;

import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.WeaponAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.combat.WeaponAPI.WeaponType;

public class ASH_MakeshiftMissileAutoforge extends BaseHullMod {
    @Override
    public String getDescriptionParam(int index, HullSize hullSize) {
        if (index == 0)
            return (int) 10 + "%";
        if (index == 1)
            return (int) 50 + " seconds";
        return null;
    }

    @Override
    public void advanceInCombat(ShipAPI ship, float amount) {
        if (!ship.isAlive())
            return;

        List<WeaponAPI> weapons = ship.getAllWeapons();
        for (WeaponAPI weapon : weapons) {
            if (weapon.getType() == WeaponType.MISSILE && weapon.usesAmmo() && weapon.getAmmo() <= 0f && weapon.getAmmoPerSecond() == 0f) {
                float ammoReloadSize = (float) Math.ceil(weapon.getMaxAmmo() * 0.1f);
                float ammoPerSecond = 0.02f * ammoReloadSize;
                weapon.getAmmoTracker().setReloadSize(ammoReloadSize);
                weapon.getAmmoTracker().setAmmoPerSecond(ammoPerSecond);
            }
        }
    }
}