package aptlysimplehullmods.hullmods;

import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;

import java.awt.*;

public class ShockAbsorbers extends BaseHullMod {
    public static final float EXPLOSIVE_DMG_TAKEN_MULT = 0.20f;
    public static final float KINETIC_DMG_TAKEN_MULT = 0.20f;

    @Override
    public void applyEffectsBeforeShipCreation(ShipAPI.HullSize hullSize, MutableShipStatsAPI stats, String id) {
        stats.getHighExplosiveDamageTakenMult().modifyMult(id, 1f - EXPLOSIVE_DMG_TAKEN_MULT);
        stats.getKineticDamageTakenMult().modifyMult(id, 1f + KINETIC_DMG_TAKEN_MULT);
    }

    @Override
    public void addPostDescriptionSection(TooltipMakerAPI tooltip, ShipAPI.HullSize hullSize, ShipAPI ship, float width, boolean isForModSpec) {
        float oPad = 10f;
        Color good = Misc.getPositiveHighlightColor();
        Color bad = Misc.getNegativeHighlightColor();

        tooltip.setBulletedListMode(" - ")  ;
        tooltip.addPara("Decreases the explosive damage taken of the ship by %s", oPad, good, Math.round(EXPLOSIVE_DMG_TAKEN_MULT * 100f) + "%");
        tooltip.addPara("Increases the kinetic damage taken of the ship by %s", oPad, bad, Math.round(KINETIC_DMG_TAKEN_MULT * 100f) + "%");
        tooltip.setBulletedListMode(null);
    }
}
