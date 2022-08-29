package data.hullmods;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;

import java.awt.Color;

public class ASH_TemporalFluxReactor extends BaseHullMod {
    public static final float SHIP_TIME_MODIFIER = 33f;

    @Override
    public void advanceInCombat(ShipAPI ship, float amount) {
        if (Global.getCombatEngine().isPaused())
            return;
        if (!ship.isAlive()) {
            ship.getMutableStats().getTimeMult().unmodify(ship.getId());
            Global.getCombatEngine().getTimeMult().unmodify(ship.getId());
            return;
        }

        MutableShipStatsAPI stats = ship.getMutableStats();
        Color jitterColor = ship.getHullSpec().getShieldSpec().getInnerColor();
        float timeDeployed = 0f;

        if (Global.getCombatEngine().getCustomData().get("ASH_TimeDeployed_" + ship.getId()) instanceof Float)
            timeDeployed = (float) Global.getCombatEngine().getCustomData().get("ASH_TimeDeployed_" + ship.getId());

        ship.setJitterUnder(this, jitterColor, ship.getFluxLevel() * 2, 3, 2, 12);
        stats.getTimeMult().modifyMult(ship.getId(), 1 + ship.getFluxLevel() * (SHIP_TIME_MODIFIER * 0.01f));

        if (ship.areAnyEnemiesInRange()) {
            ship.setTimeDeployed(timeDeployed += amount + amount * ship.getFluxLevel());
        }

        if (ship == Global.getCombatEngine().getPlayerShip()) {
            Global.getCombatEngine().getTimeMult().modifyMult(ship.getId(),
                    1 / (1 + ship.getFluxLevel() * (SHIP_TIME_MODIFIER * 0.01f)));
            Global.getCombatEngine().maintainStatusForPlayerShip("ASH_TemporalFluxReactor",
                    "graphics/icons/hullsys/temporal_shell.png", "Current Time Flow",
                    Math.round(ship.getFluxLevel() * SHIP_TIME_MODIFIER) + "%", false);
        }

        Global.getCombatEngine().getCustomData().put("ASH_TimeDeployed_" + ship.getId(), timeDeployed);
    }

    @Override
    public String getDescriptionParam(int index, HullSize hullSize) {
        if (index == 0)
            return Math.round(SHIP_TIME_MODIFIER) + "%";
        if (index == 1)
            return "peak perforamnce time reduction per second is increased as flux levels rise";
        return null;
    }
}