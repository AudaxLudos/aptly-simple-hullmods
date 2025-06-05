package aptlysimplehullmods.plugins;

import aptlysimplehullmods.Ids;
import aptlysimplehullmods.Utils;
import aptlysimplehullmods.hullmods.SuppliesRecycler;
import com.fs.starfarer.api.EveryFrameScript;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.BuffManagerAPI;
import com.fs.starfarer.api.campaign.CampaignFleetAPI;
import com.fs.starfarer.api.fleet.FleetMemberAPI;

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
        Float bonusStat = (Float) playerFleet.getFleetData().getCacheClearedOnSync().get(Ids.SUPPLIES_RECYCLER);
        if (bonusStat != null) {
            return;
        }

        bonusStat = 0f;
        for (FleetMemberAPI member : playerFleet.getFleetData().getMembersListCopy()) {
            if (member.isMothballed() || !member.getVariant().hasHullMod(Ids.SUPPLIES_RECYCLER)) {
                continue;
            }
            bonusStat += SuppliesRecycler.FLEET_SUPPLIES_PER_MONTH.get(member.getVariant().getHullSize());
        }

        if (bonusStat > 0) {
            for (FleetMemberAPI member : playerFleet.getFleetData().getMembersListCopy()) {
                member.getStats().getSuppliesPerMonth().modifyMult(Ids.SUPPLIES_RECYCLER, 1f - Utils.computeStatMultiplier(bonusStat));
            }
        } else {
            for (FleetMemberAPI member : playerFleet.getFleetData().getMembersListCopy()) {
                member.getStats().getSuppliesPerMonth().unmodify(Ids.SUPPLIES_RECYCLER);
            }
        }

        playerFleet.getFleetData().getCacheClearedOnSync().put(Ids.SUPPLIES_RECYCLER, bonusStat);
    }

    // Remove when doing a big update
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
