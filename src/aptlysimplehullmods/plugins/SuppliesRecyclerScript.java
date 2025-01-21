package aptlysimplehullmods.plugins;

import aptlysimplehullmods.Utils;
import aptlysimplehullmods.hullmods.SuppliesRecycler;
import com.fs.starfarer.api.EveryFrameScript;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.BuffManagerAPI;
import com.fs.starfarer.api.campaign.CampaignFleetAPI;
import com.fs.starfarer.api.campaign.FleetDataAPI;
import com.fs.starfarer.api.fleet.FleetMemberAPI;

import java.util.List;

public class SuppliesRecyclerScript implements EveryFrameScript {
    public static final String SUPPLIES_RECYCLER_ID = "ash_supplies_recycler";

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
        FleetDataAPI playerFleetData = playerFleet.getFleetData();
        List<FleetMemberAPI> playerFleetMembers = playerFleetData.getMembersListCopy();
        Float bonusStat = (Float) playerFleet.getFleetData().getCacheClearedOnSync().get(SUPPLIES_RECYCLER_ID);
        if (bonusStat != null) {
            return;
        }

        bonusStat = 0f;
        for (FleetMemberAPI member : playerFleetMembers) {
            if (member.isMothballed()) {
                continue;
            }
            if (member.getVariant().hasHullMod(SUPPLIES_RECYCLER_ID)) {
                bonusStat += SuppliesRecycler.FLEET_SUPPLIES_PER_MONTH.get(member.getVariant().getHullSize());
            }
        }

        if (bonusStat > 0) {
            for (FleetMemberAPI member : playerFleetMembers) {
                if (member.getBuffManager().getBuff(SUPPLIES_RECYCLER_ID) != null) {
                    member.getBuffManager().removeBuff(SUPPLIES_RECYCLER_ID);
                }
                member.getBuffManager().addBuff(new SuppliesRecyclerBuff(SUPPLIES_RECYCLER_ID, 1f - Utils.computeStatMultiplier(bonusStat)));
            }
        } else {
            for (FleetMemberAPI member : playerFleetMembers) {
                member.getBuffManager().removeBuff(SUPPLIES_RECYCLER_ID);
            }
        }

        playerFleet.getFleetData().getCacheClearedOnSync().put(SUPPLIES_RECYCLER_ID, bonusStat);
    }

    public static class SuppliesRecyclerBuff implements BuffManagerAPI.Buff {
        String id;
        float statValue;

        public SuppliesRecyclerBuff(String id, float statValue) {
            this.id = id;
            this.statValue = statValue;
        }

        @Override
        public void apply(FleetMemberAPI member) {
            member.getStats().getSuppliesPerMonth().modifyMult(SUPPLIES_RECYCLER_ID, this.statValue);
        }

        @Override
        public String getId() {
            return this.id;
        }

        @Override
        public boolean isExpired() {
            return false;
        }

        @Override
        public void advance(float days) {

        }
    }
}
