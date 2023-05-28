package data.scripts.campaign;

import java.util.List;

import com.fs.starfarer.api.EveryFrameScript;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.CampaignFleetAPI;
import com.fs.starfarer.api.campaign.CargoAPI;
import com.fs.starfarer.api.fleet.FleetMemberAPI;
import com.fs.starfarer.api.util.Misc;

import data.hullmods.ASH_FuelRamscoop;

public class ASH_FuelRamscoopScript implements EveryFrameScript {
    public boolean isActive = false;
    public Long lastDay;

    @Override
    public void advance(float amount) {
        if (Global.getSector().getPlayerFleet() == null)
            return;

        CampaignFleetAPI playerFleet = Global.getSector().getPlayerFleet();
        List<FleetMemberAPI> playerFleetMembers = playerFleet.getFleetData().getMembersListCopy();
        CargoAPI playerCargo = playerFleet.getCargo();

        if (!isActive) {
            for (FleetMemberAPI fleetMember : playerFleetMembers) {
                if (fleetMember.getVariant().hasHullMod("ASH_FuelRamscoop")) {
                    isActive = true;
                    lastDay = Global.getSector().getClock().getTimestamp();
                    break;
                }
            }
        } else {
            float fuelGenerated = 0;

            if (Global.getSector().getClock().getElapsedDaysSince(lastDay) >= ASH_FuelRamscoop.DAYS_TO_GENERATE_FUEL) {
                for (FleetMemberAPI fleetMember : playerFleetMembers) {
                    if (fleetMember.getVariant().hasHullMod("ASH_FuelRamscoop"))
                        fuelGenerated += (Float) ASH_FuelRamscoop.FUEL_TO_GENERATE.get(fleetMember.getVariant().getHullSize());
                }

                if (playerCargo.getFuel() + fuelGenerated >= playerCargo.getMaxFuel())
                    fuelGenerated = playerCargo.getMaxFuel() - playerCargo.getFuel();

                if (fuelGenerated > 0) {
                    playerCargo.addFuel(fuelGenerated);
                    Global.getSector().getCampaignUI().addMessage(Math.round(fuelGenerated) + " units of fuel has been converted using accumulated interstellar matter", Misc.getTextColor());
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