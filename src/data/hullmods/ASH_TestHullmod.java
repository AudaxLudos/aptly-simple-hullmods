package data.hullmods;

import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.impl.campaign.ids.Stats;

public class ASH_TestHullmod extends BaseHullMod {
    public static final float MODIFIER = 25f;

    @Override
    public void applyEffectsBeforeShipCreation(ShipAPI.HullSize hullSize, MutableShipStatsAPI stats, String id) {
        stats.getMaxCombatReadiness().modifyFlat(id, 15f * 0.01f, "Hullmod");
        stats.getPeakCRDuration().modifyFlat(id, 60f);
        stats.getCRLossPerSecondPercent().modifyPercent(id, 50f);
    }

    @Override
    public String getDescriptionParam(int index, HullSize hullSize) {
        if (index == 0)
            return Math.round(MODIFIER) + "%";
        return null;
    }

    @Override
    public void advanceInCombat(ShipAPI ship, float amount) {
        if (!ship.isAlive())
            return;

        MutableShipStatsAPI stats = ship.getMutableStats();
    }
}