package aptlysimplehullmods.hullmods;

import aptlysimplehullmods.Utils;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.FighterWingAPI;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;

import java.awt.*;
import java.util.Iterator;

public class FighterTargetingUplink extends BaseHullMod {
    public static float FIGHTER_DAMAGE_MULT = 0.20f;
    public static float MIN_EFFECTIVE_RANGE = 760f;
    public static float MAX_EFFECTIVE_RANGE = 560f;

    @Override
    public void applyEffectsToFighterSpawnedByShip(ShipAPI fighter, ShipAPI ship, String id) {
        MutableShipStatsAPI wingStats = fighter.getMutableStats();
        if (Utils.hasTargetingCore(ship.getVariant())) {
            wingStats.getDamageToCapital().modifyMult(this.spec.getId(), 1f + FIGHTER_DAMAGE_MULT);
            wingStats.getDamageToCruisers().modifyMult(this.spec.getId(), 1f + FIGHTER_DAMAGE_MULT);
            wingStats.getDamageToDestroyers().modifyMult(this.spec.getId(), 1f + FIGHTER_DAMAGE_MULT);
            wingStats.getDamageToFrigates().modifyMult(this.spec.getId(), 1f + FIGHTER_DAMAGE_MULT);
            wingStats.getDamageToFighters().modifyMult(this.spec.getId(), 1f + FIGHTER_DAMAGE_MULT);
            wingStats.getDamageToMissiles().modifyMult(this.spec.getId(), 1f + FIGHTER_DAMAGE_MULT);
        }
    }

    @Override
    public void advanceInCombat(ShipAPI ship, float amount) {
        if (!ship.isAlive()) {
            return;
        }
        if (Utils.hasTargetingCore(ship.getVariant())) {
            if (ship == Global.getCombatEngine().getPlayerShip()) {
                Global.getCombatEngine().maintainStatusForPlayerShip(
                        "targeting_transceiver_" + ship.getId(),
                        "graphics/icons/hullsys/targeting_feed.png",
                        "Fighter Targeting Uplink",
                        "Direct connection",
                        false);
            }
            return;
        }

        MutableShipStatsAPI stats = ship.getMutableStats();
        String key = "targeting_uplink_" + ship.getId();
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

                if (!(next instanceof ShipAPI)) {
                    continue;
                }

                ShipAPI other = (ShipAPI) next;

                if (ship.isFighter()) {
                    continue;
                }
                if (ship == other) {
                    continue;
                }
                if (other.getOwner() != ship.getOwner()) {
                    continue;
                }
                if (other.isHulk()) {
                    continue;
                }
                if (!Utils.hasTargetingCore(other.getVariant())) {
                    continue;
                }

                float radiusSum = (ship.getShieldRadiusEvenIfNoShield() + other.getShieldRadiusEvenIfNoShield()) * 0.75f;
                float dist = Misc.getDistance(ship.getShieldCenterEvenIfNoShield(), other.getShieldCenterEvenIfNoShield()) - radiusSum;

                float mag = 0f;
                if (dist < minEffectRange) {
                    mag = 1f;
                } else if (dist < minEffectRange + maxEffectRange) {
                    mag = 1f - (dist - minEffectRange) / maxEffectRange;
                }

                if (mag > bestMag) {
                    bestMag = mag;
                }

                data.mag = bestMag;
            }

            for (FighterWingAPI wing : ship.getAllWings()) {
                for (ShipAPI wingShip : wing.getWingMembers()) {
                    MutableShipStatsAPI wingStats = wingShip.getMutableStats();
                    wingStats.getDamageToCapital().modifyMult(this.spec.getId(), 1f + FIGHTER_DAMAGE_MULT * bestMag);
                    wingStats.getDamageToCruisers().modifyMult(this.spec.getId(), 1f + FIGHTER_DAMAGE_MULT * bestMag);
                    wingStats.getDamageToDestroyers().modifyMult(this.spec.getId(), 1f + FIGHTER_DAMAGE_MULT * bestMag);
                    wingStats.getDamageToFrigates().modifyMult(this.spec.getId(), 1f + FIGHTER_DAMAGE_MULT * bestMag);
                    wingStats.getDamageToFighters().modifyMult(this.spec.getId(), 1f + FIGHTER_DAMAGE_MULT * bestMag);
                    wingStats.getDamageToMissiles().modifyMult(this.spec.getId(), 1f + FIGHTER_DAMAGE_MULT * bestMag);
                }
            }
        }

        if (ship == Global.getCombatEngine().getPlayerShip()) {
            String icon = "graphics/icons/hullsys/targeting_feed.png";
            if (data.mag > 0.005f) {
                Global.getCombatEngine().maintainStatusForPlayerShip(key, icon, "Fighter Targeting Uplink", Math.round(data.mag * 100f) + "% telemetry quality", false);
            } else {
                Global.getCombatEngine().maintainStatusForPlayerShip(key, icon, "Fighter Targeting Uplink", "no connection", true);
            }
        }
    }

    @Override
    public void addPostDescriptionSection(TooltipMakerAPI tooltip, ShipAPI.HullSize hullSize, ShipAPI ship, float width, boolean isForModSpec) {
        float pad = 3f;
        float oPad = 10f;
        Color b = Misc.getHighlightColor();
        Color good = Misc.getPositiveHighlightColor();

        tooltip.addPara("If a %s has a %s and is within %s OR the %s has a %s:", oPad, b, "Friendly ship", "Targeting Core/Unit", 1000 + "su", "ship", "Targeting Core/Unit");
        tooltip.setBulletWidth(20f);
        tooltip.setBulletedListMode("");
        tooltip.addPara("^ Increases the damage of all fighters by %s", pad, good, Math.round(FIGHTER_DAMAGE_MULT * 100f) + "%");
        tooltip.setBulletedListMode(null);
    }

    @Override
    public void addSModEffectSection(TooltipMakerAPI tooltip, ShipAPI.HullSize hullSize, ShipAPI ship, float width, boolean isForModSpec, boolean isForBuildInList) {
        float oPad = 10f;
        Color good = Misc.getPositiveHighlightColor();

        tooltip.addPara("Increases the detection range to %s.", oPad, good, 2000 + "su");
    }

    @Override
    public boolean hasSModEffect() {
        return true;
    }

    @Override
    public String getUnapplicableReason(ShipAPI ship) {
        return "Ship does not have fighter bays";
    }

    @Override
    public boolean isApplicableToShip(ShipAPI ship) {
        int bays = (int) ship.getMutableStats().getNumFighterBays().getModifiedValue();
        return bays > 0;
    }
}
