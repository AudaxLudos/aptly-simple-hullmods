package aptlysimplehullmods.hullmods.hidden;

import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;

import java.awt.*;

public class CompactShields extends BaseHullMod {
    public static final float SHIELD_DMG_TAKEN_MULT = 0.10f;
    public static final float SHIELD_ARC_MOD = 40f;

    @Override
    public void applyEffectsBeforeShipCreation(ShipAPI.HullSize hullSize, MutableShipStatsAPI stats, String id) {
        stats.getShieldDamageTakenMult().modifyMult(id, 1f - SHIELD_DMG_TAKEN_MULT);
        stats.getShieldArcBonus().modifyFlat(id, -SHIELD_ARC_MOD);
    }

    @Override
    public void addPostDescriptionSection(TooltipMakerAPI tooltip, ShipAPI.HullSize hullSize, ShipAPI ship, float width, boolean isForModSpec) {
        float oPad = 10f;
        float pad = 3f;
        Color good = Misc.getPositiveHighlightColor();
        Color bad = Misc.getNegativeHighlightColor();

        tooltip.setBulletedListMode(" - ");
        tooltip.addPara("Increases the ship's shield strength by %s", oPad, good, Math.round(SHIELD_DMG_TAKEN_MULT * 100f) + "%");
        tooltip.addPara("Decreases the ship's shield arc by %s", pad, bad, Math.round(SHIELD_ARC_MOD) + " degrees");
        tooltip.setBulletedListMode(null);
    }
}
