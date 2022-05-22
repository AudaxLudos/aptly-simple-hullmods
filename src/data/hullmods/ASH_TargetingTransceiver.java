package data.hullmods;

import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.impl.campaign.ids.HullMods;

import org.lazywizard.lazylib.combat.AIUtils;

public class ASH_TargetingTransceiver extends BaseHullMod {
    public static final float WEAPON_RANGE_MODIFIER = 10f;
    public static final float AUTOFIRE_AIM_ACCURACY_MODIFIER = 40f;
    public static final float MAX_RANGE_THRESHOLD = 750f;

    @Override
    public String getDescriptionParam(int index, HullSize hullSize) {
        if (index == 0)
            return Math.round(WEAPON_RANGE_MODIFIER) + "%";
        if (index == 1)
            return Math.round(AUTOFIRE_AIM_ACCURACY_MODIFIER) + "%";
        if (index == 2)
            return Math.round(MAX_RANGE_THRESHOLD + 250f) + "su";
        if (index == 3)
            return "Targeting Core / Unit";
        return null;
    }

    @Override
    public void advanceInCombat(ShipAPI ship, float amount) {
        if (!ship.isAlive())
            return;

        MutableShipStatsAPI stats = ship.getMutableStats();
        float computedRangeBonus = 0f;
        float computedAutofireAimAccuracy = 0f;

        for (ShipAPI ally : AIUtils.getNearbyAllies(ship, MAX_RANGE_THRESHOLD)) {
            if (ally.isFrigate() || ally.isDestroyer() || ally.isDrone() || ally.isFighter())
                continue;

            if (ally.getVariant().hasHullMod(HullMods.DEDICATED_TARGETING_CORE) ||
                    ally.getVariant().hasHullMod(HullMods.INTEGRATED_TARGETING_UNIT) ||
                    ally.getVariant().hasHullMod(HullMods.ADVANCED_TARGETING_CORE)) {
                computedRangeBonus = WEAPON_RANGE_MODIFIER;
                computedAutofireAimAccuracy = AUTOFIRE_AIM_ACCURACY_MODIFIER;
            }
        }

        if (ship.getHullSize() == HullSize.FRIGATE || ship.getHullSize() == HullSize.DESTROYER) {
            stats.getEnergyWeaponRangeBonus().modifyPercent(spec.getId(), computedRangeBonus);
            stats.getBallisticWeaponRangeBonus().modifyPercent(spec.getId(), computedRangeBonus);
        } else
            stats.getAutofireAimAccuracy().modifyPercent(spec.getId(), computedAutofireAimAccuracy);
    }
}