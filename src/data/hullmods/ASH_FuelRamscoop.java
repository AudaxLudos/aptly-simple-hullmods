package data.hullmods;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

import org.lwjgl.input.Keyboard;

import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.impl.hullmods.BaseLogisticsHullMod;
import com.fs.starfarer.api.ui.Alignment;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;

import data.ASH_Utils;

public class ASH_FuelRamscoop extends BaseLogisticsHullMod {
    public static final float DAYS_TO_GENERATE_FUEL = 3f;
    public static Map<HullSize, Float> FUEL_TO_GENERATE = new HashMap<HullSize, Float>();
    static {
        FUEL_TO_GENERATE.put(HullSize.FRIGATE, 1f);
        FUEL_TO_GENERATE.put(HullSize.DESTROYER, 2f);
        FUEL_TO_GENERATE.put(HullSize.CRUISER, 3f);
        FUEL_TO_GENERATE.put(HullSize.CAPITAL_SHIP, 4f);
    }
    public static Map<HullSize, Float> SMOD_FUEL_TO_GENERATE = new HashMap<HullSize, Float>();
    static {
        SMOD_FUEL_TO_GENERATE.put(HullSize.FRIGATE, 2f);
        SMOD_FUEL_TO_GENERATE.put(HullSize.DESTROYER, 3f);
        SMOD_FUEL_TO_GENERATE.put(HullSize.CRUISER, 5f);
        SMOD_FUEL_TO_GENERATE.put(HullSize.CAPITAL_SHIP, 6f);
    }

    // Affects are done in ASH_FuelRamscoopScript.java

    @Override
    public void addPostDescriptionSection(TooltipMakerAPI tooltip, HullSize hullSize, ShipAPI ship, float width, boolean isForModSpec) {
        float pad = 3f;
        float opad = 10f;
        Color b = Misc.getHighlightColor();
        Color good = Misc.getPositiveHighlightColor();
        Color story = Misc.getStoryOptionColor();

        if (ship == null || !ship.getVariant().getSMods().contains(spec.getId()) || !ASH_Utils.isModEnabled()) {
            tooltip.addSectionHeading("Effects:", Alignment.MID, opad);
            tooltip.setBulletedListMode("");
            tooltip.addPara("Every %s:", opad, b, "3 days");
            tooltip.setBulletedListMode(" ^ ");
            tooltip.addPara("Generates %s based on hull size", pad, good, Math.round(((Float) FUEL_TO_GENERATE.get(HullSize.FRIGATE)).intValue()) + "/"
                    + Math.round(((Float) FUEL_TO_GENERATE.get(HullSize.DESTROYER)).intValue()) + "/"
                    + Math.round(((Float) FUEL_TO_GENERATE.get(HullSize.CRUISER)).intValue()) + "/"
                    + Math.round(((Float) FUEL_TO_GENERATE.get(HullSize.CAPITAL_SHIP)).intValue()) + " fuel");
            tooltip.setBulletedListMode(null);

            if (!ASH_Utils.isModEnabled())
                return;
            if (!Keyboard.isKeyDown(Keyboard.KEY_F1)) {
                tooltip.addPara("Hold F1 to show S-mod effects", Misc.getGrayColor(), opad);
                return;
            }
        }

        tooltip.addSectionHeading("S-Mod Effects:", story, Misc.setAlpha(story, 110), Alignment.MID, opad);
        tooltip.setBulletedListMode("");
        tooltip.addPara("Every %s:", opad, b, "3 days");
        tooltip.setBulletedListMode(" ^ ");
        tooltip.addPara("Generates %s based on hull size", pad, good, Math.round(((Float) SMOD_FUEL_TO_GENERATE.get(HullSize.FRIGATE)).intValue()) + "/"
                + Math.round(((Float) SMOD_FUEL_TO_GENERATE.get(HullSize.DESTROYER)).intValue()) + "/"
                + Math.round(((Float) SMOD_FUEL_TO_GENERATE.get(HullSize.CRUISER)).intValue()) + "/"
                + Math.round(((Float) SMOD_FUEL_TO_GENERATE.get(HullSize.CAPITAL_SHIP)).intValue()) + " fuel");
        tooltip.setBulletedListMode(null);
    }
}