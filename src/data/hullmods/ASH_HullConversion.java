package data.hullmods;

import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;

public class ASH_HullConversion extends BaseHullMod {
    public static final float SHIP_ARMOR_MODIFIER = 20f;
    public static final float SHIP_MIN_ARMOR_MODIFIER = 0.05f;
    public static final float SHIP_HULL_MODIFIER = 0.8f;
    public static final float SHIP_BREAK_CHANCE_MODIFIER = 2f;

    @Override
    public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {
        stats.getArmorBonus().modifyPercent(id, SHIP_ARMOR_MODIFIER);
        stats.getMinArmorFraction().modifyFlat(id, SHIP_MIN_ARMOR_MODIFIER);
        stats.getHullBonus().modifyMult(id, SHIP_HULL_MODIFIER);
        stats.getBreakProb().modifyMult(id, SHIP_BREAK_CHANCE_MODIFIER);
    }

    @Override
    public String getDescriptionParam(int index, HullSize hullSize) {
        if (index == 0)
            return Math.round(SHIP_ARMOR_MODIFIER) + "%";
        if (index == 1)
            return Math.round(SHIP_MIN_ARMOR_MODIFIER * 100) + "%";
        if (index == 2)
            return SHIP_HULL_MODIFIER + "";
        if (index == 3)
            return Math.round(SHIP_BREAK_CHANCE_MODIFIER) + "";
        return null;
    }
}