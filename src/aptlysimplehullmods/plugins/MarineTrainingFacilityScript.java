package aptlysimplehullmods.plugins;

import aptlysimplehullmods.hullmods.MarineTrainingFacility;
import com.fs.starfarer.api.EveryFrameScript;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.CargoAPI;
import com.fs.starfarer.api.campaign.FleetDataAPI;
import com.fs.starfarer.api.fleet.FleetMemberAPI;
import com.fs.starfarer.api.impl.PlayerFleetPersonnelTracker;
import com.fs.starfarer.api.util.IntervalUtil;
import com.fs.starfarer.api.util.Misc;

public class MarineTrainingFacilityScript implements EveryFrameScript {
    public static final String MARINE_TRAINING_FACILITY_ID = "ASH_MarineTrainingFacility";
    public boolean isActive = false;
    public int shipsWithHullmod = 0;
    public Long lastDay;
    public IntervalUtil timer = new IntervalUtil(0.9f, 1.1f);

    @Override
    public boolean isDone() {
        return false;
    }

    @Override
    public boolean runWhilePaused() {
        return false;
    }

    @Override
    public void advance(float amount) {
        if (Global.getSector().getPlayerFleet() == null) return;
        if (Global.getSector().getPlayerFleet().getFleetData() == null) return;
        if (Global.getSector().getPlayerFleet().getCargo() == null) return;

        timer.advance(amount);
        if (timer.intervalElapsed()) {
            FleetDataAPI playerFleetData = Global.getSector().getPlayerFleet().getFleetData();
            CargoAPI playerCargo = Global.getSector().getPlayerFleet().getCargo();
            int shipsWithMarineTrainingFacility = 0;
            int maxMarines = 0;
            int addMarines = 0;
            float trainMarines = 0;
            for (FleetMemberAPI member : Global.getSector().getPlayerFleet().getFleetData().getMembersListCopy()) {
                if (!member.getVariant().hasHullMod(MARINE_TRAINING_FACILITY_ID)) continue;
                ++shipsWithMarineTrainingFacility;
                addMarines += MarineTrainingFacility.MARINES_TO_GENERATE.get(member.getVariant().getHullSize());
                maxMarines += MarineTrainingFacility.MAX_MARINES_TO_GENERATE.get(member.getVariant().getHullSize());
                if (!member.getVariant().getSMods().contains(MARINE_TRAINING_FACILITY_ID))
                    trainMarines += MarineTrainingFacility.MARINES_TO_LEVEL.get(member.getVariant().getHullSize());
                else
                    trainMarines += MarineTrainingFacility.SMOD_MARINES_TO_LEVEL.get(member.getVariant().getHullSize());
            }

            if (!isActive || shipsWithHullmod != shipsWithMarineTrainingFacility) {
                PlayerFleetPersonnelTracker.getInstance().update();
                if ((playerCargo.getCrew() > playerFleetData.getMinCrew() && playerCargo.getMarines() < maxMarines) || (playerCargo.getMarines() > 0 && PlayerFleetPersonnelTracker.getInstance().getMarineData().getXPLevel() < 1f)) {
                    isActive = true;
                    shipsWithHullmod = shipsWithMarineTrainingFacility;
                    lastDay = Global.getSector().getClock().getTimestamp();
                }
            }

            if (isActive && Global.getSector().getClock().getElapsedDaysSince(lastDay) >= MarineTrainingFacility.DAYS_TO_GENERATE_MARINES) {
                if (PlayerFleetPersonnelTracker.getInstance().getMarineData().getXPLevel() * playerCargo.getMarines() + trainMarines >= playerCargo.getMarines())
                    trainMarines = playerCargo.getMarines() - PlayerFleetPersonnelTracker.getInstance().getMarineData().getXPLevel() * playerCargo.getMarines();

                if (trainMarines > 0 && playerCargo.getMarines() > 0) {
                    PlayerFleetPersonnelTracker.getInstance().getMarineData().addXP(Math.round(trainMarines));
                    PlayerFleetPersonnelTracker.getInstance().update();
                    Global.getSector().getCampaignUI().addMessage(
                            "%s existing marines has been trained.",
                            Misc.getTextColor(),
                            Math.round(trainMarines) + "",
                            null,
                            Misc.getPositiveHighlightColor(),
                            null);
                }

                if (playerCargo.getMarines() + addMarines >= maxMarines)
                    addMarines = maxMarines - playerCargo.getMarines();

                if (playerCargo.getCrew() - addMarines <= playerFleetData.getMinCrew())
                    addMarines = (int) (playerCargo.getCrew() - playerFleetData.getMinCrew());

                if (playerCargo.getCrew() <= playerFleetData.getMinCrew() || playerCargo.getMarines() >= maxMarines)
                    addMarines = 0;

                if (addMarines > 0) {
                    playerCargo.addMarines(addMarines);
                    playerCargo.removeCrew(addMarines);
                    Global.getSector().getCampaignUI().addMessage(
                            "%s crew has been trained into marines.",
                            Misc.getTextColor(),
                            addMarines + "",
                            null,
                            Misc.getPositiveHighlightColor(),
                            null);
                }

                isActive = false;
            }
        }
    }
}
