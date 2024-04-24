package aptlysimplehullmods.hullmods;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.FighterWingAPI;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.IntervalUtil;
import com.fs.starfarer.api.util.Misc;

import java.awt.*;

public class SwiftRetreatProtocol extends BaseHullMod {
    public static final float FIGHTER_MOVEMENT_MULT = 0.25f;
    public static final float ENGINE_DMG_TAKEN_MULT = 0.25f;

    @Override
    public void advanceInCombat(ShipAPI ship, float amount) {
        if (!ship.isAlive()) return;

        String key = "fast_wing_retreat_protocol_" + ship.getId();
        FastWingRetreatProtocolData data = (FastWingRetreatProtocolData) Global.getCombatEngine().getCustomData().get(key);
        if (data == null) {
            data = new FastWingRetreatProtocolData();
            Global.getCombatEngine().getCustomData().put(key, data);
        }

        data.interval.advance(amount);
        if (data.interval.intervalElapsed() || ship == Global.getCombatEngine().getPlayerShip()) {
            for (FighterWingAPI wing : ship.getAllWings()) {
                for (FighterWingAPI.ReturningFighter returningWing : wing.getReturning()) {
                    MutableShipStatsAPI fighterStats = returningWing.fighter.getMutableStats();
                    float movementMult = FIGHTER_MOVEMENT_MULT;
                    if (isSMod(ship))
                        movementMult *= 2f;
                    fighterStats.getMaxSpeed().modifyMult(spec.getId(), 1f + movementMult);
                    fighterStats.getMaxTurnRate().modifyMult(spec.getId(), 1f + movementMult);
                    fighterStats.getAcceleration().modifyMult(spec.getId(), 1f + movementMult * 2f);
                    fighterStats.getTurnAcceleration().modifyMult(spec.getId(), 1f + movementMult * 2f);
                    fighterStats.getDeceleration().modifyMult(spec.getId(), 1f + movementMult * 2f);
                    fighterStats.getEngineDamageTakenMult().modifyMult(spec.getId(), 1f + ENGINE_DMG_TAKEN_MULT);
                    if (returningWing.fighter.getShield() != null)
                        returningWing.fighter.getShield().toggleOff();
                }
            }
        }
    }

    @Override
    public void addPostDescriptionSection(TooltipMakerAPI tooltip, ShipAPI.HullSize hullSize, ShipAPI ship, float width, boolean isForModSpec) {
        float pad = 3f;
        float oPad = 10f;
        Color b = Misc.getHighlightColor();
        Color good = Misc.getPositiveHighlightColor();
        Color bad = Misc.getNegativeHighlightColor();

        tooltip.addPara("When a fighter wing is %s:", oPad, b, "retreating/rearming");
        tooltip.setBulletWidth(20f);
        tooltip.setBulletedListMode("");
        tooltip.addPara("^ Increases the fighter's top speed by %s", pad, good, Math.round(FIGHTER_MOVEMENT_MULT * 100f) + "%");
        tooltip.addPara("^ Increases the fighter's maneuverability by %s", pad, good, Math.round(FIGHTER_MOVEMENT_MULT * 100f) + "%");
        tooltip.addPara("^ Increases the fighter's engine damage taken by %s", pad, bad, Math.round(ENGINE_DMG_TAKEN_MULT * 100f) + "%");
        tooltip.addPara("^ If the fighter has shields, %s shields", pad, bad, "turns off");
        tooltip.setBulletedListMode(null);
    }

    @Override
    public void addSModEffectSection(TooltipMakerAPI tooltip, ShipAPI.HullSize hullSize, ShipAPI ship, float width, boolean isForModSpec, boolean isForBuildInList) {
        float oPad = 10f;
        Color good = Misc.getPositiveHighlightColor();

        tooltip.addPara("Increases the buffs to fighter's by an additional %s.", oPad, good, Math.round(FIGHTER_MOVEMENT_MULT * 100f) + "%");
    }

    @Override
    public boolean hasSModEffect() {
        return true;
    }

    @Override
    public String getUnapplicableReason(ShipAPI ship) {
        return "Ship does not have fighter bays";
    }

    @Override
    public boolean isApplicableToShip(ShipAPI ship) {
        int bays = (int) ship.getMutableStats().getNumFighterBays().getModifiedValue();
        return bays > 0;
    }

    public static class FastWingRetreatProtocolData {
        IntervalUtil interval = new IntervalUtil(0.9f, 1.1f);
    }
}
