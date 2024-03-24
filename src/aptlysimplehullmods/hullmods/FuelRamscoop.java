package aptlysimplehullmods.hullmods;

import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.impl.hullmods.BaseLogisticsHullMod;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class FuelRamscoop extends BaseLogisticsHullMod {
    public static final float DAYS_TO_GENERATE_FUEL = 3f;
    public static Map<HullSize, Float> FUEL_TO_GENERATE = new HashMap<>();
    public static Map<HullSize, Float> SMOD_FUEL_TO_GENERATE = new HashMap<>();

    static {
        FUEL_TO_GENERATE.put(HullSize.FRIGATE, 1f);
        FUEL_TO_GENERATE.put(HullSize.DESTROYER, 2f);
        FUEL_TO_GENERATE.put(HullSize.CRUISER, 3f);
        FUEL_TO_GENERATE.put(HullSize.CAPITAL_SHIP, 4f);
        
        SMOD_FUEL_TO_GENERATE.put(HullSize.FRIGATE, 2f);
        SMOD_FUEL_TO_GENERATE.put(HullSize.DESTROYER, 3f);
        SMOD_FUEL_TO_GENERATE.put(HullSize.CRUISER, 5f);
        SMOD_FUEL_TO_GENERATE.put(HullSize.CAPITAL_SHIP, 6f);
    }

    // Affects are done in ASH_FuelRamscoopScript.java

    @Override
    public void addPostDescriptionSection(TooltipMakerAPI tooltip, HullSize hullSize, ShipAPI ship, float width, boolean isForModSpec) {
        float pad = 3f;
        float oPad = 10f;
        Color b = Misc.getHighlightColor();
        Color good = Misc.getPositiveHighlightColor();

        tooltip.setBulletedListMode("");
        tooltip.addPara("Every %s:", oPad, b, "3 days");
        tooltip.setBulletedListMode(" ^ ");
        tooltip.addPara("Generates %s fuel based on hull size", pad, good, FUEL_TO_GENERATE.get(HullSize.FRIGATE).intValue() + "/"
                + FUEL_TO_GENERATE.get(HullSize.DESTROYER).intValue() + "/"
                + FUEL_TO_GENERATE.get(HullSize.CRUISER).intValue() + "/"
                + FUEL_TO_GENERATE.get(HullSize.CAPITAL_SHIP).intValue());
        tooltip.setBulletedListMode(null);
    }

    @Override
    public void addSModEffectSection(TooltipMakerAPI tooltip, ShipAPI.HullSize hullSize, ShipAPI ship, float width, boolean isForModSpec, boolean isForBuildInList) {
        float oPad = 10f;
        Color good = Misc.getPositiveHighlightColor();

        tooltip.setBulletedListMode(" - ");
        tooltip.addPara("Increases the fuel generated by an additional %s based on hull size", oPad, good, "1/1/2/2 fuel");
        tooltip.setBulletedListMode(null);
    }

    @Override
    public boolean hasSModEffect() {
        return true;
    }
}