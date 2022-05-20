package data.hullmods;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.fleet.FleetMemberAPI;

public class ASH_MarineTrainingFacility extends BaseHullMod {
    public static final int MARINES_TO_GENERATE = 100;
    public static final int DAYS_TO_GENERATE_MARINES = 30;
    public static final int MAX_MARINES_GENERATED = 1000;
    private long lastDay = 0;

    @Override
    public String getDescriptionParam(int index, HullSize hullSize) {
        if (index == 0)
            return Math.round(MARINES_TO_GENERATE) + "";
        if (index == 1)
            return Math.round(MARINES_TO_GENERATE) + "";
        if (index == 2)
            return "month";
        return null;
    }

    @Override
    public void advanceInCampaign(FleetMemberAPI member, float amount) {
        long currentDay = Global.getSector().getClock().getTimestamp();
        int marinesGenerated = MARINES_TO_GENERATE;
        int minFleetCrew = 0;

        if (Global.getSector().getClock().getElapsedDaysSince(lastDay) >= DAYS_TO_GENERATE_MARINES) {
            lastDay = currentDay;

            if (member.getFleetData() == null)
                return;
            if (member.getFleetData().getFleet() == null)
                return;
            if (member.getFleetData().getFleet().getCargo() == null)
                return;
            if (member.getFleetData().getFleet().getCargo().getTotalPersonnel() >= member.getFleetData().getFleet().getCargo().getMaxPersonnel())
                return;
            if (member.getFleetData().getFleet().getCargo().getMarines() >= MAX_MARINES_GENERATED)
                return;

            for (FleetMemberAPI fleetMember : member.getFleetData().getMembersListCopy()) {
                minFleetCrew += fleetMember.getMinCrew();

                if (fleetMember == member)
                    continue;
                if (fleetMember.getVariant().hasHullMod("ASH_TestHullmod"))
                    marinesGenerated += MARINES_TO_GENERATE;
            }

            if (member.getFleetData().getFleet().getCargo().getCrew() <= minFleetCrew)
                return;

            member.getFleetData().getFleet().getCargo().addMarines(marinesGenerated);
            member.getFleetData().getFleet().getCargo().removeCrew(marinesGenerated);
        }
    }
}