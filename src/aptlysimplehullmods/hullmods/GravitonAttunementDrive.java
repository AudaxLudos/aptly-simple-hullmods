package aptlysimplehullmods.hullmods;

import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.impl.hullmods.BaseLogisticsHullMod;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class GravitonAttunementDrive extends BaseLogisticsHullMod {
    public static final float SUPPLIES_PER_MONTH_MULT = 2f;
    private static final Map<Object, Float> FLEET_BURN_MOD = new HashMap<>();

    static {
        FLEET_BURN_MOD.put(HullSize.FRIGATE, 1f);
        FLEET_BURN_MOD.put(HullSize.DESTROYER, 2f);
        FLEET_BURN_MOD.put(HullSize.CRUISER, 3f);
        FLEET_BURN_MOD.put(HullSize.CAPITAL_SHIP, 4f);
    }

    @Override
    public void applyEffectsBeforeShipCreation(ShipAPI.HullSize hullSize, MutableShipStatsAPI stats, String id) {
        stats.getDynamic().getMod("fleet_burn_bonus").modifyFlat(id, FLEET_BURN_MOD.get(hullSize));
        stats.getSensorProfile().modifyFlat(id, FLEET_BURN_MOD.get(hullSize) * 100f);
        stats.getSuppliesPerMonth().modifyMult(id, 1f + SUPPLIES_PER_MONTH_MULT);
    }

    @Override
    public void addPostDescriptionSection(TooltipMakerAPI tooltip, HullSize hullSize, ShipAPI ship, float width, boolean isForModSpec) {
        float pad = 3f;
        float oPad = 10f;
        Color good = Misc.getPositiveHighlightColor();
        Color bad = Misc.getNegativeHighlightColor();

        tooltip.setBulletedListMode(" - ");
        tooltip.addPara("Increases the fleet's maximum burn by %s based on hull size", oPad, good, FLEET_BURN_MOD.get(HullSize.FRIGATE).intValue() + "/"
                + FLEET_BURN_MOD.get(HullSize.DESTROYER).intValue() + "/"
                + FLEET_BURN_MOD.get(HullSize.CRUISER).intValue() + "/"
                + FLEET_BURN_MOD.get(HullSize.CAPITAL_SHIP).intValue());
        tooltip.addPara("Increases the ship's sensor profile by %s based on hull size", pad, bad, Math.round(FLEET_BURN_MOD.get(HullSize.FRIGATE).intValue() * 100f) + "/"
                + Math.round(FLEET_BURN_MOD.get(HullSize.DESTROYER).intValue() * 100f) + "/"
                + Math.round(FLEET_BURN_MOD.get(HullSize.CRUISER).intValue() * 100f) + "/"
                + Math.round(FLEET_BURN_MOD.get(HullSize.CAPITAL_SHIP).intValue() * 100f) + " points");
        tooltip.addPara("Increases the ship's supplies used per month by %s", pad, bad, Math.round(SUPPLIES_PER_MONTH_MULT * 100f) + "%");
        tooltip.setBulletedListMode(null);
    }
}