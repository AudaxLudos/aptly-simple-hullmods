package data.scripts;

import com.fs.starfarer.api.BaseModPlugin;
import com.fs.starfarer.api.EveryFrameScript;
import com.fs.starfarer.api.Global;

import data.scripts.campaign.ASH_FuelRamscoopScript;

public class ASH_ModPlugin extends BaseModPlugin {
    @Override
    public void onGameLoad(boolean newGame) {
        Global.getSector().addTransientScript((EveryFrameScript) new ASH_FuelRamscoopScript());
    }
}
