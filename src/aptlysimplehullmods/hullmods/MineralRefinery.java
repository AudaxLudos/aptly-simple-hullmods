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
    public static Map<ShipAPI.HullSize, Float> ALLOYS_TO_GENERATE = new HashMap<>();
    public static Map<ShipAPI.HullSize, Float> MINERALS_TO_CONSUME = new HashMap<>();

    static {
        ALLOYS_TO_GENERATE.put(ShipAPI.HullSize.FRIGATE, 80f);
        ALLOYS_TO_GENERATE.put(ShipAPI.HullSize.DESTROYER, 160f);
        ALLOYS_TO_GENERATE.put(ShipAPI.HullSize.CRUISER, 240f);
        ALLOYS_TO_GENERATE.put(ShipAPI.HullSize.CAPITAL_SHIP, 400f);

        MINERALS_TO_CONSUME.put(ShipAPI.HullSize.FRIGATE, 400f);
        MINERALS_TO_CONSUME.put(ShipAPI.HullSize.DESTROYER, 800f);
        MINERALS_TO_CONSUME.put(ShipAPI.HullSize.CRUISER, 1200f);
        MINERALS_TO_CONSUME.put(ShipAPI.HullSize.CAPITAL_SHIP, 2000f);
    }

    // Affects are done in ASH_IndustrialMachineForgeScript.java

    @Override
    public void addPostDescriptionSection(TooltipMakerAPI tooltip, ShipAPI.HullSize hullSize, ShipAPI ship, float width, boolean isForModSpec) {
        float pad = 3f;
        float oPad = 10f;
        Color b = Misc.getHighlightColor();
        Color good = Misc.getPositiveHighlightColor();
        Color bad = Misc.getNegativeHighlightColor();

        tooltip.setBulletedListMode("");
        tooltip.addPara("Every %s and have %s", oPad, b, "3 days", "1 or more heavy machinery:");
        tooltip.setBulletedListMode(" ^ ");
        tooltip.addPara("Generates %s metals/transplutonics based on hull size", pad, good, ALLOYS_TO_GENERATE.get(ShipAPI.HullSize.FRIGATE).intValue() + "/"
                + ALLOYS_TO_GENERATE.get(ShipAPI.HullSize.DESTROYER).intValue() + "/"
                + ALLOYS_TO_GENERATE.get(ShipAPI.HullSize.CRUISER).intValue() + "/"
                + ALLOYS_TO_GENERATE.get(ShipAPI.HullSize.CAPITAL_SHIP).intValue());
        tooltip.addPara("Consumes %s ore/rare ore based on hull size", pad, bad, MINERALS_TO_CONSUME.get(ShipAPI.HullSize.FRIGATE).intValue() + "/"
                + MINERALS_TO_CONSUME.get(ShipAPI.HullSize.DESTROYER).intValue() + "/"
                + MINERALS_TO_CONSUME.get(ShipAPI.HullSize.CRUISER).intValue() + "/"
                + MINERALS_TO_CONSUME.get(ShipAPI.HullSize.CAPITAL_SHIP).intValue());
        tooltip.setBulletedListMode(null);
    }
}
