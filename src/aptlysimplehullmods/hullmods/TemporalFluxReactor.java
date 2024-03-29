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
    public static final float SPEED_OF_TIME_MULT = 0.33f;

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
        float timeDeployed = 0f;
        Color jitterColor = ship.getHullSpec().getShieldSpec().getInnerColor();

        if (ship.getShield() != null)
            jitterColor = ship.getShield().getInnerColor();

        if (Global.getCombatEngine().getCustomData().get("ASH_TimeDeployed_" + spec.getId()) instanceof Float)
            timeDeployed = (float) Global.getCombatEngine().getCustomData().get("ASH_TimeDeployed_" + spec.getId());

        float jitterRangeBonus = ship.getFluxLevel() * 10f;
        float jitterLevel = (float) Math.sqrt(ship.getFluxLevel());
        ship.setJitterUnder(this, jitterColor, jitterLevel, 50, 0f, 7f + jitterRangeBonus);
        stats.getTimeMult().modifyMult(spec.getId(), 1f + ship.getFluxLevel() * SPEED_OF_TIME_MULT);

        if (ship.areAnyEnemiesInRange())
            ship.setTimeDeployed(timeDeployed += amount + (amount * ship.getFluxLevel()));

        if (ship == Global.getCombatEngine().getPlayerShip() && ship.getFluxLevel() * SPEED_OF_TIME_MULT * 100f >= 1f) {
            Global.getCombatEngine().getTimeMult().modifyMult(spec.getId(), 1f / (1f + ship.getFluxLevel() * SPEED_OF_TIME_MULT));
            Global.getCombatEngine().maintainStatusForPlayerShip("ASH_TemporalFluxReactor",
                    "graphics/icons/hullsys/temporal_shell.png", "Speed of Time Boost",
                    Math.round(ship.getFluxLevel() * SPEED_OF_TIME_MULT * 100f) + "%", false);
        }

        Global.getCombatEngine().getCustomData().put("ASH_TimeDeployed_" + spec.getId(), timeDeployed);
    }

    @Override
    public void addPostDescriptionSection(TooltipMakerAPI tooltip, HullSize hullSize, ShipAPI ship, float width, boolean isForModSpec) {
        float pad = 3f;
        float oPad = 10f;
        Color b = Misc.getHighlightColor();
        Color good = Misc.getPositiveHighlightColor();
        Color bad = Misc.getNegativeHighlightColor();

        tooltip.setBulletedListMode("");
        tooltip.addPara("As %s flux levels rise:", oPad, b, "soft");
        tooltip.setBulletedListMode(" ^ ");
        tooltip.addPara("Increases the ship's speed of time by up to %s", pad, good, Math.round(SPEED_OF_TIME_MULT * 100f) + "%");
        tooltip.addPara("Accelerates the ship's rate of peak performance time reduction by up to %s", pad, bad, "2 seconds");
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
}