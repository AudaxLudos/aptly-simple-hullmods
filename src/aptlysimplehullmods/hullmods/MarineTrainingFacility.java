package aptlysimplehullmods.hullmods;

import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.impl.hullmods.BaseLogisticsHullMod;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class MarineTrainingFacility extends BaseLogisticsHullMod {
    public static final int DAYS_TO_GENERATE_MARINES = 7;
    public static Map<HullSize, Integer> MARINES_TO_GENERATE = new HashMap<HullSize, Integer>();

    static {
        MARINES_TO_GENERATE.put(HullSize.FRIGATE, 10);
        MARINES_TO_GENERATE.put(HullSize.DESTROYER, 20);
        MARINES_TO_GENERATE.put(HullSize.CRUISER, 30);
        MARINES_TO_GENERATE.put(HullSize.CAPITAL_SHIP, 50);
    }

    public static Map<HullSize, Integer> MARINES_TO_LEVEL = new HashMap<HullSize, Integer>();

    static {
        MARINES_TO_LEVEL.put(HullSize.FRIGATE, 2);
        MARINES_TO_LEVEL.put(HullSize.DESTROYER, 4);
        MARINES_TO_LEVEL.put(HullSize.CRUISER, 6);
        MARINES_TO_LEVEL.put(HullSize.CAPITAL_SHIP, 10);
    }

    public static Map<HullSize, Integer> SMOD_MARINES_TO_LEVEL = new HashMap<HullSize, Integer>();

    static {
        SMOD_MARINES_TO_LEVEL.put(HullSize.FRIGATE, 3);
        SMOD_MARINES_TO_LEVEL.put(HullSize.DESTROYER, 6);
        SMOD_MARINES_TO_LEVEL.put(HullSize.CRUISER, 9);
        SMOD_MARINES_TO_LEVEL.put(HullSize.CAPITAL_SHIP, 15);
    }

    public static Map<HullSize, Integer> MAX_MARINES_TO_GENERATE = new HashMap<HullSize, Integer>();

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
        float oPad = 10f;
        Color b = Misc.getHighlightColor();
        Color good = Misc.getPositiveHighlightColor();

        tooltip.setBulletedListMode("");
        tooltip.addPara("Every %s:", oPad, b, "week");
        tooltip.setBulletedListMode(" ^ ");
        tooltip.addPara("levels up %s current marines based on hull size", pad, good, MARINES_TO_LEVEL.get(HullSize.FRIGATE) + "/"
                + MARINES_TO_LEVEL.get(HullSize.DESTROYER) + "/"
                + MARINES_TO_LEVEL.get(HullSize.CRUISER) + "/"
                + MARINES_TO_LEVEL.get(HullSize.CAPITAL_SHIP));
        tooltip.addPara("Converts %s crew members into marines based on hull size", pad, good, MARINES_TO_GENERATE.get(HullSize.FRIGATE) + "/"
                + MARINES_TO_GENERATE.get(HullSize.DESTROYER) + "/"
                + MARINES_TO_GENERATE.get(HullSize.CRUISER) + "/"
                + MARINES_TO_GENERATE.get(HullSize.CAPITAL_SHIP));
        tooltip.addPara("Can convert a maximum of %s marines per ship with this hullmod", pad, b, MAX_MARINES_TO_GENERATE.get(HullSize.FRIGATE) + "/"
                + MAX_MARINES_TO_GENERATE.get(HullSize.DESTROYER) + "/"
                + MAX_MARINES_TO_GENERATE.get(HullSize.CRUISER) + "/"
                + MAX_MARINES_TO_GENERATE.get(HullSize.CAPITAL_SHIP));
        tooltip.setBulletedListMode(null);
    }

    @Override
    public void addSModEffectSection(TooltipMakerAPI tooltip, ShipAPI.HullSize hullSize, ShipAPI ship, float width, boolean isForModSpec, boolean isForBuildInList) {
        float oPad = 10f;
        Color b = Misc.getHighlightColor();

        tooltip.setBulletedListMode(" - ");
        tooltip.addPara("Increases the amount of marines to level up by an additional %s based on hull size", oPad, b, "1/2/3/5");
        tooltip.setBulletedListMode(null);
    }

    @Override
    public boolean hasSModEffect() {
        return true;
    }
}
