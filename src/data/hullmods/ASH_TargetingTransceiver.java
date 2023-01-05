package data.hullmods;

import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.impl.campaign.ids.HullMods;
import com.fs.starfarer.api.ui.Alignment;
import com.fs.starfarer.api.ui.LabelAPI;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;

import data.ASH_Utils;

import java.awt.Color;

import org.lazywizard.lazylib.combat.AIUtils;
import org.lwjgl.input.Keyboard;

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

        if (ship.getVariant().getSMods().contains(spec.getId()) && ASH_Utils.isModEnabled())
            maxRangeThreshold *= 2f;
        for (ShipAPI ally : AIUtils.getNearbyAllies(ship, maxRangeThreshold)) {
            if (ally.isFrigate() || ally.isDestroyer() || ally.isDrone() || ally.isFighter())
                continue;

            if (ally.getVariant().hasHullMod(HullMods.DEDICATED_TARGETING_CORE) ||
                    ally.getVariant().hasHullMod(HullMods.INTEGRATED_TARGETING_UNIT) ||
                    ally.getVariant().hasHullMod(HullMods.ADVANCED_TARGETING_CORE)) {
                computedRangeBonus = WEAPON_RANGE_MODIFIER;
                computedAutofireAimAccuracy = AUTOFIRE_AIM_ACCURACY_MODIFIER;
                break;
            }
        }

        if (ship.getHullSize() == HullSize.FRIGATE || ship.getHullSize() == HullSize.DESTROYER) {
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
        Color story = Misc.getStoryOptionColor();

        if (!ship.getVariant().getSMods().contains(spec.getId()) || !ASH_Utils.isModEnabled()) {
            tooltip.addSectionHeading("Effects:", Alignment.MID, opad);
            tooltip.setBulletedListMode("");
            tooltip.addPara("If a %s has a %s and is within %s:", opad, b, "Cruiser/Capital ship", "Targeting Core/Unit", Math.round(MAX_RANGE_THRESHOLD + 250f) + "su");
            tooltip.setBulletedListMode(" ^ ");
            tooltip.addPara("Increases the autofire aim accuracy by %s if the ship is a Cruiser/Capital ship", pad, good, Math.round(AUTOFIRE_AIM_ACCURACY_MODIFIER * 100f) + "%");
            tooltip.addPara("Increases the range of non-missile weapons by %s if the ship is a Frigate/Destroyer", pad, good, Math.round(WEAPON_RANGE_MODIFIER * 100f) + "%");
            tooltip.setBulletedListMode(null);

            if (!ASH_Utils.isModEnabled())
                return;

            if (!Keyboard.isKeyDown(Keyboard.getKeyIndex("F1"))) {
                tooltip.addPara("Press F1 to show S-mod effects", Misc.getGrayColor(), opad);
                return;
            }
        }

        tooltip.addSectionHeading("S-Mod Effects:", story, Misc.setAlpha(story, 110), Alignment.MID, opad);
        tooltip.setBulletedListMode("");
        LabelAPI label = tooltip.addPara("If a %s has a %s and is within %s:", opad, b, "Cruiser/Capital ship", "Targeting Core/Unit", Math.round(MAX_RANGE_THRESHOLD * 2f + 500f) + "su");
        label.setHighlightColors(b, b, good);
        tooltip.setBulletedListMode(" ^ ");
        tooltip.addPara("Increases the autofire aim accuracy by %s if the ship is a Cruiser/Capital ship", pad, good, Math.round(AUTOFIRE_AIM_ACCURACY_MODIFIER * 100f) + "%");
        tooltip.addPara("Increases the range of non-missile weapons by %s if the ship is a Frigate/Destroyer", pad, good, Math.round(WEAPON_RANGE_MODIFIER * 100f) + "%");
        tooltip.setBulletedListMode(null);
    }
}