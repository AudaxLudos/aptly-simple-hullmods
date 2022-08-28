package data.hullmods;

import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;

public class ASH_Template extends BaseHullMod {
    public static final float TEST_MODIFIER = 100f;

    @Override
    public void applyEffectsAfterShipCreation(ShipAPI ship, String id) {
    }

    @Override
    public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {
    }

    @Override
    public void advanceInCombat(ShipAPI ship, float amount) {
        if (!ship.isAlive())
            return;

        MutableShipStatsAPI stats = ship.getMutableStats();
    }

    @Override
    public String getDescriptionParam(int index, HullSize hullSize) {
        if (index == 0)
            return Math.round(TEST_MODIFIER) + "%";
        if (index == 1)
            return Math.round(TEST_MODIFIER) + "%";
        if (index == 2)
            return Math.round(TEST_MODIFIER) + "%";
        if (index == 3)
            return Math.round(TEST_MODIFIER) + "%";
        return null;
    }
}