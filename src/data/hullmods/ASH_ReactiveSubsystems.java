package data.hullmods;

import java.awt.Color;

import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.ui.Alignment;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;

public class ASH_ReactiveSubsystems extends BaseHullMod {
    public static final float MAX_CR_MODIFIER = 0.15f;
    public static final float PEAK_CR_DURATION_MODIFIER = 60f;
    public static final float CR_LOSS_PER_SECOND_MULTIPLIER = 0.25f;

    @Override
    public void applyEffectsBeforeShipCreation(ShipAPI.HullSize hullSize, MutableShipStatsAPI stats, String id) {
        stats.getMaxCombatReadiness().modifyFlat(id, MAX_CR_MODIFIER, "Reactive Subsystems Hullmod");
        stats.getPeakCRDuration().modifyFlat(id, PEAK_CR_DURATION_MODIFIER);
        stats.getCRLossPerSecondPercent().modifyMult(id, 1f + CR_LOSS_PER_SECOND_MULTIPLIER);
    }

    @Override
    public void addPostDescriptionSection(TooltipMakerAPI tooltip, HullSize hullSize, ShipAPI ship, float width, boolean isForModSpec) {
        float pad = 3f;
        float opad = 10f;
        Color good = Misc.getPositiveHighlightColor();
        Color bad = Misc.getNegativeHighlightColor();

        tooltip.addSectionHeading("Effects:", Alignment.MID, opad);
        tooltip.setBulletedListMode(" - ");
        tooltip.addPara("Increases the ship's max combat readiness by %s", opad, good, Math.round(MAX_CR_MODIFIER * 100f) + "%");
        tooltip.addPara("Increases the ship's peak performance time by %s", pad, good, Math.round(PEAK_CR_DURATION_MODIFIER) + " seconds");
        tooltip.addPara("Increases the rate at which combat readiness degrades by %s", pad, bad, Math.round(CR_LOSS_PER_SECOND_MULTIPLIER * 100f) + "%");
        tooltip.setBulletedListMode(null);
    }
}