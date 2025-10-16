package aptlysimplehullmods;

import aptlysimplehullmods.hullmods.*;
import aptlysimplehullmods.plugins.*;
import com.fs.starfarer.api.BaseModPlugin;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.loading.HullModSpecAPI;
import lunalib.lunaSettings.LunaSettings;
import org.json.JSONException;
import org.json.JSONObject;

public class ModPlugin extends BaseModPlugin {

    @Override
    public void onGameLoad(boolean newGame) {
        setPlayerSkills();
        registerScripts();
        loadModSettings();
        setHullmodsVisibility();
    }

    public void setPlayerSkills() {
        if (!Global.getSector().getPlayerStats().hasSkill("ash_supplies_recycler")) {
            Global.getSector().getPlayerStats().setSkillLevel("ash_supplies_recycler", 1f);
        }
    }

    public void registerScripts() {
        Global.getSector().addTransientScript(new FuelRamscoopScript());
        Global.getSector().addTransientScript(new MarineTrainingFacilityScript());
        Global.getSector().addTransientScript(new IndustrialMachineForgeScript());
        Global.getSector().addTransientScript(new MineralRefineryScript());
        Global.getSector().addTransientScript(new InvasionPackageScript());
        Global.getSector().addTransientScript(new StreamlinedBubbleDriveScript());
    }

