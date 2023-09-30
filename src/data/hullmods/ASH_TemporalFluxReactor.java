package data.hullmods;

import java.awt.Color;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShieldAPI.ShieldType;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.ui.Alignment;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;

public class ASH_TemporalFluxReactor extends BaseHullMod {
    public static final float SPEED_OF_TIME_MULTIPLIER = 0.33f;

    @Override
    public void advanceInCombat(ShipAPI ship, float amount) {
        if (Global.getCombatEngine().isPaused())
            return;
        if (!ship.isAlive()) {
            ship.getMutableStats().getTimeMult().unmodify(spec.getId());
            Global.getCombatEngine().getTimeMult().unmodify(spec.getId());
            return;
        }

        MutableShipStatsAPI stats = ship.getMutableStats();
        Color jitterColor = Color.WHITE;
        float timeDeployed = 0f;
        float peakTimeReductionMult = stats.getVariant().getSMods().contains(spec.getId()) ? 0.5f : 1f;

        if (ship.getShield() != null)
            jitterColor = ship.getShield().getInnerColor();
        else
            jitterColor = ship.getHullSpec().getShieldSpec().getInnerColor();

        if (Global.getCombatEngine().getCustomData().get("ASH_TimeDeployed_" + spec.getId()) instanceof Float)
            timeDeployed = (float) Global.getCombatEngine().getCustomData().get("ASH_TimeDeployed_" + spec.getId());

        float jitterRangeBonus = ship.getFluxLevel() * 10f;
        float jitterLevel = (float) Math.sqrt(ship.getFluxLevel());
        ship.setJitterUnder(this, jitterColor, jitterLevel, 50, 0f, 7f + jitterRangeBonus);
        stats.getTimeMult().modifyMult(spec.getId(), 1f + ship.getFluxLevel() * SPEED_OF_TIME_MULTIPLIER);

        if (ship.areAnyEnemiesInRange())
            ship.setTimeDeployed(timeDeployed += amount + (amount * ship.getFluxLevel()) * peakTimeReductionMult);

        if (ship == Global.getCombatEngine().getPlayerShip()) {
            Global.getCombatEngine().getTimeMult().modifyMult(spec.getId(), 1f / (1f + ship.getFluxLevel() * SPEED_OF_TIME_MULTIPLIER));
            Global.getCombatEngine().maintainStatusForPlayerShip("ASH_TemporalFluxReactor",
                    "graphics/icons/hullsys/temporal_shell.png", "Speed of Time Boost",
                    Math.round(ship.getFluxLevel() * SPEED_OF_TIME_MULTIPLIER * 100f) + "%", false);
        }

        Global.getCombatEngine().getCustomData().put("ASH_TimeDeployed_" + spec.getId(), timeDeployed);
    }

    @Override
    public void addPostDescriptionSection(TooltipMakerAPI tooltip, HullSize hullSize, ShipAPI ship, float width, boolean isForModSpec) {
        float pad = 3f;
        float opad = 10f;
        Color b = Misc.getHighlightColor();
        Color good = Misc.getPositiveHighlightColor();
        Color bad = Misc.getNegativeHighlightColor();

        tooltip.addSectionHeading("Effects:", Alignment.MID, opad);
        tooltip.setBulletedListMode("");
        tooltip.addPara("As %s flux levels rise:", opad, b, "soft");
        tooltip.setBulletedListMode(" ^ ");
        tooltip.addPara("Increases the speed of time by up to %s", pad, good, Math.round(SPEED_OF_TIME_MULTIPLIER * 100f) + "%");
        tooltip.addPara("Accelerates the peak performance time reduction by up to %s", pad, bad, "2 seconds");
        tooltip.setBulletedListMode(null);
    }

    @Override
    public String getUnapplicableReason(ShipAPI ship) {
        if (ship == null || ship.getHullSpec().getShieldType() == ShieldType.PHASE)
            return "Incompatible with phase ships";
        return null;
    }

    @Override
    public boolean isApplicableToShip(ShipAPI ship) {
        return ship != null && ship.getHullSpec().getShieldType() != ShieldType.PHASE;
    }

    @Override
    public void addSModEffectSection(TooltipMakerAPI tooltip, ShipAPI.HullSize hullSize, ShipAPI ship, float width, boolean isForModSpec, boolean isForBuildInList) {
        float opad = 10f;
        Color bad = Misc.getNegativeHighlightColor();

        tooltip.setBulletedListMode(" - ");
        tooltip.addPara("Reduces the acceleration of peak performance time reduction down to %s", opad, bad, "1.5 seconds");
        tooltip.setBulletedListMode(null);
    }

    @Override
    public boolean hasSModEffect() {
        return true;
    }
}