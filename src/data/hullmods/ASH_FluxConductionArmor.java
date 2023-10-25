package data.hullmods;

import java.awt.Color;

import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;

public class ASH_FluxConductionArmor extends BaseHullMod {
    public static final float FLUX_CAPACITY_MULT = 0.20f;
    public static final float ENERGY_DAMAGE_TAKEN_MULT = 0.10f;
    public static final float ARMOR_BONUS_MULT = 0.10f;

    @Override
    public void applyEffectsBeforeShipCreation(ShipAPI.HullSize hullSize, MutableShipStatsAPI stats, String id) {
        stats.getFluxCapacity().modifyMult(id, 1f + FLUX_CAPACITY_MULT);
        stats.getEnergyDamageTakenMult().modifyMult(id, 1f + ENERGY_DAMAGE_TAKEN_MULT);
        stats.getArmorBonus().modifyMult(id, 1f - ARMOR_BONUS_MULT);
        if (isSMod(stats)) {
            stats.getArmorBonus().unmodify(id);
        }
    }

    @Override
    public void addPostDescriptionSection(TooltipMakerAPI tooltip, HullSize hullSize, ShipAPI ship, float width, boolean isForModSpec) {
        float pad = 3f;
        float opad = 10f;
        Color good = Misc.getPositiveHighlightColor();
        Color bad = Misc.getNegativeHighlightColor();

        tooltip.setBulletedListMode(" - ");
        tooltip.addPara("Increases the ship's flux capacity by %s", opad, good, Math.round(FLUX_CAPACITY_MULT * 100f) + "%");
        tooltip.addPara("Increases the energy damage taken by %s", pad, bad, Math.round(ENERGY_DAMAGE_TAKEN_MULT * 100f) + "%");
        tooltip.addPara("Decreases the armor bonus of the ship by %s", pad, bad, Math.round(ARMOR_BONUS_MULT * 100f) + "%");
        tooltip.setBulletedListMode(null);
    }

    @Override
    public void addSModEffectSection(TooltipMakerAPI tooltip, ShipAPI.HullSize hullSize, ShipAPI ship, float width, boolean isForModSpec, boolean isForBuildInList) {
        float pad = 3f;

        tooltip.setBulletedListMode(" - ");
        tooltip.addPara("Fully negates the armor bonus penalty", pad);
        tooltip.setBulletedListMode(null);
    }

    @Override
    public boolean hasSModEffect() {
        return true;
    }
}