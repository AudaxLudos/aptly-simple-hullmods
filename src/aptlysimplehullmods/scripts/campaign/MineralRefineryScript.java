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

        CampaignFleetAPI pFleet = Global.getSector().getPlayerFleet();
        CargoAPI pCargo = pFleet.getCargo();
        List<FleetMemberAPI> playerFleetMembers = pFleet.getFleetData().getMembersListCopy();

        if (!isActive) {
            for (FleetMemberAPI fleetMember : playerFleetMembers) {
                if (fleetMember.getVariant().hasHullMod("ASH_MineralRefinery")) {
                    isActive = true;
                    lastDay = Global.getSector().getClock().getTimestamp();
                    break;
                }
            }
        } else {
            float bonusMetals = 0f;
            float metalsToGenerate = 0f;
            float oresToConsume = 0f;
            float bonusRareMetals = 0f;
            float rareMetalsToGenerate = 0f;
            float rareOresToConsume = 0f;

            if (Global.getSector().getClock().getElapsedDaysSince(lastDay) >= MineralRefinery.DAYS_TO_GENERATE_ALLOYS) {
                for (FleetMemberAPI fleetMember : playerFleetMembers) {
                    if (!fleetMember.getVariant().hasHullMod("ASH_MineralRefinery"))
                        continue;
                    if (fleetMember.isMothballed())
                        continue;

                    metalsToGenerate += MineralRefinery.ALLOYS_TO_GENERATE.get(fleetMember.getVariant().getHullSize());
                    oresToConsume += MineralRefinery.MINERALS_TO_CONSUME.get(fleetMember.getVariant().getHullSize());

                    rareMetalsToGenerate += MineralRefinery.ALLOYS_TO_GENERATE.get(fleetMember.getVariant().getHullSize());
                    rareOresToConsume += MineralRefinery.MINERALS_TO_CONSUME.get(fleetMember.getVariant().getHullSize());

                    if (fleetMember.getVariant().getSMods().contains("ASH_MineralRefinery")) {
                        metalsToGenerate += MineralRefinery.ALLOYS_TO_GENERATE.get(fleetMember.getVariant().getHullSize()) * 0.25f;
                        bonusMetals += MineralRefinery.ALLOYS_TO_GENERATE.get(fleetMember.getVariant().getHullSize()) * 0.25f;

                        rareMetalsToGenerate += MineralRefinery.ALLOYS_TO_GENERATE.get(fleetMember.getVariant().getHullSize()) * 0.25f;
                        bonusRareMetals += MineralRefinery.ALLOYS_TO_GENERATE.get(fleetMember.getVariant().getHullSize()) * 0.25f;
                    }
                }

                if (!hasConsumableCommodity(pCargo, Commodities.ORE, 5f) && !hasConsumableCommodity(pCargo, Commodities.RARE_ORE, 5f)) {
                    isActive = false;
                    return;
                }

                if (pCargo.getCommodityQuantity(Commodities.ORE) - oresToConsume < -0.0001f) {
                    metalsToGenerate = pCargo.getCommodityQuantity(Commodities.ORE) / 5f;
                    oresToConsume = metalsToGenerate * 5f;
                    if (bonusMetals > 0) {
                        metalsToGenerate *= 1.25f;
                    }
                }

                if (pCargo.getCommodityQuantity(Commodities.RARE_ORE) - rareOresToConsume < -0.0001f) {
                    rareMetalsToGenerate = pCargo.getCommodityQuantity(Commodities.RARE_ORE) / 5f;
                    rareOresToConsume = rareMetalsToGenerate * 5f;
                    if (bonusRareMetals > 0) {
                        rareMetalsToGenerate *= 1.25f;
                    }
                }

                if (metalsToGenerate >= 1 && oresToConsume >= 5 && hasConsumableCommodity(pCargo, Commodities.ORE, 5f)) {
                    Global.getSector().getCampaignUI().addMessage(
                            "%s units of metals has been refined from %s units of ore",
                            Misc.getTextColor(),
                            Math.round(metalsToGenerate) + "",
                            Math.round(oresToConsume) + "",
                            Misc.getPositiveHighlightColor(),
                            Misc.getNegativeHighlightColor());
                    pCargo.addCommodity(Commodities.METALS, Math.round(metalsToGenerate));
                    pCargo.removeCommodity(Commodities.ORE, Math.round(oresToConsume));
                }

                if (rareMetalsToGenerate >= 1 && rareOresToConsume >= 5 && hasConsumableCommodity(pCargo, Commodities.RARE_ORE, 5f)) {
                    Global.getSector().getCampaignUI().addMessage(
                            "%s units of transplutonics has been refined from %s units of rare ore",
                            Misc.getTextColor(),
                            Math.round(rareMetalsToGenerate) + "",
                            Math.round(rareOresToConsume) + "",
                            Misc.getPositiveHighlightColor(),
                            Misc.getHighlightColor());
                    pCargo.addCommodity(Commodities.RARE_METALS, Math.round(rareMetalsToGenerate));
                    pCargo.removeCommodity(Commodities.RARE_ORE, Math.round(rareOresToConsume));
                }

                isActive = false;
            }
        }
    }

    public boolean hasConsumableCommodity(CargoAPI cargo, String commodityId, float minAmount) {
        return cargo.getCommodityQuantity(commodityId) > minAmount;
    }
}
