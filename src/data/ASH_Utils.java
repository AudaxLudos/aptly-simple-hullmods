package data;

import com.fs.starfarer.api.Global;

public class ASH_Utils {
    public static float getValueWithinMax(float value, float min, float max) {
        return Math.min(Math.max(value, min), max);
    }

    public static float getValueWithinRange(float percentage, float min, float max) {
        return percentage * (max - min) + min;
    }

    public static boolean isModEnabled() {
        return Global.getSettings().getModManager().isModEnabled("better_deserving_smods") || Global.getSettings().getBoolean("ASH_SmodBonusesWithoutBDSM");
    }
}
