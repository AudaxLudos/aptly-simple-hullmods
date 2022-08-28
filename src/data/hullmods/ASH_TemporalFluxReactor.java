package data.hullmods;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;

import java.awt.Color;

public class ASH_TemporalFluxReactor extends BaseHullMod {
    public static final float SHIP_TIME_MODIFIER = 33f;
    public Color jitterColor;

    @Override
    public void applyEffectsAfterShipCreation(ShipAPI ship, String id) {
        jitterColor = ship.getShield().getInnerColor();
    }

    @Override
    public void advanceInCombat(ShipAPI ship, float amount) {
        if (Global.getCombatEngine().isPaused())
            return;
        if (!ship.isAlive())
            return;

        MutableShipStatsAPI stats = ship.getMutableStats();
        boolean player = ship == Global.getCombatEngine().getPlayerShip();

        ship.setJitterUnder(this, jitterColor, ship.getFluxLevel() * 2, 3, 2, 12);
        stats.getTimeMult().modifyMult(spec.getId(), 1 + ship.getFluxLevel() * (SHIP_TIME_MODIFIER * 0.01f));

        if (player) {
            if (ship.isAlive()) {
                Global.getCombatEngine().getTimeMult().modifyMult(spec.getId(), 1 / (1 + ship.getFluxLevel() * (SHIP_TIME_MODIFIER * 0.01f)));
                Global.getCombatEngine().maintainStatusForPlayerShip(spec.getId(), "graphics/icons/hullsys/temporal_shell.png", "Time Flow", ship.getFluxLevel() * SHIP_TIME_MODIFIER + "%", false);
            } else {
                Global.getCombatEngine().getTimeMult().unmodify(spec.getId());
            }
        }
    }

    @Override
    public String getDescriptionParam(int index, HullSize hullSize) {
        if (index == 0)
            return Math.round(SHIP_TIME_MODIFIER) + "%";
        return null;
    }
}