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

public class ASH_CircuitBreakers extends BaseHullMod {
    public static final float OVERLOAD_TIME_MULTIPLIER = 0.25f;
    public static final float EMP_DAMAGE_TAKEN_MULTIPLIER = 0.25f;

    @Override
    public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {
        stats.getOverloadTimeMod().modifyMult(id, 1f + -OVERLOAD_TIME_MULTIPLIER);

        if (stats.getVariant().getSMods().contains(id))
            stats.getEmpDamageTakenMult().modifyMult(id, 1f + -EMP_DAMAGE_TAKEN_MULTIPLIER);
    }

    @Override
    public void addPostDescriptionSection(TooltipMakerAPI tooltip, HullSize hullSize, ShipAPI ship, float width, boolean isForModSpec) {
        float pad = 3f;
        float opad = 10f;
        Color good = Misc.getPositiveHighlightColor();
        Color story = Misc.getStoryOptionColor();

        if (!ship.getVariant().getSMods().contains(spec.getId())) {
            tooltip.addSectionHeading("Effects:", Alignment.MID, opad);
            tooltip.setBulletedListMode(" - ");
            tooltip.addPara("Reduces the duration of overloads by %s", opad, good, Math.round(OVERLOAD_TIME_MULTIPLIER * 100f) + "%");
            tooltip.setBulletedListMode(null);

            if (!Keyboard.isKeyDown(Keyboard.getKeyIndex("F1"))) {
                tooltip.addPara("Press F1 to show S-mod effects", Misc.getGrayColor(), opad);
                return;
            }
        }

        tooltip.addSectionHeading("S-Mod Effects:", story, Misc.setAlpha(story, 110), Alignment.MID, opad);
        tooltip.setBulletedListMode(" - ");
        tooltip.addPara("Reduces the duration of overloads by %s", opad, good, Math.round(OVERLOAD_TIME_MULTIPLIER * 100f) + "%");
        tooltip.addPara("Decreases damage taken from emp damage by %s", pad, good, Math.round(EMP_DAMAGE_TAKEN_MULTIPLIER * 100f) + "%");
        tooltip.setBulletedListMode(null);
    }
}