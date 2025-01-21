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
        Float bonusStat = (Float) playerFleet.getFleetData().getCacheClearedOnSync().get(INVASION_PACKAGE_ID);
        if (bonusStat != null) {
            return;
        }

        int shipsWithMod = 0;
        bonusStat = 0f;
        for (FleetMemberAPI member : playerFleet.getFleetData().getMembersListCopy()) {
            if (member.isMothballed()) {
                continue;
            }
            if (member.getVariant().hasHullMod(INVASION_PACKAGE_ID)) {
                ++shipsWithMod;
                bonusStat += InvasionPackage.PLANETARY_OPERATION_CASUALTIES_MULT.get(member.getVariant().getHullSize());
            }
        }

        if (bonusStat > 0f) {
            playerFleet.getStats().getDynamic().getMod(Stats.PLANETARY_OPERATIONS_MOD).modifyMult(INVASION_PACKAGE_ID,
                    1f + Utils.computeStatMultiplier(bonusStat),
                    shipsWithMod + " ships with Invasion Package");
            playerFleet.getStats().getDynamic().getStat(Stats.PLANETARY_OPERATIONS_CASUALTIES_MULT).modifyMult(INVASION_PACKAGE_ID,
                    1f - Utils.computeStatMultiplier(bonusStat),
                    shipsWithMod + " ships with Invasion Package");
        } else {
            playerFleet.getStats().getDynamic().getMod(Stats.PLANETARY_OPERATIONS_MOD).unmodify(INVASION_PACKAGE_ID);
            playerFleet.getStats().getDynamic().getStat(Stats.PLANETARY_OPERATIONS_CASUALTIES_MULT).unmodify(INVASION_PACKAGE_ID);
        }

        playerFleet.getFleetData().getCacheClearedOnSync().put(INVASION_PACKAGE_ID, bonusStat);
    }
}
