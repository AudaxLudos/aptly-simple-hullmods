package data.hullmods;

import java.awt.Color;

import org.lwjgl.input.Keyboard;

import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.ui.Alignment;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;

public class ASH_AdvanceRadarSystems extends BaseHullMod {
    public static final float SHIP_STATS_MULTIPLIER = 0.20f;

    @Override
    public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {
        stats.getSightRadiusMod().modifyMult(id, 1f + SHIP_STATS_MULTIPLIER);

        if (stats.getVariant().getSMods().contains(id))
            stats.getAutofireAimAccuracy().modifyMult(id, 1f + SHIP_STATS_MULTIPLIER);
    }

    @Override
    public void addPostDescriptionSection(TooltipMakerAPI tooltip, HullSize hullSize, ShipAPI ship, float width, boolean isForModSpec) {
        float pad = 3f;
        float opad = 10f;
        Color good = Misc.getPositiveHighlightColor();
        Color story = Misc.getStoryOptionColor();

        if (!ship.getVariant().getSMods().contains(spec.getId())) {
            tooltip.addSectionHeading("Effects:", Alignment.MID, opad);
            tooltip.setBulletedListMode(" - ");
            tooltip.addPara("Increases the ship's sight radius by %s", opad, good, Math.round(SHIP_STATS_MULTIPLIER * 100f) + "%");
            tooltip.setBulletedListMode(null);

            if (!Keyboard.isKeyDown(Keyboard.getKeyIndex("F1"))) {
                tooltip.addPara("Press F1 to show S-mod effects", Misc.getGrayColor(), opad);
                return;
            }
        }

        tooltip.addSectionHeading("S-Mod Effects:", story, Misc.setAlpha(story, 110), Alignment.MID, opad);
        tooltip.setBulletedListMode(" - ");
        tooltip.addPara("Increases the ship's sight radius by %s", opad, good, Math.round(SHIP_STATS_MULTIPLIER * 100f) + "%");
        tooltip.addPara("Increases the ship's autofire aim accuracy by %s", pad, good, Math.round(SHIP_STATS_MULTIPLIER * 100f) + "%");
        tooltip.setBulletedListMode(null);
    }
}