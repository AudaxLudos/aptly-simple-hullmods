package data.hullmods;

import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;

public class ASH_AlignedThrustVariants extends BaseHullMod {
    public static final float SHIP_MAX_SPEED_MODIFIER = 1.1f;
    public static final float SHIP_MAX_TURN_RATE_MODIFIER = 50f;

    @Override
    public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {
        stats.getMaxSpeed().modifyMult(id, SHIP_MAX_SPEED_MODIFIER);
        stats.getMaxTurnRate().modifyPercent(id, -SHIP_MAX_TURN_RATE_MODIFIER);
    }

    @Override
    public void advanceInCombat(ShipAPI ship, float amount) {
        if (!ship.isAlive())
            return;
    }

    @Override
    public String getDescriptionParam(int index, HullSize hullSize) {
        if (index == 0)
            return Math.round(SHIP_MAX_SPEED_MODIFIER * 100 - 100) + "%";
        if (index == 1)
            return Math.round(SHIP_MAX_TURN_RATE_MODIFIER) + "%";
        return null;
    }
}