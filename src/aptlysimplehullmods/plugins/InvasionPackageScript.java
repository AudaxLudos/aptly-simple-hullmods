package aptlysimplehullmods.plugins;

import aptlysimplehullmods.Utils;
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

    @Override
    public boolean isDone() {
        return false;
    }

    @Override
    public boolean runWhilePaused() {
        return true;
    }

    @Override
    public void advance(float amount) {
        if (Global.getSector().getPlayerFleet() == null) {
            return;
        }
        if (Global.getSector().getPlayerFleet().getFleetData() == null) {
            return;
        }

        CampaignFleetAPI playerFleet = Global.getSector().getPlayerFleet();

        int shipsWithInvasionPackage = 0;
        float totalStat = 0;
        for (FleetMemberAPI member : playerFleet.getFleetData().getMembersListCopy()) {
            if (member.isMothballed()) {
                continue;
            }
            if (member.getVariant().hasHullMod(INVASION_PACKAGE_ID)) {
                ++shipsWithInvasionPackage;
                totalStat += InvasionPackage.PLANETARY_OPERATION_CASUALTIES_MULT.get(member.getVariant().getHullSize());
            }
        }

        if (this.shipsWithHullmod != shipsWithInvasionPackage) {
            // un-modify fleet stats here
            this.isActive = false;
            this.shipsWithHullmod = shipsWithInvasionPackage;
        }

        if (!this.isActive) {
            // modify fleet stats here
            this.isActive = true;
            if (totalStat > 0f) {
                playerFleet.getStats().getDynamic().getMod(Stats.PLANETARY_OPERATIONS_MOD).modifyMult(INVASION_PACKAGE_ID,
                        1f + Utils.computeStatMultiplier(totalStat),
                        this.shipsWithHullmod + " ships with Invasion Package");
                playerFleet.getStats().getDynamic().getStat(Stats.PLANETARY_OPERATIONS_CASUALTIES_MULT).modifyMult(INVASION_PACKAGE_ID,
                        1f - Utils.computeStatMultiplier(totalStat),
                        this.shipsWithHullmod + " ships with Invasion Package");
            } else {
                playerFleet.getStats().getDynamic().getMod(Stats.PLANETARY_OPERATIONS_MOD).unmodify(INVASION_PACKAGE_ID);
                playerFleet.getStats().getDynamic().getStat(Stats.PLANETARY_OPERATIONS_CASUALTIES_MULT).unmodify(INVASION_PACKAGE_ID);
            }
        }
    }
}
