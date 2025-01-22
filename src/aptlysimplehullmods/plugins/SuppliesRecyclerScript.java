package aptlysimplehullmods.plugins;

import aptlysimplehullmods.Ids;
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
        FleetDataAPI playerFleetData = playerFleet.getFleetData();
        List<FleetMemberAPI> playerFleetMembers = playerFleetData.getMembersListCopy();
        Float bonusStat = (Float) playerFleet.getFleetData().getCacheClearedOnSync().get(Ids.SUPPLIES_RECYCLER);
        if (bonusStat != null) {
            return;
        }

        bonusStat = 0f;
        for (FleetMemberAPI member : playerFleetMembers) {
            if (member.isMothballed()) {
                continue;
            }
            if (member.getVariant().hasHullMod(Ids.SUPPLIES_RECYCLER)) {
                bonusStat += SuppliesRecycler.FLEET_SUPPLIES_PER_MONTH.get(member.getVariant().getHullSize());
            }
        }

        if (bonusStat > 0) {
            for (FleetMemberAPI member : playerFleetMembers) {
                if (member.getBuffManager().getBuff(Ids.SUPPLIES_RECYCLER) != null) {
                    member.getBuffManager().removeBuff(Ids.SUPPLIES_RECYCLER);
                }
                member.getBuffManager().addBuff(new SuppliesRecyclerBuff(Ids.SUPPLIES_RECYCLER, 1f - Utils.computeStatMultiplier(bonusStat)));
            }
        } else {
            for (FleetMemberAPI member : playerFleetMembers) {
                member.getBuffManager().removeBuff(Ids.SUPPLIES_RECYCLER);
            }
        }

        playerFleet.getFleetData().getCacheClearedOnSync().put(Ids.SUPPLIES_RECYCLER, bonusStat);
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
            member.getStats().getSuppliesPerMonth().modifyMult(Ids.SUPPLIES_RECYCLER, this.statValue);
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
