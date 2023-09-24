package data.hullmods;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

import org.lwjgl.input.Keyboard;

import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.impl.campaign.ids.Stats;
import com.fs.starfarer.api.ui.Alignment;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;

import data.ASH_Utils;

public class ASH_ReactorBay extends BaseHullMod {
    public static final float SHIP_STATS_MULT = 0.1f;
    public static final float FIGHTER_BAY_MOD = 1f;
    public static Map<HullSize, Float> DEPLOYMENT_POINTS_MOD = new HashMap<HullSize, Float>();
    static {
        DEPLOYMENT_POINTS_MOD.put(HullSize.FRIGATE, 1f);
        DEPLOYMENT_POINTS_MOD.put(HullSize.DESTROYER, 1f);
        DEPLOYMENT_POINTS_MOD.put(HullSize.CRUISER, 2f);
        DEPLOYMENT_POINTS_MOD.put(HullSize.CAPITAL_SHIP, 2f);
    }

    @Override
    public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {
        stats.getFluxDissipation().modifyMult(id, 1f + SHIP_STATS_MULT);
        stats.getFluxCapacity().modifyMult(id, 1f + SHIP_STATS_MULT);
        stats.getNumFighterBays().modifyFlat(id, -FIGHTER_BAY_MOD);

        if (stats.getVariant().getSMods().contains(id) && ASH_Utils.isModEnabled())
            if (stats.getFleetMember().getDeploymentPointsCost() > 1f)
                stats.getDynamic().getMod(Stats.DEPLOYMENT_POINTS_MOD).modifyFlat(id, -(Float) DEPLOYMENT_POINTS_MOD.get(hullSize));
    }

    @Override
    public void addPostDescriptionSection(TooltipMakerAPI tooltip, HullSize hullSize, ShipAPI ship, float width, boolean isForModSpec) {
        float pad = 3f;
        float opad = 10f;
        Color good = Misc.getPositiveHighlightColor();
        Color bad = Misc.getNegativeHighlightColor();
        Color story = Misc.getStoryOptionColor();

        if (ship == null || !ship.getVariant().getSMods().contains(spec.getId()) || !ASH_Utils.isModEnabled()) {
            tooltip.addSectionHeading("Effects:", Alignment.MID, opad);
            tooltip.setBulletedListMode(" - ");
            tooltip.addPara("Increases flux dissipation by %s", opad, good, Math.round(SHIP_STATS_MULT * 100f) + "%");
            tooltip.addPara("Increases flux capacity by %s", pad, good, Math.round(SHIP_STATS_MULT * 100f) + "%");
            tooltip.addPara("Removes %s built-in fighter bay", pad, bad, Math.round(FIGHTER_BAY_MOD) + "");
            tooltip.setBulletedListMode(null);

            if (!ASH_Utils.isModEnabled())
                return;
            if (!Keyboard.isKeyDown(Keyboard.KEY_F1)) {
                tooltip.addPara("Hold F1 to show S-mod effects", Misc.getGrayColor(), opad);
                return;
            }
        }

        tooltip.addSectionHeading("S-Mod Effects:", story, Misc.setAlpha(story, 110), Alignment.MID, opad);
        tooltip.setBulletedListMode(" - ");
        tooltip.addPara("Increases flux dissipation by %s", opad, good, Math.round(SHIP_STATS_MULT * 100f) + "%");
        tooltip.addPara("Increases flux capacity by %s", pad, good, Math.round(SHIP_STATS_MULT * 100f) + "%");
        tooltip.addPara("Removes %s built-in fighter bay", pad, bad, Math.round(FIGHTER_BAY_MOD) + "");
        tooltip.addPara("Decreases deployment points by %s", pad, good, Math.round(((Float) DEPLOYMENT_POINTS_MOD.get(HullSize.FRIGATE)).intValue()) + "/"
                + Math.round(((Float) DEPLOYMENT_POINTS_MOD.get(HullSize.DESTROYER)).intValue()) + "/"
                + Math.round(((Float) DEPLOYMENT_POINTS_MOD.get(HullSize.CRUISER)).intValue()) + "/"
                + Math.round(((Float) DEPLOYMENT_POINTS_MOD.get(HullSize.CAPITAL_SHIP)).intValue()));
        tooltip.setBulletedListMode(null);
    }

    public boolean isApplicableToShip(ShipAPI ship) {
        int builtIn = ship.getHullSpec().getBuiltInWings().size();
        int bays = (int) Math.round(ship.getMutableStats().getNumFighterBays().getBaseValue());
        if (builtIn <= 0 || bays > builtIn)
            return false;
        return true;
    }

    public String getUnapplicableReason(ShipAPI ship) {
        return "Requires built-in fighter wings only";
    }
}