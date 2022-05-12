package data.hullmods;

import java.util.HashMap;
import java.util.Map;

import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;

public class ASH_TestHullmod extends BaseHullMod {
    public static final float MAX_BURN_LEVEL_MODIFIER = 1f;
    public static final float MAX_SPEED_MODIFIER = 20f;
    public static final float MAX_TURN_RATE_MODIFIER = 20f;
    private static Map CARGO_MODIFIER = new HashMap();
    static {
        CARGO_MODIFIER.put(HullSize.FRIGATE, 250f);
        CARGO_MODIFIER.put(HullSize.DESTROYER, 500f);
        CARGO_MODIFIER.put(HullSize.CRUISER, 750f);
        CARGO_MODIFIER.put(HullSize.CAPITAL_SHIP, 1000f);
    }

    @Override
    public void applyEffectsBeforeShipCreation(ShipAPI.HullSize hullSize, MutableShipStatsAPI stats, String id) {
        stats.getCargoMod().modifyFlat(id, (Float) CARGO_MODIFIER.get(hullSize));
        stats.getMaxBurnLevel().modifyFlat(id, -MAX_BURN_LEVEL_MODIFIER);
        stats.getMaxSpeed().modifyPercent(id, -MAX_SPEED_MODIFIER);
        stats.getMaxTurnRate().modifyPercent(id, -MAX_TURN_RATE_MODIFIER);
    }

    @Override
    public String getDescriptionParam(int index, HullSize hullSize) {
        if (index == 0)
            return Math.round(((Float) CARGO_MODIFIER.get(HullSize.FRIGATE)).intValue()) + "%";
        if (index == 1)
            return Math.round(((Float) CARGO_MODIFIER.get(HullSize.FRIGATE)).intValue()) + "%";
        if (index == 2)
            return Math.round(((Float) CARGO_MODIFIER.get(HullSize.FRIGATE)).intValue()) + "%";
        if (index == 3)
            return Math.round(((Float) CARGO_MODIFIER.get(HullSize.FRIGATE)).intValue()) + "%";
        if (index == 4)
            return Math.round(MAX_BURN_LEVEL_MODIFIER) + "%";
        if (index == 5)
            return Math.round(MAX_SPEED_MODIFIER) + "%";
        if (index == 6)
            return Math.round(MAX_TURN_RATE_MODIFIER) + "%";
        return null;
    }
}