package data.hullmods;

import java.util.HashMap;
import java.util.Map;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.fleet.FleetMemberAPI;
import com.fs.starfarer.api.impl.hullmods.BaseLogisticsHullMod;
import com.fs.starfarer.api.util.Misc;

public class ASH_MarineTrainingFacility extends BaseLogisticsHullMod {
    public static final int DAYS_TO_GENERATE_MARINES = 7;
    private static Map<Object, Integer> MARINES_TO_GENERATE = new HashMap<Object, Integer>();
    static {
        MARINES_TO_GENERATE.put(HullSize.FRIGATE, 5);
        MARINES_TO_GENERATE.put(HullSize.DESTROYER, 10);
        MARINES_TO_GENERATE.put(HullSize.CRUISER, 20);
        MARINES_TO_GENERATE.put(HullSize.CAPITAL_SHIP, 40);
    }
    private long lastDay = Global.getSector().getClock().getTimestamp();

    @Override
    public String getDescriptionParam(int index, HullSize hullSize) {
        if (index == 0)
            return Math.round(((Integer) MARINES_TO_GENERATE.get(HullSize.FRIGATE)).intValue()) + "";
        if (index == 1)
            return Math.round(((Integer) MARINES_TO_GENERATE.get(HullSize.DESTROYER)).intValue()) + "";
        if (index == 2)
            return Math.round(((Integer) MARINES_TO_GENERATE.get(HullSize.CRUISER)).intValue()) + "";
        if (index == 3)
            return Math.round(((Integer) MARINES_TO_GENERATE.get(HullSize.CAPITAL_SHIP)).intValue())
                    + " crew to marines";
        if (index == 4)
            return "week";
        return null;
    }

    @Override
    public void advanceInCampaign(FleetMemberAPI member, float amount) {
        long currentDay = Global.getSector().getClock().getTimestamp();
        int marinesGenerated = 0;
        int minFleetCrew = 0;

        if (Global.getSector().getClock().getElapsedDaysSince(lastDay) >= DAYS_TO_GENERATE_MARINES) {
            lastDay = currentDay;

            if (member.getFleetData() == null || member.getFleetData().getFleet() == null)
                return;

            if (member.getFleetData().getFleet().getCargo() == null)
                return;

            for (FleetMemberAPI fleetMember : member.getFleetData().getMembersListCopy()) {
                minFleetCrew += fleetMember.getMinCrew();

                if (fleetMember.getVariant().hasHullMod("ASH_MarineTrainingFacility"))
                    marinesGenerated += (Integer) MARINES_TO_GENERATE.get(fleetMember.getVariant().getHullSize());
            }

            if (member.getFleetData().getFleet().getCargo().getCrew() <= minFleetCrew)
                return;
            if (member.getFleetData().getFleet().getCargo().getCrew() - marinesGenerated <= minFleetCrew)
                marinesGenerated = member.getFleetData().getFleet().getCargo().getCrew() - minFleetCrew;
            if (marinesGenerated > 0)
                Global.getSector().getCampaignUI().addMessage(marinesGenerated + " crew has been trained into marines.",
                        Misc.getTextColor());

            member.getFleetData().getFleet().getCargo().addMarines(marinesGenerated);
            member.getFleetData().getFleet().getCargo().removeCrew(marinesGenerated);
        }
    }
}