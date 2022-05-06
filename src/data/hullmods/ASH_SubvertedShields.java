package data.hullmods;

import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.impl.campaign.ids.Stats;

public class ASH_SubvertedShields extends BaseHullMod {
    public static final float SHIELD_STRENGTH_MODIFIER = 50f;
    public static final float SHIELD_UPKEEP_MODIFIER = 50f;
    public static final float SHIELD_PIERCED_MODIFIER = 2f;
    public static final float HARD_FLUX_DISSIPATION_MODIFIER = 15f;

    @Override
    public String getUnapplicableReason(ShipAPI ship) {
        if (ship == null || ship.getShield() == null)
            return "Ship has no shields";
        return null;
    }

    @Override
    public boolean isApplicableToShip(ShipAPI ship) {
        return ship != null && ship.getShield() != null;
    }

    @Override
    public String getDescriptionParam(int index, HullSize hullSize) {
        if (index == 0)
            return Math.round(SHIELD_STRENGTH_MODIFIER) + "%";
        if (index == 1)
            return Math.round(SHIELD_PIERCED_MODIFIER) + "";
        if (index == 2)
            return Math.round(SHIELD_UPKEEP_MODIFIER) + "%";
        if (index == 3)
            return Math.round(HARD_FLUX_DISSIPATION_MODIFIER) + "%";
        return null;
    }

    @Override
    public void applyEffectsBeforeShipCreation(ShipAPI.HullSize hullSize, MutableShipStatsAPI stats, String id) {
        stats.getShieldDamageTakenMult().modifyPercent(id, SHIELD_STRENGTH_MODIFIER);
        stats.getShieldUpkeepMult().modifyPercent(id, -SHIELD_UPKEEP_MODIFIER);
        stats.getDynamic().getStat(Stats.SHIELD_PIERCED_MULT).modifyMult(id, SHIELD_PIERCED_MODIFIER);
    }

    @Override
    public void advanceInCombat(ShipAPI ship, float amount) {
        if (!ship.isAlive())
            return;

        MutableShipStatsAPI stats = ship.getMutableStats();

        stats.getHardFluxDissipationFraction().modifyFlat(spec.getId(), HARD_FLUX_DISSIPATION_MODIFIER * 0.01f);
    }
}