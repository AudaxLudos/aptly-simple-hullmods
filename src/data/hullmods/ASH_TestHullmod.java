package data.hullmods;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.impl.campaign.ids.HullMods;

import org.lazywizard.lazylib.MathUtils;
import org.lazywizard.lazylib.combat.AIUtils;

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
    public String getDescriptionParam(int index, HullSize hullSize) {
        if (index == 0)
            return Math.round(((Float) CARGO_MODIFIER.get(HullSize.FRIGATE)).intValue()) + "%";
        if (index == 1)
            return Math.round(((Float) CARGO_MODIFIER.get(HullSize.FRIGATE)).intValue()) + "%";
        if (index == 2)
            return Math.round(((Float) CARGO_MODIFIER.get(HullSize.FRIGATE)).intValue()) + "%";
        if (index == 3)
            return Math.round(((Float) CARGO_MODIFIER.get(HullSize.FRIGATE)).intValue()) + "%";
        return null;
    }

    @Override
    public void advanceInCombat(ShipAPI ship, float amount) {
        if (!ship.isAlive())
            return;

        MutableShipStatsAPI stats = ship.getMutableStats();
        float calculatedRangeBonus = 0f;

        for (ShipAPI ally : AIUtils.getNearbyAllies(ship, 2000f)) {
            if(ally.isFrigate() || ally.isDestroyer() || ally.isDrone() || ally.isFighter())
                continue;

            if (ally.getVariant().hasHullMod(HullMods.ADVANCED_TARGETING_CORE) || ally.getVariant().hasHullMod(HullMods.DEDICATED_TARGETING_CORE))
                calculatedRangeBonus = 1000f;
        }

        stats.getEnergyWeaponRangeBonus().modifyPercent(spec.getId(), calculatedRangeBonus);
        stats.getBallisticWeaponRangeBonus().modifyPercent(spec.getId(), calculatedRangeBonus);
    }
}