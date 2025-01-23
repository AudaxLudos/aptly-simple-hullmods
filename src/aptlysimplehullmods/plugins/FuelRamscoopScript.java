package aptlysimplehullmods.plugins;

import aptlysimplehullmods.Ids;
import aptlysimplehullmods.Utils;
import aptlysimplehullmods.hullmods.FuelRamscoop;
import com.fs.starfarer.api.EveryFrameScript;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.CargoAPI;
import com.fs.starfarer.api.fleet.FleetMemberAPI;
import com.fs.starfarer.api.util.IntervalUtil;
import com.fs.starfarer.api.util.Misc;

public class FuelRamscoopScript implements EveryFrameScript {
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
        if (!Utils.getProductionHullmodActivity(Ids.FUEL_RAMSCOOP_MEM, false)) {
            this.lastDay = Global.getSector().getClock().getTimestamp();
            return;
        }

        this.timer.advance(amount);
        if (this.timer.intervalElapsed()) {
            CargoAPI playerCargo = Global.getSector().getPlayerFleet().getCargo();
            int shipsWithMod = 0;
            float fuelGenerated = 0;
            for (FleetMemberAPI member : Global.getSector().getPlayerFleet().getFleetData().getMembersListCopy()) {
                if (member.isMothballed() || !member.getVariant().hasHullMod(Ids.FUEL_RAMSCOOP)) {
                    continue;
                }
                ++shipsWithMod;
                if (!member.getVariant().getSMods().contains(Ids.FUEL_RAMSCOOP)) {
                    fuelGenerated += FuelRamscoop.FUEL_TO_GENERATE.get(member.getVariant().getHullSize());
                } else {
                    fuelGenerated += FuelRamscoop.SMOD_FUEL_TO_GENERATE.get(member.getVariant().getHullSize());
                }
            }

            if (this.lastDay == null || this.shipsWithMod != shipsWithMod || this.shipsWithMod <= 0 || playerCargo.getFuel() == playerCargo.getMaxFuel()) {
                this.shipsWithMod = shipsWithMod;
                this.lastDay = Global.getSector().getClock().getTimestamp();
                return;
            }

            if (Global.getSector().getClock().getElapsedDaysSince(this.lastDay) >= FuelRamscoop.DAYS_TO_GENERATE_FUEL) {
                this.lastDay = Global.getSector().getClock().getTimestamp();
                if (playerCargo.getFuel() + fuelGenerated >= playerCargo.getMaxFuel()) {
                    fuelGenerated = playerCargo.getMaxFuel() - playerCargo.getFuel();
                }

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
            }
        }
    }
}
