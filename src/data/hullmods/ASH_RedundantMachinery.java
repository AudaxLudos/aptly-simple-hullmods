package data.hullmods;

import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;

public class ASH_RedundantMachinery extends BaseHullMod {
    public static final float OVERLOAD_TIME_MODIFIER = 0.5f;
    public static final float ENGINE_REPAIR_TIME_MODIFIER = 0.5f;
    public static final float WEAPON_REPAIR_TIME_MODIFIER = 0.5f;
    public static final float SUPPLIES_PER_MONTH_MODIFIER = 50f;

    @Override
    public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {
        stats.getOverloadTimeMod().modifyMult(id, OVERLOAD_TIME_MODIFIER);
        stats.getCombatEngineRepairTimeMult().modifyMult(id, ENGINE_REPAIR_TIME_MODIFIER);
        stats.getCombatWeaponRepairTimeMult().modifyMult(id, WEAPON_REPAIR_TIME_MODIFIER);
        stats.getSuppliesPerMonth().modifyPercent(id, SUPPLIES_PER_MONTH_MODIFIER);
    }

    @Override
    public String getDescriptionParam(int index, HullSize hullSize) {
        if (index == 0)
            return OVERLOAD_TIME_MODIFIER + "";
        if (index == 1)
            return Math.round(SUPPLIES_PER_MONTH_MODIFIER) + "%";
        return null;
    }
}