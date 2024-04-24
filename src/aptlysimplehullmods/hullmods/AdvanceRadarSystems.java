package aptlysimplehullmods.hullmods;

import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;

import java.awt.*;

public class AdvanceRadarSystems extends BaseHullMod {
    public static final float SIGHT_RADIUS_MULT = 0.25f;
    public static final float WEAPON_RANGE_MULT = 0.05f;

    @Override
    public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {
        stats.getSightRadiusMod().modifyMult(id, 1f + SIGHT_RADIUS_MULT);

        if (isSMod(stats)) {
            stats.getEnergyWeaponRangeBonus().modifyMult(id, 1f + WEAPON_RANGE_MULT);
            stats.getBallisticWeaponRangeBonus().modifyMult(id, 1f + WEAPON_RANGE_MULT);
        }
    }

    @Override
    public void addPostDescriptionSection(TooltipMakerAPI tooltip, HullSize hullSize, ShipAPI ship, float width, boolean isForModSpec) {
        float oPad = 10f;
        Color good = Misc.getPositiveHighlightColor();

        tooltip.addPara("Increases the ship's sight radius by %s.", oPad, good, Math.round(SIGHT_RADIUS_MULT * 100f) + "%");
    }

    @Override
    public void addSModEffectSection(TooltipMakerAPI tooltip, ShipAPI.HullSize hullSize, ShipAPI ship, float width, boolean isForModSpec, boolean isForBuildInList) {
        float oPad = 10;
        Color good = Misc.getPositiveHighlightColor();

        tooltip.addPara("Increases the range of non-missile weapons by %s.", oPad, good, Math.round(WEAPON_RANGE_MULT * 100f) + "%");
    }

    @Override
    public boolean hasSModEffect() {
        return true;
    }
}