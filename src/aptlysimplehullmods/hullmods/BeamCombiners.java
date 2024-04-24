package aptlysimplehullmods.hullmods;

import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;

import java.awt.*;

public class BeamCombiners extends BaseHullMod {
    public static final float BEAM_STATS_MULT = 0.20f;

    @Override
    public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {
        stats.getBeamWeaponDamageMult().modifyMult(id, 1f + BEAM_STATS_MULT);

        float beamFluxCostMult = BEAM_STATS_MULT;
        if (isSMod(stats))
            beamFluxCostMult *= 0.5f;
        stats.getBeamWeaponFluxCostMult().modifyMult(id, 1f + beamFluxCostMult);
    }

    @Override
    public void addPostDescriptionSection(TooltipMakerAPI tooltip, HullSize hullSize, ShipAPI ship, float width, boolean isForModSpec) {
        float pad = 3f;
        float oPad = 10f;
        Color good = Misc.getPositiveHighlightColor();
        Color bad = Misc.getNegativeHighlightColor();

        tooltip.addPara("Increases the damage of beam weapons by %s.", oPad, good, Math.round(BEAM_STATS_MULT * 100f) + "%");
        tooltip.addPara("Increases the flux cost of beam weapons by %s.", pad, bad, Math.round(BEAM_STATS_MULT * 100f) + "%");
    }

    @Override
    public void addSModEffectSection(TooltipMakerAPI tooltip, ShipAPI.HullSize hullSize, ShipAPI ship, float width, boolean isForModSpec, boolean isForBuildInList) {
        float oPad = 10f;
        Color good = Misc.getPositiveHighlightColor();

        tooltip.addPara("Reduces the flux cost penalty of beam weapons by %s.", oPad, good, Math.round(BEAM_STATS_MULT * 100f * 0.5f) + "%");
    }

    @Override
    public boolean hasSModEffect() {
        return true;
    }
}