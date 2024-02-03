package aptlysimplehullmods;

public class Utils {
    public static float getValueWithinMax(float value, float min, float max) {
        return Math.min(Math.max(value, min), max);
    }

    public static float getValueWithinRange(float percentage, float min, float max) {
        return percentage * (max - min) + min;
    }
}
