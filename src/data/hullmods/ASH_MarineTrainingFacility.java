package data.hullmods;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.fleet.FleetMemberAPI;
import com.fs.starfarer.api.impl.PlayerFleetPersonnelTracker;
import com.fs.starfarer.api.impl.hullmods.BaseLogisticsHullMod;
import com.fs.starfarer.api.ui.Alignment;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;

public class ASH_MarineTrainingFacility extends BaseLogisticsHullMod {
    public static final int DAYS_TO_GENERATE_MARINES = 7;
    private static Map<Object, Integer> MARINES_TO_GENERATE = new HashMap<Object, Integer>();
    static {
        MARINES_TO_GENERATE.put(HullSize.FRIGATE, 10);
        MARINES_TO_GENERATE.put(HullSize.DESTROYER, 20);
        MARINES_TO_GENERATE.put(HullSize.CRUISER, 30);
        MARINES_TO_GENERATE.put(HullSize.CAPITAL_SHIP, 50);
    }
    private static Map<Object, Integer> MAX_MARINES_TO_GENERATE = new HashMap<Object, Integer>();
    static {
        MAX_MARINES_TO_GENERATE.put(HullSize.FRIGATE, 50);
        MAX_MARINES_TO_GENERATE.put(HullSize.DESTROYER, 100);
        MAX_MARINES_TO_GENERATE.put(HullSize.CRUISER, 150);
        MAX_MARINES_TO_GENERATE.put(HullSize.CAPITAL_SHIP, 250);
    }
    private long lastDay = Global.getSector().getClock().getTimestamp();

    @Override
    public void advanceInCampaign(FleetMemberAPI member, float amount) {
        long currentDay = Global.getSector().getClock().getTimestamp();
        int maxMarines = 0;
        int addMarines = 0;
        float trainMarines = 0;

        if (Global.getSector().getClock().getElapsedDaysSince(lastDay) >= DAYS_TO_GENERATE_MARINES) {
            lastDay = currentDay;

            if (member.getFleetData() == null || member.getFleetData().getFleet() == null || !member.getFleetData().getFleet().isPlayerFleet())
                return;

            if (member.getFleetData().getFleet().getCargo() == null || member.getFleetData().getFleet().getCargo().getCrew() <= member.getFleetData().getMinCrew())
                return;

            for (FleetMemberAPI fleetMember : member.getFleetData().getMembersListCopy()) {
                if (fleetMember.getVariant().hasHullMod("ASH_MarineTrainingFacility")) {
                    addMarines += (Integer) MARINES_TO_GENERATE.get(fleetMember.getVariant().getHullSize());
                    maxMarines += (Integer) MAX_MARINES_TO_GENERATE.get(fleetMember.getVariant().getHullSize());
                    trainMarines += Math.round((Integer) MARINES_TO_GENERATE.get(fleetMember.getVariant().getHullSize()) / 2);
                }
            }

            if (PlayerFleetPersonnelTracker.getInstance().getMarineData().getXPLevel() * member.getFleetData().getFleet().getCargo().getMarines() + trainMarines >= member.getFleetData().getFleet()
                    .getCargo().getMarines())
                trainMarines = member.getFleetData().getFleet().getCargo().getMarines() - PlayerFleetPersonnelTracker.getInstance().getMarineData().getXPLevel() *
                        member.getFleetData().getFleet().getCargo().getMarines();

            if (trainMarines > 0 && member.getFleetData().getFleet().getCargo().getMarines() > 0) {
                Global.getSector().getCampaignUI().addMessage(Math.round(trainMarines) + " existing marines has been trained.", Misc.getTextColor());
                PlayerFleetPersonnelTracker.getInstance().getMarineData().addXP(Math.round(trainMarines));
                PlayerFleetPersonnelTracker.getInstance().update();
            }

            if (member.getFleetData().getFleet().getCargo().getMarines() >= maxMarines)
                return;

            if (member.getFleetData().getFleet().getCargo().getMarines() + addMarines >= maxMarines)
                addMarines = maxMarines - (int) member.getFleetData().getFleet().getCargo().getMarines();

            if (member.getFleetData().getFleet().getCargo().getCrew() - addMarines <= member.getFleetData().getMinCrew())
                addMarines = (int) (member.getFleetData().getFleet().getCargo().getCrew() - member.getFleetData().getMinCrew());

            if (addMarines > 0) {
                member.getFleetData().getFleet().getCargo().addMarines(addMarines);
                member.getFleetData().getFleet().getCargo().removeCrew(addMarines);
                Global.getSector().getCampaignUI().addMessage(addMarines + " crew has been trained into marines.", Misc.getTextColor());
            }

        }
    }

    @Override
    public void addPostDescriptionSection(TooltipMakerAPI tooltip, HullSize hullSize, ShipAPI ship, float width, boolean isForModSpec) {
        float pad = 3f;
        float opad = 10f;
        Color b = Misc.getHighlightColor();
        Color good = Misc.getPositiveHighlightColor();

        tooltip.addSectionHeading("Effects:", Alignment.MID, opad);
        tooltip.setBulletedListMode("");
        tooltip.addPara("Every %s:", opad, b, "week");
        tooltip.setBulletedListMode(" ^ ");
        tooltip.addPara("levels up %s current marines based on hull size", pad, good, Math.round(((Integer) MARINES_TO_GENERATE.get(HullSize.FRIGATE)).intValue()) / 2 + "/"
                + Math.round(((Integer) MARINES_TO_GENERATE.get(HullSize.DESTROYER)).intValue()) / 2 + "/"
                + Math.round(((Integer) MARINES_TO_GENERATE.get(HullSize.CRUISER)).intValue()) / 2 + "/"
                + Math.round(((Integer) MARINES_TO_GENERATE.get(HullSize.CAPITAL_SHIP)).intValue()) / 2);
        tooltip.addPara("Converts %s crew members into marines based on hull size", pad, good, Math.round(((Integer) MARINES_TO_GENERATE.get(HullSize.FRIGATE)).intValue()) + "/"
                + Math.round(((Integer) MARINES_TO_GENERATE.get(HullSize.DESTROYER)).intValue()) + "/"
                + Math.round(((Integer) MARINES_TO_GENERATE.get(HullSize.CRUISER)).intValue()) + "/"
                + Math.round(((Integer) MARINES_TO_GENERATE.get(HullSize.CAPITAL_SHIP)).intValue()));
        tooltip.addPara("Can convert a maximum of %s marines per ship with this hullmod", pad, b, Math.round(((Integer) MAX_MARINES_TO_GENERATE.get(HullSize.FRIGATE)).intValue()) + "/"
                + Math.round(((Integer) MAX_MARINES_TO_GENERATE.get(HullSize.DESTROYER)).intValue()) + "/"
                + Math.round(((Integer) MAX_MARINES_TO_GENERATE.get(HullSize.CRUISER)).intValue()) + "/"
                + Math.round(((Integer) MAX_MARINES_TO_GENERATE.get(HullSize.CAPITAL_SHIP)).intValue()));
        tooltip.setBulletedListMode(null);
    }
}