package aptlysimplehullmods.hullmods;

import com.fs.starfarer.api.Global;
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
    public void advanceInCombat(ShipAPI ship, float amount) {
        if (!ship.isAlive())
            return;

        float[][] armorGrid = ship.getArmorGrid().getGrid();
        int armorCellX = armorGrid.length;
        int armorCellY = armorGrid[0].length;
        boolean initArmor = false;

        if (Global.getCombatEngine().getCustomData().get("ASH_FrontLoadedArmor_" + ship.getId()) instanceof Boolean)
            initArmor = (boolean) Global.getCombatEngine().getCustomData().get("ASH_FrontLoadedArmor_" + ship.getId());

        if (!initArmor) {
            initArmor = true;
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

        Global.getCombatEngine().getCustomData().put("ASH_FrontLoadedArmor_" + ship.getId(), initArmor);
    }

    @Override
    public void addPostDescriptionSection(TooltipMakerAPI tooltip, HullSize hullSize, ShipAPI ship, float width, boolean isForModSpec) {
        float pad = 3f;
        float oPad = 10f;
        Color good = Misc.getPositiveHighlightColor();
        Color bad = Misc.getNegativeHighlightColor();

        tooltip.setBulletedListMode(" - ");
        tooltip.addPara("%s the armor values towards the front of the ship", oPad, good, "Doubles");
        tooltip.addPara("%s the armor values towards the back of the ship", pad, bad, "Halves");
        tooltip.setBulletedListMode(null);
    }

    @Override
    public void addSModEffectSection(TooltipMakerAPI tooltip, ShipAPI.HullSize hullSize, ShipAPI ship, float width, boolean isForModSpec, boolean isForBuildInList) {
        float oPad = 10f;

        tooltip.setBulletedListMode(" - ");
        tooltip.addPara("Fully negates the armor values penalty towards the back of the ship", oPad);
        tooltip.setBulletedListMode(null);
    }

    @Override
    public boolean hasSModEffect() {
        return true;
    }
}