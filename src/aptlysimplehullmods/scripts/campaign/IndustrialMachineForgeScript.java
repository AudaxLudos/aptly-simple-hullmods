package aptlysimplehullmods.scripts.campaign;

import aptlysimplehullmods.hullmods.IndustrialMachineForge;
import com.fs.starfarer.api.EveryFrameScript;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.CampaignFleetAPI;
import com.fs.starfarer.api.campaign.CargoAPI;
import com.fs.starfarer.api.fleet.FleetMemberAPI;
import com.fs.starfarer.api.impl.campaign.ids.Commodities;
import com.fs.starfarer.api.util.Misc;

import java.util.List;

public class IndustrialMachineForgeScript implements EveryFrameScript {
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
        CargoAPI playerCargo = playerFleet.getCargo();
        List<FleetMemberAPI> playerFleetMembers = playerFleet.getFleetData().getMembersListCopy();

        if (!isActive) {
            for (FleetMemberAPI fleetMember : playerFleetMembers) {
                if (fleetMember.getVariant().hasHullMod("ASH_IndustrialMachineForge")) {
                    isActive = true;
                    lastDay = Global.getSector().getClock().getTimestamp();
                    break;
                }
            }
        } else {
            float heavyMachineryGenerated = 0;
            float metalsConsumed = 0;

            if (Global.getSector().getClock().getElapsedDaysSince(lastDay) >= IndustrialMachineForge.DAYS_TO_GENERATE_HEAVY_MACHINERY) {
                for (FleetMemberAPI fleetMember : playerFleetMembers) {
                    if (!fleetMember.getVariant().hasHullMod("ASH_IndustrialMachineForge"))
                        continue;

                    metalsConsumed += IndustrialMachineForge.METALS_TO_CONSUME.get(fleetMember.getVariant().getHullSize());
                    heavyMachineryGenerated += IndustrialMachineForge.HEAVY_MACHINERY_TO_GENERATE.get(fleetMember.getVariant().getHullSize());
                }

                if (!hasConsumableCommodity(playerCargo, Commodities.METALS, 5f))
                    return;

                if (playerCargo.getCommodityQuantity(Commodities.METALS) - metalsConsumed < 0f) {
                    heavyMachineryGenerated = playerCargo.getCommodityQuantity(Commodities.METALS) / 5f;
                    metalsConsumed = heavyMachineryGenerated * 5f;
                }

                if (heavyMachineryGenerated >= 1 && metalsConsumed >= 5 && hasConsumableCommodity(playerCargo, Commodities.METALS, 5f)) {
                    Global.getSector().getCampaignUI().addMessage(
                            "%s units of transplutonics has been refined from %s units of rare ore",
                            Misc.getTextColor(),
                            Math.round(heavyMachineryGenerated) + "",
                            Math.round(metalsConsumed) + "",
                            Misc.getPositiveHighlightColor(),
                            Misc.getHighlightColor());
                    playerCargo.addCommodity(Commodities.HEAVY_MACHINERY, Math.round(heavyMachineryGenerated));
                    playerCargo.removeCommodity(Commodities.METALS, Math.round(metalsConsumed));
                }

                isActive = false;
            }
        }
    }

    public boolean hasConsumableCommodity(CargoAPI cargo, String commodityId, float minAmount) {
        return cargo.getCommodityQuantity(commodityId) > minAmount;
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
