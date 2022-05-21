package data.hullmods;

import java.util.HashMap;
import java.util.Map;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.fleet.FleetMemberAPI;

public class ASH_FuelRamscoop extends BaseHullMod {
    public static final float DAYS_TO_GENERATE_FUEL = 3f;
    
    private static Map FUEL_TO_GENERATE = new HashMap();
    static {
        FUEL_TO_GENERATE.put(HullSize.FRIGATE, 1f);
        FUEL_TO_GENERATE.put(HullSize.DESTROYER, 1f);
        FUEL_TO_GENERATE.put(HullSize.CRUISER, 2f);
        FUEL_TO_GENERATE.put(HullSize.CAPITAL_SHIP, 2f);
    }
    private long lastDay = Global.getSector().getClock().getTimestamp();


    @Override
    public String getDescriptionParam(int index, HullSize hullSize) {
        if (index == 0)
            return Math.round(((Float) FUEL_TO_GENERATE.get(HullSize.FRIGATE)).intValue()) + " fuel";
        if (index == 1)
            return Math.round(((Float) FUEL_TO_GENERATE.get(HullSize.DESTROYER)).intValue()) + " fuel";
        if (index == 2)
            return Math.round(((Float) FUEL_TO_GENERATE.get(HullSize.CRUISER)).intValue()) + " fuel";
        if (index == 3)
            return Math.round(((Float) FUEL_TO_GENERATE.get(HullSize.CAPITAL_SHIP)).intValue()) + " fuel";
        if (index == 4)
            return Math.round(DAYS_TO_GENERATE_FUEL) + " days";
        return null;
    }

    @Override
    public void advanceInCampaign(FleetMemberAPI member, float amount) {
        long currentDay = Global.getSector().getClock().getTimestamp();
        float fuelGenerated = 0;

        if (Global.getSector().getClock().getElapsedDaysSince(lastDay) >= DAYS_TO_GENERATE_FUEL) {
            lastDay = currentDay;

            if (member.getFleetData() == null)
                return;
            if (member.getFleetData().getFleet() == null)
                return;
            if (member.getFleetData().getFleet().getCargo() == null)
                return;
            if (member.getFleetData().getFleet().getCargo().getFuel() >= member.getFleetData().getFleet().getCargo().getMaxFuel())
                return;

            for (FleetMemberAPI fleetMember : member.getFleetData().getMembersListCopy()) {
                if (fleetMember.getVariant().hasHullMod("ASH_FuelRamscoop"))
                    fuelGenerated += (Float) FUEL_TO_GENERATE.get(fleetMember.getVariant().getHullSize());
            }

            member.getFleetData().getFleet().getCargo().addFuel(fuelGenerated);
        }
    }
}