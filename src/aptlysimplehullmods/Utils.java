package aptlysimplehullmods;

import com.fs.starfarer.api.EveryFrameScript;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.ShipVariantAPI;
import com.fs.starfarer.api.impl.campaign.ids.HullMods;
import com.fs.starfarer.api.util.IntervalUtil;

import java.util.List;

public class Utils {
    public static boolean hasTargetingCore(ShipVariantAPI variant) {
        return variant.hasHullMod(HullMods.DEDICATED_TARGETING_CORE)
                || variant.hasHullMod(HullMods.INTEGRATED_TARGETING_UNIT)
                || variant.hasHullMod(HullMods.ADVANCED_TARGETING_CORE);
    }

    public static float computeStatMultiplier(float totalStat) {
        float computedStat = 0f;
        if (totalStat > 0f) {
            computedStat = (0.01f * (totalStat / 0.01f)) / (0.01f * (totalStat / 0.01f) + 1);
        }
        return computedStat;
    }

    public static EveryFrameScript getTransientScript(Class<?> cls) {
        if (Global.getSector().hasTransientScript(cls)) {
            for (EveryFrameScript s : Global.getSector().getTransientScripts()) {
                if (cls.isInstance(s)){
                    return s;
                }
            }
        }
        return null;
    }

    public static class TargetingTransceiverData {
        public IntervalUtil interval = new IntervalUtil(1f, 1f);
        public float mag = 0f;
    }
}
