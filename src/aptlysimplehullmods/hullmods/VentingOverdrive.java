package aptlysimplehullmods.hullmods;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.*;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;
import org.lazywizard.lazylib.CollisionUtils;
import org.lwjgl.util.vector.Vector2f;

import java.awt.*;

public class VentingOverdrive extends BaseHullMod {
    public static float SHIP_MOVEMENT_MULT = 1f;

    @Override
    public void advanceInCombat(ShipAPI ship, float amount) {
        if (!ship.isAlive()) {
            return;
        }

        MutableShipStatsAPI stats = ship.getMutableStats();
        CombatEngineAPI engine = Global.getCombatEngine();

        if (ship.getHullLevel() >= 0.2f) {
            if (ship.getFluxTracker().isVenting()) {
                stats.getVentRateMult().modifyMult(this.spec.getId(), 1f + SHIP_MOVEMENT_MULT);
                stats.getMaxSpeed().modifyMult(this.spec.getId(), 1f + SHIP_MOVEMENT_MULT);
                stats.getMaxTurnRate().modifyMult(this.spec.getId(), 1f + SHIP_MOVEMENT_MULT);

                Vector2f point = new Vector2f(ship.getLocation());
                point.x += ship.getCollisionRadius() * ((float) Math.random() * 2f - 1f);
                point.y += ship.getCollisionRadius() * ((float) Math.random() * 2f - 1f);

                if (!CollisionUtils.isPointWithinBounds(point, ship)) {
                    point = CollisionUtils.getNearestPointOnBounds(point, ship);
                }

                engine.applyDamage(ship, point, amount * (0.20f * ship.getVariant().getHullSpec().getFluxCapacity()), DamageType.OTHER, 0f, true, false, null);
            } else {
                stats.getMaxSpeed().unmodify(this.spec.getId());
                stats.getMaxTurnRate().unmodify(this.spec.getId());
            }
        } else {
            stats.getVentRateMult().unmodify(this.spec.getId());
            stats.getMaxSpeed().unmodify(this.spec.getId());
            stats.getMaxTurnRate().unmodify(this.spec.getId());
        }
    }

    @Override
    public void addPostDescriptionSection(TooltipMakerAPI tooltip, HullSize hullSize, ShipAPI ship, float width, boolean isForModSpec) {
        float pad = 3f;
        float oPad = 10f;
        Color b = Misc.getHighlightColor();
        Color good = Misc.getPositiveHighlightColor();
        Color bad = Misc.getNegativeHighlightColor();

        tooltip.addPara("While %s and hull integrity is above %s:", oPad, b, "venting", "20%");
        tooltip.setBulletedListMode("  ^ ");
        tooltip.addPara("Increases the ship's rate of venting by %s.", pad, good, Math.round(SHIP_MOVEMENT_MULT * 100f) + "%");
        tooltip.addPara("Increases the ship's speed by %s.", pad, good, Math.round(SHIP_MOVEMENT_MULT * 100f) + "%");
        tooltip.addPara("Increases the ship's maneuverability by %s.", pad, good, Math.round(SHIP_MOVEMENT_MULT * 100f) + "%");
        tooltip.addPara("%s, the higher the flux the longer you take damage.", pad, bad, "Damages the ship");
        tooltip.setBulletedListMode(null);
    }
}