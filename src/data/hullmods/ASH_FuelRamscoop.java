package data.hullmods;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.fleet.FleetMemberAPI;

public class ASH_FuelRamscoop extends BaseHullMod {
    public static final float FUEL_TO_GENERATE = 1f;
    public static final float DAYS_TO_GENERATE_FUEL = 2f;
    private long lastDay = 0;

    @Override
    public String getDescriptionParam(int index, HullSize hullSize) {
        if (index == 0)
            return Math.round(FUEL_TO_GENERATE) + "";
        if (index == 1)
            return Math.round(DAYS_TO_GENERATE_FUEL) + "";
        return null;
    }


    @Override
    public void advanceInCampaign(FleetMemberAPI member, float amount) {
        long currentDay = Global.getSector().getClock().getTimestamp();
        float fuelGenerated = FUEL_TO_GENERATE;

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
                if (fleetMember == member)
                    continue;
                if (fleetMember.getVariant().hasHullMod("ASH_FuelRamscoop"))
                    fuelGenerated++;
            }

            member.getFleetData().getFleet().getCargo().addFuel(fuelGenerated);
        }
    }
}