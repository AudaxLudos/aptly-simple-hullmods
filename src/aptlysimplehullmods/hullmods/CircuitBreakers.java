package aptlysimplehullmods.hullmods;

import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;

import java.awt.*;

public class CircuitBreakers extends BaseHullMod {
    public static final float OVERLOAD_TIME_MULT = 0.33f;
    public static final float EMP_DAMAGE_TAKEN_MULT = 0.50f;

    @Override
    public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {
        stats.getOverloadTimeMod().modifyMult(id, 1f - OVERLOAD_TIME_MULT);

        if (isSMod(stats))
            stats.getEmpDamageTakenMult().modifyMult(id, 1f - EMP_DAMAGE_TAKEN_MULT);
    }

    @Override
    public void addPostDescriptionSection(TooltipMakerAPI tooltip, HullSize hullSize, ShipAPI ship, float width, boolean isForModSpec) {
        float oPad = 10f;
        Color good = Misc.getPositiveHighlightColor();

        tooltip.addPara("Decreases the duration of overloads by %s.", oPad, good, Math.round(OVERLOAD_TIME_MULT * 100f) + "%");
    }

    @Override
    public void addSModEffectSection(TooltipMakerAPI tooltip, ShipAPI.HullSize hullSize, ShipAPI ship, float width, boolean isForModSpec, boolean isForBuildInList) {
        float oPad = 10f;
        Color good = Misc.getPositiveHighlightColor();

        tooltip.addPara("Decreases the ship's EMP damage taken by %s.", oPad, good, Math.round(EMP_DAMAGE_TAKEN_MULT * 100f) + "%");
    }

    @Override
    public boolean hasSModEffect() {
        return true;
    }
}