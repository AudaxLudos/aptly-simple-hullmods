package data.hullmods;

import java.util.HashMap;
import java.util.Map;

import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.impl.campaign.ids.HullMods;
import com.fs.starfarer.api.impl.hullmods.BaseLogisticsHullMod;

public class ASH_ExternalCargoHolds extends BaseLogisticsHullMod {
    public static final float SHIP_STATS_MODIFIER = 20f;
    private static Map<Object, Float> CARGO_MODIFIER = new HashMap<Object, Float>();
    static {
        CARGO_MODIFIER.put(HullSize.FRIGATE, 250f);
        CARGO_MODIFIER.put(HullSize.DESTROYER, 500f);
        CARGO_MODIFIER.put(HullSize.CRUISER, 750f);
        CARGO_MODIFIER.put(HullSize.CAPITAL_SHIP, 1000f);
    }

    @Override
    public void applyEffectsBeforeShipCreation(ShipAPI.HullSize hullSize, MutableShipStatsAPI stats, String id) {
        stats.getCargoMod().modifyFlat(id, (Float) CARGO_MODIFIER.get(hullSize));
        stats.getMaxSpeed().modifyPercent(id, -SHIP_STATS_MODIFIER);
        stats.getMaxTurnRate().modifyPercent(id, -SHIP_STATS_MODIFIER);
        stats.getFluxDissipation().modifyPercent(id, -SHIP_STATS_MODIFIER);
        stats.getArmorDamageTakenMult().modifyPercent(id, SHIP_STATS_MODIFIER);
    }

    @Override
    public String getUnapplicableReason(ShipAPI ship) {
        int numLogisticsMods = getNumLogisticsMods(ship);
        if (this.spec != null && ship.getVariant().hasHullMod(this.spec.getId()))
            numLogisticsMods--; 
        if (ship == null || (ship.getVariant().hasHullMod(HullMods.CIVGRADE) && !ship.getVariant().hasHullMod(HullMods.MILITARIZED_SUBSYSTEMS)))
            return "Cannot be installed on a civilian ship without Militarized Subsystems";
        if (ship == null || numLogisticsMods >= MAX_MODS)
            return "Maximum of 2 non-built-in \"Logistics\" hullmods per hull";
        return null;
    }

    @Override
    public boolean isApplicableToShip(ShipAPI ship) {
        int numLogisticsMods = getNumLogisticsMods(ship);
        if (this.spec != null && ship.getVariant().hasHullMod(this.spec.getId()))
            numLogisticsMods--; 
        return ship != null && (!ship.getVariant().hasHullMod(HullMods.CIVGRADE) || ship.getVariant().hasHullMod(HullMods.MILITARIZED_SUBSYSTEMS)) && numLogisticsMods < MAX_MODS;
    }

    @Override
    public String getDescriptionParam(int index, HullSize hullSize) {
        if (index == 0)
            return Math.round(((Float) CARGO_MODIFIER.get(HullSize.FRIGATE)).intValue()) + "";
        if (index == 1)
            return Math.round(((Float) CARGO_MODIFIER.get(HullSize.DESTROYER)).intValue()) + "";
        if (index == 2)
            return Math.round(((Float) CARGO_MODIFIER.get(HullSize.CRUISER)).intValue()) + "";
        if (index == 3)
            return Math.round(((Float) CARGO_MODIFIER.get(HullSize.CAPITAL_SHIP)).intValue()) + "";
        if (index == 4)
            return Math.round(SHIP_STATS_MODIFIER) + "%";
        if (index == 5)
            return Math.round(SHIP_STATS_MODIFIER) + "%";
        return null;
    }
}