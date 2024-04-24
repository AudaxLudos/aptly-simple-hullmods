package aptlysimplehullmods.hullmods;

import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;

import java.awt.*;

public class VolatileWarheads extends BaseHullMod {
    public static final float MISSILE_DAMAGE_MULT = 0.20f;
    public static final float MISSILE_SPEED_MULT = 0.10f;
    public static final float MISSILE_HEALTH_MULT = 0.10f;

    @Override
    public void applyEffectsBeforeShipCreation(ShipAPI.HullSize hullSize, MutableShipStatsAPI stats, String id) {
        stats.getMissileWeaponDamageMult().modifyMult(id, 1f + MISSILE_DAMAGE_MULT);
        stats.getMissileMaxSpeedBonus().modifyMult(id, 1f - MISSILE_HEALTH_MULT);
        stats.getMissileHealthBonus().modifyMult(id, 1f - MISSILE_HEALTH_MULT);

        if (isSMod(stats))
            stats.getMissileHealthBonus().unmodifyMult(id);
    }

    @Override
    public void addPostDescriptionSection(TooltipMakerAPI tooltip, HullSize hullSize, ShipAPI ship, float width, boolean isForModSpec) {
        float pad = 3f;
        float oPad = 10f;
        Color good = Misc.getPositiveHighlightColor();
        Color bad = Misc.getNegativeHighlightColor();

        tooltip.addPara("Increases the damage of missiles by %s.", oPad, good, Math.round(MISSILE_DAMAGE_MULT * 100f) + "%");
        tooltip.addPara("Reduces the max speed of missiles by %s.", pad, bad, Math.round(MISSILE_SPEED_MULT * 100f) + "%");
        tooltip.addPara("Reduces the health of missiles by %s.", pad, bad, Math.round(MISSILE_HEALTH_MULT * 100f) + "%");
    }

    @Override
    public void addSModEffectSection(TooltipMakerAPI tooltip, ShipAPI.HullSize hullSize, ShipAPI ship, float width, boolean isForModSpec, boolean isForBuildInList) {
        float oPad = 10f;
        Color good = Misc.getPositiveHighlightColor();

        tooltip.addPara("Fully %s the health penalty of missiles.", oPad, good, "negates");
    }

    @Override
    public boolean hasSModEffect() {
        return true;
    }
}