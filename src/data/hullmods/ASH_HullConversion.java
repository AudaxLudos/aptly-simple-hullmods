package data.hullmods;

import java.awt.Color;

import org.lwjgl.input.Keyboard;

import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.ui.Alignment;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;

import data.ASH_Utils;

public class ASH_HullConversion extends BaseHullMod {
    public static final float ARMOR_MULTIPLIER = 0.20f;
    public static final float MIN_ARMOR_MODIFIER = 0.05f;
    public static final float ARMOR_DAMAGE_TAKEN_MULTIPLIER = 0.05f;
    public static final float HULL_MULTIPLIER = 0.20f;
    public static final float BREAK_CHANCE_MULTIPLIER = 1f;

    @Override
    public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {
        stats.getArmorBonus().modifyMult(id, 1f + ARMOR_MULTIPLIER);
        stats.getMinArmorFraction().modifyFlat(id, MIN_ARMOR_MODIFIER);
        stats.getHullBonus().modifyMult(id, 1f + -HULL_MULTIPLIER);
        stats.getBreakProb().modifyMult(id, 1f + BREAK_CHANCE_MULTIPLIER);

        if (stats.getVariant().getSMods().contains(id) && ASH_Utils.isModEnabled())
            stats.getArmorDamageTakenMult().modifyMult(id, 1f + -ARMOR_DAMAGE_TAKEN_MULTIPLIER);
    }

    @Override
    public void addPostDescriptionSection(TooltipMakerAPI tooltip, HullSize hullSize, ShipAPI ship, float width, boolean isForModSpec) {
        float pad = 3f;
        float opad = 10f;
        Color good = Misc.getPositiveHighlightColor();
        Color bad = Misc.getNegativeHighlightColor();
        Color story = Misc.getStoryOptionColor();

        if (ship == null || !ship.getVariant().getSMods().contains(spec.getId()) || !ASH_Utils.isModEnabled()) {
            tooltip.addSectionHeading("Effects:", Alignment.MID, opad);
            tooltip.setBulletedListMode(" - ");
            tooltip.addPara("Increases the armor value by %s", opad, good, Math.round(ARMOR_MULTIPLIER * 100f) + "%");
            tooltip.addPara("Increases the minimum armor value by %s", pad, good, Math.round(MIN_ARMOR_MODIFIER * 100f) + "%");
            tooltip.addPara("Decreases hull integrity by %s", pad, bad, Math.round(HULL_MULTIPLIER * 100f) + "%");
            tooltip.addPara("Increases the chance of the ship breaking by %s", pad, bad, Math.round(BREAK_CHANCE_MULTIPLIER * 100f) + "%");
            tooltip.setBulletedListMode(null);
        }

        if (!ASH_Utils.isModEnabled())
            return;
        if (!Keyboard.isKeyDown(Keyboard.KEY_F1)) {
            tooltip.addPara("Press or Hold F1 to show S-mod effects", Misc.getGrayColor(), opad);
            return;
        }

        tooltip.addSectionHeading("S-Mod Effects:", story, Misc.setAlpha(story, 110), Alignment.MID, opad);
        tooltip.setBulletedListMode(" - ");
        tooltip.addPara("Increases the armor value by %s", opad, good, Math.round(ARMOR_MULTIPLIER * 100f) + "%");
        tooltip.addPara("Increases the minimum armor value by %s", pad, good, Math.round(MIN_ARMOR_MODIFIER * 100f) + "%");
        tooltip.addPara("Decreases hull integrity by %s", pad, bad, Math.round(HULL_MULTIPLIER * 100f) + "%");
        tooltip.addPara("Increases the chance of the ship breaking by %s", pad, bad, Math.round(BREAK_CHANCE_MULTIPLIER * 100f) + "%");
        tooltip.addPara("Decreases the damage taken of armor by %s", pad, good, Math.round(ARMOR_DAMAGE_TAKEN_MULTIPLIER * 100f) + "%");
        tooltip.setBulletedListMode(null);
    }
}