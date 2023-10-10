package data.hullmods;

import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.impl.campaign.ids.HullMods;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;

import java.awt.Color;

import org.lazywizard.lazylib.MathUtils;
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
        float maxRangeThreshold = MAX_RANGE_THRESHOLD;

        if (ship.getVariant().getSMods().contains(spec.getId()))
            maxRangeThreshold *= 2f;

        ShipAPI ally = AIUtils.getNearestAlly(ship);

        if (ally == null)
            return;

        float distance = MathUtils.getDistance(ship, ally);

        if (distance <= maxRangeThreshold)
            return;

        if (ally.getVariant().hasHullMod(HullMods.DEDICATED_TARGETING_CORE) ||
                ally.getVariant().hasHullMod(HullMods.INTEGRATED_TARGETING_UNIT) ||
                ally.getVariant().hasHullMod(HullMods.ADVANCED_TARGETING_CORE)) {
            computedRangeBonus = WEAPON_RANGE_MODIFIER;
            computedAutofireAimAccuracy = AUTOFIRE_AIM_ACCURACY_MODIFIER;
        }

        if (ship.getHullSize().ordinal() <= 2) {
            stats.getEnergyWeaponRangeBonus().modifyMult(spec.getId(), 1f + computedRangeBonus);
            stats.getBallisticWeaponRangeBonus().modifyMult(spec.getId(), 1f + computedRangeBonus);
        } else
            stats.getAutofireAimAccuracy().modifyMult(spec.getId(), 1f + computedAutofireAimAccuracy);
    }

    @Override
    public void addPostDescriptionSection(TooltipMakerAPI tooltip, HullSize hullSize, ShipAPI ship, float width, boolean isForModSpec) {
        float pad = 3f;
        float opad = 10f;
        Color b = Misc.getHighlightColor();
        Color good = Misc.getPositiveHighlightColor();

        tooltip.setBulletedListMode("");
        tooltip.addPara("If a %s has a %s and is within %s:", opad, b, "Friendly ship", "Targeting Core/Unit", Math.round(MAX_RANGE_THRESHOLD + 250f) + "su");
        tooltip.setBulletedListMode(" ^ ");
        tooltip.addPara("Increases the autofire aim accuracy by %s if the ship is a Cruiser/Capital ship", pad, good, Math.round(AUTOFIRE_AIM_ACCURACY_MODIFIER * 100f) + "%");
        tooltip.addPara("Increases the range of non-missile weapons by %s if the ship is a Frigate/Destroyer", pad, good, Math.round(WEAPON_RANGE_MODIFIER * 100f) + "%");
        tooltip.setBulletedListMode(null);
    }

    @Override
    public void addSModEffectSection(TooltipMakerAPI tooltip, ShipAPI.HullSize hullSize, ShipAPI ship, float width, boolean isForModSpec, boolean isForBuildInList) {
        float opad = 10f;
        Color good = Misc.getPositiveHighlightColor();

        tooltip.setBulletedListMode(" - ");
        tooltip.addPara("Increases the detection range to %s", opad, good, Math.round((MAX_RANGE_THRESHOLD + 250f) * 2f) + " su");
        tooltip.setBulletedListMode(null);
    }

    @Override
    public boolean hasSModEffect() {
        return true;
    }
}