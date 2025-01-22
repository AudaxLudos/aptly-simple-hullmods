package aptlysimplehullmods.plugins;

import aptlysimplehullmods.Ids;
import aptlysimplehullmods.Utils;
import aptlysimplehullmods.hullmods.MineralRefinery;
import com.fs.starfarer.api.EveryFrameScript;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.CargoAPI;
import com.fs.starfarer.api.fleet.FleetMemberAPI;
import com.fs.starfarer.api.impl.campaign.ids.Commodities;
import com.fs.starfarer.api.util.IntervalUtil;
import com.fs.starfarer.api.util.Misc;

public class MineralRefineryScript implements EveryFrameScript {
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
        if (!Utils.getProductionHullmodActivity(Ids.MINERAL_REFINERY_MEM, false)) {
            this.lastDay = Global.getSector().getClock().getTimestamp();
            return;
        }

        this.timer.advance(amount);
        if (this.timer.intervalElapsed()) {
            CargoAPI playerCargo = Global.getSector().getPlayerFleet().getCargo();
            int shipsWithMod = 0;
            float baseMetalsToMake = 0f;
            float extraMetalsToMake = 0f;
            float baseOreToUse = 0f;
            float extraOreToUse = 0f;

            float baseRareMetalsToMake = 0f;
            float extraRareMetalsToMake = 0f;
            float baseRareOreToUse = 0f;
            float extraRareOreToUse = 0f;
            for (FleetMemberAPI member : Global.getSector().getPlayerFleet().getFleetData().getMembersListCopy()) {
                if (!member.getVariant().hasHullMod(Ids.MINERAL_REFINERY)) {
                    continue;
                }
                ++shipsWithMod;
                if (member.getVariant().getSMods().contains(Ids.MINERAL_REFINERY)) {
                    extraMetalsToMake += MineralRefinery.ALLOYS_TO_GENERATE.get(member.getVariant().getHullSize()) * 1.25f;
                    extraOreToUse += MineralRefinery.MINERALS_TO_CONSUME.get(member.getVariant().getHullSize());
                    extraRareMetalsToMake += MineralRefinery.ALLOYS_TO_GENERATE.get(member.getVariant().getHullSize()) * 1.25f;
                    extraRareOreToUse += MineralRefinery.MINERALS_TO_CONSUME.get(member.getVariant().getHullSize());
                } else {
                    baseMetalsToMake += MineralRefinery.ALLOYS_TO_GENERATE.get(member.getVariant().getHullSize());
                    baseOreToUse += MineralRefinery.MINERALS_TO_CONSUME.get(member.getVariant().getHullSize());
                    baseRareMetalsToMake += MineralRefinery.ALLOYS_TO_GENERATE.get(member.getVariant().getHullSize());
                    baseRareOreToUse += MineralRefinery.MINERALS_TO_CONSUME.get(member.getVariant().getHullSize());
                }
            }

            if (this.lastDay == null || this.shipsWithMod != shipsWithMod || this.shipsWithMod <= 0 || !(Utils.hasConsumableCommodity(Commodities.ORE, 5f) || Utils.hasConsumableCommodity(Commodities.RARE_ORE, 5f))) {
                this.shipsWithMod = shipsWithMod;
                this.lastDay = Global.getSector().getClock().getTimestamp();
                return;
            }

            if (Global.getSector().getClock().getElapsedDaysSince(this.lastDay) >= MineralRefinery.DAYS_TO_GENERATE_ALLOYS) {
                this.lastDay = Global.getSector().getClock().getTimestamp();
                float currentOre = playerCargo.getCommodityQuantity(Commodities.ORE);
                float totalMetalsToMake = baseMetalsToMake + extraMetalsToMake;
                float totalOreToUse = baseOreToUse + extraOreToUse;
                if (currentOre < totalOreToUse) {
                    if (currentOre < extraOreToUse) {
                        totalMetalsToMake = currentOre / 4f;
                        totalOreToUse = totalMetalsToMake * 4f;
                    } else {
                        extraMetalsToMake = extraOreToUse / 4f;
                        baseOreToUse = currentOre - extraOreToUse;
                        baseMetalsToMake = baseOreToUse / 5f;
                        totalMetalsToMake = baseMetalsToMake + extraMetalsToMake;
                        totalOreToUse = extraOreToUse + baseOreToUse;
                    }
                }

                float currentRareOre = playerCargo.getCommodityQuantity(Commodities.RARE_ORE);
                float totalRareMetalsToMake = baseRareMetalsToMake + extraRareMetalsToMake;
                float totalRareOreToUse = baseRareOreToUse + extraRareOreToUse;
                if (currentRareOre < totalRareOreToUse) {
                    if (currentRareOre < extraRareOreToUse) {
                        totalRareMetalsToMake = currentRareOre / 4f;
                        totalRareOreToUse = totalRareMetalsToMake * 4f;
                    } else {
                        extraRareMetalsToMake = extraRareOreToUse / 4f;
                        baseRareOreToUse = currentRareOre - extraRareOreToUse;
                        baseRareMetalsToMake = baseRareOreToUse / 5f;
                        totalRareMetalsToMake = baseRareMetalsToMake + extraRareMetalsToMake;
                        totalRareOreToUse = extraRareOreToUse + baseRareOreToUse;
                    }
                }

                if (totalMetalsToMake >= 1 && totalOreToUse >= 5 && Utils.hasConsumableCommodity(Commodities.ORE, 5f)) {
                    Global.getSector().getCampaignUI().addMessage(
                            "%s units of metals has been refined from %s units of ore",
                            Misc.getTextColor(),
                            Math.round(totalMetalsToMake) + "",
                            Math.round(totalOreToUse) + "",
                            Misc.getPositiveHighlightColor(),
                            Misc.getHighlightColor());
                    playerCargo.addCommodity(Commodities.METALS, Math.round(totalMetalsToMake));
                    playerCargo.removeCommodity(Commodities.ORE, Math.round(totalOreToUse));
                }

                if (totalRareMetalsToMake >= 1 && totalRareOreToUse >= 5 && Utils.hasConsumableCommodity(Commodities.RARE_ORE, 5f)) {
                    Global.getSector().getCampaignUI().addMessage(
                            "%s units of transplutonics has been refined from %s units of rare ore",
                            Misc.getTextColor(),
                            Math.round(totalRareMetalsToMake) + "",
                            Math.round(totalRareOreToUse) + "",
                            Misc.getPositiveHighlightColor(),
                            Misc.getHighlightColor());
                    playerCargo.addCommodity(Commodities.RARE_METALS, Math.round(totalRareMetalsToMake));
                    playerCargo.removeCommodity(Commodities.RARE_ORE, Math.round(totalRareOreToUse));
                }
            }
        }
    }
}
