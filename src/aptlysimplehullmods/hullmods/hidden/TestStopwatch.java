package aptlysimplehullmods.hullmods.hidden;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.ShipAPI;

public class TestStopwatch extends BaseHullMod {
    @Override
    public void advanceInCombat(ShipAPI ship, float amount) {
        if (!ship.isAlive())
            return;

        float stopwatchTime = 0f;

        if (Global.getCombatEngine().getCustomData().get("ASH_StopwatchTime_" + spec.getId()) instanceof Float)
            stopwatchTime = (float) Global.getCombatEngine().getCustomData().get("ASH_StopwatchTime_" + spec.getId());

        if (ship.areSignificantEnemiesInRange())
            stopwatchTime += amount;

        if (ship == Global.getCombatEngine().getPlayerShip() && stopwatchTime >= 1f) {
            Global.getCombatEngine().maintainStatusForPlayerShip("ASH_TestStopwatch",
                    "graphics/icons/hullsys/high_energy_focus.png", "Stopwatch Time",
                    stopwatchTime + " seconds", false);
        }

        Global.getCombatEngine().getCustomData().put("ASH_StopwatchTime_" + spec.getId(), stopwatchTime);
    }
}