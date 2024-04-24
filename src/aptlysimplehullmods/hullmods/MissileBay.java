package aptlysimplehullmods.hullmods;

import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.impl.campaign.ids.Stats;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class MissileBay extends BaseHullMod {
    public static final float SHIP_STATS_MULT = 0.25f;
    public static final float FIGHTER_BAY_MOD = 1f;
    public static Map<HullSize, Float> DEPLOYMENT_POINTS_MOD = new HashMap<>();

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

        if (isSMod(stats))
            stats.getDynamic().getMod(Stats.DEPLOYMENT_POINTS_MOD).modifyFlat(id, -(Float) DEPLOYMENT_POINTS_MOD.get(hullSize));
    }

    @Override
    public void addPostDescriptionSection(TooltipMakerAPI tooltip, HullSize hullSize, ShipAPI ship, float width, boolean isForModSpec) {
        float pad = 3f;
        float oPad = 10f;
        Color good = Misc.getPositiveHighlightColor();
        Color bad = Misc.getNegativeHighlightColor();

        tooltip.addPara("Increases the ammo capacity of missile weapons by %s.", oPad, good, Math.round(SHIP_STATS_MULT * 100f) + "%");
        tooltip.addPara("Removes %s built-in fighter bay.", pad, bad, Math.round(FIGHTER_BAY_MOD) + "");
    }

    public boolean isApplicableToShip(ShipAPI ship) {
        int bays = Math.round(ship.getMutableStats().getNumFighterBays().getBaseValue());
        return !(bays <= 0f);
    }

    public String getUnapplicableReason(ShipAPI ship) {
        return "Requires built-in fighter bays";
    }

    @Override
    public void addSModEffectSection(TooltipMakerAPI tooltip, ShipAPI.HullSize hullSize, ShipAPI ship, float width, boolean isForModSpec, boolean isForBuildInList) {
        float oPad = 10f;
        Color good = Misc.getPositiveHighlightColor();

        tooltip.addPara("Decreases the ship's deployment points by %s based on its hull size.", oPad, good, DEPLOYMENT_POINTS_MOD.get(HullSize.FRIGATE).intValue() + "/"
                + DEPLOYMENT_POINTS_MOD.get(HullSize.DESTROYER).intValue() + "/"
                + DEPLOYMENT_POINTS_MOD.get(HullSize.CRUISER).intValue() + "/"
                + DEPLOYMENT_POINTS_MOD.get(HullSize.CAPITAL_SHIP).intValue());
    }

    @Override
    public boolean hasSModEffect() {
        return true;
    }
}