package data.hullmods;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.ShipAPI;

public class ASH_TestStopwatch extends BaseHullMod {
    @Override
    public void advanceInCombat(ShipAPI ship, float amount) {
        if (!ship.isAlive())
            return;

        float stopwatchTime = 0f;

        if (Global.getCombatEngine().getCustomData().get("ASH_StopwatchTime_" + spec.getId()) instanceof Float)
            stopwatchTime = (float) Global.getCombatEngine().getCustomData().get("ASH_StopwatchTime_" + spec.getId());

        if (ship.areSignificantEnemiesInRange())
            stopwatchTime += amount;

        if (ship == Global.getCombatEngine().getPlayerShip()) {
            Global.getCombatEngine().maintainStatusForPlayerShip("ASH_TestStopwatch",
                    "graphics/icons/hullsys/high_energy_focus.png", "Stopwatch Time",
                    stopwatchTime + " seconds", false);
        }

        Global.getCombatEngine().getCustomData().put("ASH_StopwatchTime_" + spec.getId(), stopwatchTime);
    }
}