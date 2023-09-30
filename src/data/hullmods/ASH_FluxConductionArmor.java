package data.hullmods;

import java.awt.Color;

import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.ui.Alignment;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;

public class ASH_FluxConductionArmor extends BaseHullMod {
    public static final float FLUX_CAPACITY_MULTIPLIER = 0.15f;
    public static final float ENERGY_DAMAGE_TAKEN_MULTIPLIER = 0.15f;

    @Override
    public void applyEffectsBeforeShipCreation(ShipAPI.HullSize hullSize, MutableShipStatsAPI stats, String id) {
        float fluxCapacityMult = FLUX_CAPACITY_MULTIPLIER;
        float energyDamageTakenMult = ENERGY_DAMAGE_TAKEN_MULTIPLIER;

        if (stats.getVariant().getSMods().contains(id)) {
            fluxCapacityMult += 0.05f;
            energyDamageTakenMult -= 0.05f;
        }

        stats.getFluxCapacity().modifyPercent(id, 100f * fluxCapacityMult);
        stats.getEnergyDamageTakenMult().modifyMult(id, 1f + energyDamageTakenMult);
    }

    @Override
    public void addPostDescriptionSection(TooltipMakerAPI tooltip, HullSize hullSize, ShipAPI ship, float width, boolean isForModSpec) {
        float pad = 3f;
        float opad = 10f;
        Color good = Misc.getPositiveHighlightColor();
        Color bad = Misc.getNegativeHighlightColor();

        tooltip.addSectionHeading("Effects:", Alignment.MID, opad);
        tooltip.setBulletedListMode(" - ");
        tooltip.addPara("Increases the ship's base flux capacity by %s", opad, good, Math.round(FLUX_CAPACITY_MULTIPLIER * 100f) + "%");
        tooltip.addPara("Increases the energy damage taken by %s", pad, bad, Math.round(ENERGY_DAMAGE_TAKEN_MULTIPLIER * 100f) + "%");
        tooltip.setBulletedListMode(null);
    }

    @Override
    public void addSModEffectSection(TooltipMakerAPI tooltip, ShipAPI.HullSize hullSize, ShipAPI ship, float width, boolean isForModSpec, boolean isForBuildInList) {
        float pad = 3f;
        float opad = 10f;
        Color good = Misc.getPositiveHighlightColor();
        Color bad = Misc.getNegativeHighlightColor();

        tooltip.setBulletedListMode(" - ");
        tooltip.addPara("Increases the ship's base flux capacity by an additional %s", opad, good, Math.round(5f) + "%");
        tooltip.addPara("Reduces the energy damage taken penalty by %s", pad, bad, Math.round(5f) + "%");
        tooltip.setBulletedListMode(null);
    }

    @Override
    public boolean hasSModEffect() {
        return true;
    }
}