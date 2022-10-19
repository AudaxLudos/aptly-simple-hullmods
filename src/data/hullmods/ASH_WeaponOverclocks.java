package data.hullmods;

import java.awt.Color;

import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.ui.Alignment;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;

public class ASH_WeaponOverclocks extends BaseHullMod {
    public static final float WEAPON_FIRE_RATE_MULTIPLIER = 0.20f;
    public static final float WEAPON_AMMO_REGEN_MULTIPLIER = 0.20f;

    @Override
    public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {
        stats.getBallisticRoFMult().modifyMult(id, 1f + WEAPON_FIRE_RATE_MULTIPLIER);
        stats.getBallisticAmmoRegenMult().modifyMult(id, 1f + WEAPON_AMMO_REGEN_MULTIPLIER);

        stats.getEnergyRoFMult().modifyMult(id, 1f + WEAPON_FIRE_RATE_MULTIPLIER);
        stats.getEnergyAmmoRegenMult().modifyMult(id, 1f + WEAPON_AMMO_REGEN_MULTIPLIER);
    }

    @Override
    public void addPostDescriptionSection(TooltipMakerAPI tooltip, HullSize hullSize, ShipAPI ship, float width, boolean isForModSpec) {
        float pad = 3f;
        float opad = 10f;
        Color good = Misc.getPositiveHighlightColor();

        tooltip.addSectionHeading("Effects:", Alignment.MID, opad);
        tooltip.setBulletedListMode(" - ");
        tooltip.addPara("Increases the fire rate of non-missile weapons by %s", opad, good, Math.round(WEAPON_FIRE_RATE_MULTIPLIER * 100f) + "%");
        tooltip.addPara("Increases the ammo regen of non-missile weapons by %s", pad, good, Math.round(WEAPON_AMMO_REGEN_MULTIPLIER * 100f) + "%");
        tooltip.setBulletedListMode(null);
    }
}