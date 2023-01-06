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

public class ASH_VolatileWarheads extends BaseHullMod {
    public static final float MISSILE_DAMAGE_MULTIPLIER = 0.20f;
    public static final float MISSILE_SPEED_MULTIPLIER = 0.10f;
    public static final float MISSILE_HEALTH_MULTIPLIER = 0.10f;

    @Override
    public void applyEffectsBeforeShipCreation(ShipAPI.HullSize hullSize, MutableShipStatsAPI stats, String id) {
        stats.getMissileWeaponDamageMult().modifyMult(id, 1f + MISSILE_DAMAGE_MULTIPLIER);
        stats.getMissileMaxSpeedBonus().modifyMult(id, 1f + -MISSILE_HEALTH_MULTIPLIER);
        stats.getMissileHealthBonus().modifyMult(id, 1f + -MISSILE_HEALTH_MULTIPLIER);

        if (stats.getVariant().getSMods().contains(id) && ASH_Utils.isModEnabled())
            stats.getMissileHealthBonus().unmodifyMult(id);
    }

    @Override
    public void addPostDescriptionSection(TooltipMakerAPI tooltip, HullSize hullSize, ShipAPI ship, float width, boolean isForModSpec) {
        float pad = 3f;
        float opad = 10f;
        Color good = Misc.getPositiveHighlightColor();
        Color bad = Misc.getNegativeHighlightColor();
        Color story = Misc.getStoryOptionColor();

        if (ship == null || !ship.getVariant().getSMods().contains(spec.getId()) || !ASH_Utils.isModEnabled()) {
            tooltip.addSectionHeading("Effects:", Alignment.MID, opad);
            tooltip.setBulletedListMode(" - ");
            tooltip.addPara("Increases the damage of missiles by %s", opad, good, Math.round(MISSILE_DAMAGE_MULTIPLIER * 100f) + "%");
            tooltip.addPara("Reduces the max speed of missiles by %s", pad, bad, Math.round(MISSILE_SPEED_MULTIPLIER * 100f) + "%");
            tooltip.addPara("Reduces the health of missiles by %s", pad, bad, Math.round(MISSILE_HEALTH_MULTIPLIER * 100f) + "%");
            tooltip.setBulletedListMode(null);
        }

        if (!ASH_Utils.isModEnabled())
            return;
        if (!Keyboard.isKeyDown(Keyboard.KEY_F1)) {
            tooltip.addPara("Press or Hold F1 to show S-mod effects", Misc.getGrayColor(), opad);
            return;
        }

        tooltip.addSectionHeading("S-Mod Effects:", story, Misc.setAlpha(story, 110), Alignment.MID, opad);
        tooltip.setBulletedListMode(" - ");
        tooltip.addPara("Increases the damage of missiles by %s", opad, good, Math.round(MISSILE_DAMAGE_MULTIPLIER * 100f) + "%");
        tooltip.addPara("Reduces the max speed of missiles by %s", pad, bad, Math.round(MISSILE_SPEED_MULTIPLIER * 100f) + "%");
        tooltip.setBulletedListMode(null);
    }
}