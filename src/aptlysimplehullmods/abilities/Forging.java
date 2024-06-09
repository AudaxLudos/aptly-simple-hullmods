package aptlysimplehullmods.abilities;

import com.fs.starfarer.api.campaign.SectorEntityToken;
import com.fs.starfarer.api.impl.campaign.abilities.BaseToggleAbility;
import com.fs.starfarer.api.ui.LabelAPI;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;

import java.awt.*;

public class Forging extends BaseToggleAbility {
    @Override
    public void init(String id, SectorEntityToken entity) {
        super.init(id, entity);
        activate();
    }

    @Override
    public void createTooltip(TooltipMakerAPI tooltip, boolean expanded) {
        Color gray = Misc.getGrayColor();

        String status = " (off)";
        if (isActive()) {
            status = " (on)";
        }

        LabelAPI title = tooltip.addTitle(this.spec.getName() + status);
        title.highlightLast(status);
        title.setHighlightColor(gray);

        float pad = 10f;
        tooltip.addPara("Disables or enables forging of commodities when using certain ASH logistics hullmod.", pad);
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
