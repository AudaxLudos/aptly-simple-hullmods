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

public class ReactiveSubsystems extends BaseHullMod {
    public static final float MAX_CR_MOD = 0.15f;
    public static Map<HullSize, Float> DEPLOYMENT_POINTS_MOD = new HashMap<>();

    static {
        DEPLOYMENT_POINTS_MOD.put(HullSize.FRIGATE, 1f);
        DEPLOYMENT_POINTS_MOD.put(HullSize.DESTROYER, 2f);
        DEPLOYMENT_POINTS_MOD.put(HullSize.CRUISER, 3f);
        DEPLOYMENT_POINTS_MOD.put(HullSize.CAPITAL_SHIP, 5f);
    }

    @Override
    public void applyEffectsBeforeShipCreation(ShipAPI.HullSize hullSize, MutableShipStatsAPI stats, String id) {
        stats.getMaxCombatReadiness().modifyFlat(id, MAX_CR_MOD, "Reactive Subsystems Hullmod");
        stats.getDynamic().getMod(Stats.DEPLOYMENT_POINTS_MOD).modifyFlat(id, DEPLOYMENT_POINTS_MOD.get(hullSize));

        if (isSMod(stats))
            stats.getDynamic().getMod(Stats.DEPLOYMENT_POINTS_MOD).unmodify(id);
    }

    @Override
    public void addPostDescriptionSection(TooltipMakerAPI tooltip, HullSize hullSize, ShipAPI ship, float width, boolean isForModSpec) {
        float pad = 3f;
        float oPad = 10f;
        Color good = Misc.getPositiveHighlightColor();
        Color bad = Misc.getNegativeHighlightColor();

        tooltip.addPara("Increases the ship's max combat readiness by %s.", oPad, good, Math.round(MAX_CR_MOD * 100f) + "%");
        tooltip.addPara("Increases the ship's deployment points by %s/%s/%s/%s based on its hull size.", pad, bad,
                DEPLOYMENT_POINTS_MOD.get(HullSize.FRIGATE).intValue() + "",
                DEPLOYMENT_POINTS_MOD.get(HullSize.DESTROYER).intValue() + "",
                DEPLOYMENT_POINTS_MOD.get(HullSize.CRUISER).intValue() + "",
                DEPLOYMENT_POINTS_MOD.get(HullSize.CAPITAL_SHIP).intValue() + "");
    }

    @Override
    public void addSModEffectSection(TooltipMakerAPI tooltip, ShipAPI.HullSize hullSize, ShipAPI ship, float width, boolean isForModSpec, boolean isForBuildInList) {
        float oPad = 10f;
        Color good = Misc.getPositiveHighlightColor();

        tooltip.addPara("Fully %s the ship's deployment points penalty.", oPad, good, "negates");
    }

    @Override
    public boolean hasSModEffect() {
        return true;
    }
}