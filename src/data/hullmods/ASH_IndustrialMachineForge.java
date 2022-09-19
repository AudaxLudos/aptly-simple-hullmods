package data.hullmods;

import java.util.HashMap;
import java.util.Map;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.fleet.FleetMemberAPI;
import com.fs.starfarer.api.impl.campaign.ids.Commodities;
import com.fs.starfarer.api.impl.hullmods.BaseLogisticsHullMod;
import com.fs.starfarer.api.util.Misc;

public class ASH_IndustrialMachineForge extends BaseLogisticsHullMod {
    public static final float DAYS_TO_GENERATE_HEAVY_MACHINERY = 3f;
    private static Map<Object, Float> HEAVY_MACHINERY_TO_GENERATE = new HashMap<Object, Float>();
    static {
        HEAVY_MACHINERY_TO_GENERATE.put(HullSize.FRIGATE, 5f);
        HEAVY_MACHINERY_TO_GENERATE.put(HullSize.DESTROYER, 15f);
        HEAVY_MACHINERY_TO_GENERATE.put(HullSize.CRUISER, 30f);
        HEAVY_MACHINERY_TO_GENERATE.put(HullSize.CAPITAL_SHIP, 50f);
    }
    private static Map<Object, Float> METALS_TO_CONSUME = new HashMap<Object, Float>();
    static {
        METALS_TO_CONSUME.put(HullSize.FRIGATE, 25f);
        METALS_TO_CONSUME.put(HullSize.DESTROYER, 75f);
        METALS_TO_CONSUME.put(HullSize.CRUISER, 150f);
        METALS_TO_CONSUME.put(HullSize.CAPITAL_SHIP, 250f);
    }
    private long lastDay = Global.getSector().getClock().getTimestamp();

    @Override
    public String getDescriptionParam(int index, HullSize hullSize) {
        if (index == 0)
            return Math.round(((Float)HEAVY_MACHINERY_TO_GENERATE.get(HullSize.FRIGATE)).intValue()) + "/"
                + Math.round(((Float)HEAVY_MACHINERY_TO_GENERATE.get(HullSize.DESTROYER)).intValue()) + "/"
                + Math.round(((Float)HEAVY_MACHINERY_TO_GENERATE.get(HullSize.CRUISER)).intValue()) + "/"
                + Math.round(((Float)HEAVY_MACHINERY_TO_GENERATE.get(HullSize.CAPITAL_SHIP)).intValue());
        if (index == 1)
            return Math.round(((Float)METALS_TO_CONSUME.get(HullSize.FRIGATE)).intValue()) + "/"
                + Math.round(((Float)METALS_TO_CONSUME.get(HullSize.DESTROYER)).intValue()) + "/"
                + Math.round(((Float)METALS_TO_CONSUME.get(HullSize.CRUISER)).intValue()) + "/"
                + Math.round(((Float)METALS_TO_CONSUME.get(HullSize.CAPITAL_SHIP)).intValue());
        if (index == 2)
            return Math.round(DAYS_TO_GENERATE_HEAVY_MACHINERY) + " days";
        return null;
    }

    @Override
    public void advanceInCampaign(FleetMemberAPI member, float amount) {
        long currentDay = Global.getSector().getClock().getTimestamp();
        float heavyMachineryGenerated = 0;
        float metalsConsumed = 0;

        if (Global.getSector().getClock().getElapsedDaysSince(lastDay) >= DAYS_TO_GENERATE_HEAVY_MACHINERY) {
            lastDay = currentDay;

            if (member.getFleetData() == null || member.getFleetData().getFleet() == null)
                return;

            if (member.getFleetData().getFleet().getCargo() == null)
                return;

            for (FleetMemberAPI fleetMember : member.getFleetData().getMembersListCopy()) {
                if (fleetMember.getVariant().hasHullMod("ASH_TestHullmod")) {
                    heavyMachineryGenerated += (Float)HEAVY_MACHINERY_TO_GENERATE.get(fleetMember.getVariant().getHullSize());
                    metalsConsumed += (Float)METALS_TO_CONSUME.get(fleetMember.getVariant().getHullSize());
                }
            }

            if (member.getFleetData().getFleet().getCargo().getCommodityQuantity(Commodities.METALS) < 5f
                && member.getFleetData().getFleet().getCargo().getCommodityQuantity(Commodities.HEAVY_MACHINERY) > 0f)
                return;
            if (member.getFleetData().getFleet().getCargo().getCommodityQuantity(Commodities.METALS) < metalsConsumed) {
                heavyMachineryGenerated = member.getFleetData().getFleet().getCargo().getCommodityQuantity(Commodities.METALS) / 5f;
                metalsConsumed = heavyMachineryGenerated * 5f;
            }
            if (heavyMachineryGenerated > 0 && metalsConsumed > 0)
                Global.getSector().getCampaignUI().addMessage(Math.round(metalsConsumed) + " metals have been constructed into " + Math.round(heavyMachineryGenerated) + " heavy machinery",
                    Misc.getTextColor());

            member.getFleetData().getFleet().getCargo().addCommodity(Commodities.HEAVY_MACHINERY, Math.round(heavyMachineryGenerated));
            member.getFleetData().getFleet().getCargo().removeCommodity(Commodities.METALS, Math.round(metalsConsumed));
        }
    }
}