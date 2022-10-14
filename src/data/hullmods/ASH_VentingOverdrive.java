package data.hullmods;

import java.awt.Color;

import org.lazywizard.lazylib.CollisionUtils;
import org.lwjgl.util.vector.Vector2f;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.DamageType;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.ui.Alignment;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;

public class ASH_VentingOverdrive extends BaseHullMod {
    public static final float SHIP_STATS_MULTIPLIER = 1f;

    @Override
    public void advanceInCombat(ShipAPI ship, float amount) {
        if (!ship.isAlive())
            return;

        MutableShipStatsAPI stats = ship.getMutableStats();
        CombatEngineAPI engine = Global.getCombatEngine();

        if (ship.getHullLevel() >= 0.2f) {
            stats.getVentRateMult().modifyMult(spec.getId(), 1f + SHIP_STATS_MULTIPLIER);

            if (ship.getFluxTracker().isVenting()) {
                stats.getMaxSpeed().modifyMult(spec.getId(), 1f + SHIP_STATS_MULTIPLIER);
                stats.getMaxTurnRate().modifyMult(spec.getId(), 1f + SHIP_STATS_MULTIPLIER);

                Vector2f point = new Vector2f(ship.getLocation());
                point.x += ship.getCollisionRadius() * ((float) Math.random() * 2f - 1f);
                point.y += ship.getCollisionRadius() * ((float) Math.random() * 2f - 1f);

                if (!CollisionUtils.isPointWithinBounds(point, ship))
                    point = CollisionUtils.getNearestPointOnBounds(point, ship);

                engine.applyDamage(ship, point, amount * (0.20f * ship.getVariant().getHullSpec().getFluxCapacity()), DamageType.OTHER, 0f, true, false, null);
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
    public void addPostDescriptionSection(TooltipMakerAPI tooltip, HullSize hullSize, ShipAPI ship, float width, boolean isForModSpec) {
        float pad = 3f;
        float opad = 10f;
        Color b = Misc.getHighlightColor();
        Color good = Misc.getPositiveHighlightColor();
        Color bad = Misc.getNegativeHighlightColor();

        tooltip.addSectionHeading("Effects:", Alignment.MID, opad);
        tooltip.setBulletedListMode("");
        tooltip.addPara("While %s and hull integrity is above %s:", opad, b, "venting", "20%");
        tooltip.setBulletedListMode(" ^ ");
        tooltip.addPara("Increases the rate of venting by %s.", pad, good, Math.round(SHIP_STATS_MULTIPLIER * 100f) + "%");
        tooltip.addPara("Increases the ship's speed by %s.", pad, good, Math.round(SHIP_STATS_MULTIPLIER * 100f) + "%");
        tooltip.addPara("Increases the ship's maneuverability by %s.", pad, good, Math.round(SHIP_STATS_MULTIPLIER * 100f) + "%");
        tooltip.addPara("%s, the higher the flux the longer you take damage.", pad, bad, "Damages the ship");
        tooltip.setBulletedListMode(null);
    }
}