package data.hullmods;

import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;

public class ASH_VolatileWarheads extends BaseHullMod {
    public static final float MISSILE_DMG_MODIFIER = 25f;
    public static final float MISSILE_HEALTH_MODIFIER = 50f;

    @Override
    public void applyEffectsBeforeShipCreation(ShipAPI.HullSize hullSize, MutableShipStatsAPI stats, String id) {
        stats.getMissileWeaponDamageMult().modifyPercent(id, MISSILE_DMG_MODIFIER);
        stats.getMissileHealthBonus().modifyPercent(id, -MISSILE_HEALTH_MODIFIER);
    }

    @Override
    public String getDescriptionParam(int index, HullSize hullSize) {
        if (index == 0)
            return (int) MISSILE_DMG_MODIFIER + "%";
        if (index == 1)
            return (int) MISSILE_HEALTH_MODIFIER + "%";
        return null;
    }
}