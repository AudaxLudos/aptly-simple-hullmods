package aptlysimplehullmods.plugins;

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
    public static final String STREAMLINED_BUBBLE_DRIVE_ID = "ash_streamlined_bubble_drive";

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
        Float bonusStat = (Float) playerFleet.getFleetData().getCacheClearedOnSync().get(STREAMLINED_BUBBLE_DRIVE_ID);
        if (bonusStat != null) {
            return;
        }

        bonusStat = 0f;
        for (FleetMemberAPI member : playerFleet.getFleetData().getMembersListCopy()) {
            if (member.isMothballed() || !member.getVariant().hasHullMod(STREAMLINED_BUBBLE_DRIVE_ID)) {
                continue;
            }
            bonusStat += StreamlinedBubbleDrive.TERRAIN_PENALTY_MOD.get(member.getVariant().getHullSize());
        }

        if (bonusStat > 0f) {
            Global.getSector().getPlayerStats().getDynamic().getStat(Stats.NAVIGATION_PENALTY_MULT).modifyFlat(STREAMLINED_BUBBLE_DRIVE_ID, -bonusStat);
        } else {
            Global.getSector().getPlayerStats().getDynamic().getStat(Stats.NAVIGATION_PENALTY_MULT).unmodify(STREAMLINED_BUBBLE_DRIVE_ID);
        }

        playerFleet.getFleetData().getCacheClearedOnSync().put(STREAMLINED_BUBBLE_DRIVE_ID, bonusStat);
    }
}
