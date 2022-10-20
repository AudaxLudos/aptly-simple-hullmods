package data.hullmods;

import java.awt.Color;

import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.ui.Alignment;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;

public class ASH_BeamCombiners extends BaseHullMod {
    public static final float BEAM_STATS_MULTIPLIER = 0.20f;

    @Override
    public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {
        stats.getBeamWeaponDamageMult().modifyMult(id, 1f + BEAM_STATS_MULTIPLIER);
        stats.getBeamWeaponFluxCostMult().modifyMult(id, 1f + BEAM_STATS_MULTIPLIER);
    }

    @Override
    public void addPostDescriptionSection(TooltipMakerAPI tooltip, HullSize hullSize, ShipAPI ship, float width, boolean isForModSpec) {
        float pad = 3f;
        float opad = 10f;
        Color good = Misc.getPositiveHighlightColor();
        Color bad = Misc.getNegativeHighlightColor();

        tooltip.addSectionHeading("Effects:", Alignment.MID, opad);
        tooltip.setBulletedListMode(" - ");
        tooltip.addPara("Increases the damage of beam weapons by %s", opad, good, Math.round(BEAM_STATS_MULTIPLIER * 100f) + "%");
        tooltip.addPara("Increases the flux cost of beam weapons by %s", pad, bad, Math.round(BEAM_STATS_MULTIPLIER * 100f) + "%");
        tooltip.setBulletedListMode(null);
    }
}