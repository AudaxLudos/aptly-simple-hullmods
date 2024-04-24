package aptlysimplehullmods.plugins;

import aptlysimplehullmods.hullmods.InvasionPackage;
import com.fs.starfarer.api.EveryFrameScript;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.CampaignFleetAPI;
import com.fs.starfarer.api.fleet.FleetMemberAPI;
import com.fs.starfarer.api.impl.campaign.ids.Stats;

public class InvasionPackageScript implements EveryFrameScript {
    public static final String INVASION_PACKAGE_ID = "ash_invasion_package";
    public boolean isActive = false;
    public int shipsWithHullmod = 0;

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
        if (Global.getSector().getPlayerFleet() == null)
            return;
        if (Global.getSector().getPlayerFleet().getFleetData() == null)
            return;

        CampaignFleetAPI playerFleet = Global.getSector().getPlayerFleet();

        int shipsWithInvasionPackage = 0;
        for (FleetMemberAPI member : playerFleet.getFleetData().getMembersListCopy()) {
            if (member.getVariant().hasHullMod(INVASION_PACKAGE_ID)) {
                ++shipsWithInvasionPackage;
            }
        }

        if (shipsWithHullmod != shipsWithInvasionPackage) {
            // un-modify fleet stats here
            isActive = false;
            shipsWithHullmod = shipsWithInvasionPackage;
            playerFleet.getStats().getDynamic().getMod(Stats.PLANETARY_OPERATIONS_MOD).unmodify(INVASION_PACKAGE_ID);
            playerFleet.getStats().getDynamic().getStat(Stats.PLANETARY_OPERATIONS_CASUALTIES_MULT).unmodify(INVASION_PACKAGE_ID);
        }

        if (!isActive) {
            // modify fleet stats here
            isActive = true;
            playerFleet.getStats().getDynamic().getMod(Stats.PLANETARY_OPERATIONS_MOD).modifyMult(INVASION_PACKAGE_ID,
                    1f + shipsWithHullmod * InvasionPackage.PLANETARY_OPERATIONS_MULT,
                    shipsWithHullmod + " ships with Invasion Package");
            playerFleet.getStats().getDynamic().getStat(Stats.PLANETARY_OPERATIONS_CASUALTIES_MULT).modifyMult(INVASION_PACKAGE_ID,
                    1f - computeStatMultiplier(shipsWithHullmod, InvasionPackage.PLANETARY_OPERATIONS_MULT),
                    shipsWithHullmod + " ships with Invasion Package");
        }
    }
}
