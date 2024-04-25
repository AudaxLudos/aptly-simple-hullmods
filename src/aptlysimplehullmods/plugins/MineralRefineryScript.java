package aptlysimplehullmods.plugins;

import aptlysimplehullmods.hullmods.MineralRefinery;
import com.fs.starfarer.api.EveryFrameScript;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.CargoAPI;
import com.fs.starfarer.api.fleet.FleetMemberAPI;
import com.fs.starfarer.api.impl.campaign.ids.Commodities;
import com.fs.starfarer.api.util.IntervalUtil;
import com.fs.starfarer.api.util.Misc;

public class MineralRefineryScript implements EveryFrameScript {
    public static final String MINERAL_REFINERY_ID = "ASH_MineralRefinery";
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
            int shipsWithMineralRefinery = 0;
            float bonusMetals = 0f;
            float metalsToGenerate = 0f;
            float oresToConsume = 0f;
            float bonusRareMetals = 0f;
            float rareMetalsToGenerate = 0f;
            float rareOresToConsume = 0f;
            for (FleetMemberAPI member : Global.getSector().getPlayerFleet().getFleetData().getMembersListCopy()) {
                if (!member.getVariant().hasHullMod(MINERAL_REFINERY_ID)) continue;
                ++shipsWithMineralRefinery;
                metalsToGenerate += MineralRefinery.ALLOYS_TO_GENERATE.get(member.getVariant().getHullSize());
                oresToConsume += MineralRefinery.MINERALS_TO_CONSUME.get(member.getVariant().getHullSize());
                rareMetalsToGenerate += MineralRefinery.ALLOYS_TO_GENERATE.get(member.getVariant().getHullSize());
                rareOresToConsume += MineralRefinery.MINERALS_TO_CONSUME.get(member.getVariant().getHullSize());
                if (member.getVariant().getSMods().contains(MINERAL_REFINERY_ID)) {
                    metalsToGenerate += MineralRefinery.ALLOYS_TO_GENERATE.get(member.getVariant().getHullSize()) * 0.25f;
                    bonusMetals += MineralRefinery.ALLOYS_TO_GENERATE.get(member.getVariant().getHullSize()) * 0.25f;
                    rareMetalsToGenerate += MineralRefinery.ALLOYS_TO_GENERATE.get(member.getVariant().getHullSize()) * 0.25f;
                    bonusRareMetals += MineralRefinery.ALLOYS_TO_GENERATE.get(member.getVariant().getHullSize()) * 0.25f;
                }
            }

            if (!isActive || shipsWithHullmod != shipsWithMineralRefinery) {
                if (hasConsumableCommodity(playerCargo, Commodities.ORE, 5f) || hasConsumableCommodity(playerCargo, Commodities.RARE_ORE, 5f)) {
                    isActive = true;
                    shipsWithHullmod = shipsWithMineralRefinery;
                    lastDay = Global.getSector().getClock().getTimestamp();
                }
            }

            if (isActive && Global.getSector().getClock().getElapsedDaysSince(lastDay) >= MineralRefinery.DAYS_TO_GENERATE_ALLOYS) {
                if (playerCargo.getCommodityQuantity(Commodities.ORE) - oresToConsume < -0.0001f) {
                    metalsToGenerate = playerCargo.getCommodityQuantity(Commodities.ORE) / 5f;
                    oresToConsume = metalsToGenerate * 5f;
                    if (bonusMetals > 0)
                        metalsToGenerate *= 1.25f;
                }

                if (playerCargo.getCommodityQuantity(Commodities.RARE_ORE) - rareOresToConsume < -0.0001f) {
                    rareMetalsToGenerate = playerCargo.getCommodityQuantity(Commodities.RARE_ORE) / 5f;
                    rareOresToConsume = rareMetalsToGenerate * 5f;
                    if (bonusRareMetals > 0)
                        rareMetalsToGenerate *= 1.25f;
                }

                if (metalsToGenerate >= 1 && oresToConsume >= 5 && hasConsumableCommodity(playerCargo, Commodities.ORE, 5f)) {
                    Global.getSector().getCampaignUI().addMessage(
                            "%s units of metals has been refined from %s units of ore",
                            Misc.getTextColor(),
                            Math.round(metalsToGenerate) + "",
                            Math.round(oresToConsume) + "",
                            Misc.getPositiveHighlightColor(),
                            Misc.getNegativeHighlightColor());
                    playerCargo.addCommodity(Commodities.METALS, Math.round(metalsToGenerate));
                    playerCargo.removeCommodity(Commodities.ORE, Math.round(oresToConsume));
                }

                if (rareMetalsToGenerate >= 1 && rareOresToConsume >= 5 && hasConsumableCommodity(playerCargo, Commodities.RARE_ORE, 5f)) {
                    Global.getSector().getCampaignUI().addMessage(
                            "%s units of transplutonics has been refined from %s units of rare ore",
                            Misc.getTextColor(),
                            Math.round(rareMetalsToGenerate) + "",
                            Math.round(rareOresToConsume) + "",
                            Misc.getPositiveHighlightColor(),
                            Misc.getHighlightColor());
                    playerCargo.addCommodity(Commodities.RARE_METALS, Math.round(rareMetalsToGenerate));
                    playerCargo.removeCommodity(Commodities.RARE_ORE, Math.round(rareOresToConsume));
                }

                isActive = false;
            }
        }
    }

    public boolean hasConsumableCommodity(CargoAPI cargo, String commodityId, float minAmount) {
        return cargo.getCommodityQuantity(commodityId) > minAmount;
    }
}
