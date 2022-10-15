package data.hullmods;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.impl.hullmods.BaseLogisticsHullMod;
import com.fs.starfarer.api.ui.Alignment;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;

public class ASH_GravitonAttunementDrive extends BaseLogisticsHullMod {
    public static final float SUPPLIES_PER_MONTH_MULTIPLIER = 2f;
    private static Map<Object, Float> FLEET_BURN_MODIFIER = new HashMap<Object, Float>();
    static {
        FLEET_BURN_MODIFIER.put(HullSize.FRIGATE, 1f);
        FLEET_BURN_MODIFIER.put(HullSize.DESTROYER, 2f);
        FLEET_BURN_MODIFIER.put(HullSize.CRUISER, 3f);
        FLEET_BURN_MODIFIER.put(HullSize.CAPITAL_SHIP, 4f);
    }

    @Override
    public void applyEffectsBeforeShipCreation(ShipAPI.HullSize hullSize, MutableShipStatsAPI stats, String id) {
        stats.getDynamic().getMod("fleet_burn_bonus").modifyFlat(id, (Float) FLEET_BURN_MODIFIER.get(hullSize));
        stats.getSensorProfile().modifyFlat(id, (Float) FLEET_BURN_MODIFIER.get(hullSize) * 100f);
        stats.getSuppliesPerMonth().modifyMult(id, (Float) 1f + SUPPLIES_PER_MONTH_MULTIPLIER);
    }

    @Override
    public void addPostDescriptionSection(TooltipMakerAPI tooltip, HullSize hullSize, ShipAPI ship, float width, boolean isForModSpec) {
        float pad = 3f;
        float opad = 10f;
        Color good = Misc.getPositiveHighlightColor();
        Color bad = Misc.getNegativeHighlightColor();

        tooltip.addSectionHeading("Effects:", Alignment.MID, opad);
        tooltip.setBulletedListMode(" - ");
        tooltip.addPara("Increases the fleet's maximum burn by %s based on hull size", pad, good, Math.round(((Float) FLEET_BURN_MODIFIER.get(HullSize.FRIGATE)).intValue()) + "/"
                + Math.round(((Float) FLEET_BURN_MODIFIER.get(HullSize.DESTROYER)).intValue()) + "/"
                + Math.round(((Float) FLEET_BURN_MODIFIER.get(HullSize.CRUISER)).intValue()) + "/"
                + Math.round(((Float) FLEET_BURN_MODIFIER.get(HullSize.CAPITAL_SHIP)).intValue()));
        tooltip.addPara("Increases the sensor profile by %s based on hull size", pad, bad, Math.round(((Float) FLEET_BURN_MODIFIER.get(HullSize.FRIGATE)).intValue() * 100f) + "/"
                + Math.round(((Float) FLEET_BURN_MODIFIER.get(HullSize.DESTROYER)).intValue() * 100f) + "/"
                + Math.round(((Float) FLEET_BURN_MODIFIER.get(HullSize.CRUISER)).intValue() * 100f) + "/"
                + Math.round(((Float) FLEET_BURN_MODIFIER.get(HullSize.CAPITAL_SHIP)).intValue() * 100f) + " points");
        tooltip.addPara("Increases supplies used per month by %s", pad, bad, Math.round(SUPPLIES_PER_MONTH_MULTIPLIER * 100f) + "%");
        tooltip.setBulletedListMode(null);
    }
}