    public void loadModSettings() {
        try {
            JSONObject statsData;

            statsData = Global.getSettings().getJSONObject("ash_advance_radar_systems");
            AdvanceRadarSystems.ENABLED = getBoolean(statsData, "ash_advance_radar_systems_stat0");
            AdvanceRadarSystems.WEAPON_RANGE_MULT = getFloat(statsData, "ash_advance_radar_systems_stat1");
            AdvanceRadarSystems.WEAPON_RANGE_MULT = getFloat(statsData, "ash_advance_radar_systems_stat2");

            statsData = Global.getSettings().getJSONObject("ash_automated_racks");
            AutomatedRacks.ENABLED = getBoolean(statsData, "ash_advance_radar_systems_stat0");
            AutomatedRacks.MISSILE_FIRE_RATE_MULT = getFloat(statsData, "ash_automated_racks_stat1");
            AutomatedRacks.MISSILE_AMMO_MULT = getFloat(statsData, "ash_automated_racks_stat2");
            AutomatedRacks.MISSILE_TURN_RATE_MULT = getFloat(statsData, "ash_automated_racks_stat3");

            statsData = Global.getSettings().getJSONObject("ash_beam_combiners");
            BeamCombiners.ENABLED = getBoolean(statsData, "ash_beam_combiners_stat0");
            BeamCombiners.BEAM_DAMAGE_MULT = getFloat(statsData, "ash_beam_combiners_stat1");
            BeamCombiners.BEAM_FLUX_COST_MULT = getFloat(statsData, "ash_beam_combiners_stat2");

            statsData = Global.getSettings().getJSONObject("ash_circuit_breakers");
            CircuitBreakers.ENABLED = getBoolean(statsData, "ash_circuit_breakers_stat0");
            CircuitBreakers.OVERLOAD_TIME_MULT = getFloat(statsData, "ash_circuit_breakers_stat1");
            CircuitBreakers.EMP_DAMAGE_TAKEN_MULT = getFloat(statsData, "ash_circuit_breakers_stat2");

            statsData = Global.getSettings().getJSONObject("ash_convicted_crewmates");
            ConvictedCrewmates.ENABLED = getBoolean(statsData, "ash_convicted_crewmates_stat0");
            ConvictedCrewmates.FRIGATE_PPT_MULT = getFloat(statsData, "ash_convicted_crewmates_stat1");
            ConvictedCrewmates.DESTROYER_PPT_MULT = getFloat(statsData, "ash_convicted_crewmates_stat2");
            ConvictedCrewmates.MAX_CR_MOD = getFloat(statsData, "ash_convicted_crewmates_stat3");
            ConvictedCrewmates.KILL_TIMER = getFloat(statsData, "ash_convicted_crewmates_stat4");
            ConvictedCrewmates.PPT_GAIN.put(ShipAPI.HullSize.FRIGATE, getFloat(statsData, "ash_convicted_crewmates_stat5", 0));
            ConvictedCrewmates.PPT_GAIN.put(ShipAPI.HullSize.DESTROYER, getFloat(statsData, "ash_convicted_crewmates_stat5", 1));
            ConvictedCrewmates.PPT_GAIN.put(ShipAPI.HullSize.CRUISER, getFloat(statsData, "ash_convicted_crewmates_stat5", 2));
            ConvictedCrewmates.PPT_GAIN.put(ShipAPI.HullSize.CAPITAL_SHIP, getFloat(statsData, "ash_convicted_crewmates_stat5", 3));

            statsData = Global.getSettings().getJSONObject("ash_external_cargo_holds");
            ExternalCargoHolds.ENABLED = getBoolean(statsData, "ash_external_cargo_holds_stat0");
            ExternalCargoHolds.FLUX_DISSIPATION_MULT = getFloat(statsData, "ash_external_cargo_holds_stat1");
            ExternalCargoHolds.SENSOR_PROFILE_MULT = getFloat(statsData, "ash_external_cargo_holds_stat2");
            ExternalCargoHolds.ARMOR_DAMAGE_TAKEN_MULT = getFloat(statsData, "ash_external_cargo_holds_stat3");
            ExternalCargoHolds.CARGO_MOD.put(ShipAPI.HullSize.FRIGATE, getFloat(statsData, "ash_external_cargo_holds_stat4", 0));
            ExternalCargoHolds.CARGO_MOD.put(ShipAPI.HullSize.DESTROYER, getFloat(statsData, "ash_external_cargo_holds_stat4", 1));
            ExternalCargoHolds.CARGO_MOD.put(ShipAPI.HullSize.CRUISER, getFloat(statsData, "ash_external_cargo_holds_stat4", 2));
            ExternalCargoHolds.CARGO_MOD.put(ShipAPI.HullSize.CAPITAL_SHIP, getFloat(statsData, "ash_external_cargo_holds_stat4", 3));

            statsData = Global.getSettings().getJSONObject("ash_external_cryo_pods");
            ExternalCryoPods.ENABLED = getBoolean(statsData, "ash_external_cryo_pods_stat0");
            ExternalCryoPods.FLUX_DISSIPATION_MULT = getFloat(statsData, "ash_external_cryo_pods_stat1");
            ExternalCryoPods.SENSOR_PROFILE_MULT = getFloat(statsData, "ash_external_cryo_pods_stat2");
            ExternalCryoPods.CREW_LOST_MULT = getFloat(statsData, "ash_external_cryo_pods_stat3");
            ExternalCryoPods.CREW_MOD.put(ShipAPI.HullSize.FRIGATE, getFloat(statsData, "ash_external_cryo_pods_stat4", 0));
            ExternalCryoPods.CREW_MOD.put(ShipAPI.HullSize.DESTROYER, getFloat(statsData, "ash_external_cryo_pods_stat4", 1));
            ExternalCryoPods.CREW_MOD.put(ShipAPI.HullSize.CRUISER, getFloat(statsData, "ash_external_cryo_pods_stat4", 2));
            ExternalCryoPods.CREW_MOD.put(ShipAPI.HullSize.CAPITAL_SHIP, getFloat(statsData, "ash_external_cryo_pods_stat4", 3));

            statsData = Global.getSettings().getJSONObject("ash_external_fuel_tanks");
            ExternalFuelTanks.ENABLED = getBoolean(statsData, "ash_external_fuel_tanks_stat0");
            ExternalFuelTanks.FLUX_DISSIPATION_MULT = getFloat(statsData, "ash_external_fuel_tanks_stat1");
            ExternalFuelTanks.SENSOR_PROFILE_MULT = getFloat(statsData, "ash_external_fuel_tanks_stat2");
            ExternalFuelTanks.HIGH_EXPLOSIVE_DAMAGE_TAKEN_MULT = getFloat(statsData, "ash_external_fuel_tanks_stat3");
            ExternalFuelTanks.FUEL_MOD.put(ShipAPI.HullSize.FRIGATE, getFloat(statsData, "ash_external_fuel_tanks_stat4", 0));
            ExternalFuelTanks.FUEL_MOD.put(ShipAPI.HullSize.DESTROYER, getFloat(statsData, "ash_external_fuel_tanks_stat4", 1));
            ExternalFuelTanks.FUEL_MOD.put(ShipAPI.HullSize.CRUISER, getFloat(statsData, "ash_external_fuel_tanks_stat4", 2));
            ExternalFuelTanks.FUEL_MOD.put(ShipAPI.HullSize.CAPITAL_SHIP, getFloat(statsData, "ash_external_fuel_tanks_stat4", 3));

            statsData = Global.getSettings().getJSONObject("ash_fighter_targeting_uplink");
            FighterTargetingUplink.ENABLED = getBoolean(statsData, "ash_fighter_targeting_uplink_stat0");
            FighterTargetingUplink.FIGHTER_DAMAGE_MULT = getFloat(statsData, "ash_fighter_targeting_uplink_stat1");
            FighterTargetingUplink.MIN_EFFECTIVE_RANGE = getFloat(statsData, "ash_fighter_targeting_uplink_stat2");
            FighterTargetingUplink.MAX_EFFECTIVE_RANGE = getFloat(statsData, "ash_fighter_targeting_uplink_stat3");

            statsData = Global.getSettings().getJSONObject("ash_flux_amplifiers");
            FluxAmplifiers.ENABLED = getBoolean(statsData, "ash_flux_amplifiers_stat0");
            FluxAmplifiers.WEAPON_DAMAGE_MULT = getFloat(statsData, "ash_flux_amplifiers_stat1");
            FluxAmplifiers.WEAPON_FLUX_MULT = getFloat(statsData, "ash_flux_amplifiers_stat2");
            FluxAmplifiers.WEAPON_FIRE_RATE_MULT = getFloat(statsData, "ash_flux_amplifiers_stat3");

            statsData = Global.getSettings().getJSONObject("ash_flux_limiters");
            FluxLimiters.ENABLED = getBoolean(statsData, "ash_flux_limiters_stat0");
            FluxLimiters.WEAPON_FLUX_MULT = getFloat(statsData, "ash_flux_limiters_stat1");
            FluxLimiters.WEAPON_DAMAGE_MULT = getFloat(statsData, "ash_flux_limiters_stat2");
            FluxLimiters.WEAPON_FIRE_RATE_MULT = getFloat(statsData, "ash_flux_limiters_stat3");

            statsData = Global.getSettings().getJSONObject("ash_front_loaded_armor");
            FrontLoadedArmor.ENABLED = getBoolean(statsData, "ash_front_loaded_armor_stat0");
            FrontLoadedArmor.POSITIVE_ARMOR_VALUE_MULT = getFloat(statsData, "ash_front_loaded_armor_stat1");
            FrontLoadedArmor.NEGATIVE_ARMOR_VALUE_MULT = getFloat(statsData, "ash_front_loaded_armor_stat2");

            statsData = Global.getSettings().getJSONObject("ash_fuel_ramscoop");
            FuelRamscoop.ENABLED = getBoolean(statsData, "ash_fuel_ramscoop_stat0");
            FuelRamscoop.DAYS_TO_GENERATE_FUEL = getFloat(statsData, "ash_fuel_ramscoop_stat1");
            FuelRamscoop.FUEL_TO_GENERATE.put(ShipAPI.HullSize.FRIGATE, getFloat(statsData, "ash_fuel_ramscoop_stat2", 0));
            FuelRamscoop.FUEL_TO_GENERATE.put(ShipAPI.HullSize.DESTROYER, getFloat(statsData, "ash_fuel_ramscoop_stat2", 1));
            FuelRamscoop.FUEL_TO_GENERATE.put(ShipAPI.HullSize.CRUISER, getFloat(statsData, "ash_fuel_ramscoop_stat2", 2));
            FuelRamscoop.FUEL_TO_GENERATE.put(ShipAPI.HullSize.CAPITAL_SHIP, getFloat(statsData, "ash_fuel_ramscoop_stat2", 3));
            FuelRamscoop.SMOD_FUEL_TO_GENERATE.put(ShipAPI.HullSize.FRIGATE, getFloat(statsData, "ash_fuel_ramscoop_stat3", 0));
            FuelRamscoop.SMOD_FUEL_TO_GENERATE.put(ShipAPI.HullSize.DESTROYER, getFloat(statsData, "ash_fuel_ramscoop_stat3", 1));
            FuelRamscoop.SMOD_FUEL_TO_GENERATE.put(ShipAPI.HullSize.CRUISER, getFloat(statsData, "ash_fuel_ramscoop_stat3", 2));
            FuelRamscoop.SMOD_FUEL_TO_GENERATE.put(ShipAPI.HullSize.CAPITAL_SHIP, getFloat(statsData, "ash_fuel_ramscoop_stat3", 3));

            statsData = Global.getSettings().getJSONObject("ash_graviton_attunement_drive");
            GravitonAttunementDrive.ENABLED = getBoolean(statsData, "ash_graviton_attunement_drive_stat0");
            GravitonAttunementDrive.SUPPLIES_PER_MONTH_MULT = getFloat(statsData, "ash_graviton_attunement_drive_stat1");
            GravitonAttunementDrive.SENSOR_PROFILE_MULT = getFloat(statsData, "ash_graviton_attunement_drive_stat2");
            GravitonAttunementDrive.FLEET_BURN_MOD.put(ShipAPI.HullSize.FRIGATE, getFloat(statsData, "ash_graviton_attunement_drive_stat3", 0));
            GravitonAttunementDrive.FLEET_BURN_MOD.put(ShipAPI.HullSize.DESTROYER, getFloat(statsData, "ash_graviton_attunement_drive_stat3", 1));
            GravitonAttunementDrive.FLEET_BURN_MOD.put(ShipAPI.HullSize.CRUISER, getFloat(statsData, "ash_graviton_attunement_drive_stat3", 2));
            GravitonAttunementDrive.FLEET_BURN_MOD.put(ShipAPI.HullSize.CAPITAL_SHIP, getFloat(statsData, "ash_graviton_attunement_drive_stat3", 3));

            statsData = Global.getSettings().getJSONObject("ash_industrial_machine_forge");
            IndustrialMachineForge.ENABLED = getBoolean(statsData, "ash_industrial_machine_forge_stat0");
            IndustrialMachineForge.DAYS_TO_GENERATE_HEAVY_MACHINERY = getFloat(statsData, "ash_industrial_machine_forge_stat1");
            IndustrialMachineForge.HEAVY_MACHINERY_TO_GENERATE.put(ShipAPI.HullSize.FRIGATE, getInt(statsData, "ash_industrial_machine_forge_stat2", 0));
            IndustrialMachineForge.HEAVY_MACHINERY_TO_GENERATE.put(ShipAPI.HullSize.DESTROYER, getInt(statsData, "ash_industrial_machine_forge_stat2", 1));
            IndustrialMachineForge.HEAVY_MACHINERY_TO_GENERATE.put(ShipAPI.HullSize.CRUISER, getInt(statsData, "ash_industrial_machine_forge_stat2", 2));
            IndustrialMachineForge.HEAVY_MACHINERY_TO_GENERATE.put(ShipAPI.HullSize.CAPITAL_SHIP, getInt(statsData, "ash_industrial_machine_forge_stat2", 3));
            IndustrialMachineForge.METALS_TO_CONSUME.put(ShipAPI.HullSize.FRIGATE, getInt(statsData, "ash_industrial_machine_forge_stat3", 0));
            IndustrialMachineForge.METALS_TO_CONSUME.put(ShipAPI.HullSize.DESTROYER, getInt(statsData, "ash_industrial_machine_forge_stat3", 1));
            IndustrialMachineForge.METALS_TO_CONSUME.put(ShipAPI.HullSize.CRUISER, getInt(statsData, "ash_industrial_machine_forge_stat3", 2));
            IndustrialMachineForge.METALS_TO_CONSUME.put(ShipAPI.HullSize.CAPITAL_SHIP, getInt(statsData, "ash_industrial_machine_forge_stat3", 3));

            statsData = Global.getSettings().getJSONObject("ash_invasion_package");
            InvasionPackage.ENABLED = getBoolean(statsData, "ash_invasion_package_stat0");
            InvasionPackage.PLANETARY_OPERATION_CASUALTIES_MULT.put(ShipAPI.HullSize.FRIGATE, getFloat(statsData, "ash_invasion_package_stat1", 0));
            InvasionPackage.PLANETARY_OPERATION_CASUALTIES_MULT.put(ShipAPI.HullSize.DESTROYER, getFloat(statsData, "ash_invasion_package_stat1", 1));
            InvasionPackage.PLANETARY_OPERATION_CASUALTIES_MULT.put(ShipAPI.HullSize.CRUISER, getFloat(statsData, "ash_invasion_package_stat1", 2));
            InvasionPackage.PLANETARY_OPERATION_CASUALTIES_MULT.put(ShipAPI.HullSize.CAPITAL_SHIP, getFloat(statsData, "ash_invasion_package_stat1", 3));
            InvasionPackage.PLANETARY_OPERATIONS_MULT.put(ShipAPI.HullSize.FRIGATE, getFloat(statsData, "ash_invasion_package_stat2", 0));
            InvasionPackage.PLANETARY_OPERATIONS_MULT.put(ShipAPI.HullSize.DESTROYER, getFloat(statsData, "ash_invasion_package_stat2", 1));
            InvasionPackage.PLANETARY_OPERATIONS_MULT.put(ShipAPI.HullSize.CRUISER, getFloat(statsData, "ash_invasion_package_stat2", 2));
            InvasionPackage.PLANETARY_OPERATIONS_MULT.put(ShipAPI.HullSize.CAPITAL_SHIP, getFloat(statsData, "ash_invasion_package_stat2", 3));
            InvasionPackage.CARGO_CAPACITY_MULT = getFloat(statsData, "ash_invasion_package_stat3");

            statsData = Global.getSettings().getJSONObject("ash_makeshift_missile_autoforge");
            MakeshiftMissileAutoforge.ENABLED = getBoolean(statsData, "ash_makeshift_missile_autoforge_stat0");
            MakeshiftMissileAutoforge.MISSILE_AMMO_RELOAD_SIZE_MOD = getFloat(statsData, "ash_makeshift_missile_autoforge_stat1");
            MakeshiftMissileAutoforge.MISSILE_AMMO_PER_SECOND_MOD = getFloat(statsData, "ash_makeshift_missile_autoforge_stat2");

            statsData = Global.getSettings().getJSONObject("ash_marine_training_facility");
            MarineTrainingFacility.ENABLED = getBoolean(statsData, "ash_marine_training_facility_stat0");
            MarineTrainingFacility.DAYS_TO_GENERATE_MARINES = getInt(statsData, "ash_marine_training_facility_stat1");
            MarineTrainingFacility.MARINES_TO_GENERATE.put(ShipAPI.HullSize.FRIGATE, getInt(statsData, "ash_marine_training_facility_stat2", 0));
            MarineTrainingFacility.MARINES_TO_GENERATE.put(ShipAPI.HullSize.DESTROYER, getInt(statsData, "ash_marine_training_facility_stat2", 1));
            MarineTrainingFacility.MARINES_TO_GENERATE.put(ShipAPI.HullSize.CRUISER, getInt(statsData, "ash_marine_training_facility_stat2", 2));
            MarineTrainingFacility.MARINES_TO_GENERATE.put(ShipAPI.HullSize.CAPITAL_SHIP, getInt(statsData, "ash_marine_training_facility_stat2", 3));
            MarineTrainingFacility.MARINES_TO_LEVEL.put(ShipAPI.HullSize.FRIGATE, getInt(statsData, "ash_marine_training_facility_stat3", 0));
            MarineTrainingFacility.MARINES_TO_LEVEL.put(ShipAPI.HullSize.DESTROYER, getInt(statsData, "ash_marine_training_facility_stat3", 1));
            MarineTrainingFacility.MARINES_TO_LEVEL.put(ShipAPI.HullSize.CRUISER, getInt(statsData, "ash_marine_training_facility_stat3", 2));
            MarineTrainingFacility.MARINES_TO_LEVEL.put(ShipAPI.HullSize.CAPITAL_SHIP, getInt(statsData, "ash_marine_training_facility_stat3", 3));
            MarineTrainingFacility.SMOD_MARINES_TO_LEVEL.put(ShipAPI.HullSize.FRIGATE, getInt(statsData, "ash_marine_training_facility_stat4", 0));
            MarineTrainingFacility.SMOD_MARINES_TO_LEVEL.put(ShipAPI.HullSize.DESTROYER, getInt(statsData, "ash_marine_training_facility_stat4", 1));
            MarineTrainingFacility.SMOD_MARINES_TO_LEVEL.put(ShipAPI.HullSize.CRUISER, getInt(statsData, "ash_marine_training_facility_stat4", 2));
            MarineTrainingFacility.SMOD_MARINES_TO_LEVEL.put(ShipAPI.HullSize.CAPITAL_SHIP, getInt(statsData, "ash_marine_training_facility_stat4", 3));
            MarineTrainingFacility.MAX_MARINES_TO_GENERATE.put(ShipAPI.HullSize.FRIGATE, getInt(statsData, "ash_marine_training_facility_stat5", 0));
            MarineTrainingFacility.MAX_MARINES_TO_GENERATE.put(ShipAPI.HullSize.DESTROYER, getInt(statsData, "ash_marine_training_facility_stat5", 1));
            MarineTrainingFacility.MAX_MARINES_TO_GENERATE.put(ShipAPI.HullSize.CRUISER, getInt(statsData, "ash_marine_training_facility_stat5", 2));
            MarineTrainingFacility.MAX_MARINES_TO_GENERATE.put(ShipAPI.HullSize.CAPITAL_SHIP, getInt(statsData, "ash_marine_training_facility_stat5", 3));

            statsData = Global.getSettings().getJSONObject("ash_mineral_refinery");
            MineralRefinery.ENABLED = getBoolean(statsData, "ash_mineral_refinery_stat0");
            MineralRefinery.DAYS_TO_GENERATE_ALLOYS = getInt(statsData, "ash_mineral_refinery_stat1");
            MineralRefinery.ALLOYS_TO_GENERATE.put(ShipAPI.HullSize.FRIGATE, getInt(statsData, "ash_mineral_refinery_stat2", 0));
            MineralRefinery.ALLOYS_TO_GENERATE.put(ShipAPI.HullSize.DESTROYER, getInt(statsData, "ash_mineral_refinery_stat2", 1));
            MineralRefinery.ALLOYS_TO_GENERATE.put(ShipAPI.HullSize.CRUISER, getInt(statsData, "ash_mineral_refinery_stat2", 2));
            MineralRefinery.ALLOYS_TO_GENERATE.put(ShipAPI.HullSize.CAPITAL_SHIP, getInt(statsData, "ash_mineral_refinery_stat2", 3));
            MineralRefinery.MINERALS_TO_CONSUME.put(ShipAPI.HullSize.FRIGATE, getInt(statsData, "ash_mineral_refinery_stat3", 0));
            MineralRefinery.MINERALS_TO_CONSUME.put(ShipAPI.HullSize.DESTROYER, getInt(statsData, "ash_mineral_refinery_stat3", 1));
            MineralRefinery.MINERALS_TO_CONSUME.put(ShipAPI.HullSize.CRUISER, getInt(statsData, "ash_mineral_refinery_stat3", 2));
            MineralRefinery.MINERALS_TO_CONSUME.put(ShipAPI.HullSize.CAPITAL_SHIP, getInt(statsData, "ash_mineral_refinery_stat3", 3));

            statsData = Global.getSettings().getJSONObject("ash_missile_bay");
            MissileBay.ENABLED = getBoolean(statsData, "ash_missile_bay_stat0");
            MissileBay.MISSILE_AMMO_MULT = getFloat(statsData, "ash_missile_bay_stat1");
            MissileBay.FIGHTER_BAY_MOD = getFloat(statsData, "ash_missile_bay_stat2");
            MissileBay.DEPLOYMENT_POINTS_MOD.put(ShipAPI.HullSize.FRIGATE, getFloat(statsData, "ash_missile_bay_stat3", 0));
            MissileBay.DEPLOYMENT_POINTS_MOD.put(ShipAPI.HullSize.DESTROYER, getFloat(statsData, "ash_missile_bay_stat3", 1));
            MissileBay.DEPLOYMENT_POINTS_MOD.put(ShipAPI.HullSize.CRUISER, getFloat(statsData, "ash_missile_bay_stat3", 2));
            MissileBay.DEPLOYMENT_POINTS_MOD.put(ShipAPI.HullSize.CAPITAL_SHIP, getFloat(statsData, "ash_missile_bay_stat3", 3));

            statsData = Global.getSettings().getJSONObject("ash_reactive_subsystems");
            ReactiveSubsystems.ENABLED = getBoolean(statsData, "ash_reactive_subsystems_stat0");
            ReactiveSubsystems.MAX_CR_MOD = getFloat(statsData, "ash_reactive_subsystems_stat1");
            ReactiveSubsystems.DEPLOYMENT_POINTS_MOD.put(ShipAPI.HullSize.FRIGATE, getFloat(statsData, "ash_reactive_subsystems_stat2", 0));
            ReactiveSubsystems.DEPLOYMENT_POINTS_MOD.put(ShipAPI.HullSize.DESTROYER, getFloat(statsData, "ash_reactive_subsystems_stat2", 1));
            ReactiveSubsystems.DEPLOYMENT_POINTS_MOD.put(ShipAPI.HullSize.CRUISER, getFloat(statsData, "ash_reactive_subsystems_stat2", 2));
            ReactiveSubsystems.DEPLOYMENT_POINTS_MOD.put(ShipAPI.HullSize.CAPITAL_SHIP, getFloat(statsData, "ash_reactive_subsystems_stat2", 3));

            statsData = Global.getSettings().getJSONObject("ash_reactor_bay");
            ReactorBay.ENABLED = getBoolean(statsData, "ash_reactor_bay_stat0");
            ReactorBay.FLUX_DISSIPATION_MULT = getFloat(statsData, "ash_reactor_bay_stat1");
            ReactorBay.FLUX_CAPACITY_MULT = getFloat(statsData, "ash_reactor_bay_stat2");
            ReactorBay.FIGHTER_BAY_MOD = getFloat(statsData, "ash_reactor_bay_stat3");
            ReactorBay.DEPLOYMENT_POINTS_MOD.put(ShipAPI.HullSize.FRIGATE, getFloat(statsData, "ash_reactor_bay_stat4", 0));
            ReactorBay.DEPLOYMENT_POINTS_MOD.put(ShipAPI.HullSize.DESTROYER, getFloat(statsData, "ash_reactor_bay_stat4", 1));
            ReactorBay.DEPLOYMENT_POINTS_MOD.put(ShipAPI.HullSize.CRUISER, getFloat(statsData, "ash_reactor_bay_stat4", 2));
            ReactorBay.DEPLOYMENT_POINTS_MOD.put(ShipAPI.HullSize.CAPITAL_SHIP, getFloat(statsData, "ash_reactor_bay_stat4", 3));

            statsData = Global.getSettings().getJSONObject("ash_streamlined_bubble_drive");
            StreamlinedBubbleDrive.ENABLED = getBoolean(statsData, "ash_streamlined_bubble_drive_stat0");
            StreamlinedBubbleDrive.TERRAIN_PENALTY_MOD.put(ShipAPI.HullSize.FRIGATE, getFloat(statsData, "ash_streamlined_bubble_drive_stat1", 0));
            StreamlinedBubbleDrive.TERRAIN_PENALTY_MOD.put(ShipAPI.HullSize.DESTROYER, getFloat(statsData, "ash_streamlined_bubble_drive_stat1", 1));
            StreamlinedBubbleDrive.TERRAIN_PENALTY_MOD.put(ShipAPI.HullSize.CRUISER, getFloat(statsData, "ash_streamlined_bubble_drive_stat1", 2));
            StreamlinedBubbleDrive.TERRAIN_PENALTY_MOD.put(ShipAPI.HullSize.CAPITAL_SHIP, getFloat(statsData, "ash_streamlined_bubble_drive_stat1", 3));

            statsData = Global.getSettings().getJSONObject("ash_supplies_recycler");
            SuppliesRecycler.ENABLED = getBoolean(statsData, "ash_supplies_recycler_stat0");
            SuppliesRecycler.FLEET_SUPPLIES_PER_MONTH.put(ShipAPI.HullSize.FRIGATE, getFloat(statsData, "ash_supplies_recycler_stat1", 0));
            SuppliesRecycler.FLEET_SUPPLIES_PER_MONTH.put(ShipAPI.HullSize.DESTROYER, getFloat(statsData, "ash_supplies_recycler_stat1", 1));
            SuppliesRecycler.FLEET_SUPPLIES_PER_MONTH.put(ShipAPI.HullSize.CRUISER, getFloat(statsData, "ash_supplies_recycler_stat1", 2));
            SuppliesRecycler.FLEET_SUPPLIES_PER_MONTH.put(ShipAPI.HullSize.CAPITAL_SHIP, getFloat(statsData, "ash_supplies_recycler_stat1", 3));
            SuppliesRecycler.CARGO_CAPACITY_MULT = getFloat(statsData, "ash_supplies_recycler_stat2");

            statsData = Global.getSettings().getJSONObject("ash_swift_retreat_protocol");
            SwiftRetreatProtocol.ENABLED = getBoolean(statsData, "ash_swift_retreat_protocol_stat0");
            SwiftRetreatProtocol.FIGHTER_MOVEMENT_MULT = getFloat(statsData, "ash_swift_retreat_protocol_stat1");
            SwiftRetreatProtocol.FIGHTER_ENGINE_DMG_TAKEN_MULT = getFloat(statsData, "ash_swift_retreat_protocol_stat2");

            statsData = Global.getSettings().getJSONObject("ash_targeting_transceiver");
            TargetingTransceiver.ENABLED = getBoolean(statsData, "ash_targeting_transceiver_stat0");
            TargetingTransceiver.WEAPON_RANGE_MOD = getFloat(statsData, "ash_targeting_transceiver_stat1");
            TargetingTransceiver.AUTOFIRE_ACCURACY_MOD = getFloat(statsData, "ash_targeting_transceiver_stat2");
            TargetingTransceiver.MIN_EFFECTIVE_RANGE = getFloat(statsData, "ash_targeting_transceiver_stat3");
            TargetingTransceiver.MAX_EFFECTIVE_RANGE = getFloat(statsData, "ash_targeting_transceiver_stat4");

            statsData = Global.getSettings().getJSONObject("ash_temporal_flux_reactor");
            TemporalFluxReactor.ENABLED = getBoolean(statsData, "ash_temporal_flux_reactor_stat0");
            TemporalFluxReactor.TIME_FLOW_MULT = getFloat(statsData, "ash_temporal_flux_reactor_stat1");
            TemporalFluxReactor.PEAK_PERFORMANCE_TIME_MULT = getFloat(statsData, "ash_temporal_flux_reactor_stat2");

            statsData = Global.getSettings().getJSONObject("ash_venting_overdrive");
            VentingOverdrive.ENABLED = getBoolean(statsData, "ash_venting_overdrive_stat0");
            VentingOverdrive.SHIP_MOVEMENT_MULT = getFloat(statsData, "ash_venting_overdrive_stat1");

            statsData = Global.getSettings().getJSONObject("ash_volatile_warheads");
            VolatileWarheads.ENABLED = getBoolean(statsData, "ash_volatile_warheads_stat0");
            VolatileWarheads.MISSILE_DAMAGE_MULT = getFloat(statsData, "ash_volatile_warheads_stat1");
            VolatileWarheads.MISSILE_SPEED_MULT = getFloat(statsData, "ash_volatile_warheads_stat2");
            VolatileWarheads.MISSILE_HEALTH_MULT = getFloat(statsData, "ash_volatile_warheads_stat3");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public int getInt(JSONObject data, String statId, Integer index) throws JSONException {
        Integer result;

        if (isModEnabled(Ids.LUNALIB)) {
            String key = (index == null) ? statId : statId + "_" + index;
            result = LunaSettings.getInt(Ids.APTLYSIMPLEHULLMODS, key);
        } else {
            if (index == null) {
                result = data.getInt(statId);
            } else {
                result = data.getJSONArray(statId).getInt(index);
            }
        }

        if (result == null) {
            return 0;
        }

        return result;
    }

    public int getInt(JSONObject data, String statId) throws JSONException {
        return getInt(data, statId, null);
    }

    public float getFloat(JSONObject data, String statId, Integer index) throws JSONException {
        Float result;

        if (isModEnabled(Ids.LUNALIB)) {
            String key = (index == null) ? statId : statId + "_" + index;
            result = LunaSettings.getFloat(Ids.APTLYSIMPLEHULLMODS, key);
        } else {
            if (index == null) {
                result = (float) data.getDouble(statId);
            } else {
                result = (float) data.getJSONArray(statId).getDouble(index);
            }
        }

        if (result == null) {
            return 0;
        }

        return result;
    }

    public float getFloat(JSONObject data, String statId) throws JSONException {
        return getFloat(data, statId, null);
    }

    public boolean getBoolean(JSONObject data, String statId) throws JSONException {
        if (isModEnabled(Ids.LUNALIB)) {
            Boolean result = LunaSettings.getBoolean(Ids.APTLYSIMPLEHULLMODS, statId);
            if (result == null) {
                return false;
            }
            return result;
        }
        return data.getBoolean(statId);
    }

    public boolean isModEnabled(String modName) {
        return Global.getSettings().getModManager().isModEnabled(modName);
    }

    public void setHullmodsVisibility() {
        HullModSpecAPI hullModSpec;
        hullModSpec = Global.getSettings().getHullModSpec(Ids.ADVANCE_RADAR_SYSTEMS);
        hullModSpec.setHidden(!AdvanceRadarSystems.ENABLED);
        hullModSpec.setHiddenEverywhere(!AdvanceRadarSystems.ENABLED);

        hullModSpec = Global.getSettings().getHullModSpec(Ids.AUTOMATED_RACKS);
        hullModSpec.setHidden(!AutomatedRacks.ENABLED);
        hullModSpec.setHiddenEverywhere(!AutomatedRacks.ENABLED);

        hullModSpec = Global.getSettings().getHullModSpec(Ids.BEAM_COMBINERS);
        hullModSpec.setHidden(!BeamCombiners.ENABLED);
        hullModSpec.setHiddenEverywhere(!BeamCombiners.ENABLED);

        hullModSpec = Global.getSettings().getHullModSpec(Ids.CIRCUIT_BREAKERS);
        hullModSpec.setHidden(!CircuitBreakers.ENABLED);
        hullModSpec.setHiddenEverywhere(!CircuitBreakers.ENABLED);

        hullModSpec = Global.getSettings().getHullModSpec(Ids.CONVICTED_CREWMATES);
        hullModSpec.setHidden(!ConvictedCrewmates.ENABLED);
        hullModSpec.setHiddenEverywhere(!ConvictedCrewmates.ENABLED);

        hullModSpec = Global.getSettings().getHullModSpec(Ids.EXTERNAL_CARGO_HOLDS);
        hullModSpec.setHidden(!ExternalCargoHolds.ENABLED);
        hullModSpec.setHiddenEverywhere(!ExternalCargoHolds.ENABLED);

        hullModSpec = Global.getSettings().getHullModSpec(Ids.EXTERNAL_CRYO_PODS);
        hullModSpec.setHidden(!ExternalCryoPods.ENABLED);
        hullModSpec.setHiddenEverywhere(!ExternalCryoPods.ENABLED);

        hullModSpec = Global.getSettings().getHullModSpec(Ids.EXTERNAL_FUEL_TANKS);
        hullModSpec.setHidden(!ExternalFuelTanks.ENABLED);
        hullModSpec.setHiddenEverywhere(!ExternalFuelTanks.ENABLED);

        hullModSpec = Global.getSettings().getHullModSpec(Ids.FIGHTER_TARGETING_UPLINK);
        hullModSpec.setHidden(!FighterTargetingUplink.ENABLED);
        hullModSpec.setHiddenEverywhere(!FighterTargetingUplink.ENABLED);

        hullModSpec = Global.getSettings().getHullModSpec(Ids.FLUX_AMPLIFIERS);
        hullModSpec.setHidden(!FluxAmplifiers.ENABLED);
        hullModSpec.setHiddenEverywhere(!FluxAmplifiers.ENABLED);

        hullModSpec = Global.getSettings().getHullModSpec(Ids.FLUX_LIMITERS);
        hullModSpec.setHidden(!FluxLimiters.ENABLED);
        hullModSpec.setHiddenEverywhere(!FluxLimiters.ENABLED);

        hullModSpec = Global.getSettings().getHullModSpec(Ids.FRONT_LOADED_ARMOR);
        hullModSpec.setHidden(!FrontLoadedArmor.ENABLED);
        hullModSpec.setHiddenEverywhere(!FrontLoadedArmor.ENABLED);

        hullModSpec = Global.getSettings().getHullModSpec(Ids.FUEL_RAMSCOOP);
        hullModSpec.setHidden(!FuelRamscoop.ENABLED);
        hullModSpec.setHiddenEverywhere(!FuelRamscoop.ENABLED);

        hullModSpec = Global.getSettings().getHullModSpec(Ids.GRAVITON_ATTUNEMENT_DRIVE);
        hullModSpec.setHidden(!GravitonAttunementDrive.ENABLED);
        hullModSpec.setHiddenEverywhere(!GravitonAttunementDrive.ENABLED);

        hullModSpec = Global.getSettings().getHullModSpec(Ids.INDUSTRIAL_MACHINE_FORGE);
        hullModSpec.setHidden(!IndustrialMachineForge.ENABLED);
        hullModSpec.setHiddenEverywhere(!IndustrialMachineForge.ENABLED);

        hullModSpec = Global.getSettings().getHullModSpec(Ids.INVASION_PACKAGE);
        hullModSpec.setHidden(!InvasionPackage.ENABLED);
        hullModSpec.setHiddenEverywhere(!InvasionPackage.ENABLED);

        hullModSpec = Global.getSettings().getHullModSpec(Ids.MAKESHIFT_MISSILE_AUTOFORGE);
        hullModSpec.setHidden(!MakeshiftMissileAutoforge.ENABLED);
        hullModSpec.setHiddenEverywhere(!MakeshiftMissileAutoforge.ENABLED);

        hullModSpec = Global.getSettings().getHullModSpec(Ids.MARINE_TRAINING_FACILITY);
        hullModSpec.setHidden(!MarineTrainingFacility.ENABLED);
        hullModSpec.setHiddenEverywhere(!MarineTrainingFacility.ENABLED);

        hullModSpec = Global.getSettings().getHullModSpec(Ids.MINERAL_REFINERY);
        hullModSpec.setHidden(!MineralRefinery.ENABLED);
        hullModSpec.setHiddenEverywhere(!MineralRefinery.ENABLED);

        hullModSpec = Global.getSettings().getHullModSpec(Ids.MISSILE_BAY);
        hullModSpec.setHidden(!MissileBay.ENABLED);
        hullModSpec.setHiddenEverywhere(!MissileBay.ENABLED);

        hullModSpec = Global.getSettings().getHullModSpec(Ids.REACTIVE_SUBSYSTEMS);
        hullModSpec.setHidden(!ReactiveSubsystems.ENABLED);
        hullModSpec.setHiddenEverywhere(!ReactiveSubsystems.ENABLED);

        hullModSpec = Global.getSettings().getHullModSpec(Ids.REACTOR_BAY);
        hullModSpec.setHidden(!ReactorBay.ENABLED);
        hullModSpec.setHiddenEverywhere(!ReactorBay.ENABLED);

        hullModSpec = Global.getSettings().getHullModSpec(Ids.STREAMLINED_BUBBLE_DRIVE);
        hullModSpec.setHidden(!StreamlinedBubbleDrive.ENABLED);
        hullModSpec.setHiddenEverywhere(!StreamlinedBubbleDrive.ENABLED);

        hullModSpec = Global.getSettings().getHullModSpec(Ids.SUPPLIES_RECYCLER);
        hullModSpec.setHidden(!SuppliesRecycler.ENABLED);
        hullModSpec.setHiddenEverywhere(!SuppliesRecycler.ENABLED);

        hullModSpec = Global.getSettings().getHullModSpec(Ids.SWIFT_RETREAT_PROTOCOL);
        hullModSpec.setHidden(!SwiftRetreatProtocol.ENABLED);
        hullModSpec.setHiddenEverywhere(!SwiftRetreatProtocol.ENABLED);

        hullModSpec = Global.getSettings().getHullModSpec(Ids.TARGETING_TRANSCEIVER);
        hullModSpec.setHidden(!TargetingTransceiver.ENABLED);
        hullModSpec.setHiddenEverywhere(!TargetingTransceiver.ENABLED);

        hullModSpec = Global.getSettings().getHullModSpec(Ids.TEMPORAL_FLUX_REACTOR);
        hullModSpec.setHidden(!TemporalFluxReactor.ENABLED);
        hullModSpec.setHiddenEverywhere(!TemporalFluxReactor.ENABLED);

        hullModSpec = Global.getSettings().getHullModSpec(Ids.VENTING_OVERDRIVE);
        hullModSpec.setHidden(!VentingOverdrive.ENABLED);
        hullModSpec.setHiddenEverywhere(!VentingOverdrive.ENABLED);

        hullModSpec = Global.getSettings().getHullModSpec(Ids.VOLATILE_WARHEADS);
        hullModSpec.setHidden(!VolatileWarheads.ENABLED);
        hullModSpec.setHiddenEverywhere(!VolatileWarheads.ENABLED);
    }
}
