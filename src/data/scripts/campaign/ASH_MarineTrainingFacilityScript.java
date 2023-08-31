package data.scripts.campaign;

import java.util.List;

import com.fs.starfarer.api.EveryFrameScript;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.CampaignFleetAPI;
import com.fs.starfarer.api.campaign.CargoAPI;
import com.fs.starfarer.api.campaign.FleetDataAPI;
import com.fs.starfarer.api.fleet.FleetMemberAPI;
import com.fs.starfarer.api.impl.PlayerFleetPersonnelTracker;
import com.fs.starfarer.api.util.Misc;

import data.ASH_Utils;
import data.hullmods.ASH_MarineTrainingFacility;

public class ASH_MarineTrainingFacilityScript implements EveryFrameScript {
    public boolean isActive = false;
    public Long lastDay;

    @Override
    public void advance(float amount) {
        if (Global.getSector().getPlayerFleet() == null)
            return;
        if (Global.getSector().getPlayerFleet().getFleetData() == null)
            return;
        if (Global.getSector().getPlayerFleet().getCargo() == null)
            return;

        CampaignFleetAPI playerFleet = Global.getSector().getPlayerFleet();
        FleetDataAPI playerFleetData = playerFleet.getFleetData();
        CargoAPI playerCargo = playerFleet.getCargo();
        List<FleetMemberAPI> playerFleetMembers = playerFleet.getFleetData().getMembersListCopy();

        if (!isActive) {
            for (FleetMemberAPI fleetMember : playerFleetMembers) {
                if (fleetMember.getVariant().hasHullMod("ASH_MarineTrainingFacility")) {
                    isActive = true;
                    lastDay = Global.getSector().getClock().getTimestamp();
                    break;
                }
            }
        } else {
            int maxMarines = 0;
            int addMarines = 0;
            float trainMarines = 0;

            if (Global.getSector().getClock().getElapsedDaysSince(lastDay) >= ASH_MarineTrainingFacility.DAYS_TO_GENERATE_MARINES) {
                for (FleetMemberAPI fleetMember : playerFleetMembers) {
                    if (!fleetMember.getVariant().hasHullMod("ASH_MarineTrainingFacility"))
                        continue;

                    addMarines += (Integer) ASH_MarineTrainingFacility.MARINES_TO_GENERATE.get(fleetMember.getVariant().getHullSize());
                    maxMarines += (Integer) ASH_MarineTrainingFacility.MAX_MARINES_TO_GENERATE.get(fleetMember.getVariant().getHullSize());

                    if (fleetMember.getVariant().getSMods().contains("ASH_MarineTrainingFacility") && ASH_Utils.isModEnabled())
                        trainMarines += (Integer) ASH_MarineTrainingFacility.SMOD_MARINES_TO_LEVEL.get(fleetMember.getVariant().getHullSize());
                    else
                        trainMarines += (Integer) ASH_MarineTrainingFacility.MARINES_TO_LEVEL.get(fleetMember.getVariant().getHullSize());

                }

                if (PlayerFleetPersonnelTracker.getInstance().getMarineData().getXPLevel() * playerCargo.getMarines() + trainMarines >= playerCargo.getMarines())
                    trainMarines = playerCargo.getMarines() - PlayerFleetPersonnelTracker.getInstance().getMarineData().getXPLevel() *
                            playerCargo.getMarines();

                if (trainMarines > 0 && playerCargo.getMarines() > 0) {
                    PlayerFleetPersonnelTracker.getInstance().getMarineData().addXP(Math.round(trainMarines));
                    PlayerFleetPersonnelTracker.getInstance().update();
                    Global.getSector().getCampaignUI().addMessage(Math.round(trainMarines) + " existing marines has been trained.", Misc.getTextColor());
                }

                if (playerCargo.getMarines() + addMarines >= maxMarines)
                    addMarines = maxMarines - (int) playerCargo.getMarines();

                if (playerCargo.getCrew() - addMarines <= playerFleetData.getMinCrew())
                    addMarines = (int) (playerCargo.getCrew() - playerFleetData.getMinCrew());

                if (playerCargo.getCrew() <= playerFleetData.getMinCrew() || playerCargo.getMarines() >= maxMarines)
                    addMarines = 0;

                if (addMarines > 0) {
                    playerCargo.addMarines(addMarines);
                    playerCargo.removeCrew(addMarines);
                    Global.getSector().getCampaignUI().addMessage(addMarines + " crew has been trained into marines.", Misc.getTextColor());
                }

                isActive = false;
            }
        }
    }

    @Override
    public boolean isDone() {
        return false;
    }

    @Override
    public boolean runWhilePaused() {
        return false;
    }

}
