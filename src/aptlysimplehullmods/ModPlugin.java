package aptlysimplehullmods;

import aptlysimplehullmods.hullmods.*;
import aptlysimplehullmods.plugins.*;
import com.fs.starfarer.api.BaseModPlugin;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.ShipAPI;
import lunalib.lunaSettings.LunaSettings;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class ModPlugin extends BaseModPlugin {
    public static final String HULLMOD_SETTINGS_FILE = "data/config/hullmod_stats.ini";
    public static final String lunaLibId = "lunalib";
    public static final String modId = "aptlysimplehullmods";

    @Override
    public void onGameLoad(boolean newGame) {
        Global.getSector().addTransientScript(new FuelRamscoopScript());
        Global.getSector().addTransientScript(new MarineTrainingFacilityScript());
        Global.getSector().addTransientScript(new IndustrialMachineForgeScript());
        Global.getSector().addTransientScript(new MineralRefineryScript());
        Global.getSector().addTransientScript(new InvasionPackageScript());

        try {
            JSONObject hullmodData = Global.getSettings().loadJSON(HULLMOD_SETTINGS_FILE);
            JSONObject statsData;

            statsData = hullmodData.getJSONObject("ash_advance_radar_systems");
            AdvanceRadarSystems.WEAPON_RANGE_MULT = getFloat(statsData, "ash_advance_radar_systems_stat1");
            AdvanceRadarSystems.WEAPON_RANGE_MULT = getFloat(statsData, "ash_advance_radar_systems_stat2");

            statsData = hullmodData.getJSONObject("ash_automated_racks");
            AutomatedRacks.MISSILE_FIRE_RATE_MULT = getFloat(statsData, "ash_automated_racks_stat1");
            AutomatedRacks.MISSILE_AMMO_MULT = getFloat(statsData, "ash_automated_racks_stat2");
            AutomatedRacks.MISSILE_TURN_RATE_MULT = getFloat(statsData, "ash_automated_racks_stat3");

            statsData = hullmodData.getJSONObject("ash_beam_combiners");
            BeamCombiners.BEAM_DAMAGE_MULT = getFloat(statsData, "ash_beam_combiners_stat1");
            BeamCombiners.BEAM_FLUX_COST_MULT = getFloat(statsData, "ash_beam_combiners_stat2");

            statsData = hullmodData.getJSONObject("ash_circuit_breakers");
            CircuitBreakers.OVERLOAD_TIME_MULT = getFloat(statsData, "ash_circuit_breakers_stat1");
            CircuitBreakers.EMP_DAMAGE_TAKEN_MULT = getFloat(statsData, "ash_circuit_breakers_stat2");

            statsData = hullmodData.getJSONObject("ash_convicted_crewmates");
            ConvictedCrewmates.FRIGATE_PPT_MULT = getFloat(statsData, "ash_convicted_crewmates_stat1");
            ConvictedCrewmates.DESTROYER_PPT_MULT = getFloat(statsData, "ash_convicted_crewmates_stat2");
            ConvictedCrewmates.MAX_CR_MOD = getFloat(statsData, "ash_convicted_crewmates_stat3");
            ConvictedCrewmates.KILL_TIMER = getFloat(statsData, "ash_convicted_crewmates_stat4");
            ConvictedCrewmates.PPT_GAIN.put(ShipAPI.HullSize.FRIGATE, getFloat(statsData, "ash_convicted_crewmates_stat5", 0));
            ConvictedCrewmates.PPT_GAIN.put(ShipAPI.HullSize.DESTROYER, getFloat(statsData, "ash_convicted_crewmates_stat5", 1));
            ConvictedCrewmates.PPT_GAIN.put(ShipAPI.HullSize.CRUISER, getFloat(statsData, "ash_convicted_crewmates_stat5", 2));
            ConvictedCrewmates.PPT_GAIN.put(ShipAPI.HullSize.CAPITAL_SHIP, getFloat(statsData, "ash_convicted_crewmates_stat5", 3));

            statsData = hullmodData.getJSONObject("ash_external_cargo_holds");
            ExternalCargoHolds.FLUX_DISSIPATION_MULT = getFloat(statsData, "ash_external_cargo_holds_stat1");
            ExternalCargoHolds.SENSOR_PROFILE_MULT = getFloat(statsData, "ash_external_cargo_holds_stat2");
            ExternalCargoHolds.ARMOR_DAMAGE_TAKEN_MULT = getFloat(statsData, "ash_external_cargo_holds_stat3");
            ExternalCargoHolds.CARGO_MOD.put(ShipAPI.HullSize.FRIGATE, getFloat(statsData, "ash_external_cargo_holds_stat4", 0));
            ExternalCargoHolds.CARGO_MOD.put(ShipAPI.HullSize.DESTROYER, getFloat(statsData, "ash_external_cargo_holds_stat4", 1));
            ExternalCargoHolds.CARGO_MOD.put(ShipAPI.HullSize.CRUISER, getFloat(statsData, "ash_external_cargo_holds_stat4", 2));
            ExternalCargoHolds.CARGO_MOD.put(ShipAPI.HullSize.CAPITAL_SHIP, getFloat(statsData, "ash_external_cargo_holds_stat4", 3));

            statsData = hullmodData.getJSONObject("ash_external_cryo_pods");
            ExternalCryoPods.FLUX_DISSIPATION_MULT = getFloat(statsData, "ash_external_cryo_pods_stat1");
            ExternalCryoPods.SENSOR_PROFILE_MULT = getFloat(statsData, "ash_external_cryo_pods_stat2");
            ExternalCryoPods.CREW_LOST_MULT = getFloat(statsData, "ash_external_cryo_pods_stat3");
            ExternalCryoPods.CREW_MOD.put(ShipAPI.HullSize.FRIGATE, getFloat(statsData, "ash_external_cryo_pods_stat4", 0));
            ExternalCryoPods.CREW_MOD.put(ShipAPI.HullSize.DESTROYER, getFloat(statsData, "ash_external_cryo_pods_stat4", 1));
            ExternalCryoPods.CREW_MOD.put(ShipAPI.HullSize.CRUISER, getFloat(statsData, "ash_external_cryo_pods_stat4", 2));
            ExternalCryoPods.CREW_MOD.put(ShipAPI.HullSize.CAPITAL_SHIP, getFloat(statsData, "ash_external_cryo_pods_stat4", 3));

            statsData = hullmodData.getJSONObject("ash_external_fuel_tanks");
            ExternalFuelTanks.FLUX_DISSIPATION_MULT = getFloat(statsData, "ash_external_fuel_tanks_stat1");
            ExternalFuelTanks.SENSOR_PROFILE_MULT = getFloat(statsData, "ash_external_fuel_tanks_stat2");
            ExternalFuelTanks.HIGH_EXPLOSIVE_DAMAGE_TAKEN_MULT = getFloat(statsData, "ash_external_fuel_tanks_stat3");
            ExternalFuelTanks.FUEL_MOD.put(ShipAPI.HullSize.FRIGATE, getFloat(statsData, "ash_external_fuel_tanks_stat4", 0));
            ExternalFuelTanks.FUEL_MOD.put(ShipAPI.HullSize.DESTROYER, getFloat(statsData, "ash_external_fuel_tanks_stat4", 1));
            ExternalFuelTanks.FUEL_MOD.put(ShipAPI.HullSize.CRUISER, getFloat(statsData, "ash_external_fuel_tanks_stat4", 2));
            ExternalFuelTanks.FUEL_MOD.put(ShipAPI.HullSize.CAPITAL_SHIP, getFloat(statsData, "ash_external_fuel_tanks_stat4", 3));

            statsData = hullmodData.getJSONObject("ash_fighter_targeting_uplink");
            FighterTargetingUplink.FIGHTER_DAMAGE_MULT = getFloat(statsData, "ash_fighter_targeting_uplink_stat1");
            FighterTargetingUplink.MIN_EFFECTIVE_RANGE = getFloat(statsData, "ash_fighter_targeting_uplink_stat2");
            FighterTargetingUplink.MAX_EFFECTIVE_RANGE = getFloat(statsData, "ash_fighter_targeting_uplink_stat3");

            statsData = hullmodData.getJSONObject("ash_flux_amplifiers");
            FluxAmplifiers.WEAPON_DAMAGE_MULT = getFloat(statsData, "ash_flux_amplifiers_stat1");
            FluxAmplifiers.WEAPON_FLUX_MULT = getFloat(statsData, "ash_flux_amplifiers_stat2");
            FluxAmplifiers.WEAPON_FIRE_RATE_MULT = getFloat(statsData, "ash_flux_amplifiers_stat3");

            statsData = hullmodData.getJSONObject("ash_flux_limiters");
            FluxLimiters.WEAPON_FLUX_MULT = getFloat(statsData, "ash_flux_limiters_stat1");
            FluxLimiters.WEAPON_DAMAGE_MULT = getFloat(statsData, "ash_flux_limiters_stat2");
            FluxLimiters.WEAPON_FIRE_RATE_MULT = getFloat(statsData, "ash_flux_limiters_stat3");

            statsData = hullmodData.getJSONObject("ash_front_loaded_armor");
            FrontLoadedArmor.POSITIVE_ARMOR_VALUE_MULT = getFloat(statsData, "ash_front_loaded_armor_stat1");
            FrontLoadedArmor.NEGATIVE_ARMOR_VALUE_MULT = getFloat(statsData, "ash_front_loaded_armor_stat2");

            statsData = hullmodData.getJSONObject("ash_fuel_ramscoop");
            FuelRamscoop.DAYS_TO_GENERATE_FUEL = getFloat(statsData, "ash_fuel_ramscoop_stat1");
            FuelRamscoop.FUEL_TO_GENERATE.put(ShipAPI.HullSize.FRIGATE, getFloat(statsData, "ash_fuel_ramscoop_stat2", 0));
            FuelRamscoop.FUEL_TO_GENERATE.put(ShipAPI.HullSize.DESTROYER, getFloat(statsData, "ash_fuel_ramscoop_stat2", 1));
            FuelRamscoop.FUEL_TO_GENERATE.put(ShipAPI.HullSize.CRUISER, getFloat(statsData, "ash_fuel_ramscoop_stat2", 2));
            FuelRamscoop.FUEL_TO_GENERATE.put(ShipAPI.HullSize.CAPITAL_SHIP, getFloat(statsData, "ash_fuel_ramscoop_stat2", 3));
            FuelRamscoop.SMOD_FUEL_TO_GENERATE.put(ShipAPI.HullSize.FRIGATE, getFloat(statsData, "ash_fuel_ramscoop_stat3", 0));
            FuelRamscoop.SMOD_FUEL_TO_GENERATE.put(ShipAPI.HullSize.DESTROYER, getFloat(statsData, "ash_fuel_ramscoop_stat3", 1));
            FuelRamscoop.SMOD_FUEL_TO_GENERATE.put(ShipAPI.HullSize.CRUISER, getFloat(statsData, "ash_fuel_ramscoop_stat3", 2));
            FuelRamscoop.SMOD_FUEL_TO_GENERATE.put(ShipAPI.HullSize.CAPITAL_SHIP, getFloat(statsData, "ash_fuel_ramscoop_stat3", 3));

            statsData = hullmodData.getJSONObject("ash_graviton_attunement_drive");
            GravitonAttunementDrive.SUPPLIES_PER_MONTH_MULT = getFloat(statsData, "ash_graviton_attunement_drive_stat1");
            GravitonAttunementDrive.SENSOR_PROFILE_MULT = getFloat(statsData, "ash_graviton_attunement_drive_stat2");
            GravitonAttunementDrive.FLEET_BURN_MOD.put(ShipAPI.HullSize.FRIGATE, getFloat(statsData, "ash_graviton_attunement_drive_stat3", 0));
            GravitonAttunementDrive.FLEET_BURN_MOD.put(ShipAPI.HullSize.DESTROYER, getFloat(statsData, "ash_graviton_attunement_drive_stat3", 1));
            GravitonAttunementDrive.FLEET_BURN_MOD.put(ShipAPI.HullSize.CRUISER, getFloat(statsData, "ash_graviton_attunement_drive_stat3", 2));
            GravitonAttunementDrive.FLEET_BURN_MOD.put(ShipAPI.HullSize.CAPITAL_SHIP, getFloat(statsData, "ash_graviton_attunement_drive_stat3", 3));

            statsData = hullmodData.getJSONObject("ash_industrial_machine_forge");
            IndustrialMachineForge.DAYS_TO_GENERATE_HEAVY_MACHINERY = getFloat(statsData, "ash_industrial_machine_forge_stat1");
            IndustrialMachineForge.HEAVY_MACHINERY_TO_GENERATE.put(ShipAPI.HullSize.FRIGATE, getInt(statsData, "ash_industrial_machine_forge_stat2", 0));
            IndustrialMachineForge.HEAVY_MACHINERY_TO_GENERATE.put(ShipAPI.HullSize.DESTROYER, getInt(statsData, "ash_industrial_machine_forge_stat2", 1));
            IndustrialMachineForge.HEAVY_MACHINERY_TO_GENERATE.put(ShipAPI.HullSize.CRUISER, getInt(statsData, "ash_industrial_machine_forge_stat2", 2));
            IndustrialMachineForge.HEAVY_MACHINERY_TO_GENERATE.put(ShipAPI.HullSize.CAPITAL_SHIP, getInt(statsData, "ash_industrial_machine_forge_stat2", 3));
            IndustrialMachineForge.METALS_TO_CONSUME.put(ShipAPI.HullSize.FRIGATE, getInt(statsData, "ash_industrial_machine_forge_stat3", 0));
            IndustrialMachineForge.METALS_TO_CONSUME.put(ShipAPI.HullSize.DESTROYER, getInt(statsData, "ash_industrial_machine_forge_stat3", 1));
            IndustrialMachineForge.METALS_TO_CONSUME.put(ShipAPI.HullSize.CRUISER, getInt(statsData, "ash_industrial_machine_forge_stat3", 2));
            IndustrialMachineForge.METALS_TO_CONSUME.put(ShipAPI.HullSize.CAPITAL_SHIP, getInt(statsData, "ash_industrial_machine_forge_stat3", 3));

            statsData = hullmodData.getJSONObject("ash_invasion_package");
            InvasionPackage.PLANETARY_OPERATION_CASUALTIES_MULT.put(ShipAPI.HullSize.FRIGATE, getFloat(statsData, "ash_invasion_package_stat1", 0));
            InvasionPackage.PLANETARY_OPERATION_CASUALTIES_MULT.put(ShipAPI.HullSize.DESTROYER, getFloat(statsData, "ash_invasion_package_stat1", 1));
            InvasionPackage.PLANETARY_OPERATION_CASUALTIES_MULT.put(ShipAPI.HullSize.CRUISER, getFloat(statsData, "ash_invasion_package_stat1", 2));
            InvasionPackage.PLANETARY_OPERATION_CASUALTIES_MULT.put(ShipAPI.HullSize.CAPITAL_SHIP, getFloat(statsData, "ash_invasion_package_stat1", 3));
            InvasionPackage.PLANETARY_OPERATIONS_MULT.put(ShipAPI.HullSize.FRIGATE, getFloat(statsData, "ash_invasion_package_stat2", 0));
            InvasionPackage.PLANETARY_OPERATIONS_MULT.put(ShipAPI.HullSize.DESTROYER, getFloat(statsData, "ash_invasion_package_stat2", 1));
            InvasionPackage.PLANETARY_OPERATIONS_MULT.put(ShipAPI.HullSize.CRUISER, getFloat(statsData, "ash_invasion_package_stat2", 2));
            InvasionPackage.PLANETARY_OPERATIONS_MULT.put(ShipAPI.HullSize.CAPITAL_SHIP, getFloat(statsData, "ash_invasion_package_stat2", 3));
            InvasionPackage.CARGO_CAPACITY_MULT = getFloat(statsData, "ash_invasion_package_stat3");

            statsData = hullmodData.getJSONObject("ash_makeshift_missile_autoforge");
            MakeshiftMissileAutoforge.MISSILE_AMMO_RELOAD_SIZE_MOD = getFloat(statsData, "ash_makeshift_missile_autoforge_stat1");
            MakeshiftMissileAutoforge.MISSILE_AMMO_PER_SECOND_MOD = getFloat(statsData, "ash_makeshift_missile_autoforge_stat2");

            statsData = hullmodData.getJSONObject("ash_marine_training_facility");
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

            statsData = hullmodData.getJSONObject("ash_mineral_refinery");
            MineralRefinery.DAYS_TO_GENERATE_ALLOYS = getInt(statsData, "ash_mineral_refinery_stat1");
            MineralRefinery.ALLOYS_TO_GENERATE.put(ShipAPI.HullSize.FRIGATE, getInt(statsData, "ash_mineral_refinery_stat2", 0));
            MineralRefinery.ALLOYS_TO_GENERATE.put(ShipAPI.HullSize.DESTROYER, getInt(statsData, "ash_mineral_refinery_stat2", 1));
            MineralRefinery.ALLOYS_TO_GENERATE.put(ShipAPI.HullSize.CRUISER, getInt(statsData, "ash_mineral_refinery_stat2", 2));
            MineralRefinery.ALLOYS_TO_GENERATE.put(ShipAPI.HullSize.CAPITAL_SHIP, getInt(statsData, "ash_mineral_refinery_stat2", 3));
            MineralRefinery.MINERALS_TO_CONSUME.put(ShipAPI.HullSize.FRIGATE, getInt(statsData, "ash_mineral_refinery_stat3", 0));
            MineralRefinery.MINERALS_TO_CONSUME.put(ShipAPI.HullSize.DESTROYER, getInt(statsData, "ash_mineral_refinery_stat3", 1));
            MineralRefinery.MINERALS_TO_CONSUME.put(ShipAPI.HullSize.CRUISER, getInt(statsData, "ash_mineral_refinery_stat3", 2));
            MineralRefinery.MINERALS_TO_CONSUME.put(ShipAPI.HullSize.CAPITAL_SHIP, getInt(statsData, "ash_mineral_refinery_stat3", 3));

            statsData = hullmodData.getJSONObject("ash_missile_bay");
            MissileBay.MISSILE_AMMO_MULT = getFloat(statsData, "ash_missile_bay_stat1");
            MissileBay.FIGHTER_BAY_MOD = getFloat(statsData, "ash_missile_bay_stat2");
            MissileBay.DEPLOYMENT_POINTS_MOD.put(ShipAPI.HullSize.FRIGATE, getFloat(statsData, "ash_missile_bay_stat3", 0));
            MissileBay.DEPLOYMENT_POINTS_MOD.put(ShipAPI.HullSize.DESTROYER, getFloat(statsData, "ash_missile_bay_stat3", 1));
            MissileBay.DEPLOYMENT_POINTS_MOD.put(ShipAPI.HullSize.CRUISER, getFloat(statsData, "ash_missile_bay_stat3", 2));
            MissileBay.DEPLOYMENT_POINTS_MOD.put(ShipAPI.HullSize.CAPITAL_SHIP, getFloat(statsData, "ash_missile_bay_stat3", 3));

            statsData = hullmodData.getJSONObject("ash_reactive_subsystems");
            ReactiveSubsystems.MAX_CR_MOD = getFloat(statsData, "ash_reactive_subsystems_stat1");
            ReactiveSubsystems.DEPLOYMENT_POINTS_MOD.put(ShipAPI.HullSize.FRIGATE, getFloat(statsData, "ash_reactive_subsystems_stat2", 0));
            ReactiveSubsystems.DEPLOYMENT_POINTS_MOD.put(ShipAPI.HullSize.DESTROYER, getFloat(statsData, "ash_reactive_subsystems_stat2", 1));
            ReactiveSubsystems.DEPLOYMENT_POINTS_MOD.put(ShipAPI.HullSize.CRUISER, getFloat(statsData, "ash_reactive_subsystems_stat2", 2));
            ReactiveSubsystems.DEPLOYMENT_POINTS_MOD.put(ShipAPI.HullSize.CAPITAL_SHIP, getFloat(statsData, "ash_reactive_subsystems_stat2", 3));

            statsData = hullmodData.getJSONObject("ash_reactor_bay");
            ReactorBay.FLUX_DISSIPATION_MULT = getFloat(statsData, "ash_reactor_bay_stat1");
            ReactorBay.FLUX_CAPACITY_MULT = getFloat(statsData, "ash_reactor_bay_stat2");
            ReactorBay.FIGHTER_BAY_MOD = getFloat(statsData, "ash_reactor_bay_stat3");
            ReactorBay.DEPLOYMENT_POINTS_MOD.put(ShipAPI.HullSize.FRIGATE, getFloat(statsData, "ash_reactor_bay_stat4", 0));
            ReactorBay.DEPLOYMENT_POINTS_MOD.put(ShipAPI.HullSize.DESTROYER, getFloat(statsData, "ash_reactor_bay_stat4", 1));
            ReactorBay.DEPLOYMENT_POINTS_MOD.put(ShipAPI.HullSize.CRUISER, getFloat(statsData, "ash_reactor_bay_stat4", 2));
            ReactorBay.DEPLOYMENT_POINTS_MOD.put(ShipAPI.HullSize.CAPITAL_SHIP, getFloat(statsData, "ash_reactor_bay_stat4", 3));

            statsData = hullmodData.getJSONObject("ash_swift_retreat_protocol");
            SwiftRetreatProtocol.FIGHTER_MOVEMENT_MULT = getFloat(statsData, "ash_swift_retreat_protocol_stat1");
            SwiftRetreatProtocol.FIGHTER_ENGINE_DMG_TAKEN_MULT = getFloat(statsData, "ash_swift_retreat_protocol_stat2");

            statsData = hullmodData.getJSONObject("ash_targeting_transceiver");
            TargetingTransceiver.WEAPON_RANGE_MOD = getFloat(statsData, "ash_targeting_transceiver_stat1");
            TargetingTransceiver.AUTOFIRE_ACCURACY_MOD = getFloat(statsData, "ash_targeting_transceiver_stat2");
            TargetingTransceiver.MIN_EFFECTIVE_RANGE = getFloat(statsData, "ash_targeting_transceiver_stat3");
            TargetingTransceiver.MAX_EFFECTIVE_RANGE = getFloat(statsData, "ash_targeting_transceiver_stat4");

            statsData = hullmodData.getJSONObject("ash_temporal_flux_reactor");
            TemporalFluxReactor.TIME_FLOW_MULT = getFloat(statsData, "ash_temporal_flux_reactor_stat1");

            statsData = hullmodData.getJSONObject("ash_venting_overdrive");
            VentingOverdrive.SHIP_MOVEMENT_MULT = getFloat(statsData, "ash_venting_overdrive_stat1");

            statsData = hullmodData.getJSONObject("ash_volatile_warheads");
            VolatileWarheads.MISSILE_DAMAGE_MULT = getFloat(statsData, "ash_volatile_warheads_stat1");
            VolatileWarheads.MISSILE_SPEED_MULT = getFloat(statsData, "ash_volatile_warheads_stat2");
            VolatileWarheads.MISSILE_HEALTH_MULT = getFloat(statsData, "ash_volatile_warheads_stat3");
        } catch (IOException | JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public int getInt(JSONObject data, String statId) throws JSONException {
        if (isModActive("lunalib")) {
            Integer result = LunaSettings.getInt(modId, statId);
            if (result == null) {
                System.out.println("settings not found");
                return 0;
            }
            return result;
        }
        return data.getInt(statId);
    }

    public int getInt(JSONObject data, String statId, int index) throws JSONException {
        if (isModActive(lunaLibId)) {
            Integer result = LunaSettings.getInt(modId, statId + "_" + index);
            if (result == null) {
                return 0;
            }
            return result;
        }
        return data.getJSONArray(statId).getInt(index);
    }

    public float getFloat(JSONObject data, String statId) throws JSONException {
        if (isModActive(lunaLibId)) {
            Float result = LunaSettings.getFloat(modId, statId);
            if (result == null) {
                return 0;
            }
            return result;
        }
        return (float) data.getDouble(statId);
    }

    public float getFloat(JSONObject data, String statId, int index) throws JSONException {
        if (isModActive(lunaLibId)) {
            Float result = LunaSettings.getFloat(modId, statId + "_" + index);
            if (result == null) {
                return 0;
            }
            return result;
        }
        return (float) data.getJSONArray(statId).getDouble(index);
    }

    public boolean isModActive(String modName) {
        return Global.getSettings().getModManager().isModEnabled(modName);
    }
}
