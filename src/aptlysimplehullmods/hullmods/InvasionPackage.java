package aptlysimplehullmods.hullmods;

import aptlysimplehullmods.plugins.InvasionPackageScript;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipVariantAPI;
import com.fs.starfarer.api.fleet.FleetMemberAPI;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;

import java.awt.*;

public class InvasionPackage extends BaseHullMod {
    public static float PLANETARY_OPERATIONS_MULT = 0.10f;
    public static float PLANETARY_OPERATION_CASUALTIES_MULT = 0.10f;
    public static float CARGO_CAPACITY_MULT = 0.40f;

    @Override
    public void applyEffectsBeforeShipCreation(ShipAPI.HullSize hullSize, MutableShipStatsAPI stats, String id) {
        stats.getCargoMod().modifyMult(id, CARGO_CAPACITY_MULT);
    }

    // other effects are located in InvasionPackageScript file

    @Override
    public void addPostDescriptionSection(TooltipMakerAPI tooltip, ShipAPI.HullSize hullSize, ShipAPI ship, float width, boolean isForModSpec) {
        float pad = 3f;
        float oPad = 10f;
        Color b = Misc.getHighlightColor();
        Color good = Misc.getPositiveHighlightColor();
        Color bad = Misc.getNegativeHighlightColor();

        tooltip.addPara("Increases the effectiveness of ground operations by %s.", oPad, good, Math.round(PLANETARY_OPERATIONS_MULT * 100f) + "%");
        tooltip.addPara("Reduces casualties during ground operations by %s.", pad, good, Math.round(PLANETARY_OPERATION_CASUALTIES_MULT * 100f) + "%");
        tooltip.setBulletWidth(20f);
        tooltip.setBulletedListMode("");
        tooltip.addPara("^ This stat has %s.", pad, b, "diminishing returns");
        tooltip.addPara("^ The Total bonus is %s.", pad, good, Math.round(getComputedStatMultiplier(0) * 100f) + "%");
        if (!ship.getVariant().hasHullMod(spec.getId()))
            tooltip.addPara("^ Adding this to the ship increases it to %s.", pad, good, Math.round(getComputedStatMultiplier(1) * 100f) + "%");
        else
            tooltip.addPara("^ Removing this to the ship decreases it to %s.", pad, bad, Math.round(getComputedStatMultiplier(-1) * 100f) + "%");
        tooltip.setBulletedListMode(null);
        tooltip.addPara("Decreases the ship's cargo capacity by %s.", pad, bad, Math.round(CARGO_CAPACITY_MULT * 100f) + "%");
    }

    public float getComputedStatMultiplier(int offset) {
        int shipsWithInvasionPackage = 0;
        for (FleetMemberAPI member : Global.getSector().getPlayerFleet().getFleetData().getMembersListCopy()) {
            if (member.getVariant().hasHullMod("ash_invasion_package")) {
                ++shipsWithInvasionPackage;
            }
        }
        return InvasionPackageScript.computeStatMultiplier(shipsWithInvasionPackage + offset, PLANETARY_OPERATION_CASUALTIES_MULT);
    }
}
