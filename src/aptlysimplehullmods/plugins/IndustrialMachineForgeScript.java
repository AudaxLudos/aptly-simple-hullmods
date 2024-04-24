package aptlysimplehullmods.plugins;

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
    public boolean isDone() {
        return false;
    }

    @Override
    public boolean runWhilePaused() {
        return false;
    }

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
            float bonusHeavyMachinery = 0f;
            float heavyMachineryToGenerate = 0f;
            float metalsToConsume = 0f;

            if (Global.getSector().getClock().getElapsedDaysSince(lastDay) >= IndustrialMachineForge.DAYS_TO_GENERATE_HEAVY_MACHINERY) {
                for (FleetMemberAPI fleetMember : playerFleetMembers) {
                    if (!fleetMember.getVariant().hasHullMod("ASH_IndustrialMachineForge"))
                        continue;
                    if (fleetMember.isMothballed())
                        continue;

                    heavyMachineryToGenerate += IndustrialMachineForge.HEAVY_MACHINERY_TO_GENERATE.get(fleetMember.getVariant().getHullSize());
                    metalsToConsume += IndustrialMachineForge.METALS_TO_CONSUME.get(fleetMember.getVariant().getHullSize());

                    if (fleetMember.getVariant().getSMods().contains("ASH_IndustrialMachineForge"))
                        bonusHeavyMachinery += IndustrialMachineForge.METALS_TO_CONSUME.get(fleetMember.getVariant().getHullSize()) * 0.25f;
                }

                if (!hasConsumableCommodity(playerCargo, Commodities.METALS, 5f)) {
                    isActive = false;
                    return;
                }

                if (playerCargo.getCommodityQuantity(Commodities.METALS) - metalsToConsume < 0f) {
                    heavyMachineryToGenerate = playerCargo.getCommodityQuantity(Commodities.METALS) / 7f;
                    metalsToConsume = heavyMachineryToGenerate * 7f;
                    if (bonusHeavyMachinery > 0) {
                        heavyMachineryToGenerate *= 1.25f;
                    }
                }

                if (heavyMachineryToGenerate >= 1 && metalsToConsume >= 5 && hasConsumableCommodity(playerCargo, Commodities.METALS, 5f)) {
                    Global.getSector().getCampaignUI().addMessage(
                            "%s units of Heavy Machinery has been refined from %s units of Metals",
                            Misc.getTextColor(),
                            Math.round(heavyMachineryToGenerate) + "",
                            Math.round(metalsToConsume) + "",
                            Misc.getPositiveHighlightColor(),
                            Misc.getHighlightColor());
                    playerCargo.addCommodity(Commodities.HEAVY_MACHINERY, Math.round(heavyMachineryToGenerate));
                    playerCargo.removeCommodity(Commodities.METALS, Math.round(metalsToConsume));
                }

                isActive = false;
            }
        }
    }

    public boolean hasConsumableCommodity(CargoAPI cargo, String commodityId, float minAmount) {
        return cargo.getCommodityQuantity(commodityId) > minAmount;
    }
}
