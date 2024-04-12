package aptlysimplehullmods.hullmods;

import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class MineralRefinery extends BaseHullMod {
    public static final float DAYS_TO_GENERATE_ALLOYS = 3f;
    public static Map<ShipAPI.HullSize, Integer> ALLOYS_TO_GENERATE = new HashMap<>();
    public static Map<ShipAPI.HullSize, Integer> MINERALS_TO_CONSUME = new HashMap<>();

    static {
        ALLOYS_TO_GENERATE.put(ShipAPI.HullSize.FRIGATE, 80);
        ALLOYS_TO_GENERATE.put(ShipAPI.HullSize.DESTROYER, 160);
        ALLOYS_TO_GENERATE.put(ShipAPI.HullSize.CRUISER, 240);
        ALLOYS_TO_GENERATE.put(ShipAPI.HullSize.CAPITAL_SHIP, 400);

        MINERALS_TO_CONSUME.put(ShipAPI.HullSize.FRIGATE, 400);
        MINERALS_TO_CONSUME.put(ShipAPI.HullSize.DESTROYER, 800);
        MINERALS_TO_CONSUME.put(ShipAPI.HullSize.CRUISER, 1200);
        MINERALS_TO_CONSUME.put(ShipAPI.HullSize.CAPITAL_SHIP, 2000);
    }

    // Affects are done in ASH_MineralRefineryScript.java

    @Override
    public void addPostDescriptionSection(TooltipMakerAPI tooltip, ShipAPI.HullSize hullSize, ShipAPI ship, float width, boolean isForModSpec) {
        float pad = 3f;
        float oPad = 10f;
        Color b = Misc.getHighlightColor();
        Color good = Misc.getPositiveHighlightColor();
        Color bad = Misc.getNegativeHighlightColor();

        tooltip.setBulletedListMode("");
        tooltip.addPara("Every %s", oPad, b, "3 days");
        tooltip.setBulletedListMode(" ^ ");
        tooltip.addPara("Generates %s/%s/%s/%s Metals / Transplutonics based on hull size", pad, good,
                ALLOYS_TO_GENERATE.get(ShipAPI.HullSize.FRIGATE).toString(),
                ALLOYS_TO_GENERATE.get(ShipAPI.HullSize.DESTROYER).toString(),
                ALLOYS_TO_GENERATE.get(ShipAPI.HullSize.CRUISER).toString(),
                ALLOYS_TO_GENERATE.get(ShipAPI.HullSize.CAPITAL_SHIP).toString());
        tooltip.addPara("Consumes %s/%s/%s/%s Ores / Rare Ores based on hull size", pad, bad,
                MINERALS_TO_CONSUME.get(ShipAPI.HullSize.FRIGATE).toString(),
                MINERALS_TO_CONSUME.get(ShipAPI.HullSize.DESTROYER).toString(),
                MINERALS_TO_CONSUME.get(ShipAPI.HullSize.CRUISER).toString(),
                MINERALS_TO_CONSUME.get(ShipAPI.HullSize.CAPITAL_SHIP).toString());
        tooltip.setBulletedListMode(null);
    }

    @Override
    public void addSModEffectSection(TooltipMakerAPI tooltip, ShipAPI.HullSize hullSize, ShipAPI ship, float width, boolean isForModSpec, boolean isForBuildInList) {
        float oPad = 10f;
        Color good = Misc.getPositiveHighlightColor();

        tooltip.setBulletedListMode(" - ");
        tooltip.addPara("Increases the Metals / Transplutonics generated by an additional %s/%s/%s/%s based on hull size", oPad, good,
                ((int) (ALLOYS_TO_GENERATE.get(ShipAPI.HullSize.FRIGATE) * 0.25f)) + "",
                ((int) (ALLOYS_TO_GENERATE.get(ShipAPI.HullSize.DESTROYER) * 0.25f)) + "",
                ((int) (ALLOYS_TO_GENERATE.get(ShipAPI.HullSize.CRUISER) * 0.25f)) + "",
                ((int) (ALLOYS_TO_GENERATE.get(ShipAPI.HullSize.CAPITAL_SHIP) * 0.25f)) + "");
        tooltip.setBulletedListMode(null);
    }

    @Override
    public boolean hasSModEffect() {
        return true;
    }
}
