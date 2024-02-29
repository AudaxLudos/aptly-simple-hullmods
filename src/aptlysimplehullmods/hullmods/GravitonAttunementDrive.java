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
    public static final float SUPPLIES_PER_MONTH_MULTIPLIER = 2f;
    private static final Map<Object, Float> FLEET_BURN_MODIFIER = new HashMap<Object, Float>();

    static {
        FLEET_BURN_MODIFIER.put(HullSize.FRIGATE, 1f);
        FLEET_BURN_MODIFIER.put(HullSize.DESTROYER, 2f);
        FLEET_BURN_MODIFIER.put(HullSize.CRUISER, 3f);
        FLEET_BURN_MODIFIER.put(HullSize.CAPITAL_SHIP, 4f);
    }

    @Override
    public void applyEffectsBeforeShipCreation(ShipAPI.HullSize hullSize, MutableShipStatsAPI stats, String id) {
        stats.getDynamic().getMod("fleet_burn_bonus").modifyFlat(id, FLEET_BURN_MODIFIER.get(hullSize));
        stats.getSensorProfile().modifyFlat(id, FLEET_BURN_MODIFIER.get(hullSize) * 100f);
        stats.getSuppliesPerMonth().modifyMult(id, 1f + SUPPLIES_PER_MONTH_MULTIPLIER);
    }

    @Override
    public void addPostDescriptionSection(TooltipMakerAPI tooltip, HullSize hullSize, ShipAPI ship, float width, boolean isForModSpec) {
        float pad = 3f;
        float oPad = 10f;
        Color good = Misc.getPositiveHighlightColor();
        Color bad = Misc.getNegativeHighlightColor();

        tooltip.setBulletedListMode(" - ");
        tooltip.addPara("Increases the fleet's maximum burn by %s based on hull size", oPad, good, FLEET_BURN_MODIFIER.get(HullSize.FRIGATE).intValue() + "/"
                + FLEET_BURN_MODIFIER.get(HullSize.DESTROYER).intValue() + "/"
                + FLEET_BURN_MODIFIER.get(HullSize.CRUISER).intValue() + "/"
                + FLEET_BURN_MODIFIER.get(HullSize.CAPITAL_SHIP).intValue());
        tooltip.addPara("Increases the sensor profile by %s based on hull size", pad, bad, Math.round(FLEET_BURN_MODIFIER.get(HullSize.FRIGATE).intValue() * 100f) + "/"
                + Math.round(FLEET_BURN_MODIFIER.get(HullSize.DESTROYER).intValue() * 100f) + "/"
                + Math.round(FLEET_BURN_MODIFIER.get(HullSize.CRUISER).intValue() * 100f) + "/"
                + Math.round(FLEET_BURN_MODIFIER.get(HullSize.CAPITAL_SHIP).intValue() * 100f) + " points");
        tooltip.addPara("Increases supplies used per month by %s", pad, bad, Math.round(SUPPLIES_PER_MONTH_MULTIPLIER * 100f) + "%");
        tooltip.setBulletedListMode(null);
    }
}