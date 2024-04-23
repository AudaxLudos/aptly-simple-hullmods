package aptlysimplehullmods.scripts.campaign;

import com.fs.starfarer.api.EveryFrameScript;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.fleet.FleetMemberAPI;

public class TestScript implements EveryFrameScript {
    @Override
    public boolean isDone() {
        return false;
    }

    @Override
    public boolean runWhilePaused() {
        return false;
    }

    @Override
    public void advance(float amount) {
        if (Global.getSector().getPlayerFleet() == null) return;

        String key = "$ash_test_script_key";
        TestData data = (TestData) Global.getSector().getMemoryWithoutUpdate().get(key);
        if (data == null) {
            data = new TestData();
            Global.getSector().getMemoryWithoutUpdate().set(key, data);
        }

        int counter = 0;
        for (FleetMemberAPI member : Global.getSector().getPlayerFleet().getFleetData().getMembersListCopy()) {
            if (member.getVariant().hasHullMod("ASH_TestHullmod")) {
                ++counter;
            }
        }

        if (data.counter != counter) {
            data.isActive = false;
            data.counter = counter;
            // un-modify fleet stats here
        }

        if (!data.isActive) {
            data.isActive = true;
            // modify fleet stats here
        }
    }

    public static class TestData {
        boolean isActive = false;
        int counter = 0;
    }
}
