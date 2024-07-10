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

public class IndustrialMachineForge extends BaseLogisticsHullMod {
    public static float DAYS_TO_GENERATE_HEAVY_MACHINERY = 3f;
    public static Map<HullSize, Integer> HEAVY_MACHINERY_TO_GENERATE = new HashMap<>();
    public static Map<HullSize, Integer> METALS_TO_CONSUME = new HashMap<>();

    static {
        HEAVY_MACHINERY_TO_GENERATE.put(HullSize.FRIGATE, 5);
        HEAVY_MACHINERY_TO_GENERATE.put(HullSize.DESTROYER, 15);
        HEAVY_MACHINERY_TO_GENERATE.put(HullSize.CRUISER, 30);
        HEAVY_MACHINERY_TO_GENERATE.put(HullSize.CAPITAL_SHIP, 50);

        METALS_TO_CONSUME.put(HullSize.FRIGATE, 35);
        METALS_TO_CONSUME.put(HullSize.DESTROYER, 105);
        METALS_TO_CONSUME.put(HullSize.CRUISER, 210);
        METALS_TO_CONSUME.put(HullSize.CAPITAL_SHIP, 350);
    }

    // Affects are done in ASH_IndustrialMachineForgeScript.java

    @Override
    public void addPostDescriptionSection(TooltipMakerAPI tooltip, HullSize hullSize, ShipAPI ship, float width, boolean isForModSpec) {
        float pad = 3f;
        float oPad = 10f;
        Color b = Misc.getHighlightColor();
        Color good = Misc.getPositiveHighlightColor();
        Color bad = Misc.getNegativeHighlightColor();

        tooltip.addPara("Every %s:", oPad, b, "3 days");
        tooltip.setBulletWidth(20f);
        tooltip.setBulletedListMode("");
        tooltip.addPara("^ Constructs %s/%s/%s/%s Heavy Machinery\n   based on hull size.", pad, good,
                HEAVY_MACHINERY_TO_GENERATE.get(HullSize.FRIGATE).toString(),
                HEAVY_MACHINERY_TO_GENERATE.get(HullSize.DESTROYER).toString(),
                HEAVY_MACHINERY_TO_GENERATE.get(HullSize.CRUISER).toString(),
                HEAVY_MACHINERY_TO_GENERATE.get(HullSize.CAPITAL_SHIP).toString());
        tooltip.addPara("^ Consumes %s/%s/%s/%s Metals based on hull size.", pad, bad,
                METALS_TO_CONSUME.get(HullSize.FRIGATE).toString(),
                METALS_TO_CONSUME.get(HullSize.DESTROYER).toString(),
                METALS_TO_CONSUME.get(HullSize.CRUISER).toString(),
                METALS_TO_CONSUME.get(HullSize.CAPITAL_SHIP).toString());
        tooltip.setBulletedListMode(null);

        if (!isForModSpec && Global.getCurrentState() == GameState.CAMPAIGN && ship.getVariant().hasHullMod(this.spec.getId())) {
            if (Mouse.getEventButton() == MouseEvent.BUTTON1) {
                Utils.getProductionHullmodActivity(Ids.INDUSTRIAL_MACHINE_FORGE_MEM, true);
                Global.getSoundPlayer().playSound("ui_neutrino_detector_on", 0.5f, 1f, Global.getSoundPlayer().getListenerPos(), new Vector2f());
                // Fix bug where pressing special keyboard keys (space, alt, etc.) would trigger mouse events
                Mouse.destroy();
                try {
                    Mouse.create();
                } catch (LWJGLException e) {
                    throw new RuntimeException(e);
                }
            }

            boolean isEnabled = Utils.getProductionHullmodActivity(Ids.INDUSTRIAL_MACHINE_FORGE_MEM, false);
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

        tooltip.addPara("Increases the Heavy Machinery generated by an additional %s/%s/%s/%s based on hull size.", oPad, good,
                ((int) (HEAVY_MACHINERY_TO_GENERATE.get(ShipAPI.HullSize.FRIGATE) * 0.25f)) + "",
                ((int) (HEAVY_MACHINERY_TO_GENERATE.get(ShipAPI.HullSize.DESTROYER) * 0.25f)) + "",
                ((int) (HEAVY_MACHINERY_TO_GENERATE.get(ShipAPI.HullSize.CRUISER) * 0.25f)) + "",
                ((int) (HEAVY_MACHINERY_TO_GENERATE.get(ShipAPI.HullSize.CAPITAL_SHIP) * 0.25f)) + "");
    }

    @Override
    public boolean hasSModEffect() {
        return true;
    }
}