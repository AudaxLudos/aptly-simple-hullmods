package data.hullmods;

import java.awt.Color;

import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.ui.Alignment;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;

public class ASH_FluxAmplifiers extends BaseHullMod {
    public static final float WEAPON_DAMAGE_MULTIPLIER = 0.20f;
    public static final float WEAPON_FLUX_MULTIPLIER = 0.10f;
    public static final float WEAPON_FIRE_RATE_MULTIPLIER = 0.10f;

    @Override
    public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {
        stats.getBallisticWeaponDamageMult().modifyMult(id, 1f + WEAPON_DAMAGE_MULTIPLIER);
        stats.getBallisticWeaponFluxCostMod().modifyMult(id, 1f + WEAPON_FLUX_MULTIPLIER);
        stats.getBallisticRoFMult().modifyMult(id, 1f + -WEAPON_FIRE_RATE_MULTIPLIER);

        stats.getEnergyWeaponDamageMult().modifyMult(id, 1f + WEAPON_DAMAGE_MULTIPLIER);
        stats.getEnergyWeaponFluxCostMod().modifyMult(id, 1f + WEAPON_FLUX_MULTIPLIER);
        stats.getEnergyRoFMult().modifyMult(id, 1f + -WEAPON_FIRE_RATE_MULTIPLIER);
    }

    @Override
    public void addPostDescriptionSection(TooltipMakerAPI tooltip, HullSize hullSize, ShipAPI ship, float width, boolean isForModSpec) {
        float pad = 3f;
        float opad = 10f;
        Color good = Misc.getPositiveHighlightColor();
        Color bad = Misc.getNegativeHighlightColor();

        tooltip.addSectionHeading("Effects:", Alignment.MID, opad);
        tooltip.setBulletedListMode(" - ");
        tooltip.addPara("Increases the damage of non-missile weapons by %s", opad, good, Math.round(WEAPON_DAMAGE_MULTIPLIER * 100f) + "%");
        tooltip.addPara("Increases the flux cost of non-missile weapons by %s", pad, bad, Math.round(WEAPON_FLUX_MULTIPLIER * 100f) + "%");
        tooltip.addPara("Decreases the fire rate of non-missile weapons by %s", pad, bad, Math.round(WEAPON_FIRE_RATE_MULTIPLIER * 100f) + "%");
        tooltip.setBulletedListMode(null);
    }
}