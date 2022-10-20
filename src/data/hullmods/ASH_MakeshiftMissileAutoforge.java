package data.hullmods;

import java.awt.Color;
import java.util.List;

import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.WeaponAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.combat.WeaponAPI.WeaponType;
import com.fs.starfarer.api.ui.Alignment;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;

public class ASH_MakeshiftMissileAutoforge extends BaseHullMod {
    public static final float MISSILE_AMMO_RELOAD_SIZE_MODIFIER = 10f;
    public static final float MISSILE_AMMO_PER_SECOND_MODIFIER = 50f;

    @Override
    public void advanceInCombat(ShipAPI ship, float amount) {
        if (!ship.isAlive())
            return;

        List<WeaponAPI> weapons = ship.getAllWeapons();
        for (WeaponAPI weapon : weapons) {
            if (weapon.getType() == WeaponType.MISSILE && weapon.usesAmmo() && weapon.getAmmo() <= 0f && weapon.getAmmoPerSecond() == 0f) {
                float ammoReloadSize = (float) Math.ceil(weapon.getMaxAmmo() / MISSILE_AMMO_RELOAD_SIZE_MODIFIER);
                float ammoPerSecond = ammoReloadSize / MISSILE_AMMO_PER_SECOND_MODIFIER;
                weapon.getAmmoTracker().setReloadSize(ammoReloadSize);
                weapon.getAmmoTracker().setAmmoPerSecond(ammoPerSecond);
            }
        }
    }

    @Override
    public void addPostDescriptionSection(TooltipMakerAPI tooltip, HullSize hullSize, ShipAPI ship, float width, boolean isForModSpec) {
        float pad = 3f;
        float opad = 10f;
        Color b = Misc.getHighlightColor();
        Color good = Misc.getPositiveHighlightColor();
        Color bad = Misc.getNegativeHighlightColor();

        tooltip.addSectionHeading("Effects:", Alignment.MID, opad);
        tooltip.setBulletedListMode("");
        tooltip.addPara("When missile weapons %s:", opad, b, "run out of ammo");
        tooltip.setBulletedListMode(" ^ ");
        tooltip.addPara("Start Replenishing %s of ammo", pad, good, Math.round(MISSILE_AMMO_RELOAD_SIZE_MODIFIER) + "%");
        tooltip.addPara("Only replenish ammo every %s", pad, bad, Math.round(MISSILE_AMMO_PER_SECOND_MODIFIER) + " seconds");
        tooltip.setBulletedListMode(null);
    }
}