package data.hullmods;

import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.impl.campaign.ids.HullMods;
import com.fs.starfarer.api.ui.Alignment;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;

import java.awt.Color;

import org.lazywizard.lazylib.combat.AIUtils;

public class ASH_TargetingTransceiver extends BaseHullMod {
    public static final float WEAPON_RANGE_MODIFIER = 0.10f;
    public static final float AUTOFIRE_AIM_ACCURACY_MODIFIER = 0.40f;
    public static final float MAX_RANGE_THRESHOLD = 750f;

    @Override
    public void advanceInCombat(ShipAPI ship, float amount) {
        if (!ship.isAlive())
            return;

        MutableShipStatsAPI stats = ship.getMutableStats();
        float computedRangeBonus = 0f;
        float computedAutofireAimAccuracy = 0f;

        for (ShipAPI ally : AIUtils.getNearbyAllies(ship, MAX_RANGE_THRESHOLD)) {
            if (ally.isFrigate() || ally.isDestroyer() || ally.isDrone() || ally.isFighter())
                continue;

            if (ally.getVariant().hasHullMod(HullMods.DEDICATED_TARGETING_CORE) ||
                    ally.getVariant().hasHullMod(HullMods.INTEGRATED_TARGETING_UNIT) ||
                    ally.getVariant().hasHullMod(HullMods.ADVANCED_TARGETING_CORE)) {
                computedRangeBonus = WEAPON_RANGE_MODIFIER;
                computedAutofireAimAccuracy = AUTOFIRE_AIM_ACCURACY_MODIFIER;
            }
        }

        if (ship.getHullSize() == HullSize.FRIGATE || ship.getHullSize() == HullSize.DESTROYER) {
            stats.getEnergyWeaponRangeBonus().modifyMult(spec.getId(), 1f + computedRangeBonus);
            stats.getBallisticWeaponRangeBonus().modifyMult(spec.getId(), 1f + computedRangeBonus);
        } else
            stats.getAutofireAimAccuracy().modifyMult(spec.getId(), 1f + computedAutofireAimAccuracy);
    }

    @Override
    public String getDescriptionParam(int index, HullSize hullSize) {
        if (index == 0)
            return Math.round(WEAPON_RANGE_MODIFIER) + "%";
        if (index == 1)
            return Math.round(AUTOFIRE_AIM_ACCURACY_MODIFIER) + "%";
        if (index == 2)
            return Math.round(MAX_RANGE_THRESHOLD + 250f) + "su";
        if (index == 3)
            return "Targeting Core / Unit";
        return null;
    }

    @Override
    public void addPostDescriptionSection(TooltipMakerAPI tooltip, HullSize hullSize, ShipAPI ship, float width, boolean isForModSpec) {
        float pad = 3f;
        float opad = 10f;
        Color b = Misc.getHighlightColor();
        Color bad = Misc.getNegativeHighlightColor();
        Color good = Misc.getPositiveHighlightColor();

        tooltip.addSectionHeading("Effects:", Alignment.MID, opad);
        tooltip.setBulletedListMode("");
        tooltip.addPara("If a %s has a %s and is within %s:", opad, b, "Capital or Cruise ship", "Targeting Core / Unit", Math.round(MAX_RANGE_THRESHOLD + 250f) + "su");
        tooltip.setBulletedListMode(" ^ ");
        tooltip.addPara("Increases the autoaim accuracy of Capital and Cruise ship's by %s", pad, good, Math.round(AUTOFIRE_AIM_ACCURACY_MODIFIER * 100f) + "%");
        tooltip.addPara("Increases the range of non-missile weapons of destroyers and frigates by %s", pad, good, Math.round(WEAPON_RANGE_MODIFIER * 100f) + "%");
        tooltip.setBulletedListMode(null);
    }
}