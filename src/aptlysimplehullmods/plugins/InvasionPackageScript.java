package aptlysimplehullmods.plugins;

import aptlysimplehullmods.hullmods.InvasionPackage;
import com.fs.starfarer.api.EveryFrameScript;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.CampaignFleetAPI;
import com.fs.starfarer.api.fleet.FleetMemberAPI;
import com.fs.starfarer.api.impl.campaign.ids.Stats;

public class InvasionPackageScript implements EveryFrameScript {
    public static final String InvasionPackageId = "ash_invasion_package";
    public static final String InvasionPackageKey = "$" + InvasionPackageId + "_data_key";

    public static float computeStatMultiplier(int shipCount, float stat) {
        float computedStat = stat;
        if (shipCount > 1) {
            computedStat = (stat * shipCount) / (stat * shipCount + 1f);
        } else if (shipCount == 0) {
            computedStat = 0f;
        }
        return computedStat;
    }

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

        CampaignFleetAPI playerFleet = Global.getSector().getPlayerFleet();

        InvasionPackageData data = (InvasionPackageData) Global.getSector().getMemoryWithoutUpdate().get(InvasionPackageKey);
        if (data == null) {
            data = new InvasionPackageData();
            Global.getSector().getMemoryWithoutUpdate().set(InvasionPackageKey, data);
        }

        int shipsWithInvasionPackage = 0;
        for (FleetMemberAPI member : playerFleet.getFleetData().getMembersListCopy()) {
            if (member.getVariant().hasHullMod(InvasionPackageId)) {
                ++shipsWithInvasionPackage;
            }
        }

        if (data.shipsWithInvasionPackage != shipsWithInvasionPackage) {
            data.isActive = false;
            data.shipsWithInvasionPackage = shipsWithInvasionPackage;
            // un-modify fleet stats here
            playerFleet.getStats().getDynamic().getMod(Stats.PLANETARY_OPERATIONS_MOD).unmodify(InvasionPackageId);
            playerFleet.getStats().getDynamic().getStat(Stats.PLANETARY_OPERATIONS_CASUALTIES_MULT).unmodify(InvasionPackageId);
        }

        if (!data.isActive) {
            data.isActive = true;
            // modify fleet stats here
            playerFleet.getStats().getDynamic().getMod(Stats.PLANETARY_OPERATIONS_MOD).modifyMult(InvasionPackageId,
                    1f + data.shipsWithInvasionPackage * InvasionPackage.PLANETARY_OPERATIONS_MULT,
                    data.shipsWithInvasionPackage + " ships with Invasion Package");
            playerFleet.getStats().getDynamic().getStat(Stats.PLANETARY_OPERATIONS_CASUALTIES_MULT).modifyMult(InvasionPackageId,
                    1f - computeStatMultiplier(data.shipsWithInvasionPackage, InvasionPackage.PLANETARY_OPERATIONS_MULT),
                    data.shipsWithInvasionPackage + " ships with Invasion Package");
        }
    }

    public static class InvasionPackageData {
        boolean isActive = false;
        int shipsWithInvasionPackage = 0;
    }
}
