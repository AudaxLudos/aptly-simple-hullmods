package aptlysimplehullmods.plugins;

import aptlysimplehullmods.Ids;
import aptlysimplehullmods.Utils;
import aptlysimplehullmods.hullmods.InvasionPackage;
import com.fs.starfarer.api.EveryFrameScript;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.CampaignFleetAPI;
import com.fs.starfarer.api.fleet.FleetMemberAPI;
import com.fs.starfarer.api.impl.campaign.ids.Stats;

public class InvasionPackageScript implements EveryFrameScript {
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
        CampaignFleetAPI playerFleet = Global.getSector().getPlayerFleet();
        Float bonusStat = (Float) playerFleet.getFleetData().getCacheClearedOnSync().get(Ids.INVASION_PACKAGE);
        if (bonusStat != null) {
            return;
        }

        int shipsWithMod = 0;
        bonusStat = 0f;
        for (FleetMemberAPI member : playerFleet.getFleetData().getMembersListCopy()) {
            if (member.isMothballed() || !member.getVariant().hasHullMod(Ids.INVASION_PACKAGE)) {
                continue;
            }
            ++shipsWithMod;
            bonusStat += InvasionPackage.PLANETARY_OPERATION_CASUALTIES_MULT.get(member.getVariant().getHullSize());
        }

        if (bonusStat > 0f) {
            playerFleet.getStats().getDynamic().getMod(Stats.PLANETARY_OPERATIONS_MOD).modifyMult(Ids.INVASION_PACKAGE,
                    1f + Utils.computeStatMultiplier(bonusStat),
                    shipsWithMod + " ships with Invasion Package");
            playerFleet.getStats().getDynamic().getStat(Stats.PLANETARY_OPERATIONS_CASUALTIES_MULT).modifyMult(Ids.INVASION_PACKAGE,
                    1f - Utils.computeStatMultiplier(bonusStat),
                    shipsWithMod + " ships with Invasion Package");
        } else {
            playerFleet.getStats().getDynamic().getMod(Stats.PLANETARY_OPERATIONS_MOD).unmodify(Ids.INVASION_PACKAGE);
            playerFleet.getStats().getDynamic().getStat(Stats.PLANETARY_OPERATIONS_CASUALTIES_MULT).unmodify(Ids.INVASION_PACKAGE);
        }

        playerFleet.getFleetData().getCacheClearedOnSync().put(Ids.INVASION_PACKAGE, bonusStat);
    }
}
