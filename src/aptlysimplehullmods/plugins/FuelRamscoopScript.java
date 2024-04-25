package aptlysimplehullmods.plugins;

import aptlysimplehullmods.hullmods.FuelRamscoop;
import com.fs.starfarer.api.EveryFrameScript;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.CargoAPI;
import com.fs.starfarer.api.fleet.FleetMemberAPI;
import com.fs.starfarer.api.util.IntervalUtil;
import com.fs.starfarer.api.util.Misc;

public class FuelRamscoopScript implements EveryFrameScript {
    public static final String FUEL_RAMSCOOP_ID = "ASH_FuelRamscoop";
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
            CargoAPI playerCargo = Global.getSector().getPlayerFleet().getCargo();
            int shipsWithFuelRamscoop = 0;
            float fuelGenerated = 0;
            for (FleetMemberAPI member : Global.getSector().getPlayerFleet().getFleetData().getMembersListCopy()) {
                if (!member.getVariant().hasHullMod(FUEL_RAMSCOOP_ID)) continue;
                ++shipsWithFuelRamscoop;
                if (!member.getVariant().getSMods().contains(FUEL_RAMSCOOP_ID))
                    fuelGenerated += FuelRamscoop.FUEL_TO_GENERATE.get(member.getVariant().getHullSize());
                else
                    fuelGenerated += FuelRamscoop.SMOD_FUEL_TO_GENERATE.get(member.getVariant().getHullSize());
            }

            if (!isActive || shipsWithHullmod != shipsWithFuelRamscoop) {
                if (playerCargo.getFuel() < playerCargo.getMaxFuel()) {
                    isActive = true;
                    shipsWithHullmod = shipsWithFuelRamscoop;
                    lastDay = Global.getSector().getClock().getTimestamp();
                }
            }

            if (isActive && Global.getSector().getClock().getElapsedDaysSince(lastDay) >= FuelRamscoop.DAYS_TO_GENERATE_FUEL) {
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
}
