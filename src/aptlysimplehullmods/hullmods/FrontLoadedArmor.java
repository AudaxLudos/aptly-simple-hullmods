package aptlysimplehullmods.hullmods;

import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;

import java.awt.*;

public class FrontLoadedArmor extends BaseHullMod {
    public static final float POSITIVE_ARMOR_VALUE_MULT = 2f;
    public static final float NEGATIVE_ARMOR_VALUE_MULT = 0.5f;

    @Override
    public void applyEffectsAfterShipCreation(ShipAPI ship, String id) {
        if (ship == null) return;

        float[][] armorGrid = ship.getArmorGrid().getGrid();
        int armorCellX = armorGrid.length;
        int armorCellY = armorGrid[0].length;

        for (int x = 0; x < armorCellX; x++) {
            for (int y = 0; y < armorCellY; y++) {
                float currentArmorValue = ship.getArmorGrid().getArmorValue(x, y);
                if (y >= armorCellY / 2f) {
                    ship.getArmorGrid().setArmorValue(x, y, currentArmorValue * POSITIVE_ARMOR_VALUE_MULT);
                } else {
                    if (isSMod(ship))
                        continue;
                    ship.getArmorGrid().setArmorValue(x, y, currentArmorValue * NEGATIVE_ARMOR_VALUE_MULT);
                }
            }
        }
    }

    @Override
    public void addPostDescriptionSection(TooltipMakerAPI tooltip, HullSize hullSize, ShipAPI ship, float width, boolean isForModSpec) {
        float pad = 3f;
        float oPad = 10f;
        Color good = Misc.getPositiveHighlightColor();
        Color bad = Misc.getNegativeHighlightColor();

        tooltip.addPara("%s the armor values towards the front of the ship.", oPad, good, "Doubles");
        tooltip.addPara("%s the armor values towards the back of the ship.", pad, bad, "Halves");
    }

    @Override
    public void addSModEffectSection(TooltipMakerAPI tooltip, ShipAPI.HullSize hullSize, ShipAPI ship, float width, boolean isForModSpec, boolean isForBuildInList) {
        float oPad = 10f;
        Color good = Misc.getPositiveHighlightColor();

        tooltip.addPara("Fully %s the armor values penalty towards the back of the ship.", oPad, good, "negates");
    }

    @Override
    public boolean hasSModEffect() {
        return true;
    }
}