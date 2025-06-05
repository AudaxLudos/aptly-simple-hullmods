package aptlysimplehullmods.hullmods;

import aptlysimplehullmods.Ids;
import aptlysimplehullmods.Utils;
import com.fs.starfarer.api.GameState;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.impl.hullmods.BaseLogisticsHullMod;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector2f;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;

public class MarineTrainingFacility extends BaseLogisticsHullMod {
    public static int DAYS_TO_GENERATE_MARINES = 7;
    public static Map<HullSize, Integer> MARINES_TO_GENERATE = new HashMap<>();
    public static Map<HullSize, Integer> MARINES_TO_LEVEL = new HashMap<>();
    public static Map<HullSize, Integer> SMOD_MARINES_TO_LEVEL = new HashMap<>();
    public static Map<HullSize, Integer> MAX_MARINES_TO_GENERATE = new HashMap<>();

    static {
        MARINES_TO_GENERATE.put(HullSize.FRIGATE, 10);
        MARINES_TO_GENERATE.put(HullSize.DESTROYER, 20);
        MARINES_TO_GENERATE.put(HullSize.CRUISER, 30);
        MARINES_TO_GENERATE.put(HullSize.CAPITAL_SHIP, 50);

        MARINES_TO_LEVEL.put(HullSize.FRIGATE, 2);
        MARINES_TO_LEVEL.put(HullSize.DESTROYER, 4);
        MARINES_TO_LEVEL.put(HullSize.CRUISER, 6);
        MARINES_TO_LEVEL.put(HullSize.CAPITAL_SHIP, 10);

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
        Color bad = Misc.getNegativeHighlightColor();

        tooltip.addPara("Every %s:", oPad, b, "week");
        tooltip.setBulletedListMode("  ^ ");
        tooltip.addPara("Levels up %s/%s/%s/%s current marines based on hull size.", pad, good,
                MARINES_TO_LEVEL.get(HullSize.FRIGATE) + "",
                MARINES_TO_LEVEL.get(HullSize.DESTROYER) + "",
                MARINES_TO_LEVEL.get(HullSize.CRUISER) + "",
                MARINES_TO_LEVEL.get(HullSize.CAPITAL_SHIP) + "");
        tooltip.addPara("Converts %s/%s/%s/%s crew members into marines based on hull size.", pad, good,
                MARINES_TO_GENERATE.get(HullSize.FRIGATE) + "",
                MARINES_TO_GENERATE.get(HullSize.DESTROYER) + "",
                MARINES_TO_GENERATE.get(HullSize.CRUISER) + "",
                MARINES_TO_GENERATE.get(HullSize.CAPITAL_SHIP) + "");
        tooltip.addPara("Can convert a maximum of %s/%s/%s/%s marines per ship with this hullmod based on hull size.", pad, b,
                MAX_MARINES_TO_GENERATE.get(HullSize.FRIGATE) + "",
                MAX_MARINES_TO_GENERATE.get(HullSize.DESTROYER) + "",
                MAX_MARINES_TO_GENERATE.get(HullSize.CRUISER) + "",
                MAX_MARINES_TO_GENERATE.get(HullSize.CAPITAL_SHIP) + "");
        tooltip.setBulletedListMode(null);

        if (!isForModSpec && !Global.CODEX_TOOLTIP_MODE && Global.getCurrentState() == GameState.CAMPAIGN && ship.getVariant().hasHullMod(this.spec.getId())) {
            if (Mouse.getEventButton() == MouseEvent.BUTTON1) {
                Utils.getProductionHullmodActivity(Ids.MARINE_TRAINING_FACILITY_MEM, true);
                Global.getSoundPlayer().playSound("ui_neutrino_detector_on", 0.5f, 1f, Global.getSoundPlayer().getListenerPos(), new Vector2f());
                // Fix bug where pressing special keyboard keys (space, alt, etc.) would trigger mouse events
                Mouse.destroy();
                try {
                    Mouse.create();
                } catch (LWJGLException e) {
                    throw new RuntimeException(e);
                }
            }

            boolean isEnabled = Utils.getProductionHullmodActivity(Ids.MARINE_TRAINING_FACILITY_MEM, false);
            String status = isEnabled ? "Enabled" : "Disabled";
            Color statusColor = isEnabled ? good : bad;

            tooltip.addPara("Status: %s", oPad, statusColor, status);
            tooltip.addPara("%s the hullmod to disable/enable its effects. %s all ships with this hullmod", oPad, Misc.getGrayColor(), Misc.setAlpha(b, 200), "Right-click", "Affects");
        }
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
