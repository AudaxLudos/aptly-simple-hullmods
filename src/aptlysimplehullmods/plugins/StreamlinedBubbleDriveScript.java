package aptlysimplehullmods.plugins;

import aptlysimplehullmods.Ids;
import aptlysimplehullmods.Utils;
import aptlysimplehullmods.hullmods.InvasionPackage;
import aptlysimplehullmods.hullmods.MarineTrainingFacility;
import aptlysimplehullmods.hullmods.StreamlinedBubbleDrive;
import com.fs.starfarer.api.EveryFrameScript;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.CampaignFleetAPI;
import com.fs.starfarer.api.fleet.FleetMemberAPI;
import com.fs.starfarer.api.impl.campaign.ids.Stats;

public class StreamlinedBubbleDriveScript implements EveryFrameScript {
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
        Float bonusStat = (Float) playerFleet.getFleetData().getCacheClearedOnSync().get(Ids.STREAMLINED_BUBBLE_DRIVE);
        if (bonusStat != null) {
            return;
        }

        bonusStat = 0f;
        for (FleetMemberAPI member : playerFleet.getFleetData().getMembersListCopy()) {
            if (member.isMothballed() || !member.getVariant().hasHullMod(Ids.STREAMLINED_BUBBLE_DRIVE)) {
                continue;
            }
            bonusStat += StreamlinedBubbleDrive.TERRAIN_PENALTY_MOD.get(member.getVariant().getHullSize());
        }

        if (bonusStat > 0f) {
            playerFleet.getCommanderStats().getDynamic().getStat(Stats.NAVIGATION_PENALTY_MULT).modifyFlat(Ids.STREAMLINED_BUBBLE_DRIVE, -bonusStat);
        } else {
            playerFleet.getCommanderStats().getDynamic().getStat(Stats.NAVIGATION_PENALTY_MULT).unmodify(Ids.STREAMLINED_BUBBLE_DRIVE);
        }

        playerFleet.getFleetData().getCacheClearedOnSync().put(Ids.STREAMLINED_BUBBLE_DRIVE, bonusStat);
    }
}
