package aptlysimplehullmods.hullmods;

import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.impl.campaign.ids.Commodities;
import com.fs.starfarer.api.impl.campaign.ids.Stats;
import com.fs.starfarer.api.impl.hullmods.BaseLogisticsHullMod;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class IndustrialMachineForge extends BaseLogisticsHullMod {
    public static final float DAYS_TO_GENERATE_HEAVY_MACHINERY = 3f;
    public static Map<HullSize, Float> HEAVY_MACHINERY_TO_GENERATE = new HashMap<HullSize, Float>();

    static {
        HEAVY_MACHINERY_TO_GENERATE.put(HullSize.FRIGATE, 5f);
        HEAVY_MACHINERY_TO_GENERATE.put(HullSize.DESTROYER, 15f);
        HEAVY_MACHINERY_TO_GENERATE.put(HullSize.CRUISER, 30f);
        HEAVY_MACHINERY_TO_GENERATE.put(HullSize.CAPITAL_SHIP, 50f);
    }

    public static Map<HullSize, Float> METALS_TO_CONSUME = new HashMap<HullSize, Float>();

    static {
        METALS_TO_CONSUME.put(HullSize.FRIGATE, 25f);
        METALS_TO_CONSUME.put(HullSize.DESTROYER, 75f);
        METALS_TO_CONSUME.put(HullSize.CRUISER, 150f);
        METALS_TO_CONSUME.put(HullSize.CAPITAL_SHIP, 250f);
    }

    public static Map<HullSize, Float> HEAVY_MACHINERY_SURVEY_COST_MOD = new HashMap<HullSize, Float>();

    static {
        HEAVY_MACHINERY_SURVEY_COST_MOD.put(HullSize.FRIGATE, 5f);
        HEAVY_MACHINERY_SURVEY_COST_MOD.put(HullSize.DESTROYER, 10f);
        HEAVY_MACHINERY_SURVEY_COST_MOD.put(HullSize.CRUISER, 15f);
        HEAVY_MACHINERY_SURVEY_COST_MOD.put(HullSize.CAPITAL_SHIP, 25f);
    }

    public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {
        if (isSMod(stats))
            stats.getDynamic().getMod(Stats.getSurveyCostReductionId(Commodities.HEAVY_MACHINERY)).modifyFlat(id, HEAVY_MACHINERY_SURVEY_COST_MOD.get(hullSize));
    }

    // Affects are done in ASH_IndustrialMachineForgeScript.java

    @Override
    public void addPostDescriptionSection(TooltipMakerAPI tooltip, HullSize hullSize, ShipAPI ship, float width, boolean isForModSpec) {
        float pad = 3f;
        float opad = 10f;
        Color b = Misc.getHighlightColor();
        Color good = Misc.getPositiveHighlightColor();
        Color bad = Misc.getNegativeHighlightColor();

        tooltip.setBulletedListMode("");
        tooltip.addPara("Every %s and have %s", opad, b, "3 days", "1 or more heavy machinery:");
        tooltip.setBulletedListMode(" ^ ");
        tooltip.addPara("Generates %s based on hull size", pad, good, Math.round(HEAVY_MACHINERY_TO_GENERATE.get(HullSize.FRIGATE).intValue()) + "/"
                + Math.round(HEAVY_MACHINERY_TO_GENERATE.get(HullSize.DESTROYER).intValue()) + "/"
                + Math.round(HEAVY_MACHINERY_TO_GENERATE.get(HullSize.CRUISER).intValue()) + "/"
                + Math.round(HEAVY_MACHINERY_TO_GENERATE.get(HullSize.CAPITAL_SHIP).intValue()) + " heavy machinery");
        tooltip.addPara("Consumes %s based on hull size", pad, bad, Math.round(METALS_TO_CONSUME.get(HullSize.FRIGATE).intValue()) + "/"
                + Math.round(METALS_TO_CONSUME.get(HullSize.DESTROYER).intValue()) + "/"
                + Math.round(METALS_TO_CONSUME.get(HullSize.CRUISER).intValue()) + "/"
                + Math.round(METALS_TO_CONSUME.get(HullSize.CAPITAL_SHIP).intValue()) + " metal");
        tooltip.setBulletedListMode(null);
    }

    @Override
    public void addSModEffectSection(TooltipMakerAPI tooltip, ShipAPI.HullSize hullSize, ShipAPI ship, float width, boolean isForModSpec, boolean isForBuildInList) {
        float opad = 10f;
        Color good = Misc.getPositiveHighlightColor();

        tooltip.setBulletedListMode(" - ");
        tooltip.addPara("Decreases the heavy machinery required to perform surveys by %s", opad, good, Math.round(HEAVY_MACHINERY_SURVEY_COST_MOD.get(HullSize.FRIGATE).intValue()) + "/"
                + Math.round(HEAVY_MACHINERY_SURVEY_COST_MOD.get(HullSize.DESTROYER).intValue()) + "/"
                + Math.round(HEAVY_MACHINERY_SURVEY_COST_MOD.get(HullSize.CRUISER).intValue()) + "/"
                + Math.round(HEAVY_MACHINERY_SURVEY_COST_MOD.get(HullSize.CAPITAL_SHIP).intValue()));
        tooltip.setBulletedListMode(null);
    }

    @Override
    public boolean hasSModEffect() {
        return true;
    }
}