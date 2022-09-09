package data.hullmods;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;

public class ASH_TestHullmod extends BaseHullMod {
    public static final float POSITIVE_ARMOR_VALUE_MODIFIER = 2f;
    public static final float NEGATIVE_ARMOR_VALUE_MODIFIER = 0.5f;

    @Override
    public void advanceInCombat(ShipAPI ship, float amount) {
        if (!ship.isAlive())
            return;

        float[][] armorGrid = ship.getArmorGrid().getGrid();
        float maxArmor = ship.getArmorGrid().getMaxArmorInCell();
        int armorCellX = armorGrid.length;
        int armorCellY = armorGrid[0].length;
        boolean initArmor = false;

        if (Global.getCombatEngine().getCustomData().get("ASH_TestHullmod_" + ship.getId()) instanceof Boolean)
            initArmor = (boolean) Global.getCombatEngine().getCustomData().get("ASH_TestHullmod_" + ship.getId());

        if (!initArmor) {
            initArmor = true;
            for (int x = 0; x < armorCellX; x++) {
                for (int y = 0; y < armorCellY; y++) {
                    if (y >= armorCellY / 2f)
                        ship.getArmorGrid().setArmorValue(x, y, maxArmor * POSITIVE_ARMOR_VALUE_MODIFIER);
                    else
                        ship.getArmorGrid().setArmorValue(x, y, maxArmor * NEGATIVE_ARMOR_VALUE_MODIFIER);
                }
            }
        }

        Global.getCombatEngine().getCustomData().put("ASH_TestHullmod_" + ship.getId(), initArmor);
    }

    @Override
    public String getDescriptionParam(int index, HullSize hullSize) {
        if (index == 0)
            return POSITIVE_ARMOR_VALUE_MODIFIER + "%";
        return null;
    }
}