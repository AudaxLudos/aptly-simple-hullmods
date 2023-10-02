package data.hullmods;

import java.awt.Color;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.lwjgl.util.vector.Vector2f;

import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.CombatEntityAPI;
import com.fs.starfarer.api.combat.DamageAPI;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.combat.listeners.AdvanceableListener;
import com.fs.starfarer.api.combat.listeners.DamageDealtModifier;
import com.fs.starfarer.api.ui.Alignment;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.IntervalUtil;
import com.fs.starfarer.api.util.Misc;

public class ASH_ConvictedCrewmates extends BaseHullMod {
    public static final float SHIP_PPT_MULT = 0.15f;
    public static final float FRIGATE_PPT_MULT = 2f;
    public static final float DESTROYER_PPT_MULT = 1.5f;
    public static final float MAX_CR_MOD = 0.15f;
    public static final float KILL_TIMER = 30f;
    private static Map<Object, Float> PPT_GAIN = new HashMap<Object, Float>();
    static {
        PPT_GAIN.put(HullSize.FRIGATE, 5f);
        PPT_GAIN.put(HullSize.DESTROYER, 10f);
        PPT_GAIN.put(HullSize.CRUISER, 15f);
        PPT_GAIN.put(HullSize.CAPITAL_SHIP, 25f);
    }

    public static class AP_ConvictedCrewmatesScript implements AdvanceableListener, DamageDealtModifier {
        public static class TargetData {
            public ShipAPI target;
            public float timer = KILL_TIMER;
        }

        public Map<String, TargetData> damagedTargets = new HashMap<String, TargetData>();
        public ShipAPI ship;
        public IntervalUtil killChecker = new IntervalUtil(1f, 1f);
        public float maxPPT = 0f;

        public AP_ConvictedCrewmatesScript(ShipAPI ship) {
            this.ship = ship;
            this.maxPPT = ship.getPeakTimeRemaining();
        }

        @Override
        public void advance(float amount) {
            killChecker.advance(amount);
            if (killChecker.intervalElapsed() && !damagedTargets.isEmpty()) {
                for (Iterator<Map.Entry<String, TargetData>> itr = damagedTargets.entrySet().iterator(); itr.hasNext();) {
                    Map.Entry<String, TargetData> entry = itr.next();
                    entry.getValue().timer -= amount;
                    ShipAPI target = entry.getValue().target;
                    if (target.isHulk()) {
                        float pptHullSizeMult = 1f;
                        if (ship.isFrigate() && target.getHullSize().ordinal() >= 3)
                            pptHullSizeMult = FRIGATE_PPT_MULT;
                        else if (ship.isDestroyer() && target.getHullSize().ordinal() >= 4)
                            pptHullSizeMult = DESTROYER_PPT_MULT;

                        // Guarantee to get peak performance time on kill
                        float currentPPT = ship.getTimeDeployedForCRReduction();
                        if (ship.getTimeDeployedForCRReduction() > maxPPT)
                            currentPPT = ship.getTimeDeployedForCRReduction() - maxPPT;

                        ship.setTimeDeployed(currentPPT - PPT_GAIN.get(target.getHullSpec().getHullSize()) * pptHullSizeMult);
                        itr.remove();
                    } else if (entry.getValue().timer <= 0) {
                        itr.remove();
                    }
                }
            }
        }

        @Override
        public String modifyDamageDealt(Object param, CombatEntityAPI target, DamageAPI damage, Vector2f point, boolean shieldHit) {
            if (!(target instanceof ShipAPI))
                return null;

            ShipAPI targetShip = (ShipAPI) target;

            if (targetShip.isHulk() || targetShip.isFighter())
                return null;
            if (damagedTargets.containsKey(targetShip.getId())) {
                damagedTargets.get(targetShip.getId()).timer = KILL_TIMER;
                return null;
            }

            TargetData data = new TargetData();
            data.target = targetShip;

            damagedTargets.put(targetShip.getId(), data);

            return null;
        }

    }

    @Override
    public void applyEffectsAfterShipCreation(ShipAPI ship, String id) {
        ship.addListener(new AP_ConvictedCrewmatesScript(ship));
    }

    @Override
    public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {
        stats.getPeakCRDuration().modifyMult(id, 1f - SHIP_PPT_MULT);
        stats.getMaxCombatReadiness().modifyFlat(id, -MAX_CR_MOD, "Convicted Crewmates Hullmod");

        if (stats.getVariant().getSMods().contains(id))
            stats.getMaxCombatReadiness().unmodify(id);
    }

    @Override
    public void addPostDescriptionSection(TooltipMakerAPI tooltip, HullSize hullSize, ShipAPI ship, float width, boolean isForModSpec) {
        float pad = 3f;
        float opad = 10f;
        Color b = Misc.getHighlightColor();
        Color bad = Misc.getNegativeHighlightColor();
        Color good = Misc.getPositiveHighlightColor();

        tooltip.addSectionHeading("Effects:", Alignment.MID, opad);
        tooltip.addPara("When contributing to killing an %s:", opad, b, "enemy");
        tooltip.setBulletedListMode(" ^ ");
        tooltip.addPara("Gain %s seconds of peak performance time based on the targets hull size", pad, good, Math.round(((Float) PPT_GAIN.get(HullSize.FRIGATE)).intValue()) + "/"
                + Math.round(((Float) PPT_GAIN.get(HullSize.DESTROYER)).intValue()) + "/"
                + Math.round(((Float) PPT_GAIN.get(HullSize.CRUISER)).intValue()) + "/"
                + Math.round(((Float) PPT_GAIN.get(HullSize.CAPITAL_SHIP)).intValue()));
        tooltip.setBulletedListMode(" - ");
        tooltip.addPara("Frigates gain %s the peak performance time against larger ships", opad, good, FRIGATE_PPT_MULT + " Times");
        tooltip.addPara("Destroyers gain %s the peak performance time against larger ships", pad, good, DESTROYER_PPT_MULT + " Times");
        tooltip.addPara("Targets must die within %s to gain PPT bonuses", pad, good, Math.round(KILL_TIMER) + " seconds");
        tooltip.addPara("Decreases the peak performance time by %s", pad, bad, Math.round(SHIP_PPT_MULT * 100f) + "%");
        tooltip.addPara("Decreases the max combat readiness by %s", pad, bad, Math.round(MAX_CR_MOD * 100f) + "%");
        tooltip.setBulletedListMode(null);

    }

    @Override
    public void addSModEffectSection(TooltipMakerAPI tooltip, ShipAPI.HullSize hullSize, ShipAPI ship, float width, boolean isForModSpec, boolean isForBuildInList) {
        float opad = 10f;

        tooltip.setBulletedListMode(" - ");
        tooltip.addPara("Fully negates the max combat readiness penalty", opad);
        tooltip.setBulletedListMode(null);
    }

    @Override
    public boolean hasSModEffect() {
        return true;
    }
}
