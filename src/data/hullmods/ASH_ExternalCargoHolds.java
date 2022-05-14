package data.hullmods;

import java.util.HashMap;
import java.util.Map;

import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.impl.campaign.ids.HullMods;

public class ASH_ExternalCargoHolds extends BaseHullMod {
    public static final float SHIP_STATS_MODIFIER = 20f;
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
        stats.getMaxSpeed().modifyPercent(id, -SHIP_STATS_MODIFIER);
        stats.getMaxTurnRate().modifyPercent(id, -SHIP_STATS_MODIFIER);
        stats.getFluxDissipation().modifyPercent(id, -SHIP_STATS_MODIFIER);
        stats.getArmorDamageTakenMult().modifyPercent(id, SHIP_STATS_MODIFIER);
    }

    @Override
    public String getUnapplicableReason(ShipAPI ship) {
        if (ship == null || (ship.getVariant().hasHullMod(HullMods.CIVGRADE) && !ship.getVariant().hasHullMod(HullMods.MILITARIZED_SUBSYSTEMS)))
            return "Cannot be installed on a civilian ship without Militarized Subsystems";
        return null;
    }

    @Override
    public boolean isApplicableToShip(ShipAPI ship) {
        return ship != null && (!ship.getVariant().hasHullMod(HullMods.CIVGRADE) || ship.getVariant().hasHullMod(HullMods.MILITARIZED_SUBSYSTEMS));
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
        return null;
    }
}