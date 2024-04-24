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
    public static Map<HullSize, Integer> MARINES_TO_GENERATE = new HashMap<>();
    public static Map<HullSize, Integer> MARINES_TO_LEVEL = new HashMap<>();
    public static Map<HullSize, Integer> SMOD_MARINES_TO_LEVEL = new HashMap<>();
    public static Map<HullSize, Integer> MAX_MARINES_TO_GENERATE = new HashMap<>();

    static {
        MARINES_TO_GENERATE.put(HullSize.FRIGATE, 10);
        MARINES_TO_GENERATE.put(HullSize.DESTROYER, 20);
        MARINES_TO_GENERATE.put(HullSize.CRUISER, 30);
        MARINES_TO_GENERATE.put(HullSize.CAPITAL_SHIP, 50);
    }

    static {
        MARINES_TO_LEVEL.put(HullSize.FRIGATE, 2);
        MARINES_TO_LEVEL.put(HullSize.DESTROYER, 4);
        MARINES_TO_LEVEL.put(HullSize.CRUISER, 6);
        MARINES_TO_LEVEL.put(HullSize.CAPITAL_SHIP, 10);
    }

    static {
        SMOD_MARINES_TO_LEVEL.put(HullSize.FRIGATE, 3);
        SMOD_MARINES_TO_LEVEL.put(HullSize.DESTROYER, 6);
        SMOD_MARINES_TO_LEVEL.put(HullSize.CRUISER, 9);
        SMOD_MARINES_TO_LEVEL.put(HullSize.CAPITAL_SHIP, 15);
    }

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

        tooltip.addPara("Every %s:", oPad, b, "week");
        tooltip.setBulletWidth(20f);
        tooltip.setBulletedListMode("");
        tooltip.addPara("^ Levels up %s/%s/%s/%s current marines based on hull size.", pad, good,
                MARINES_TO_LEVEL.get(HullSize.FRIGATE) + "",
                MARINES_TO_LEVEL.get(HullSize.DESTROYER) + "",
                MARINES_TO_LEVEL.get(HullSize.CRUISER) + "",
                MARINES_TO_LEVEL.get(HullSize.CAPITAL_SHIP) + "");
        tooltip.addPara("^ Converts %s/%s/%s/%s crew members into marines\n   based on hull size.", pad, good,
                MARINES_TO_GENERATE.get(HullSize.FRIGATE) + "",
                MARINES_TO_GENERATE.get(HullSize.DESTROYER) + "",
                MARINES_TO_GENERATE.get(HullSize.CRUISER) + "",
                MARINES_TO_GENERATE.get(HullSize.CAPITAL_SHIP) + "");
        tooltip.addPara("^ Can convert a maximum of %s/%s/%s/%s marines per\n   ship with this hullmod.", pad, b,
                MAX_MARINES_TO_GENERATE.get(HullSize.FRIGATE) + "",
                MAX_MARINES_TO_GENERATE.get(HullSize.DESTROYER) + "",
                MAX_MARINES_TO_GENERATE.get(HullSize.CRUISER) + "",
                MAX_MARINES_TO_GENERATE.get(HullSize.CAPITAL_SHIP) + "");
        tooltip.setBulletedListMode(null);
    }

    @Override
    public void addSModEffectSection(TooltipMakerAPI tooltip, ShipAPI.HullSize hullSize, ShipAPI ship, float width, boolean isForModSpec, boolean isForBuildInList) {
        float oPad = 10f;
        Color good = Misc.getPositiveHighlightColor();

        tooltip.addPara("Increases the amount of marines to level up by an additional %s/%s/%s/%s based on hull size.", oPad, good,
                SMOD_MARINES_TO_LEVEL.get(HullSize.FRIGATE) - MARINES_TO_LEVEL.get(HullSize.FRIGATE) + "",
                SMOD_MARINES_TO_LEVEL.get(HullSize.DESTROYER) - MARINES_TO_LEVEL.get(HullSize.DESTROYER) + "",
                SMOD_MARINES_TO_LEVEL.get(HullSize.CRUISER) - MARINES_TO_LEVEL.get(HullSize.CRUISER) + "",
                SMOD_MARINES_TO_LEVEL.get(HullSize.CAPITAL_SHIP) - MARINES_TO_LEVEL.get(HullSize.CAPITAL_SHIP) + "");
    }

    @Override
    public boolean hasSModEffect() {
        return true;
    }
}
