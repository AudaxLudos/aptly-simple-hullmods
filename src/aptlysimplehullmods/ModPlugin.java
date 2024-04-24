package aptlysimplehullmods;

import aptlysimplehullmods.plugins.*;
import com.fs.starfarer.api.BaseModPlugin;
import com.fs.starfarer.api.Global;

public class ModPlugin extends BaseModPlugin {
    @Override
    public void onGameLoad(boolean newGame) {
        Global.getSector().addTransientScript(new FuelRamscoopScript());
        Global.getSector().addTransientScript(new MarineTrainingFacilityScript());
        Global.getSector().addTransientScript(new IndustrialMachineForgeScript());
        Global.getSector().addTransientScript(new MineralRefineryScript());
        Global.getSector().addTransientScript(new InvasionPackageScript());
    }
}
