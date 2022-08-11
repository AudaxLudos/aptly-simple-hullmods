package data.hullmods;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipCommand;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;

public class ASH_TestHullmod extends BaseHullMod {
    public static final float SHIP_ARMOR_MODIFIER = 20f;
    public static final float SHIP_HULL_MODIFIER = 0.8f;
    public static final float SHIP_MIN_ARMOR_MODIFIER = 5f;
    public static final float SHIP_BREAK_CHANCE_MODIFIER = 2f;
    public static final float OVERLOAD_TIME_MODIFIER = 0.5f;

    @Override
    public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {
        // stats.getArmorBonus().modifyPercent(id, 20f);
        // stats.getMinArmorFraction().modifyPercent(id, 5f);
        // stats.getHullBonus().modifyMult(id, 0.9f);
        // stats.getBreakProb().modifyMult(id, 2f);
    }

    @Override
    public void advanceInCombat(ShipAPI ship, float amount) {
        if (!ship.isAlive())
            return;

        MutableShipStatsAPI stats = ship.getMutableStats();

        stats.getVentRateMult().modifyMult(spec.getId(), 2f);

        if(ship.getFluxTracker().isVenting()) {
            stats.getEngineMalfunctionChance().modifyFlat(spec.getId(), 2f);
            stats.getShieldMalfunctionChance().modifyFlat(spec.getId(), 2f);
            stats.getWeaponMalfunctionChance().modifyFlat(spec.getId(), 2f);
        } else {
            stats.getEngineMalfunctionChance().unmodify();
            stats.getShieldMalfunctionChance().unmodify();
            stats.getWeaponMalfunctionChance().unmodify();
        }
    }

    @Override
    public String getDescriptionParam(int index, HullSize hullSize) {
        if (index == 0)
            return Math.round(SHIP_ARMOR_MODIFIER) + "";
        if (index == 1)
            return Math.round(SHIP_MIN_ARMOR_MODIFIER) + "";
        if (index == 2)
            return Math.round(SHIP_HULL_MODIFIER) + "";
        if (index == 3)
            return Math.round(SHIP_BREAK_CHANCE_MODIFIER) + "";
        return null;
    }
}