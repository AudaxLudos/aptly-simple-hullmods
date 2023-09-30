package data.hullmods;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.impl.campaign.ids.Stats;
import com.fs.starfarer.api.ui.Alignment;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;

public class ASH_MissileBay extends BaseHullMod {
    public static final float SHIP_STATS_MULT = 0.50f;
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
        stats.getMissileAmmoBonus().modifyPercent(id, SHIP_STATS_MULT * 100f);
        stats.getNumFighterBays().modifyFlat(id, -FIGHTER_BAY_MOD);

        if (stats.getVariant().getSMods().contains(id))
            if (stats.getFleetMember().getDeploymentPointsCost() > 1f)
                stats.getDynamic().getMod(Stats.DEPLOYMENT_POINTS_MOD).modifyFlat(id, -(Float) DEPLOYMENT_POINTS_MOD.get(hullSize));
    }

    @Override
    public void addPostDescriptionSection(TooltipMakerAPI tooltip, HullSize hullSize, ShipAPI ship, float width, boolean isForModSpec) {
        float pad = 3f;
        float opad = 10f;
        Color good = Misc.getPositiveHighlightColor();
        Color bad = Misc.getNegativeHighlightColor();

        tooltip.addSectionHeading("Effects:", Alignment.MID, opad);
        tooltip.setBulletedListMode(" - ");
        tooltip.addPara("Increases the ammo capacity of missile weapons by %s", opad, good, Math.round(SHIP_STATS_MULT * 100f) + "%");
        tooltip.addPara("Removes %s built-in fighter bay", pad, bad, Math.round(FIGHTER_BAY_MOD) + "");
        tooltip.setBulletedListMode(null);
    }

    public boolean isApplicableToShip(ShipAPI ship) {
        int bays = (int) Math.round(ship.getMutableStats().getNumFighterBays().getBaseValue());
        if (bays <= 0f)
            return false;
        return true;
    }

    public String getUnapplicableReason(ShipAPI ship) {
        return "Requires built-in fighter bays";
    }

    @Override
    public void addSModEffectSection(TooltipMakerAPI tooltip, ShipAPI.HullSize hullSize, ShipAPI ship, float width, boolean isForModSpec, boolean isForBuildInList) {
        float opad = 10f;
        Color good = Misc.getPositiveHighlightColor();

        tooltip.setBulletedListMode(" - ");
        tooltip.addPara("Decreases deployment points by %s", opad, good, Math.round(((Float) DEPLOYMENT_POINTS_MOD.get(HullSize.FRIGATE)).intValue()) + "/"
                + Math.round(((Float) DEPLOYMENT_POINTS_MOD.get(HullSize.DESTROYER)).intValue()) + "/"
                + Math.round(((Float) DEPLOYMENT_POINTS_MOD.get(HullSize.CRUISER)).intValue()) + "/"
                + Math.round(((Float) DEPLOYMENT_POINTS_MOD.get(HullSize.CAPITAL_SHIP)).intValue()));
        tooltip.setBulletedListMode(null);
    }

    @Override
    public boolean hasSModEffect() {
        return true;
    }
}