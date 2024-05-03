package aptlysimplehullmods.hullmods;

import aptlysimplehullmods.plugins.InvasionPackageScript;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.fleet.FleetMemberAPI;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class InvasionPackage extends BaseHullMod {
    public static final Map<Object, Float> PLANETARY_OPERATION_CASUALTIES_MULT = new HashMap<>();
    public static final Map<Object, Float> PLANETARY_OPERATIONS_MULT = new HashMap<>();
    public static float CARGO_CAPACITY_MULT = 0.40f;

    static {
        PLANETARY_OPERATIONS_MULT.put(ShipAPI.HullSize.FRIGATE, 0.02f);
        PLANETARY_OPERATIONS_MULT.put(ShipAPI.HullSize.DESTROYER, 0.04f);
        PLANETARY_OPERATIONS_MULT.put(ShipAPI.HullSize.CRUISER, 0.06f);
        PLANETARY_OPERATIONS_MULT.put(ShipAPI.HullSize.CAPITAL_SHIP, 0.10f);

        PLANETARY_OPERATION_CASUALTIES_MULT.put(ShipAPI.HullSize.FRIGATE, 0.02f);
        PLANETARY_OPERATION_CASUALTIES_MULT.put(ShipAPI.HullSize.DESTROYER, 0.04f);
        PLANETARY_OPERATION_CASUALTIES_MULT.put(ShipAPI.HullSize.CRUISER, 0.06f);
        PLANETARY_OPERATION_CASUALTIES_MULT.put(ShipAPI.HullSize.CAPITAL_SHIP, 0.10f);
    }

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

        tooltip.addPara("Increases the effectiveness of ground operations by %s/%s/%s/%s.", oPad, good,
                Math.round(PLANETARY_OPERATIONS_MULT.get(ShipAPI.HullSize.FRIGATE) * 100f) + "%",
                Math.round(PLANETARY_OPERATIONS_MULT.get(ShipAPI.HullSize.DESTROYER) * 100f) + "%",
                Math.round(PLANETARY_OPERATIONS_MULT.get(ShipAPI.HullSize.CRUISER) * 100f) + "%",
                Math.round(PLANETARY_OPERATIONS_MULT.get(ShipAPI.HullSize.CAPITAL_SHIP) * 100f) + "%");
        tooltip.addPara("Reduces casualties during ground operations by %s/%s/%s/%s.", pad, good,
                Math.round(PLANETARY_OPERATION_CASUALTIES_MULT.get(ShipAPI.HullSize.FRIGATE) * 100f) + "%",
                Math.round(PLANETARY_OPERATION_CASUALTIES_MULT.get(ShipAPI.HullSize.DESTROYER) * 100f) + "%",
                Math.round(PLANETARY_OPERATION_CASUALTIES_MULT.get(ShipAPI.HullSize.CRUISER) * 100f) + "%",
                Math.round(PLANETARY_OPERATION_CASUALTIES_MULT.get(ShipAPI.HullSize.CAPITAL_SHIP) * 100f) + "%");
        tooltip.addPara("Decreases the ship's cargo capacity by %s.", pad, bad, Math.round(CARGO_CAPACITY_MULT * 100f) + "%");

        tooltip.addPara("Both buffs has %s.", oPad, b, "diminishing returns");
        if (!isForModSpec) {
            tooltip.addPara("The Total bonus for both buffs is %s.", pad, good, Math.round(getStatMultiplier(0) * 100f) + "%");
            if (!ship.getVariant().hasHullMod(spec.getId()))
                tooltip.addPara("Adding this hullmod increases both buffs to %s.", pad, good, Math.round(getStatMultiplier(PLANETARY_OPERATIONS_MULT.get(hullSize)) * 100f) + "%");
            else
                tooltip.addPara("Removing this hullmod decreases both buffs to %s.", pad, bad, Math.round(getStatMultiplier(-PLANETARY_OPERATIONS_MULT.get(hullSize)) * 100f) + "%");
        }
    }

    public float getStatMultiplier(float statOffset) {
        float totalStat = 0;
        for (FleetMemberAPI member : Global.getSector().getPlayerFleet().getFleetData().getMembersListCopy()) {
            if (member.isMothballed()) continue;
            if (member.getVariant().hasHullMod(InvasionPackageScript.INVASION_PACKAGE_ID))
                totalStat += PLANETARY_OPERATION_CASUALTIES_MULT.get(member.getVariant().getHullSize());
        }

        return InvasionPackageScript.computeStatMultiplier(totalStat + statOffset);
    }
}
