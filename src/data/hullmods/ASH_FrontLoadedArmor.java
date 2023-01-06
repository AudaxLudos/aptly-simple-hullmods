package data.hullmods;

import java.awt.Color;

import org.lwjgl.input.Keyboard;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.ui.Alignment;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;

import data.ASH_Utils;

public class ASH_FrontLoadedArmor extends BaseHullMod {
    public static final float POSITIVE_ARMOR_VALUE_MULTIPLIER = 2f;
    public static final float NEGATIVE_ARMOR_VALUE_MULTIPLIER = 0.5f;

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
                        ship.getArmorGrid().setArmorValue(x, y, currentArmorValue * POSITIVE_ARMOR_VALUE_MULTIPLIER);
                    } else {
                        if (ship.getVariant().getSMods().contains(spec.getId()) && ASH_Utils.isModEnabled())
                            continue;
                        ship.getArmorGrid().setArmorValue(x, y, currentArmorValue * NEGATIVE_ARMOR_VALUE_MULTIPLIER);
                    }
                }
            }
        }

        Global.getCombatEngine().getCustomData().put("ASH_FrontLoadedArmor_" + ship.getId(), initArmor);
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
            tooltip.addPara("%s the armor values towards the front of the ship", pad, good, "Doubles");
            tooltip.addPara("%s the armor values towards the back of the ship", pad, bad, "Halves");
            tooltip.setBulletedListMode(null);
        }

        if (!ASH_Utils.isModEnabled())
            return;
        if (!Keyboard.isKeyDown(Keyboard.KEY_F1)) {
            tooltip.addPara("Press or Hold F1 to show S-mod effects", Misc.getGrayColor(), opad);
            return;
        }

        tooltip.addSectionHeading("S-Mod Effects:", story, Misc.setAlpha(story, 110), Alignment.MID, opad);
        tooltip.setBulletedListMode(" - ");
        tooltip.addPara("%s the armor values towards the front of the ship", opad, good, "Doubles");
        tooltip.setBulletedListMode(null);
    }
}