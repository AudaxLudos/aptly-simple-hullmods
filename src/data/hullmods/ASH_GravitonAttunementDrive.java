package data.hullmods;

import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.impl.hullmods.BaseLogisticsHullMod;

public class ASH_GravitonAttunementDrive extends BaseLogisticsHullMod {
    public static final float FLEET_BURN_MODIFIER = 3f;
    public static final float SENSOR_PROFILE_MODIFIER = 3f;
    public static final float SUPPLIES_PER_MONTH_MODIFIER = 3f;

    @Override
    public void applyEffectsBeforeShipCreation(ShipAPI.HullSize hullSize, MutableShipStatsAPI stats, String id) {
        stats.getDynamic().getMod("fleet_burn_bonus").modifyFlat(id, FLEET_BURN_MODIFIER);
        stats.getSensorProfile().modifyMult(id, SENSOR_PROFILE_MODIFIER);
        stats.getSuppliesPerMonth().modifyMult(id, SUPPLIES_PER_MONTH_MODIFIER);
    }

    @Override
    public String getUnapplicableReason(ShipAPI ship) {
        if (ship.getHullSize() != HullSize.CAPITAL_SHIP)
            return "Ship must be a Capital Ship";
        return null;
    }

    @Override
    public boolean isApplicableToShip(ShipAPI ship) {
        return ship.getHullSize() == HullSize.CAPITAL_SHIP;
    }

    @Override
    public String getDescriptionParam(int index, HullSize hullSize) {
        if (index == 0)
            return Math.round(FLEET_BURN_MODIFIER) + "";
        if (index == 1)
            return Math.round(3f) + "";
        return null;
    }
}