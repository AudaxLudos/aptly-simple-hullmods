package aptlysimplehullmods.hullmods;

import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;

import java.awt.*;

public class InvasionPackage extends BaseHullMod {
    public static float PLANETARY_OPERATIONS_MULT = 0.05f;
    public static float PLANETARY_OPERATION_CASUALTIES_MULT = 0.05f;
    public static float CARGO_CAPACITY_MULT = 0.40f;

    @Override
    public void applyEffectsBeforeShipCreation(ShipAPI.HullSize hullSize, MutableShipStatsAPI stats, String id) {
        stats.getCargoMod().modifyMult(id, 1f - CARGO_CAPACITY_MULT);
    }

    // other effects are located in InvasionPackageScript file

    @Override
    public void addPostDescriptionSection(TooltipMakerAPI tooltip, ShipAPI.HullSize hullSize, ShipAPI ship, float width, boolean isForModSpec) {
        float pad = 3f;
        float oPad = 10f;
        Color b = Misc.getHighlightColor();
        Color good = Misc.getPositiveHighlightColor();
        Color bad = Misc.getNegativeHighlightColor();

        tooltip.setBulletedListMode(" - ");
        tooltip.addPara("Increases the effectiveness of ground operations of the fleet by %s", oPad, good, Math.round(PLANETARY_OPERATIONS_MULT * 100f) + "%");
        tooltip.addPara("Decreases the marine casualties during ground operations of the fleet by %s", pad, good, Math.round(PLANETARY_OPERATION_CASUALTIES_MULT * 100f) + "%");
        tooltip.addPara("Decreases the ship's cargo capacity by %s", pad, bad, Math.round(CARGO_CAPACITY_MULT * 100f) + "%");
        tooltip.setBulletedListMode(null);
    }
}
