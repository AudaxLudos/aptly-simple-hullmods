package aptlysimplehullmods.plugins;

import aptlysimplehullmods.hullmods.InvasionPackage;
import com.fs.starfarer.api.EveryFrameScript;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.fleet.FleetMemberAPI;
import com.fs.starfarer.api.impl.campaign.ids.Stats;

public class InvasionPackageScript implements EveryFrameScript {
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

        String key = "ash_invasion_package_key";
        InvasionPackageData data = (InvasionPackageData) Global.getSector().getMemoryWithoutUpdate().get("$" + key);
        if (data == null) {
            data = new InvasionPackageData();
            Global.getSector().getMemoryWithoutUpdate().set("$" + key, data);
        }

        int shipsWithInvasionPackage = 0;
        for (FleetMemberAPI member : Global.getSector().getPlayerFleet().getFleetData().getMembersListCopy()) {
            if (member.getVariant().hasHullMod("ash_invasion_package")) {
                ++shipsWithInvasionPackage;
            }
        }

        if (data.shipsWithInvasionPackage != shipsWithInvasionPackage) {
            data.isActive = false;
            data.shipsWithInvasionPackage = shipsWithInvasionPackage;
            // un-modify fleet stats here
            Global.getSector().getPlayerFleet().getStats().getDynamic().getMod(Stats.PLANETARY_OPERATIONS_MOD).unmodify(key);
            Global.getSector().getPlayerFleet().getStats().getDynamic().getStat(Stats.PLANETARY_OPERATIONS_CASUALTIES_MULT).unmodify(key);
        }

        if (!data.isActive) {
            data.isActive = true;
            // modify fleet stats here
            Global.getSector().getPlayerFleet().getStats().getDynamic().getMod(Stats.PLANETARY_OPERATIONS_MOD).modifyMult(key, 1f + data.shipsWithInvasionPackage * InvasionPackage.PLANETARY_OPERATIONS_MULT);
            Global.getSector().getPlayerFleet().getStats().getDynamic().getStat(Stats.PLANETARY_OPERATIONS_CASUALTIES_MULT).modifyMult(key, 1f + data.shipsWithInvasionPackage * InvasionPackage.PLANETARY_OPERATION_CASUALTIES_MULT);
        }
    }

    public static class InvasionPackageData {
        boolean isActive = false;
        int shipsWithInvasionPackage = 0;
    }
}
