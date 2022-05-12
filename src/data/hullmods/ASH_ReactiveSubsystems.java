package data.hullmods;

import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;

public class ASH_ReactiveSubsystems extends BaseHullMod {
    public static final float MAX_CR_MODIFIER = 15f;
    public static final float PEAK_CR_DURATION_MODIFIER = 60f;
    public static final float CR_LOSS_PER_SECOND_MODIFIER = 50f;

    @Override
    public void applyEffectsBeforeShipCreation(ShipAPI.HullSize hullSize, MutableShipStatsAPI stats, String id) {
        stats.getMaxCombatReadiness().modifyFlat(id, MAX_CR_MODIFIER * 0.01f, "Reactive Subsystems Hullmod");
        stats.getPeakCRDuration().modifyFlat(id, PEAK_CR_DURATION_MODIFIER);
        stats.getCRLossPerSecondPercent().modifyPercent(id, CR_LOSS_PER_SECOND_MODIFIER);
    }

    @Override
    public String getDescriptionParam(int index, HullSize hullSize) {
        if (index == 0)
            return Math.round(MAX_CR_MODIFIER) + "%";
        if (index == 1)
            return Math.round(PEAK_CR_DURATION_MODIFIER) + " seconds";
        if (index == 2)
            return Math.round(CR_LOSS_PER_SECOND_MODIFIER) + "%";
        return null;
    }
}