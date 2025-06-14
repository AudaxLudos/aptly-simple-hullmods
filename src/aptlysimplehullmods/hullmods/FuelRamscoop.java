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

public class FuelRamscoop extends BaseLogisticsHullMod {
    public static float DAYS_TO_GENERATE_FUEL = 3f;
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
        Color bad = Misc.getNegativeHighlightColor();

        tooltip.addPara("Every %s:", oPad, b, "3 days");
        tooltip.setBulletedListMode("  ^ ");
        tooltip.addPara("Generates %s/%s/%s/%s fuel based on hull size.", pad, good,
                FUEL_TO_GENERATE.get(HullSize.FRIGATE).intValue() + "",
                FUEL_TO_GENERATE.get(HullSize.DESTROYER).intValue() + "",
                FUEL_TO_GENERATE.get(HullSize.CRUISER).intValue() + "",
                FUEL_TO_GENERATE.get(HullSize.CAPITAL_SHIP).intValue() + "");
        tooltip.setBulletedListMode(null);

        if (!isForModSpec && !Global.CODEX_TOOLTIP_MODE && Global.getCurrentState() == GameState.CAMPAIGN && ship.getVariant().hasHullMod(this.spec.getId())) {
            if (Mouse.getEventButton() == MouseEvent.BUTTON1) {
                Utils.getProductionHullmodActivity(Ids.FUEL_RAMSCOOP_MEM, true);
                Global.getSoundPlayer().playSound("ui_neutrino_detector_on", 0.5f, 1f, Global.getSoundPlayer().getListenerPos(), new Vector2f());
                // Fix bug where pressing special keyboard keys (space, alt, etc.) would trigger mouse events
                Mouse.destroy();
                try {
                    Mouse.create();
                } catch (LWJGLException e) {
                    throw new RuntimeException(e);
                }
            }

            boolean isEnabled = Utils.getProductionHullmodActivity(Ids.FUEL_RAMSCOOP_MEM, false);
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

        tooltip.addPara("Increases the fuel generated by an additional %s/%s/%s/%s fuel based on hull size.", oPad, good,
                1 + "", 1 + "", 2 + "", 2 + "");
    }

    @Override
    public boolean hasSModEffect() {
        return true;
    }
}