package aptlysimplehullmods.scripts.campaign;

import aptlysimplehullmods.hullmods.MineralRefinery;
import com.fs.starfarer.api.EveryFrameScript;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.CampaignFleetAPI;
import com.fs.starfarer.api.campaign.CargoAPI;
import com.fs.starfarer.api.fleet.FleetMemberAPI;
import com.fs.starfarer.api.impl.campaign.ids.Commodities;
import com.fs.starfarer.api.util.Misc;

import java.util.List;

public class MineralRefineryScript implements EveryFrameScript {
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
                if (fleetMember.getVariant().hasHullMod("ASH_MineralRefinery")) {
                    isActive = true;
                    lastDay = Global.getSector().getClock().getTimestamp();
                    break;
                }
            }
        } else {
            float metalsGenerated = 0f;
            float oreConsumed = 0f;

            float transplutonicsGenerated = 0f;
            float rareOreConsumed = 0f;

            if (Global.getSector().getClock().getElapsedDaysSince(lastDay) >= MineralRefinery.DAYS_TO_GENERATE_ALLOYS) {
                for (FleetMemberAPI fleetMember : playerFleetMembers) {
                    if (!fleetMember.getVariant().hasHullMod("ASH_MineralRefinery"))
                        continue;

                    metalsGenerated += MineralRefinery.ALLOYS_TO_GENERATE.get(fleetMember.getVariant().getHullSize());
                    oreConsumed += MineralRefinery.MINERALS_TO_CONSUME.get(fleetMember.getVariant().getHullSize());

                    transplutonicsGenerated += MineralRefinery.ALLOYS_TO_GENERATE.get(fleetMember.getVariant().getHullSize());
                    rareOreConsumed += MineralRefinery.MINERALS_TO_CONSUME.get(fleetMember.getVariant().getHullSize());
                }

                if (!hasConsumableCommodity(playerCargo, Commodities.ORE, 5f) && !hasConsumableCommodity(playerCargo, Commodities.RARE_ORE, 5f))
                    return;

                if (playerCargo.getCommodityQuantity(Commodities.ORE) - oreConsumed < 0f) {
                    metalsGenerated = playerCargo.getCommodityQuantity(Commodities.ORE) / 5f;
                    oreConsumed = metalsGenerated * 5f;
                }

                if (playerCargo.getCommodityQuantity(Commodities.RARE_ORE) - rareOreConsumed < 0f) {
                    transplutonicsGenerated = playerCargo.getCommodityQuantity(Commodities.RARE_ORE) / 5f;
                    rareOreConsumed = transplutonicsGenerated * 5f;
                }

                if (metalsGenerated >= 1 && oreConsumed >= 5 && hasConsumableCommodity(playerCargo, Commodities.ORE, 5f)) {
                    Global.getSector().getCampaignUI().addMessage(Math.round(metalsGenerated) + " units of metals has been refined from " + Math.round(oreConsumed) + " units of ore", Misc.getTextColor());
                    playerCargo.addCommodity(Commodities.METALS, Math.round(metalsGenerated));
                    playerCargo.removeCommodity(Commodities.ORE, Math.round(oreConsumed));
                }

                if (transplutonicsGenerated >= 1 && rareOreConsumed >= 5 && hasConsumableCommodity(playerCargo, Commodities.RARE_ORE, 5f)) {
                    Global.getSector().getCampaignUI().addMessage(Math.round(transplutonicsGenerated) + " units of transplutonics has been refined from " + Math.round(rareOreConsumed) + " units of rare ore", Misc.getTextColor());
                    playerCargo.addCommodity(Commodities.RARE_METALS, Math.round(transplutonicsGenerated));
                    playerCargo.removeCommodity(Commodities.RARE_ORE, Math.round(rareOreConsumed));
                }

                isActive = false;
            }
        }
    }

    public boolean hasConsumableCommodity(CargoAPI cargo,String commodityId, float minAmount) {
        return cargo.getCommodityQuantity(commodityId) > minAmount;
    }
}
