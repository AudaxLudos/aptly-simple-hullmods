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
    public int shipsWithMod = 0;
    public Long lastDay = null;
    public IntervalUtil timer = new IntervalUtil(0.9f, 1.1f);

    public static float getResourceMadeOrUsed(boolean returnUsed) {
        float baseMachineryToMake = 0f;
        float extraMachineryToMake = 0f;
        float baseMetalsToUse = 0f;
        float extraMetalsToUse = 0f;
        for (FleetMemberAPI member : Global.getSector().getPlayerFleet().getFleetData().getMembersListCopy()) {
            if (member.isMothballed() || !member.getVariant().hasHullMod(Ids.INDUSTRIAL_MACHINE_FORGE)) {
                continue;
            }
            if (member.getVariant().getSMods().contains(Ids.INDUSTRIAL_MACHINE_FORGE)) {
                extraMachineryToMake += IndustrialMachineForge.HEAVY_MACHINERY_TO_GENERATE.get(member.getVariant().getHullSize()) * 1.2f;
                extraMetalsToUse += IndustrialMachineForge.METALS_TO_CONSUME.get(member.getVariant().getHullSize());
            } else {
                baseMachineryToMake += IndustrialMachineForge.HEAVY_MACHINERY_TO_GENERATE.get(member.getVariant().getHullSize());
                baseMetalsToUse += IndustrialMachineForge.METALS_TO_CONSUME.get(member.getVariant().getHullSize());
            }
        }

        float currentMetals = Global.getSector().getPlayerFleet().getCargo().getCommodityQuantity(Commodities.METALS);
        float totalMachineryToMake = baseMachineryToMake + extraMachineryToMake;
        float totalMetalsToUse = baseMetalsToUse + extraMetalsToUse;

        if (currentMetals < totalMetalsToUse) {
            if (currentMetals < extraMetalsToUse) {
                totalMachineryToMake = currentMetals / 5.8333f;
                totalMetalsToUse = totalMachineryToMake * 5.8333f;
            } else {
                extraMachineryToMake = extraMetalsToUse / 5.8333f;
                baseMetalsToUse = currentMetals - extraMetalsToUse;
                baseMachineryToMake = baseMetalsToUse / 7f;
                totalMachineryToMake = baseMachineryToMake + extraMachineryToMake;
                totalMetalsToUse = extraMetalsToUse + baseMetalsToUse;
            }
        }

        return !returnUsed ? totalMachineryToMake : totalMetalsToUse;
    }

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
        if (!Utils.getProductionHullmodActivity(Ids.INDUSTRIAL_MACHINE_FORGE_MEM, false)) {
            this.lastDay = Global.getSector().getClock().getTimestamp();
            return;
        }

        this.timer.advance(amount);
        if (this.timer.intervalElapsed()) {
            CargoAPI playerCargo = Global.getSector().getPlayerFleet().getCargo();
            int shipsWithMod = 0;
            float baseMachineryToMake = 0f;
            float extraMachineryToMake = 0f;
            float baseMetalsToUse = 0f;
            float extraMetalsToUse = 0f;
            for (FleetMemberAPI member : Global.getSector().getPlayerFleet().getFleetData().getMembersListCopy()) {
                if (member.isMothballed() || !member.getVariant().hasHullMod(Ids.INDUSTRIAL_MACHINE_FORGE)) {
                    continue;
                }
                ++shipsWithMod;
                if (member.getVariant().getSMods().contains(Ids.INDUSTRIAL_MACHINE_FORGE)) {
                    extraMachineryToMake += IndustrialMachineForge.HEAVY_MACHINERY_TO_GENERATE.get(member.getVariant().getHullSize()) * 1.2f;
                    extraMetalsToUse += IndustrialMachineForge.METALS_TO_CONSUME.get(member.getVariant().getHullSize());
                } else {
                    baseMachineryToMake += IndustrialMachineForge.HEAVY_MACHINERY_TO_GENERATE.get(member.getVariant().getHullSize());
                    baseMetalsToUse += IndustrialMachineForge.METALS_TO_CONSUME.get(member.getVariant().getHullSize());
                }
            }

            if (this.lastDay == null || this.shipsWithMod != shipsWithMod || this.shipsWithMod <= 0 || !Utils.hasConsumableCommodity(Commodities.METALS, 5f)) {
                this.shipsWithMod = shipsWithMod;
                this.lastDay = Global.getSector().getClock().getTimestamp();
                return;
            }

            if (Global.getSector().getClock().getElapsedDaysSince(this.lastDay) >= IndustrialMachineForge.DAYS_TO_GENERATE_HEAVY_MACHINERY) {
                this.lastDay = Global.getSector().getClock().getTimestamp();
                float currentMetals = playerCargo.getCommodityQuantity(Commodities.METALS);
                float totalMachineryToMake = baseMachineryToMake + extraMachineryToMake;
                float totalMetalsToUse = baseMetalsToUse + extraMetalsToUse;

                if (currentMetals < totalMetalsToUse) {
                    if (currentMetals < extraMetalsToUse) {
                        totalMachineryToMake = currentMetals / 5.8333f;
                        totalMetalsToUse = totalMachineryToMake * 5.8333f;
                    } else {
                        extraMachineryToMake = extraMetalsToUse / 5.8333f;
                        baseMetalsToUse = currentMetals - extraMetalsToUse;
                        baseMachineryToMake = baseMetalsToUse / 7f;
                        totalMachineryToMake = baseMachineryToMake + extraMachineryToMake;
                        totalMetalsToUse = extraMetalsToUse + baseMetalsToUse;
                    }
                }

                if (totalMachineryToMake >= 1 && totalMetalsToUse >= 5 && Utils.hasConsumableCommodity(Commodities.METALS, 5f)) {
                    Global.getSector().getCampaignUI().addMessage(
                            "%s units of Heavy Machinery has been refined from %s units of Metals",
                            Misc.getTextColor(),
                            Math.round(totalMachineryToMake) + "",
                            Math.round(totalMetalsToUse) + "",
                            Misc.getPositiveHighlightColor(),
                            Misc.getHighlightColor());
                    playerCargo.addCommodity(Commodities.HEAVY_MACHINERY, Math.round(totalMachineryToMake));
                    playerCargo.removeCommodity(Commodities.METALS, Math.round(totalMetalsToUse));
                }
            }
        }
    }
}
