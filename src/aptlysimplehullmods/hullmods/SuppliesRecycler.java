package aptlysimplehullmods.hullmods;

import aptlysimplehullmods.Ids;
import aptlysimplehullmods.Utils;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.FleetDataAPI;
import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.impl.campaign.skills.BaseSkillEffectDescription;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

import static com.fs.starfarer.api.impl.campaign.skills.BaseSkillEffectDescription.isInCampaign;

public class SuppliesRecycler extends BaseHullMod {
    public static Map<Object, Float> FLEET_SUPPLIES_PER_MONTH = new HashMap<>();
    public static float CARGO_CAPACITY_MULT = 0.40f;

    static {
        FLEET_SUPPLIES_PER_MONTH.put(ShipAPI.HullSize.FRIGATE, 0.01f);
        FLEET_SUPPLIES_PER_MONTH.put(ShipAPI.HullSize.DESTROYER, 0.02f);
        FLEET_SUPPLIES_PER_MONTH.put(ShipAPI.HullSize.CRUISER, 0.03f);
        FLEET_SUPPLIES_PER_MONTH.put(ShipAPI.HullSize.CAPITAL_SHIP, 0.05f);
    }

    @Override
    public void applyEffectsBeforeShipCreation(ShipAPI.HullSize hullSize, MutableShipStatsAPI stats, String id) {
        stats.getCargoMod().modifyMult(id, CARGO_CAPACITY_MULT);
    }

    // other effects are located in InvasionPackageScript file

    @Override
    public void addPostDescriptionSection(TooltipMakerAPI tooltip, ShipAPI.HullSize hullSize, ShipAPI ship, float width, boolean isForModSpec) {
        float pad = 3f;
        float oPad = 10f;
        Color b = Misc.getHighlightColor();
        Color good = Misc.getPositiveHighlightColor();
        Color bad = Misc.getNegativeHighlightColor();

        tooltip.addPara("Decreases the fleet's supply use per month by %s/%s/%s/%s based on hull size.", oPad, good,
                Math.round(FLEET_SUPPLIES_PER_MONTH.get(ShipAPI.HullSize.FRIGATE) * 100f) + "%",
                Math.round(FLEET_SUPPLIES_PER_MONTH.get(ShipAPI.HullSize.DESTROYER) * 100f) + "%",
                Math.round(FLEET_SUPPLIES_PER_MONTH.get(ShipAPI.HullSize.CRUISER) * 100f) + "%",
                Math.round(FLEET_SUPPLIES_PER_MONTH.get(ShipAPI.HullSize.CAPITAL_SHIP) * 100f) + "%");
        tooltip.addPara("Decreases the ship's cargo capacity by %s.", pad, bad, Math.round(CARGO_CAPACITY_MULT * 100f) + "%");

        tooltip.addPara("The buff have %s.", oPad, b, "diminishing returns");
        if (!isForModSpec && !Global.CODEX_TOOLTIP_MODE) {
            float currentStat = getStatFromSkill(ship.getMutableStats());
            float bonusForShip = FLEET_SUPPLIES_PER_MONTH.get(hullSize);
            tooltip.addPara("The Total bonus for the buff is %s.", pad, good, Math.round(currentStat * 100f) + "%");
            if (!ship.getVariant().hasHullMod(this.spec.getId())) {
                tooltip.addPara("Adding this hullmod increases the buff to %s.", pad, good, Math.round(adjustStatForDesc(currentStat, bonusForShip) * 100f) + "%");
            } else {
                tooltip.addPara("Removing this hullmod decreases the buff to %s.", pad, bad, Math.round(adjustStatForDesc(currentStat, -bonusForShip) * 100f) + "%");
            }
        }
    }

    public float getStatFromSkill(MutableShipStatsAPI stats) {
        FleetDataAPI data = BaseSkillEffectDescription.getFleetData(stats);

        if (data != null) {
            String key = Ids.SUPPLIES_RECYCLER + "_key";
            Float useMultDiminished = (Float) data.getCacheClearedOnSync().get(key);
            if (useMultDiminished != null) {
                return 1f - useMultDiminished;
            }
        }

        return 0f;
    }

    public float adjustStatForDesc(float currentStat, float statOffset) {
        float unmodifiedMult = currentStat / (1 - currentStat);
        unmodifiedMult += statOffset;

        return Utils.computeStatMultiplier(unmodifiedMult);
    }
}
