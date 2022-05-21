package data.hullmods;

import java.util.HashMap;
import java.util.Map;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.CargoAPI;
import com.fs.starfarer.api.campaign.FleetDataAPI;
import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.fleet.FleetMemberAPI;

public class ASH_MarineTrainingFacility extends BaseHullMod {
    public static final int DAYS_TO_GENERATE_MARINES = 30;

    private static Map MARINES_TO_GENERATE = new HashMap();
    static {
        MARINES_TO_GENERATE.put(HullSize.FRIGATE, 10);
        MARINES_TO_GENERATE.put(HullSize.DESTROYER, 20);
        MARINES_TO_GENERATE.put(HullSize.CRUISER,40);
        MARINES_TO_GENERATE.put(HullSize.CAPITAL_SHIP, 50);
    }
    private static Map MAX_MARINES_GENERATED = new HashMap();
    static {
        MAX_MARINES_GENERATED.put(HullSize.FRIGATE, 50);
        MAX_MARINES_GENERATED.put(HullSize.DESTROYER, 100);
        MAX_MARINES_GENERATED.put(HullSize.CRUISER, 200);
        MAX_MARINES_GENERATED.put(HullSize.CAPITAL_SHIP, 250);
    }
    private long lastDay = Global.getSector().getClock().getTimestamp();

    @Override
    public String getDescriptionParam(int index, HullSize hullSize) {
        if (index == 0)
            return Math.round(((Float) MARINES_TO_GENERATE.get(HullSize.FRIGATE)).intValue()) + "";
        if (index == 1)
            return Math.round(((Float) MARINES_TO_GENERATE.get(HullSize.DESTROYER)).intValue()) + "";
        if (index == 2)
            return Math.round(((Float) MARINES_TO_GENERATE.get(HullSize.CRUISER)).intValue()) + "";
        if (index == 3)
            return Math.round(((Float) MARINES_TO_GENERATE.get(HullSize.CAPITAL_SHIP)).intValue()) + " crew to marines";
        if (index == 4)
            return "month";
        if (index == 5)
            return Math.round(((Float) MAX_MARINES_GENERATED.get(HullSize.FRIGATE)).intValue()) + "";
        if (index == 6)
            return Math.round(((Float) MAX_MARINES_GENERATED.get(HullSize.DESTROYER)).intValue()) + "";
        if (index == 7)
            return Math.round(((Float) MAX_MARINES_GENERATED.get(HullSize.CRUISER)).intValue()) + "";
        if (index == 8)
            return Math.round(((Float) MAX_MARINES_GENERATED.get(HullSize.CAPITAL_SHIP)).intValue()) + " marines";
        return null;
    }

    @Override
    public void advanceInCampaign(FleetMemberAPI member, float amount) {
        long currentDay = Global.getSector().getClock().getTimestamp();
        int marinesGenerated = 0;
        int maxMarines = 0;
        int minFleetCrew = 0;

        if (Global.getSector().getClock().getElapsedDaysSince(lastDay) >= DAYS_TO_GENERATE_MARINES) {
            lastDay = currentDay;

            FleetDataAPI fleetData = member.getFleetData();

            if (fleetData == null)
                return;
            if (fleetData.getFleet() == null)
                return;

            CargoAPI fleetCargo = fleetData.getFleet().getCargo();

            if (fleetCargo == null)
                return;

            for (FleetMemberAPI fleetMember : fleetData.getMembersListCopy()) {
                minFleetCrew += fleetMember.getMinCrew();

                if (fleetMember.getVariant().hasHullMod("ASH_MarineTrainingFacility")) {
                    marinesGenerated += (Integer) MARINES_TO_GENERATE.get(fleetMember.getVariant().getHullSize());
                    maxMarines += (Integer) MAX_MARINES_GENERATED.get(fleetMember.getVariant().getHullSize());
                }
            }

            if (fleetCargo.getMarines() >= maxMarines)
                return;
            if (fleetCargo.getCrew() <= minFleetCrew)
                return;
            if (fleetCargo.getCrew() - marinesGenerated <= minFleetCrew)
                marinesGenerated = fleetCargo.getCrew() - minFleetCrew;

            fleetCargo.addMarines(marinesGenerated);
            fleetCargo.removeCrew(marinesGenerated);
        }
    }
}