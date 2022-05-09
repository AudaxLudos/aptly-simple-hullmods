package data.hullmods;

import java.util.List;

import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.WeaponAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.combat.WeaponAPI.WeaponType;

public class ASH_MakeshiftMissileAutoforge extends BaseHullMod {
    public static final float MISSILE_AMMO_RELOAD_SIZE_MODIFIER = 10f;
    public static final float MISSILE_AMMO_PER_SECOND_MODIFIER = 50f;

    @Override
    public String getDescriptionParam(int index, HullSize hullSize) {
        if (index == 0)
            return Math.round(MISSILE_AMMO_RELOAD_SIZE_MODIFIER) + "%";
        if (index == 1)
            return Math.round(MISSILE_AMMO_PER_SECOND_MODIFIER) + " seconds";
        return null;
    }

    @Override
    public void advanceInCombat(ShipAPI ship, float amount) {
        if (!ship.isAlive())
            return;

        List<WeaponAPI> weapons = ship.getAllWeapons();
        for (WeaponAPI weapon : weapons) {
            if (weapon.getType() == WeaponType.MISSILE && weapon.usesAmmo() && weapon.getAmmo() <= 0f && weapon.getAmmoPerSecond() == 0f) {
                float ammoReloadSize = (float) Math.ceil(weapon.getMaxAmmo() / MISSILE_AMMO_RELOAD_SIZE_MODIFIER);
                float ammoPerSecond = ammoReloadSize / MISSILE_AMMO_PER_SECOND_MODIFIER;
                weapon.getAmmoTracker().setReloadSize(ammoReloadSize);
                weapon.getAmmoTracker().setAmmoPerSecond(ammoPerSecond);
            }
        }
    }
}