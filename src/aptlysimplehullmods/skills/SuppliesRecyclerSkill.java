package aptlysimplehullmods.skills;

import aptlysimplehullmods.Ids;
import aptlysimplehullmods.Utils;
import aptlysimplehullmods.hullmods.SuppliesRecycler;
import com.fs.starfarer.api.campaign.FleetDataAPI;
import com.fs.starfarer.api.characters.ShipSkillEffect;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.fleet.FleetMemberAPI;
import com.fs.starfarer.api.impl.campaign.skills.BaseSkillEffectDescription;

public class SuppliesRecyclerSkill {
    public static String SUPPLIES_EFFECT_ID = Ids.SUPPLIES_RECYCLER + "_mod";

    public static class Level1 extends BaseSkillEffectDescription implements ShipSkillEffect {
        public void apply(MutableShipStatsAPI stats, ShipAPI.HullSize hullSize, String id, float level) {
            float useMult = getSupplyUseMult(getFleetData(stats));
            stats.getSuppliesPerMonth().modifyMult(SUPPLIES_EFFECT_ID, useMult);
        }

        public void unapply(MutableShipStatsAPI stats, ShipAPI.HullSize hullSize, String id) {
            stats.getSuppliesPerMonth().unmodifyMult(SUPPLIES_EFFECT_ID);
        }

        public String getEffectDescription(float level) {
            return null;
        }

        protected float getSupplyUseFactor(FleetDataAPI data) {
            if (data == null) {
                return 0f;
            }

            float bonusStat = 0f;
            for (FleetMemberAPI member : data.getMembersListCopy()) {
                if (member.isMothballed() || !member.getVariant().hasHullMod(Ids.SUPPLIES_RECYCLER)) {
                    continue;
                }
                bonusStat += SuppliesRecycler.FLEET_SUPPLIES_PER_MONTH.get(member.getVariant().getHullSize());
            }

            float computedStat = 1f - Utils.computeStatMultiplier(bonusStat);
            if (computedStat < 0.25f) {
                computedStat = 0.25f;
            }

            return computedStat;
        }

        protected float getSupplyUseMult(FleetDataAPI data) {
            if (data == null) {
                return 0f;
            }

            String key = Ids.SUPPLIES_RECYCLER + "_key";
            Float bonus = (Float) data.getCacheClearedOnSync().get(key);
            if (bonus != null) {
                return bonus;
            }

            float useMult = getSupplyUseFactor(data);

            data.getCacheClearedOnSync().put(key, useMult);

            return useMult;
        }
    }
}
