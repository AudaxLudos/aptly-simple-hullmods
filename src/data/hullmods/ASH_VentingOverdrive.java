package data.hullmods;

import org.lazywizard.lazylib.CollisionUtils;
import org.lwjgl.util.vector.Vector2f;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.DamageType;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;

public class ASH_VentingOverdrive extends BaseHullMod {
    public static final float MAX_SPEED_MODIFIER = 2f;
    public static final float MAX_TURN_RATE_MODIFIER = 2f;
    public static final float VENT_RATE_MODIFIER = 2f;

    @Override
    public void advanceInCombat(ShipAPI ship, float amount) {
        if (!ship.isAlive())
            return;

        MutableShipStatsAPI stats = ship.getMutableStats();
        CombatEngineAPI engine = Global.getCombatEngine();

        if (ship.getHullLevel() >= 0.2f) {
            stats.getVentRateMult().modifyMult(spec.getId(), VENT_RATE_MODIFIER);

            if (ship.getFluxTracker().isVenting()) {
                stats.getMaxSpeed().modifyMult(spec.getId(), MAX_SPEED_MODIFIER);
                stats.getMaxTurnRate().modifyMult(spec.getId(), MAX_TURN_RATE_MODIFIER);

                Vector2f point = new Vector2f(ship.getLocation());
                point.x += ship.getCollisionRadius() * ((float) Math.random() * 2.0F - 1.0F);
                point.y += ship.getCollisionRadius() * ((float) Math.random() * 2.0F - 1.0F);

                if (CollisionUtils.isPointWithinBounds(point, ship))
                    engine.applyDamage(ship, point, amount * ship.getFluxTracker().getCurrFlux(), DamageType.OTHER, 0f,
                            true, false, null);
            } else {
                stats.getMaxSpeed().unmodify();
                stats.getMaxTurnRate().unmodify();
            }
        } else {
            stats.getVentRateMult().unmodify();
            stats.getMaxSpeed().unmodify();
            stats.getMaxTurnRate().unmodify();
        }
    }

    @Override
    public String getDescriptionParam(int index, HullSize hullSize) {
        if (index == 0)
            return Math.round(VENT_RATE_MODIFIER) + "";
        if (index == 1)
            return Math.round(MAX_SPEED_MODIFIER) + "";
        if (index == 2)
            return "venting hurts the ship and the higher the flux levels the greater the damage received";
        return null;
    }
}