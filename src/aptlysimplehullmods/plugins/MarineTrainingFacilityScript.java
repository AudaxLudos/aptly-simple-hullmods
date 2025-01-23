package aptlysimplehullmods.plugins;

import aptlysimplehullmods.Ids;
import aptlysimplehullmods.Utils;
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
    public int shipsWithMod = 0;
    public Long lastDay = null;
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
        if (!Utils.getProductionHullmodActivity(Ids.MARINE_TRAINING_FACILITY_MEM, false)) {
            this.lastDay = Global.getSector().getClock().getTimestamp();
            return;
        }

        this.timer.advance(amount);
        if (this.timer.intervalElapsed()) {
            FleetDataAPI playerFleetData = Global.getSector().getPlayerFleet().getFleetData();
            CargoAPI playerCargo = Global.getSector().getPlayerFleet().getCargo();
            int shipsWithMod = 0;
            int maxMarines = 0;
            int addMarines = 0;
            float trainMarines = 0;
            for (FleetMemberAPI member : Global.getSector().getPlayerFleet().getFleetData().getMembersListCopy()) {
                if (member.isMothballed() || !member.getVariant().hasHullMod(Ids.MARINE_TRAINING_FACILITY)) {
                    continue;
                }
                ++shipsWithMod;
                addMarines += MarineTrainingFacility.MARINES_TO_GENERATE.get(member.getVariant().getHullSize());
                maxMarines += MarineTrainingFacility.MAX_MARINES_TO_GENERATE.get(member.getVariant().getHullSize());
                if (!member.getVariant().getSMods().contains(Ids.MARINE_TRAINING_FACILITY)) {
                    trainMarines += MarineTrainingFacility.MARINES_TO_LEVEL.get(member.getVariant().getHullSize());
                } else {
                    trainMarines += MarineTrainingFacility.SMOD_MARINES_TO_LEVEL.get(member.getVariant().getHullSize());
                }
            }

            if (this.lastDay == null || this.shipsWithMod != shipsWithMod || this.shipsWithMod <= 0 || !((playerCargo.getCrew() > playerFleetData.getMinCrew() && playerCargo.getMarines() < maxMarines) || (playerCargo.getMarines() > 0 && PlayerFleetPersonnelTracker.getInstance().getMarineData().getXPLevel() < 1f))) {
                this.shipsWithMod = shipsWithMod;
                this.lastDay = Global.getSector().getClock().getTimestamp();
                return;
            }

            if (Global.getSector().getClock().getElapsedDaysSince(this.lastDay) >= MarineTrainingFacility.DAYS_TO_GENERATE_MARINES) {
                this.lastDay = Global.getSector().getClock().getTimestamp();
                if (PlayerFleetPersonnelTracker.getInstance().getMarineData().getXPLevel() * playerCargo.getMarines() + trainMarines >= playerCargo.getMarines()) {
                    trainMarines = playerCargo.getMarines() - PlayerFleetPersonnelTracker.getInstance().getMarineData().getXPLevel() * playerCargo.getMarines();
                }

                if (trainMarines > 0 && playerCargo.getMarines() > 0 && PlayerFleetPersonnelTracker.getInstance().getMarineData().getXPLevel() < 1f) {
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

                if (playerCargo.getMarines() + addMarines >= maxMarines) {
                    addMarines = maxMarines - playerCargo.getMarines();
                }

                if (playerCargo.getCrew() - addMarines <= playerFleetData.getMinCrew()) {
                    addMarines = (int) (playerCargo.getCrew() - playerFleetData.getMinCrew());
                }

                if (playerCargo.getCrew() <= playerFleetData.getMinCrew() || playerCargo.getMarines() >= maxMarines) {
                    addMarines = 0;
                }

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
            }
        }
    }
}
