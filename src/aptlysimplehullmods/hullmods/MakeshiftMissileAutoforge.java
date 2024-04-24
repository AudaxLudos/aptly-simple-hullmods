package aptlysimplehullmods.hullmods;

import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.combat.WeaponAPI;
import com.fs.starfarer.api.combat.WeaponAPI.WeaponType;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;

import java.awt.*;
import java.util.List;

public class MakeshiftMissileAutoforge extends BaseHullMod {
    public static final float MISSILE_AMMO_RELOAD_SIZE_MOD = 10f;
    public static final float MISSILE_AMMO_PER_SECOND_MOD = 50f;

    @Override
    public void advanceInCombat(ShipAPI ship, float amount) {
        if (!ship.isAlive())
            return;

        List<WeaponAPI> weapons = ship.getAllWeapons();
        for (WeaponAPI weapon : weapons) {
            if (weapon.getType() != WeaponType.MISSILE)
                continue;
            if (!weapon.usesAmmo())
                continue;
            if (weapon.getAmmoPerSecond() != 0f)
                continue;
            if (weapon.getAmmo() <= 0f || isSMod(ship)) {
                float ammoReloadSize = (float) Math.ceil(weapon.getMaxAmmo() / MISSILE_AMMO_RELOAD_SIZE_MOD);
                float ammoPerSecond = ammoReloadSize / MISSILE_AMMO_PER_SECOND_MOD;
                weapon.getAmmoTracker().setReloadSize(ammoReloadSize);
                weapon.getAmmoTracker().setAmmoPerSecond(ammoPerSecond);
            }
        }
    }

    @Override
    public void addPostDescriptionSection(TooltipMakerAPI tooltip, HullSize hullSize, ShipAPI ship, float width, boolean isForModSpec) {
        float pad = 3f;
        float oPad = 10f;
        Color b = Misc.getHighlightColor();
        Color good = Misc.getPositiveHighlightColor();
        Color bad = Misc.getNegativeHighlightColor();

        tooltip.addPara("When non-reloading missile weapons %s:", oPad, b, "run out of ammo");
        tooltip.setBulletWidth(20f);
        tooltip.setBulletedListMode("");
        tooltip.addPara("^ Start Replenishing %s of ammo.", pad, good, Math.round(MISSILE_AMMO_RELOAD_SIZE_MOD) + "%");
        tooltip.addPara("^ Replenish ammo every %s.", pad, bad, Math.round(MISSILE_AMMO_PER_SECOND_MOD) + " seconds");
        tooltip.setBulletedListMode(null);
    }

    @Override
    public void addSModEffectSection(TooltipMakerAPI tooltip, ShipAPI.HullSize hullSize, ShipAPI ship, float width, boolean isForModSpec, boolean isForBuildInList) {
        float oPad = 10f;
        Color good = Misc.getPositiveHighlightColor();

        tooltip.addPara("Replenish ammo at the %s of combat for non reloading-missile weapons.", oPad, good, "start");
    }

    @Override
    public boolean hasSModEffect() {
        return true;
    }
}