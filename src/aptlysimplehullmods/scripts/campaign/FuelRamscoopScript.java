package aptlysimplehullmods.scripts.campaign;

import aptlysimplehullmods.hullmods.FuelRamscoop;
import com.fs.starfarer.api.EveryFrameScript;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.CampaignFleetAPI;
import com.fs.starfarer.api.campaign.CargoAPI;
import com.fs.starfarer.api.fleet.FleetMemberAPI;
import com.fs.starfarer.api.util.Misc;

import java.util.List;

public class FuelRamscoopScript implements EveryFrameScript {
    public boolean isActive = false;
    public Long lastDay;

    @Override
    public void advance(float amount) {
        if (Global.getSector().getPlayerFleet() == null)
            return;
        if (Global.getSector().getPlayerFleet().getCargo() == null)
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

            if (Global.getSector().getClock().getElapsedDaysSince(lastDay) >= FuelRamscoop.DAYS_TO_GENERATE_FUEL) {
                for (FleetMemberAPI fleetMember : playerFleetMembers) {
                    if (!fleetMember.getVariant().hasHullMod("ASH_FuelRamscoop"))
                        continue;

                    if (fleetMember.getVariant().getSMods().contains("ASH_FuelRamscoop"))
                        fuelGenerated += FuelRamscoop.SMOD_FUEL_TO_GENERATE.get(fleetMember.getVariant().getHullSize());
                    else
                        fuelGenerated += FuelRamscoop.FUEL_TO_GENERATE.get(fleetMember.getVariant().getHullSize());
                }

                if (playerCargo.getFuel() + fuelGenerated >= playerCargo.getMaxFuel())
                    fuelGenerated = playerCargo.getMaxFuel() - playerCargo.getFuel();

                if (fuelGenerated > 0) {
                    playerCargo.addFuel(fuelGenerated);
                    Global.getSector().getCampaignUI().addMessage(
                            "%s units of fuel has been converted using accumulated interstellar matter",
                            Misc.getTextColor(),
                            Math.round(fuelGenerated) + "",
                            null,
                            Misc.getPositiveHighlightColor(),
                            null);
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
