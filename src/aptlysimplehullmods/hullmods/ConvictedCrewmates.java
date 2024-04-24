package aptlysimplehullmods.hullmods;

import com.fs.starfarer.api.combat.*;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.combat.listeners.AdvanceableListener;
import com.fs.starfarer.api.combat.listeners.DamageDealtModifier;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.IntervalUtil;
import com.fs.starfarer.api.util.Misc;
import org.lwjgl.util.vector.Vector2f;

import java.awt.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ConvictedCrewmates extends BaseHullMod {
    public static final float FRIGATE_PPT_MULT = 2f;
    public static final float DESTROYER_PPT_MULT = 1.5f;
    public static final float MAX_CR_MOD = 0.15f;
    public static final float KILL_TIMER = 30f;
    private static final Map<Object, Float> PPT_GAIN = new HashMap<>();

    static {
        PPT_GAIN.put(HullSize.FRIGATE, 5f);
        PPT_GAIN.put(HullSize.DESTROYER, 10f);
        PPT_GAIN.put(HullSize.CRUISER, 15f);
        PPT_GAIN.put(HullSize.CAPITAL_SHIP, 25f);
    }

    @Override
    public void applyEffectsAfterShipCreation(ShipAPI ship, String id) {
        ship.addListener(new AP_ConvictedCrewmatesScript(ship));
    }

    @Override
    public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {
        stats.getMaxCombatReadiness().modifyFlat(id, -MAX_CR_MOD, "Convicted Crewmates Hullmod");
    }

    @Override
    public void addPostDescriptionSection(TooltipMakerAPI tooltip, HullSize hullSize, ShipAPI ship, float width, boolean isForModSpec) {
        float pad = 3f;
        float oPad = 10f;
        Color b = Misc.getHighlightColor();
        Color bad = Misc.getNegativeHighlightColor();
        Color good = Misc.getPositiveHighlightColor();

        tooltip.addPara("When contributing to %s an enemy:", oPad, b, "killing");
        tooltip.setBulletWidth(20f);
        tooltip.setBulletedListMode("");
        tooltip.addPara("^ Gain %s/%s/%s/%s seconds of peak performance time\n   based on the targets hull size.", pad, good,
                PPT_GAIN.get(HullSize.FRIGATE).intValue() + "",
                PPT_GAIN.get(HullSize.DESTROYER).intValue() + "",
                PPT_GAIN.get(HullSize.CRUISER).intValue() + "",
                PPT_GAIN.get(HullSize.CAPITAL_SHIP).intValue() + "");
        tooltip.addPara("^ Frigates gain %s the peak performance time\n   against larger ships.", pad, good, FRIGATE_PPT_MULT + " Times");
        tooltip.addPara("^ Destroyers gain %s the peak performance time\n   against larger ships.", pad, good, DESTROYER_PPT_MULT + " Times");
        tooltip.addPara("^ Targets must die within %s to gain\n   peak performance time bonuses.", pad, good, Math.round(KILL_TIMER) + " seconds");
        tooltip.setBulletedListMode(null);
        tooltip.addPara("Decreases the ship's max combat readiness by %s.", pad, bad, Math.round(MAX_CR_MOD * 100f) + "%");
    }

    public static class AP_ConvictedCrewmatesScript implements AdvanceableListener, DamageDealtModifier {
        public Map<String, TargetData> damagedTargets = new HashMap<>();
        public ShipAPI ship;
        public IntervalUtil killChecker = new IntervalUtil(0.9f, 1.1f);
        public float maxPPT;

        public AP_ConvictedCrewmatesScript(ShipAPI ship) {
            this.ship = ship;
            this.maxPPT = ship.getPeakTimeRemaining();
        }

        @Override
        public void advance(float amount) {
            killChecker.advance(amount);
            if (killChecker.intervalElapsed() && !damagedTargets.isEmpty()) {
                for (Iterator<Map.Entry<String, TargetData>> itr = damagedTargets.entrySet().iterator(); itr.hasNext(); ) {
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

        public static class TargetData {
            public ShipAPI target;
            public float timer = KILL_TIMER;
        }

    }
}
