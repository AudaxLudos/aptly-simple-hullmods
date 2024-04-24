package aptlysimplehullmods.hullmods;

import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;

import java.awt.*;

public class FluxLimiters extends BaseHullMod {
    public static final float WEAPON_FLUX_MULT = 0.20f;
    public static final float WEAPON_DAMAGE_MULT = 0.10f;
    public static final float WEAPON_FIRE_RATE_MULT = 0.10f;

    @Override
    public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {
        stats.getBallisticWeaponFluxCostMod().modifyMult(id, 1f - WEAPON_FLUX_MULT);
        stats.getBallisticWeaponDamageMult().modifyMult(id, 1f - WEAPON_DAMAGE_MULT);

        stats.getEnergyWeaponFluxCostMod().modifyMult(id, 1f - WEAPON_FLUX_MULT);
        stats.getEnergyWeaponDamageMult().modifyMult(id, 1f - WEAPON_DAMAGE_MULT);

        stats.getBallisticRoFMult().modifyMult(id, 1f - WEAPON_FIRE_RATE_MULT);
        stats.getEnergyRoFMult().modifyMult(id, 1f - WEAPON_FIRE_RATE_MULT);

        if (isSMod(stats)) {
            stats.getBallisticRoFMult().unmodifyMult(id);
            stats.getEnergyRoFMult().unmodifyMult(id);
        }
    }

    @Override
    public void addPostDescriptionSection(TooltipMakerAPI tooltip, HullSize hullSize, ShipAPI ship, float width, boolean isForModSpec) {
        float pad = 3f;
        float oPad = 10f;
        Color good = Misc.getPositiveHighlightColor();
        Color bad = Misc.getNegativeHighlightColor();

        tooltip.addPara("Decreases the flux cost of non-missile weapons by %s.", oPad, good, Math.round(WEAPON_FLUX_MULT * 100f) + "%");
        tooltip.addPara("Decreases the damage of non-missile weapons by %s.", pad, bad, Math.round(WEAPON_DAMAGE_MULT * 100f) + "%");
        tooltip.addPara("Decreases the fire rate of non-missile weapons by %s.", pad, bad, Math.round(WEAPON_FIRE_RATE_MULT * 100f) + "%");
    }

    @Override
    public void addSModEffectSection(TooltipMakerAPI tooltip, ShipAPI.HullSize hullSize, ShipAPI ship, float width, boolean isForModSpec, boolean isForBuildInList) {
        float oPad = 10f;
        Color good = Misc.getPositiveHighlightColor();

        tooltip.addPara("Fully %s the fire rate penalty of non-missile weapons.", oPad, good, "negates");
    }

    @Override
    public boolean hasSModEffect() {
        return true;
    }
}