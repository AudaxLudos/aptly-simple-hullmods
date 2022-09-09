package data.hullmods;

import java.util.HashMap;
import java.util.Map;

import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.impl.hullmods.BaseLogisticsHullMod;

public class ASH_GravitonAttunementDrive extends BaseLogisticsHullMod {
    private static Map<Object, Float> FLEET_BURN_MODIFIER = new HashMap<Object, Float>();
    static {
        FLEET_BURN_MODIFIER.put(HullSize.FRIGATE, 1f);
        FLEET_BURN_MODIFIER.put(HullSize.DESTROYER, 2f);
        FLEET_BURN_MODIFIER.put(HullSize.CRUISER, 3f);
        FLEET_BURN_MODIFIER.put(HullSize.CAPITAL_SHIP, 4f);
    }
    private static Map<Object, Float> SUPPLIES_PER_MONTH_MODIFIER = new HashMap<Object, Float>();
    static {
        SUPPLIES_PER_MONTH_MODIFIER.put(HullSize.FRIGATE, 2f);
        SUPPLIES_PER_MONTH_MODIFIER.put(HullSize.DESTROYER, 2.25f);
        SUPPLIES_PER_MONTH_MODIFIER.put(HullSize.CRUISER, 2.5f);
        SUPPLIES_PER_MONTH_MODIFIER.put(HullSize.CAPITAL_SHIP, 3f);
    }

    @Override
    public void applyEffectsBeforeShipCreation(ShipAPI.HullSize hullSize, MutableShipStatsAPI stats, String id) {
        stats.getDynamic().getMod("fleet_burn_bonus").modifyFlat(id, (Float) FLEET_BURN_MODIFIER.get(hullSize));
        stats.getSensorProfile().modifyFlat(id, (Float) FLEET_BURN_MODIFIER.get(hullSize) * 200f);
        stats.getSuppliesPerMonth().modifyMult(id, (Float) SUPPLIES_PER_MONTH_MODIFIER.get(hullSize));
    }

    @Override
    public String getDescriptionParam(int index, HullSize hullSize) {
        if (index == 0)
            return Math.round(((Float) FLEET_BURN_MODIFIER.get(HullSize.FRIGATE)).intValue()) + "/"
                    + Math.round(((Float) FLEET_BURN_MODIFIER.get(HullSize.DESTROYER)).intValue()) + "/"
                    + Math.round(((Float) FLEET_BURN_MODIFIER.get(HullSize.CRUISER)).intValue()) + "/"
                    + Math.round(((Float) FLEET_BURN_MODIFIER.get(HullSize.CAPITAL_SHIP)).intValue());
        if (index == 1)
            return Math.round(((Float) FLEET_BURN_MODIFIER.get(HullSize.FRIGATE)).intValue()) * 200 + "/"
                    + Math.round(((Float) FLEET_BURN_MODIFIER.get(HullSize.DESTROYER)).intValue()) * 200 + "/"
                    + Math.round(((Float) FLEET_BURN_MODIFIER.get(HullSize.CRUISER)).intValue()) * 200 + "/"
                    + Math.round(((Float) FLEET_BURN_MODIFIER.get(HullSize.CAPITAL_SHIP)).intValue()) * 200;
        if (index == 2)
            return Math.round(((Float) SUPPLIES_PER_MONTH_MODIFIER.get(HullSize.FRIGATE)).intValue()) + "/"
                    + Math.round(((Float) SUPPLIES_PER_MONTH_MODIFIER.get(HullSize.DESTROYER)).intValue()) + "/"
                    + Math.round(((Float) SUPPLIES_PER_MONTH_MODIFIER.get(HullSize.CRUISER)).intValue()) + "/"
                    + Math.round(((Float) SUPPLIES_PER_MONTH_MODIFIER.get(HullSize.CAPITAL_SHIP)).intValue());
        return null;
    }
}