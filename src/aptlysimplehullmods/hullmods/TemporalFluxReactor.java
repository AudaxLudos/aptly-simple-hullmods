package aptlysimplehullmods.hullmods;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShieldAPI.ShieldType;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;

import java.awt.*;

public class TemporalFluxReactor extends BaseHullMod {
    public static float TIME_FLOW_MULT = 0.33f;
    public static float PEAK_PERFORMANCE_TIME_MULT = 0.40f;

    @Override
    public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {
        if (isSMod(stats)) {
            stats.getPeakCRDuration().modifyMult(id, PEAK_PERFORMANCE_TIME_MULT);
        }
    }

    @Override
    public void advanceInCombat(ShipAPI ship, float amount) {
        if (Global.getCombatEngine().isPaused()) {
            return;
        }
        if (!ship.isAlive()) {
            ship.getMutableStats().getTimeMult().unmodify(this.spec.getId());
            Global.getCombatEngine().getTimeMult().unmodify(this.spec.getId());
            return;
        }

        MutableShipStatsAPI stats = ship.getMutableStats();
        Color jitterColor = ship.getHullSpec().getShieldSpec().getInnerColor();

        if (ship.getShield() != null) {
            jitterColor = ship.getShield().getInnerColor();
        }

        float jitterRangeBonus = ship.getFluxLevel() * 10f;
        float jitterLevel = (float) Math.sqrt(ship.getFluxLevel());
        ship.setJitterUnder(this, jitterColor, jitterLevel, 50, 0f, 7f + jitterRangeBonus);
        stats.getTimeMult().modifyMult(this.spec.getId(), 1f + ship.getFluxLevel() * TIME_FLOW_MULT);

        if (ship == Global.getCombatEngine().getPlayerShip() && ship.getFluxLevel() * TIME_FLOW_MULT * 100f >= 1f) {
            Global.getCombatEngine().getTimeMult().modifyMult(this.spec.getId(), 1f / (1f + ship.getFluxLevel() * TIME_FLOW_MULT));
            Global.getCombatEngine().maintainStatusForPlayerShip("ASH_TemporalFluxReactor",
                    "graphics/icons/hullsys/temporal_shell.png", "Time flow Boost",
                    Math.round(ship.getFluxLevel() * TIME_FLOW_MULT * 100f) + "%", false);
        }
    }

    @Override
    public void addPostDescriptionSection(TooltipMakerAPI tooltip, HullSize hullSize, ShipAPI ship, float width, boolean isForModSpec) {
        float pad = 3f;
        float oPad = 10f;
        Color b = Misc.getHighlightColor();
        Color good = Misc.getPositiveHighlightColor();

        tooltip.addPara("As %s flux levels rise:", oPad, b, "soft");
        tooltip.setBulletedListMode("  ^ ");
        tooltip.addPara("Increases the ship's speed of time by up to %s", pad, good, Math.round(TIME_FLOW_MULT * 100f) + "%");
        tooltip.setBulletedListMode(null);
    }

    @Override
    public void addSModEffectSection(TooltipMakerAPI tooltip, HullSize hullSize, ShipAPI ship, float width, boolean isForModSpec, boolean isForBuildInList) {
        float oPad = 10f;
        Color bad = Misc.getNegativeHighlightColor();

        tooltip.addPara("Decreases the ship's peak performance time by %s", oPad, bad, Math.round(PEAK_PERFORMANCE_TIME_MULT * 100f) + "%");
    }

    @Override
    public boolean isSModEffectAPenalty() {
        return true;
    }

    @Override
    public boolean hasSModEffect() {
        return true;
    }

    @Override
    public String getUnapplicableReason(ShipAPI ship) {
        if (ship == null || ship.getHullSpec().getShieldType() == ShieldType.PHASE) {
            return "Incompatible with phase ships";
        }
        return null;
    }

    @Override
    public boolean isApplicableToShip(ShipAPI ship) {
        return ship != null && ship.getHullSpec().getShieldType() != ShieldType.PHASE;
    }
}