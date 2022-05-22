package data.hullmods;

import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;

public class ASH_BeamCombiners extends BaseHullMod {
    public static final float BEAM_DAMAGE_MODIFIER = 20f;
    public static final float BEAM_FLUX_COST_MODIFIER = 40f;
    public static final float BEAM_TURN_RATE_MODIFIER = 20f;

    @Override
    public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {
        stats.getBeamWeaponDamageMult().modifyPercent(id, BEAM_DAMAGE_MODIFIER);
        stats.getBeamWeaponFluxCostMult().modifyPercent(id, BEAM_FLUX_COST_MODIFIER);
        stats.getBeamWeaponTurnRateBonus().modifyPercent(id, -BEAM_TURN_RATE_MODIFIER);
    }

    @Override
    public String getDescriptionParam(int index, HullSize hullSize) {
        if (index == 0)
            return Math.round(BEAM_DAMAGE_MODIFIER) + "%";
        if (index == 1)
            return Math.round(BEAM_FLUX_COST_MODIFIER) + "%";
        if (index == 2)
            return Math.round(BEAM_TURN_RATE_MODIFIER) + "%";
        return null;
    }
}