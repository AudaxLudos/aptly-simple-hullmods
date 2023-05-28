package data.hullmods;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.impl.hullmods.BaseLogisticsHullMod;
import com.fs.starfarer.api.ui.Alignment;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;

public class ASH_MarineTrainingFacility extends BaseLogisticsHullMod {
    public static final int DAYS_TO_GENERATE_MARINES = 7;
    public static final int MARINES_TO_TRAIN_MULTIPLIER = 20;
    public static Map<Object, Integer> MARINES_TO_GENERATE = new HashMap<Object, Integer>();
    static {
        MARINES_TO_GENERATE.put(HullSize.FRIGATE, 10);
        MARINES_TO_GENERATE.put(HullSize.DESTROYER, 20);
        MARINES_TO_GENERATE.put(HullSize.CRUISER, 30);
        MARINES_TO_GENERATE.put(HullSize.CAPITAL_SHIP, 50);
    }
    public static Map<Object, Integer> MAX_MARINES_TO_GENERATE = new HashMap<Object, Integer>();
    static {
        MAX_MARINES_TO_GENERATE.put(HullSize.FRIGATE, 50);
        MAX_MARINES_TO_GENERATE.put(HullSize.DESTROYER, 100);
        MAX_MARINES_TO_GENERATE.put(HullSize.CRUISER, 150);
        MAX_MARINES_TO_GENERATE.put(HullSize.CAPITAL_SHIP, 250);
    }

    // Affects are done in ASH_MarineTrainingFacilityScript.java

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
        tooltip.addPara("levels up %s current marines based on hull size", pad, good,
                Math.round(((Integer) MARINES_TO_GENERATE.get(HullSize.FRIGATE)).intValue() * (MARINES_TO_TRAIN_MULTIPLIER / 100f)) + "/"
                        + Math.round(((Integer) MARINES_TO_GENERATE.get(HullSize.DESTROYER)).intValue() * (MARINES_TO_TRAIN_MULTIPLIER / 100f)) + "/"
                        + Math.round(((Integer) MARINES_TO_GENERATE.get(HullSize.CRUISER)).intValue() * (MARINES_TO_TRAIN_MULTIPLIER / 100f)) + "/"
                        + Math.round(((Integer) MARINES_TO_GENERATE.get(HullSize.CAPITAL_SHIP)).intValue() * (MARINES_TO_TRAIN_MULTIPLIER / 100f)));
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