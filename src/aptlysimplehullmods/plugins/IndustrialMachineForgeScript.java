package aptlysimplehullmods.plugins;

import aptlysimplehullmods.Ids;
import aptlysimplehullmods.Utils;
import aptlysimplehullmods.hullmods.IndustrialMachineForge;
import com.fs.starfarer.api.EveryFrameScript;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.CargoAPI;
import com.fs.starfarer.api.fleet.FleetMemberAPI;
import com.fs.starfarer.api.impl.campaign.ids.Commodities;
import com.fs.starfarer.api.util.IntervalUtil;
import com.fs.starfarer.api.util.Misc;

public class IndustrialMachineForgeScript implements EveryFrameScript {
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
        if (Global.getSector().getPlayerFleet() == null) {
            return;
        }
        if (Global.getSector().getPlayerFleet().getFleetData() == null) {
            return;
        }
        if (Global.getSector().getPlayerFleet().getCargo() == null) {
            return;
        }
        if (!Utils.getProductionHullmodActivity(Ids.INDUSTRIAL_MACHINE_FORGE_MEM, false)) {
            this.isActive = false;
            this.timer.setElapsed(0);
            return;
        }

        this.timer.advance(amount);
        if (this.timer.intervalElapsed()) {
            CargoAPI playerCargo = Global.getSector().getPlayerFleet().getCargo();
            int shipsWithIndustrialMachineForge = 0;
            float heavyMachineryToGenerate = 0f;
            float metalsToConsume = 0f;
            float bonusHeavyMachinery = 0f;
            for (FleetMemberAPI member : Global.getSector().getPlayerFleet().getFleetData().getMembersListCopy()) {
                if (!member.getVariant().hasHullMod(Ids.INDUSTRIAL_MACHINE_FORGE)) {
                    continue;
                }
                ++shipsWithIndustrialMachineForge;
                heavyMachineryToGenerate += IndustrialMachineForge.HEAVY_MACHINERY_TO_GENERATE.get(member.getVariant().getHullSize());
                metalsToConsume += IndustrialMachineForge.METALS_TO_CONSUME.get(member.getVariant().getHullSize());
                if (member.getVariant().getSMods().contains(Ids.INDUSTRIAL_MACHINE_FORGE)) {
                    bonusHeavyMachinery += IndustrialMachineForge.METALS_TO_CONSUME.get(member.getVariant().getHullSize()) * 0.25f;
                }
            }

            if (!this.isActive || this.shipsWithHullmod != shipsWithIndustrialMachineForge) {
                if (hasConsumableCommodity(playerCargo, Commodities.METALS, 5f)) {
                    this.isActive = true;
                    this.shipsWithHullmod = shipsWithIndustrialMachineForge;
                    this.lastDay = Global.getSector().getClock().getTimestamp();
                }
            }

            if (this.isActive && Global.getSector().getClock().getElapsedDaysSince(this.lastDay) >= IndustrialMachineForge.DAYS_TO_GENERATE_HEAVY_MACHINERY) {
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

                this.isActive = false;
            }
        }
    }

    public boolean hasConsumableCommodity(CargoAPI cargo, String commodityId, float minAmount) {
        return cargo.getCommodityQuantity(commodityId) > minAmount;
    }
}
