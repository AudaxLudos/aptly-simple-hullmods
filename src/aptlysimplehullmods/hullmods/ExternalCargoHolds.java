package aptlysimplehullmods.hullmods;

import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.impl.campaign.ids.HullMods;
import com.fs.starfarer.api.impl.hullmods.BaseLogisticsHullMod;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class ExternalCargoHolds extends BaseLogisticsHullMod {
    public static final float SHIP_STATS_MULTIPLIER = 0.10f;
    private static final Map<HullSize, Float> CARGO_MODIFIER = new HashMap<>();

    static {
        CARGO_MODIFIER.put(HullSize.FRIGATE, 60f);
        CARGO_MODIFIER.put(HullSize.DESTROYER, 120f);
        CARGO_MODIFIER.put(HullSize.CRUISER, 180f);
        CARGO_MODIFIER.put(HullSize.CAPITAL_SHIP, 300f);
    }

    @Override
    public void applyEffectsBeforeShipCreation(ShipAPI.HullSize hullSize, MutableShipStatsAPI stats, String id) {
        stats.getCargoMod().modifyFlat(id, CARGO_MODIFIER.get(hullSize));
        stats.getFluxDissipation().modifyMult(id, 1f - SHIP_STATS_MULTIPLIER);
        stats.getArmorDamageTakenMult().modifyMult(id, 1f + SHIP_STATS_MULTIPLIER);
        stats.getSensorProfile().modifyMult(id, 1f + SHIP_STATS_MULTIPLIER);
    }

    @Override
    public void addPostDescriptionSection(TooltipMakerAPI tooltip, HullSize hullSize, ShipAPI ship, float width, boolean isForModSpec) {
        float pad = 3f;
        float oPad = 10f;
        Color good = Misc.getPositiveHighlightColor();
        Color bad = Misc.getNegativeHighlightColor();

        tooltip.setBulletedListMode(" - ");
        tooltip.addPara("Increases the ship's cargo capacity by %s based on hull size", oPad, good, CARGO_MODIFIER.get(HullSize.FRIGATE).intValue() + "/"
                + CARGO_MODIFIER.get(HullSize.DESTROYER).intValue() + "/"
                + CARGO_MODIFIER.get(HullSize.CRUISER).intValue() + "/"
                + CARGO_MODIFIER.get(HullSize.CAPITAL_SHIP).intValue() + " points");
        tooltip.addPara("Decreases the ship's flux dissipation by %s", pad, bad, Math.round(SHIP_STATS_MULTIPLIER * 100f) + "%");
        tooltip.addPara("Increases the ship's sensor profile by %s", pad, bad, Math.round(SHIP_STATS_MULTIPLIER * 100f) + "%");
        tooltip.addPara("Decreases the ship's armor strength by %s", pad, bad, Math.round(SHIP_STATS_MULTIPLIER * 100f) + "%");
        tooltip.setBulletedListMode(null);
    }

    @Override
    public String getUnapplicableReason(ShipAPI ship) {
        int numLogisticsMods = getNumLogisticsMods(ship);
        if (this.spec != null && ship.getVariant().hasHullMod(this.spec.getId()))
            numLogisticsMods--;
        if (ship == null || (ship.getVariant().hasHullMod(HullMods.CIVGRADE) && !ship.getVariant().hasHullMod(HullMods.MILITARIZED_SUBSYSTEMS)))
            return "Cannot be installed on a civilian ship without Militarized Subsystems";
        if (numLogisticsMods >= getMax(ship))
            return "Maximum of 2 non-built-in \"Logistics\" hullmods per hull";
        return null;
    }

    @Override
    public boolean isApplicableToShip(ShipAPI ship) {
        int numLogisticsMods = getNumLogisticsMods(ship);
        if (this.spec != null && ship.getVariant().hasHullMod(this.spec.getId()))
            numLogisticsMods--;
        return ship != null && (!ship.getVariant().hasHullMod(HullMods.CIVGRADE) || ship.getVariant().hasHullMod(HullMods.MILITARIZED_SUBSYSTEMS)) && numLogisticsMods < getMax(ship);
    }
}