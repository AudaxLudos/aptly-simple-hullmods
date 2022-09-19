package data.hullmods;

import java.util.HashMap;
import java.util.Map;

import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.impl.campaign.ids.HullMods;
import com.fs.starfarer.api.impl.hullmods.BaseLogisticsHullMod;

public class ASH_ExternalFuelTanks extends BaseLogisticsHullMod {
    public static final float SHIP_STATS_MODIFIER = 20f;
    private static Map<Object, Float> FUEL_MODIFIER = new HashMap<Object, Float>();
    static {
        FUEL_MODIFIER.put(HullSize.FRIGATE, 250f);
        FUEL_MODIFIER.put(HullSize.DESTROYER, 500f);
        FUEL_MODIFIER.put(HullSize.CRUISER, 750f);
        FUEL_MODIFIER.put(HullSize.CAPITAL_SHIP, 1000f);
    }

    @Override
    public void applyEffectsBeforeShipCreation(ShipAPI.HullSize hullSize, MutableShipStatsAPI stats, String id) {
        stats.getFuelMod().modifyFlat(id, (Float)FUEL_MODIFIER.get(hullSize));
        stats.getMaxSpeed().modifyPercent(id, -SHIP_STATS_MODIFIER);
        stats.getMaxTurnRate().modifyPercent(id, -SHIP_STATS_MODIFIER);
        stats.getFluxDissipation().modifyPercent(id, -SHIP_STATS_MODIFIER);
        stats.getHighExplosiveDamageTakenMult().modifyPercent(id, SHIP_STATS_MODIFIER);
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
            return Math.round(((Float)FUEL_MODIFIER.get(HullSize.FRIGATE)).intValue()) + "/"
                + Math.round(((Float)FUEL_MODIFIER.get(HullSize.DESTROYER)).intValue()) + "/"
                + Math.round(((Float)FUEL_MODIFIER.get(HullSize.CRUISER)).intValue()) + "/"
                + Math.round(((Float)FUEL_MODIFIER.get(HullSize.CAPITAL_SHIP)).intValue());
        if (index == 1)
            return Math.round(SHIP_STATS_MODIFIER) + "%";
        if (index == 2)
            return Math.round(SHIP_STATS_MODIFIER) + "%";
        return null;
    }
}