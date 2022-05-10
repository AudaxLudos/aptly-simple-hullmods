package data.hullmods;

import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;

public class ASH_VolatileAmmunition extends BaseHullMod {
    public static final float BALLISTIC_DAMAGE_MODIFIER = 25f;
    public static final float BALLISTIC_FLUX_COST_MODIFIER = 1.25f;
    public static final float BALLISTIC_FIRE_RATE_MODIFIER = 0.75f;

    @Override
    public void applyEffectsBeforeShipCreation(ShipAPI.HullSize hullSize, MutableShipStatsAPI stats, String id) {
        stats.getBallisticWeaponDamageMult().modifyPercent(id, BALLISTIC_DAMAGE_MODIFIER);
        stats.getBallisticWeaponFluxCostMod().modifyMult(id, BALLISTIC_FLUX_COST_MODIFIER);
        stats.getBallisticRoFMult().modifyMult(id, BALLISTIC_FIRE_RATE_MODIFIER);
    }

    @Override
    public String getDescriptionParam(int index, HullSize hullSize) {
        if (index == 0)
            return Math.round(BALLISTIC_DAMAGE_MODIFIER) + "%";
        if (index == 1)
            return BALLISTIC_FIRE_RATE_MODIFIER + "";
        if (index == 2)
            return BALLISTIC_FLUX_COST_MODIFIER + "";
        return null;
    }
}