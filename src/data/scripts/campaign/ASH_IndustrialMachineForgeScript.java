package data.scripts.campaign;

import java.util.List;

import com.fs.starfarer.api.EveryFrameScript;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.CampaignFleetAPI;
import com.fs.starfarer.api.campaign.CargoAPI;
import com.fs.starfarer.api.fleet.FleetMemberAPI;
import com.fs.starfarer.api.impl.campaign.ids.Commodities;
import com.fs.starfarer.api.util.Misc;

import data.hullmods.ASH_IndustrialMachineForge;

public class ASH_IndustrialMachineForgeScript implements EveryFrameScript {
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

            if (Global.getSector().getClock().getElapsedDaysSince(lastDay) >= ASH_IndustrialMachineForge.DAYS_TO_GENERATE_HEAVY_MACHINERY) {
                for (FleetMemberAPI fleetMember : playerFleetMembers) {
                    if (!fleetMember.getVariant().hasHullMod("ASH_IndustrialMachineForge"))
                        continue;

                    metalsConsumed += (Float) ASH_IndustrialMachineForge.METALS_TO_CONSUME.get(fleetMember.getVariant().getHullSize());
                    heavyMachineryGenerated += (Float) ASH_IndustrialMachineForge.HEAVY_MACHINERY_TO_GENERATE.get(fleetMember.getVariant().getHullSize());
                }

                if (playerCargo.getCommodityQuantity(Commodities.METALS) < 5f
                        && playerCargo.getCommodityQuantity(Commodities.HEAVY_MACHINERY) < 5f)
                    return;

                if (playerCargo.getCommodityQuantity(Commodities.METALS) - metalsConsumed < 100f) {
                    heavyMachineryGenerated = (playerCargo.getCommodityQuantity(Commodities.METALS) - 100f) / 5f;
                    metalsConsumed = heavyMachineryGenerated * 5f;
                }

                if (heavyMachineryGenerated >= 1 && metalsConsumed >= 5) {
                    Global.getSector().getCampaignUI().addMessage(
                            Math.round(heavyMachineryGenerated) + " units of heavy machinery has been constructed using " + Math.round(metalsConsumed) + " units of metals", Misc.getTextColor());
                    playerCargo.addCommodity(Commodities.HEAVY_MACHINERY, Math.round(heavyMachineryGenerated));
                    playerCargo.removeCommodity(Commodities.METALS, Math.round(metalsConsumed));
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
