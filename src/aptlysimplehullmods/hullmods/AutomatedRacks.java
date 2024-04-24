package aptlysimplehullmods.hullmods;

import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;

import java.awt.*;

public class AutomatedRacks extends BaseHullMod {
    public static final float MISSILE_FIRE_RATE_MULT = 0.20f;
    public static final float MISSILE_AMMO_MULT = 0.10f;
    public static final float MISSILE_TURN_RATE_MULT = 0.10f;

    @Override
    public void applyEffectsBeforeShipCreation(ShipAPI.HullSize hullSize, MutableShipStatsAPI stats, String id) {
        stats.getMissileRoFMult().modifyMult(id, 1f + MISSILE_FIRE_RATE_MULT);
        stats.getMissileAmmoBonus().modifyMult(id, 1f - MISSILE_AMMO_MULT);
        stats.getMissileMaxTurnRateBonus().modifyMult(id, 1f - MISSILE_TURN_RATE_MULT);

        if (isSMod(stats)) {
            stats.getMissileMaxTurnRateBonus().unmodifyMult(id);
        }
    }

    @Override
    public void addPostDescriptionSection(TooltipMakerAPI tooltip, ShipAPI.HullSize hullSize, ShipAPI ship, float width, boolean isForModSpec) {
        float pad = 3f;
        float oPad = 10f;
        Color good = Misc.getPositiveHighlightColor();
        Color bad = Misc.getNegativeHighlightColor();

        tooltip.addPara("Increases the fire rate of missile weapons by %s.", oPad, good, Math.round(MISSILE_FIRE_RATE_MULT * 100f) + "%");
        tooltip.addPara("Decreases the max ammo of missile weapons by %s.", pad, bad, Math.round(MISSILE_AMMO_MULT * 100f) + "%");
        tooltip.addPara("Decreases the turret turn rate of missile weapons by %s.", pad, bad, Math.round(MISSILE_TURN_RATE_MULT * 100f) + "%");
    }

    @Override
    public void addSModEffectSection(TooltipMakerAPI tooltip, ShipAPI.HullSize hullSize, ShipAPI ship, float width, boolean isForModSpec, boolean isForBuildInList) {
        float oPad = 10f;
        Color good = Misc.getPositiveHighlightColor();

        tooltip.addPara("Fully %s the turret turn rate penalty for missile weapons.", oPad, good, "negates");
    }

    @Override
    public boolean hasSModEffect() {
        return true;
    }
}
