package aptlysimplehullmods.hullmods;

import aptlysimplehullmods.Utils;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.combat.ShipVariantAPI;
import com.fs.starfarer.api.impl.campaign.ids.HullMods;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;

import java.awt.*;
import java.util.Iterator;

public class TargetingTransceiver extends BaseHullMod {
    public static final float WEAPON_RANGE_MOD = 0.1f;
    public static final float AUTOFIRE_ACCURACY_MOD = 0.40f;
    public static float MIN_EFFECTIVE_RANGE = 760f;
    public static float MAX_EFFECTIVE_RANGE = 560f;

    @Override
    public void advanceInCombat(ShipAPI ship, float amount) {
        if (!ship.isAlive()) return;

        MutableShipStatsAPI stats = ship.getMutableStats();
        String key = "targeting_transceiver_" + ship.getId();
        Utils.TargetingTransceiverData data = (Utils.TargetingTransceiverData) Global.getCombatEngine().getCustomData().get(key);
        if (data == null) {
            data = new Utils.TargetingTransceiverData();
            Global.getCombatEngine().getCustomData().put(key, data);
        }

        data.interval.advance(amount);
        if (data.interval.intervalElapsed() || ship == Global.getCombatEngine().getPlayerShip()) {
            float minEffectRange = MIN_EFFECTIVE_RANGE;
            float maxEffectRange = MAX_EFFECTIVE_RANGE;
            if (isSMod(stats)) {
                minEffectRange = MIN_EFFECTIVE_RANGE * 2f + 245f;
                maxEffectRange = MAX_EFFECTIVE_RANGE * 2f + 245f;
            }

            float checkSize = (minEffectRange + maxEffectRange + ship.getCollisionRadius() + 300f) * 2f;
            float bestMag = 0f;

            for (Iterator<Object> itr = Global.getCombatEngine().getShipGrid().getCheckIterator(ship.getLocation(), checkSize, checkSize); itr.hasNext(); ) {
                Object next = itr.next();

                if (!(next instanceof ShipAPI)) continue;

                ShipAPI other = (ShipAPI) next;

                if (ship == other) continue;
                if (other.getOwner() != ship.getOwner()) continue;
                if (other.isHulk()) continue;
                if (!hasTargetingCore(other.getVariant())) continue;

                float radiusSum = (ship.getShieldRadiusEvenIfNoShield() + other.getShieldRadiusEvenIfNoShield()) * 0.75f;
                float dist = Misc.getDistance(ship.getShieldCenterEvenIfNoShield(), other.getShieldCenterEvenIfNoShield()) - radiusSum;

                float mag = 0f;
                if (dist < minEffectRange)
                    mag = 1f;
                else if (dist < minEffectRange + maxEffectRange)
                    mag = 1f - (dist - minEffectRange) / maxEffectRange;

                if (mag > bestMag)
                    bestMag = mag;

                data.mag = bestMag;
            }

            if (ship.getHullSize().ordinal() <= 2) {
                stats.getEnergyWeaponRangeBonus().modifyMult(spec.getId(), 1f + WEAPON_RANGE_MOD * data.mag);
                stats.getBallisticWeaponRangeBonus().modifyMult(spec.getId(), 1f + WEAPON_RANGE_MOD * data.mag);
            } else {
                stats.getAutofireAimAccuracy().modifyMult(spec.getId(), 1f + AUTOFIRE_ACCURACY_MOD * data.mag);
            }
        }

        if (ship == Global.getCombatEngine().getPlayerShip()) {
            String icon = Global.getSettings().getSpriteName("ui", "icon_tactical_escort_package");
            if (data.mag > 0.005f)
                Global.getCombatEngine().maintainStatusForPlayerShip(key, icon, "Targeting Transceiver", Math.round(data.mag * 100f) + "% telemetry quality", false);
            else
                Global.getCombatEngine().maintainStatusForPlayerShip(key, icon, "Targeting Transceiver", "no connection", true);
        }
    }

    @Override
    public void addPostDescriptionSection(TooltipMakerAPI tooltip, HullSize hullSize, ShipAPI ship, float width, boolean isForModSpec) {
        float pad = 3f;
        float oPad = 10f;
        Color b = Misc.getHighlightColor();
        Color good = Misc.getPositiveHighlightColor();

        tooltip.setBulletedListMode("");
        tooltip.addPara("If a %s has a %s and is within %s:", oPad, b, "Friendly ship", "Targeting Core/Unit", 1000 + "su");
        tooltip.setBulletedListMode(" ^ ");
        tooltip.addPara("Increases the autofire aim accuracy by %s if the ship is a Cruiser/Capital ship", pad, good, Math.round(AUTOFIRE_ACCURACY_MOD * 100f) + "%");
        tooltip.addPara("Increases the range of non-missile weapons by %s if the ship is a Frigate/Destroyer", pad, good, Math.round(WEAPON_RANGE_MOD * 100f) + "%");
        tooltip.setBulletedListMode(null);
    }

    @Override
    public void addSModEffectSection(TooltipMakerAPI tooltip, ShipAPI.HullSize hullSize, ShipAPI ship, float width, boolean isForModSpec, boolean isForBuildInList) {
        float oPad = 10f;
        Color good = Misc.getPositiveHighlightColor();

        tooltip.setBulletedListMode(" - ");
        tooltip.addPara("Increases the detection range to %s", oPad, good, 2000 + "su");
        tooltip.setBulletedListMode(null);
    }

    @Override
    public boolean hasSModEffect() {
        return true;
    }

    public boolean hasTargetingCore(ShipVariantAPI variant) {
        return variant.hasHullMod(HullMods.DEDICATED_TARGETING_CORE)
                || variant.hasHullMod(HullMods.INTEGRATED_TARGETING_UNIT)
                || variant.hasHullMod(HullMods.ADVANCED_TARGETING_CORE);
    }
}