package data.hullmods;

import java.util.HashMap;
import java.util.Map;

import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;

public class ASH_TestHullmod extends BaseHullMod {
    private static Map<Object, Float> STATS_MODIFIER = new HashMap<Object, Float>();
    static {
        STATS_MODIFIER.put(HullSize.FRIGATE, 1f);
        STATS_MODIFIER.put(HullSize.DESTROYER, 1f);
        STATS_MODIFIER.put(HullSize.CRUISER, 2f);
        STATS_MODIFIER.put(HullSize.CAPITAL_SHIP, 3f);
    }

    @Override
    public void applyEffectsAfterShipCreation(ShipAPI ship, String id) {
    }

    @Override
    public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {
        float burnLevelBonus = 10f - stats.getMaxBurnLevel().getBaseValue();
        stats.getMaxBurnLevel().modifyFlat(id, burnLevelBonus);
        stats.getFuelUseMod().modifyMult(id, STATS_MODIFIER.get(hullSize));
        stats.getSuppliesPerMonth().modifyMult(id, STATS_MODIFIER.get(hullSize));
    }

    @Override
    public void advanceInCombat(ShipAPI ship, float amount) {
        if (!ship.isAlive())
            return;
    }

    @Override
    public String getDescriptionParam(int index, HullSize hullSize) {
        if (index == 0)
            return "%";
        return null;
    }
}