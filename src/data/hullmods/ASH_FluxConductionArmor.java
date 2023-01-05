package data.hullmods;

import java.awt.Color;

import org.lwjgl.input.Keyboard;

import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.ui.Alignment;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;

import data.ASH_Utils;

public class ASH_FluxConductionArmor extends BaseHullMod {
    public static final float FLUX_CAPACITY_MULTIPLIER = 0.15f;
    public static final float ENERGY_DAMAGE_TAKEN_MULTIPLIER = 0.15f;

    @Override
    public void applyEffectsBeforeShipCreation(ShipAPI.HullSize hullSize, MutableShipStatsAPI stats, String id) {
        float fluxCapacityMult = FLUX_CAPACITY_MULTIPLIER;
        float energyDamageTakenMult = ENERGY_DAMAGE_TAKEN_MULTIPLIER;

        if (stats.getVariant().getSMods().contains(id) && ASH_Utils.isModEnabled()) {
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
        Color story = Misc.getStoryOptionColor();

        if (!ship.getVariant().getSMods().contains(spec.getId()) || !ASH_Utils.isModEnabled()) {
            tooltip.addSectionHeading("Effects:", Alignment.MID, opad);
            tooltip.setBulletedListMode(" - ");
            tooltip.addPara("Increases the ship's base flux capacity by %s", opad, good, Math.round(FLUX_CAPACITY_MULTIPLIER * 100f) + "%");
            tooltip.addPara("Increases the energy damage taken by %s", pad, bad, Math.round(ENERGY_DAMAGE_TAKEN_MULTIPLIER * 100f) + "%");
            tooltip.setBulletedListMode(null);

            if (!ASH_Utils.isModEnabled())
                return;

            if (!Keyboard.isKeyDown(Keyboard.getKeyIndex("F1"))) {
                tooltip.addPara("Press F1 to show S-mod effects", Misc.getGrayColor(), opad);
                return;
            }
        }

        tooltip.addSectionHeading("S-Mod Effects:", story, Misc.setAlpha(story, 110), Alignment.MID, opad);
        tooltip.setBulletedListMode(" - ");
        tooltip.addPara("Increases the ship's base flux capacity by %s", opad, good, Math.round((FLUX_CAPACITY_MULTIPLIER + 0.05f) * 100f) + "%");
        tooltip.addPara("Increases the energy damage taken by %s", pad, bad, Math.round((ENERGY_DAMAGE_TAKEN_MULTIPLIER - 0.05f) * 100f) + "%");
        tooltip.setBulletedListMode(null);
    }
}