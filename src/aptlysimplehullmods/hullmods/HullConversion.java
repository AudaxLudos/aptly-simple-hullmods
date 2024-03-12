package aptlysimplehullmods.hullmods;

import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;

import java.awt.*;

public class HullConversion extends BaseHullMod {
    public static final float ARMOR_MULTIPLIER = 0.20f;
    public static final float MIN_ARMOR_MODIFIER = 0.05f;
    public static final float ARMOR_DAMAGE_TAKEN_MULTIPLIER = 0.05f;
    public static final float HULL_MULTIPLIER = 0.20f;
    public static final float BREAK_CHANCE_MULTIPLIER = 1f;

    @Override
    public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {
        stats.getArmorBonus().modifyMult(id, 1f + ARMOR_MULTIPLIER);
        stats.getMinArmorFraction().modifyFlat(id, MIN_ARMOR_MODIFIER);
        stats.getHullBonus().modifyMult(id, 1f - HULL_MULTIPLIER);
        stats.getBreakProb().modifyMult(id, 1f + BREAK_CHANCE_MULTIPLIER);

        if (isSMod(stats))
            stats.getArmorDamageTakenMult().modifyMult(id, 1f - ARMOR_DAMAGE_TAKEN_MULTIPLIER);
    }

    @Override
    public void addPostDescriptionSection(TooltipMakerAPI tooltip, HullSize hullSize, ShipAPI ship, float width, boolean isForModSpec) {
        float pad = 3f;
        float oPad = 10f;
        Color good = Misc.getPositiveHighlightColor();
        Color bad = Misc.getNegativeHighlightColor();

        tooltip.setBulletedListMode(" - ");
        tooltip.addPara("Increases the armor value by %s", oPad, good, Math.round(ARMOR_MULTIPLIER * 100f) + "%");
        tooltip.addPara("Increases the minimum armor value by %s", pad, good, Math.round(MIN_ARMOR_MODIFIER * 100f) + "%");
        tooltip.addPara("Decreases hull integrity by %s", pad, bad, Math.round(HULL_MULTIPLIER * 100f) + "%");
        tooltip.addPara("Increases the chance of the ship breaking by %s", pad, bad, Math.round(BREAK_CHANCE_MULTIPLIER * 100f) + "%");
        tooltip.setBulletedListMode(null);
    }

    @Override
    public void addSModEffectSection(TooltipMakerAPI tooltip, ShipAPI.HullSize hullSize, ShipAPI ship, float width, boolean isForModSpec, boolean isForBuildInList) {
        float oPad = 10f;
        Color good = Misc.getPositiveHighlightColor();

        tooltip.setBulletedListMode(" - ");
        tooltip.addPara("Decreases the damage taken of armor by %s", oPad, good, Math.round(ARMOR_DAMAGE_TAKEN_MULTIPLIER * 100f) + "%");
        tooltip.setBulletedListMode(null);
    }

    @Override
    public boolean hasSModEffect() {
        return true;
    }
}