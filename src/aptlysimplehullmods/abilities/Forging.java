package aptlysimplehullmods.abilities;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.SectorEntityToken;
import com.fs.starfarer.api.fleet.FleetMemberAPI;
import com.fs.starfarer.api.impl.campaign.abilities.BaseToggleAbility;
import com.fs.starfarer.api.ui.LabelAPI;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;

import java.awt.*;
import java.util.ArrayList;

public class Forging extends BaseToggleAbility {
    @Override
    public void init(String id, SectorEntityToken entity) {
        super.init(id, entity);
        activate();
    }

    @Override
    public void createTooltip(TooltipMakerAPI tooltip, boolean expanded) {
        Color t = Misc.getGrayColor();
        Color b = Misc.getHighlightColor();

        String status = " (off)";
        if (isActive()) {
            status = " (on)";
        }

        LabelAPI title = tooltip.addTitle(this.spec.getName() + status);
        title.highlightLast(status);
        title.setHighlightColor(t);

        float pad = 10f;
        tooltip.addPara("Disables or enables ASH hullmods that transforms, produces or consumes commodities.", pad);

        if (expanded) {
            tooltip.addPara("Ships listed below will be affected:", pad);
            tooltip.addSpacer(3f);
            int shipsWithProductionHullmods = 0;
            for (FleetMemberAPI m : Global.getSector().getPlayerFleet().getFleetData().getMembersListCopy()) {
                if (m.getVariant().hasHullMod("ash_fuel_ramscoop") ||
                        m.getVariant().hasHullMod("ash_industrial_machine_forge") ||
                        m.getVariant().hasHullMod("ash_marine_training_facility") ||
                        m.getVariant().hasHullMod("ash_mineral_refinery")) {
                    tooltip.addPara("    " + m.getShipName(), b, 0f);
                    ++shipsWithProductionHullmods;
                }
            }
            if (shipsWithProductionHullmods == 0) {
                tooltip.addPara("    None", b, 0f);
            }
        } else {
            tooltip.addPara("Expand tooltip to view ships that are affected when toggling this ability", t, pad);
            tooltip.addSpacer(3f);
        }
    }

    @Override
    protected String getActivationText() {
        return "ASH Forging active";
    }

    @Override
    protected String getDeactivationText() {
        return "ASH Forging inactive";
    }

    @Override
    protected void activateImpl() {
    }

    @Override
    protected void applyEffect(float amount, float level) {
    }

    @Override
    protected void deactivateImpl() {
    }

    @Override
    protected void cleanupImpl() {
    }
}
