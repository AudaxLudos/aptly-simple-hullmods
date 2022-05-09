package data.hullmods;

import java.util.HashMap;
import java.util.Map;

import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;

public class ASH_FluxConductionArmor extends BaseHullMod {
    public static final float FLUX_CAPACITY_MODIFIER = 15f;
    public static final float EMP_DMG_MODIFIER = 30f;
    private static Map ARMOR_MODIFIER = new HashMap();
    static {
        ARMOR_MODIFIER.put(HullSize.FRIGATE, 25f);
        ARMOR_MODIFIER.put(HullSize.DESTROYER, 50f);
        ARMOR_MODIFIER.put(HullSize.CRUISER, 75f);
        ARMOR_MODIFIER.put(HullSize.CAPITAL_SHIP, 100f);
    }

    @Override
    public void applyEffectsBeforeShipCreation(ShipAPI.HullSize hullSize, MutableShipStatsAPI stats, String id) {
        stats.getArmorBonus().modifyFlat(id, (Float) ARMOR_MODIFIER.get(hullSize));
        stats.getFluxCapacity().modifyPercent(id, FLUX_CAPACITY_MODIFIER);
        stats.getEmpDamageTakenMult().modifyPercent(id, EMP_DMG_MODIFIER);
    }

    @Override
    public String getDescriptionParam(int index, HullSize hullSize) {
        if (index == 0)
            return Math.round(FLUX_CAPACITY_MODIFIER) + "%";
        if (index == 1)
            return ((Float) ARMOR_MODIFIER.get(HullSize.FRIGATE)).intValue() + "";
        if (index == 2)
            return ((Float) ARMOR_MODIFIER.get(HullSize.DESTROYER)).intValue() + "";
        if (index == 3)
            return ((Float) ARMOR_MODIFIER.get(HullSize.CRUISER)).intValue() + "";
        if (index == 4)
            return ((Float) ARMOR_MODIFIER.get(HullSize.CAPITAL_SHIP)).intValue() + "";
        if (index == 5)
            return Math.round(EMP_DMG_MODIFIER) + "%";
        return null;
    }
}