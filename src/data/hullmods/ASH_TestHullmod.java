package data.hullmods;

import java.util.List;

import org.lazywizard.lazylib.CollisionUtils;
import org.lwjgl.util.vector.Vector2f;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.DamageType;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;

public class ASH_TestHullmod extends BaseHullMod {
    public static final float SHIP_ARMOR_MODIFIER = 20f;
    public static final float SHIP_HULL_MODIFIER = 0.8f;
    public static final float SHIP_MIN_ARMOR_MODIFIER = 5f;
    public static final float SHIP_BREAK_CHANCE_MODIFIER = 2f;
    public static final float OVERLOAD_TIME_MODIFIER = 0.5f;

    @Override
    public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {
        // stats.getArmorBonus().modifyPercent(id, 20f);
        // stats.getMinArmorFraction().modifyPercent(id, 5f);
        // stats.getHullBonus().modifyMult(id, 0.9f);
        // stats.getBreakProb().modifyMult(id, 2f);
    }

    @Override
    public void advanceInCombat(ShipAPI ship, float amount) {
        if (!ship.isAlive())
            return;

        MutableShipStatsAPI stats = ship.getMutableStats();
        CombatEngineAPI engine = Global.getCombatEngine();

        if (ship.getHullLevel() >= 0.2f) {
            stats.getVentRateMult().modifyMult(spec.getId(), 2f);

            if (ship.getFluxTracker().isVenting()) {
                stats.getMaxSpeed().modifyMult(spec.getId(), 2f);
                stats.getMaxTurnRate().modifyMult(spec.getId(), 2f);
                stats.getAcceleration().modifyMult(spec.getId(), 2f);
                stats.getTurnAcceleration().modifyMult(spec.getId(), 2f);

                Vector2f point = new Vector2f(ship.getLocation());
                point.x += ship.getCollisionRadius() * ((float) Math.random() * 2.0F - 1.0F);
                point.y += ship.getCollisionRadius() * ((float) Math.random() * 2.0F - 1.0F);

                if (CollisionUtils.isPointWithinBounds(point, ship))
                    engine.applyDamage(ship, point, amount * ship.getFluxTracker().getCurrFlux(), DamageType.OTHER, 0f, true, false, null);
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
            return Math.round(SHIP_ARMOR_MODIFIER) + "";
        if (index == 1)
            return Math.round(SHIP_MIN_ARMOR_MODIFIER) + "";
        if (index == 2)
            return Math.round(SHIP_HULL_MODIFIER) + "";
        if (index == 3)
            return Math.round(SHIP_BREAK_CHANCE_MODIFIER) + "";
        return null;
    }
}