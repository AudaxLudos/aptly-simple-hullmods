package aptlysimplehullmods.hullmods.hidden;

import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;

import java.awt.*;

public class HullConversion extends BaseHullMod {
    public static final float ARMOR_MULT = 0.20f;
    public static final float MIN_ARMOR_MOD = 0.05f;
    public static final float ARMOR_DAMAGE_TAKEN_MULT = 0.05f;
    public static final float HULL_MULT = 0.20f;
    public static final float BREAK_CHANCE_MULT = 1f;

    @Override
    public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {
        stats.getArmorBonus().modifyMult(id, 1f + ARMOR_MULT);
        stats.getMinArmorFraction().modifyFlat(id, MIN_ARMOR_MOD);
        stats.getHullBonus().modifyMult(id, 1f - HULL_MULT);
        stats.getBreakProb().modifyMult(id, 1f + BREAK_CHANCE_MULT);

        if (isSMod(stats))
            stats.getArmorDamageTakenMult().modifyMult(id, 1f - ARMOR_DAMAGE_TAKEN_MULT);
    }

    @Override
    public void addPostDescriptionSection(TooltipMakerAPI tooltip, HullSize hullSize, ShipAPI ship, float width, boolean isForModSpec) {
        float pad = 3f;
        float oPad = 10f;
        Color good = Misc.getPositiveHighlightColor();
        Color bad = Misc.getNegativeHighlightColor();

        tooltip.setBulletedListMode(" - ");
        tooltip.addPara("Increases the ship's armor value by %s", oPad, good, Math.round(ARMOR_MULT * 100f) + "%");
        tooltip.addPara("Increases the ship's minimum armor value by %s", pad, good, Math.round(MIN_ARMOR_MOD * 100f) + "%");
        tooltip.addPara("Decreases the ship's hull integrity by %s", pad, bad, Math.round(HULL_MULT * 100f) + "%");
        tooltip.addPara("Increases the ship's of breaking apart by %s", pad, bad, Math.round(BREAK_CHANCE_MULT * 100f) + "%");
        tooltip.setBulletedListMode(null);
    }

    @Override
    public void addSModEffectSection(TooltipMakerAPI tooltip, ShipAPI.HullSize hullSize, ShipAPI ship, float width, boolean isForModSpec, boolean isForBuildInList) {
        float oPad = 10f;
        Color good = Misc.getPositiveHighlightColor();

        tooltip.setBulletedListMode(" - ");
        tooltip.addPara("Decreases the ship's armor damage taken by %s", oPad, good, Math.round(ARMOR_DAMAGE_TAKEN_MULT * 100f) + "%");
        tooltip.setBulletedListMode(null);
    }

    @Override
    public boolean hasSModEffect() {
        return true;
    }
}