package data.hullmods;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.fleet.FleetMemberAPI;

public class ASH_TestHullmod extends BaseHullMod {
    public static final int DAYS_PER_FUEL_GENERATED = 1;
    public static final int FUEL_GENERATED_PER_DAY = 1;

    @Override
    public String getDescriptionParam(int index, HullSize hullSize) {
        if (index == 0)
            return Math.round(DAYS_PER_FUEL_GENERATED) + "%";
        return null;
    }

    private long lastDay = 0;

    @Override
    public void advanceInCampaign(FleetMemberAPI member, float amount) {

        long currentDay = Global.getSector().getClock().getTimestamp();
        int fuelGenerated = FUEL_GENERATED_PER_DAY;

        if (Global.getSector().getClock().getElapsedDaysSince(lastDay) >= DAYS_PER_FUEL_GENERATED) {
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
                if (member.getVariant().hasHullMod("ASH_TestHullmod"))
                    fuelGenerated++;
            }

            member.getFleetData().getFleet().getCargo().addFuel(fuelGenerated);
        }
    }
}