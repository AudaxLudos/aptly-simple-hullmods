package aptlysimplehullmods.hullmods;

import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class StreamlinedBubbleDrive extends BaseHullMod {
    public static boolean ENABLED = true;
    public static Map<ShipAPI.HullSize, Float> TERRAIN_PENALTY_MOD = new HashMap<>();

    static {
        TERRAIN_PENALTY_MOD.put(ShipAPI.HullSize.FRIGATE, 0.04f);
        TERRAIN_PENALTY_MOD.put(ShipAPI.HullSize.DESTROYER, 0.08f);
        TERRAIN_PENALTY_MOD.put(ShipAPI.HullSize.CRUISER, 0.12f);
        TERRAIN_PENALTY_MOD.put(ShipAPI.HullSize.CAPITAL_SHIP, 0.20f);
    }

    @Override
    public void addPostDescriptionSection(TooltipMakerAPI tooltip, ShipAPI.HullSize hullSize, ShipAPI ship, float width, boolean isForModSpec) {
        float oPad = 10f;
        Color good = Misc.getPositiveHighlightColor();

        tooltip.addPara("Decreases the terrain movement penalty by %s/%s/%s/%s based on hull size.", oPad, good,
                Math.round(TERRAIN_PENALTY_MOD.get(ShipAPI.HullSize.FRIGATE) * 100f) + "%",
                Math.round(TERRAIN_PENALTY_MOD.get(ShipAPI.HullSize.DESTROYER) * 100f) + "%",
                Math.round(TERRAIN_PENALTY_MOD.get(ShipAPI.HullSize.CRUISER) * 100f) + "%",
                Math.round(TERRAIN_PENALTY_MOD.get(ShipAPI.HullSize.CAPITAL_SHIP) * 100f) + "%");
    }
}
