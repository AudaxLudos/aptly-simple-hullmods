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

public class ASH_ReactiveSubsystems extends BaseHullMod {
    public static final float MAX_CR_MODIFIER = 0.15f;
    public static final float CR_LOSS_PER_SECOND_MULTIPLIER = 0.25f;
    public static final float PEAK_CR_DURATION_MODIFIER = 60f;

    @Override
    public void applyEffectsBeforeShipCreation(ShipAPI.HullSize hullSize, MutableShipStatsAPI stats, String id) {
        stats.getMaxCombatReadiness().modifyFlat(id, MAX_CR_MODIFIER, "Reactive Subsystems Hullmod");
        stats.getCRLossPerSecondPercent().modifyMult(id, 1f + CR_LOSS_PER_SECOND_MULTIPLIER);

        if (stats.getVariant().getSMods().contains(spec.getId()) && ASH_Utils.isModEnabled())
            stats.getPeakCRDuration().modifyFlat(id, PEAK_CR_DURATION_MODIFIER);
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
            tooltip.addPara("Increases the ship's max combat readiness by %s", opad, good, Math.round(MAX_CR_MODIFIER * 100f) + "%");
            tooltip.addPara("Increases the rate at which combat readiness degrades by %s", pad, bad, Math.round(CR_LOSS_PER_SECOND_MULTIPLIER * 100f) + "%");
            tooltip.setBulletedListMode(null);

            if (!ASH_Utils.isModEnabled())
                return;

            if (ship == null || !Keyboard.isKeyDown(Keyboard.getKeyIndex("F1"))) {
                tooltip.addPara("Press F1 to show S-mod effects", Misc.getGrayColor(), opad);
                return;
            }
        }

        tooltip.addSectionHeading("S-Mod Effects:", story, Misc.setAlpha(story, 110), Alignment.MID, opad);
        tooltip.setBulletedListMode(" - ");
        tooltip.addPara("Increases the ship's max combat readiness by %s", opad, good, Math.round(MAX_CR_MODIFIER * 100f) + "%");
        tooltip.addPara("Increases the rate at which combat readiness degrades by %s", pad, bad, Math.round(CR_LOSS_PER_SECOND_MULTIPLIER * 100f) + "%");
        tooltip.addPara("Increases the ship's peak performance time by %s", pad, good, Math.round(PEAK_CR_DURATION_MODIFIER) + " seconds");
        tooltip.setBulletedListMode(null);
    }
}