package aptlysimplehullmods.hullmods.hidden;

import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;

import java.awt.*;

public class ShockAbsorbers extends BaseHullMod {
    public static final float DMG_TAKEN_MULT = 0.10f;
    public static final float KINETIC_DMG_TAKEN_MULT = 0.20f;

    @Override
    public void applyEffectsBeforeShipCreation(ShipAPI.HullSize hullSize, MutableShipStatsAPI stats, String id) {
        stats.getHighExplosiveDamageTakenMult().modifyMult(id, 1f - DMG_TAKEN_MULT);
        stats.getFragmentationDamageTakenMult().modifyMult(id, 1f - DMG_TAKEN_MULT);

        float kineticDmgTknMult = KINETIC_DMG_TAKEN_MULT;
        if (isSMod(stats))
            kineticDmgTknMult *= 0.5f;
        stats.getKineticDamageTakenMult().modifyMult(id, 1f + kineticDmgTknMult);
    }

    @Override
    public void addPostDescriptionSection(TooltipMakerAPI tooltip, ShipAPI.HullSize hullSize, ShipAPI ship, float width, boolean isForModSpec) {
        float oPad = 10f;
        float pad = 5f;
        Color good = Misc.getPositiveHighlightColor();
        Color bad = Misc.getNegativeHighlightColor();

        tooltip.setBulletedListMode(" - ");
        tooltip.addPara("Decreases the ship's explosive damage taken by %s", oPad, good, Math.round(DMG_TAKEN_MULT * 100f) + "%");
        tooltip.addPara("Decreases the ship's fragmentation damage taken by %s", pad, good, Math.round(DMG_TAKEN_MULT * 100f) + "%");
        tooltip.addPara("Increases the ship's kinetic damage taken by %s", pad, bad, Math.round(KINETIC_DMG_TAKEN_MULT * 100f) + "%");
        tooltip.setBulletedListMode(null);
    }

    @Override
    public void addSModEffectSection(TooltipMakerAPI tooltip, ShipAPI.HullSize hullSize, ShipAPI ship, float width, boolean isForModSpec, boolean isForBuildInList) {
        float oPad = 10f;
        Color b = Misc.getHighlightColor();

        tooltip.setBulletedListMode(" - ");
        tooltip.addPara("Decreases the ship's kinetic damage taken penalty by %s", oPad, b, Math.round(KINETIC_DMG_TAKEN_MULT * 100f * 0.5f) + "%");
        tooltip.setBulletedListMode(null);
    }

    @Override
    public boolean hasSModEffect() {
        return true;
    }
}
