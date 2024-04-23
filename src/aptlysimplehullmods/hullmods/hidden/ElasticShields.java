package aptlysimplehullmods.hullmods.hidden;

import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;

import java.awt.*;

public class ElasticShields extends BaseHullMod {
    public static final float DMG_TAKEN_MULT = 0.10f;
    public static final float EXPLOSIVE_DMG_TAKEN_MULT = 0.20f;

    @Override
    public void applyEffectsBeforeShipCreation(ShipAPI.HullSize hullSize, MutableShipStatsAPI stats, String id) {
        stats.getHighExplosiveShieldDamageTakenMult().modifyMult(id, 1f - EXPLOSIVE_DMG_TAKEN_MULT);
        stats.getKineticDamageTakenMult().modifyMult(id, 1f + DMG_TAKEN_MULT);
        stats.getEnergyShieldDamageTakenMult().modifyMult(id, 1f + DMG_TAKEN_MULT);

        if (isSMod(stats))
            stats.getEnergyShieldDamageTakenMult().unmodify(id);
    }

    @Override
    public void addPostDescriptionSection(TooltipMakerAPI tooltip, ShipAPI.HullSize hullSize, ShipAPI ship, float width, boolean isForModSpec) {
        float oPad = 10f;
        float pad = 5f;
        Color good = Misc.getPositiveHighlightColor();
        Color bad = Misc.getNegativeHighlightColor();

        tooltip.setBulletedListMode(" - ");
        tooltip.addPara("Decreases the shield's explosive damage taken by %s", oPad, good, Math.round(EXPLOSIVE_DMG_TAKEN_MULT * 100f) + "%");
        tooltip.addPara("Increases the shield's kinetic damage taken by %s", pad, bad, Math.round(DMG_TAKEN_MULT * 100f) + "%");
        tooltip.addPara("Increases the shield's energy damage taken by %s", pad, bad, Math.round(DMG_TAKEN_MULT * 100f) + "%");
        tooltip.setBulletedListMode(null);
    }

    @Override
    public void addSModEffectSection(TooltipMakerAPI tooltip, ShipAPI.HullSize hullSize, ShipAPI ship, float width, boolean isForModSpec, boolean isForBuildInList) {
        float oPad = 10f;
        Color b = Misc.getHighlightColor();

        tooltip.setBulletedListMode(" - ");
        tooltip.addPara("Fully negates the shield's energy damage taken penalty", oPad, b, Math.round(EXPLOSIVE_DMG_TAKEN_MULT * 100f * 0.5f) + "%");
        tooltip.setBulletedListMode(null);
    }

    @Override
    public boolean hasSModEffect() {
        return true;
    }
}
