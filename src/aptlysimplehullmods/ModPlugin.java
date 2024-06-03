package aptlysimplehullmods;

import aptlysimplehullmods.hullmods.*;
import aptlysimplehullmods.plugins.*;
import com.fs.starfarer.api.BaseModPlugin;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.ShipAPI;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class ModPlugin extends BaseModPlugin {
    public static final String HULLMOD_SETTINGS_FILE = "data/config/hullmod_stats.ini";

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

            statsData =  hullmodData.getJSONObject("ash_advance_radar_systems");
            AdvanceRadarSystems.WEAPON_RANGE_MULT = getFloat(statsData, "sight_radius_mult");
            AdvanceRadarSystems.WEAPON_RANGE_MULT = getFloat(statsData, "weapon_range_mult");

            statsData = hullmodData.getJSONObject("ash_automated_racks");
            AutomatedRacks.MISSILE_FIRE_RATE_MULT = getFloat(statsData, "missile_fire_rate_mult");
            AutomatedRacks.MISSILE_AMMO_MULT = getFloat(statsData, "missile_ammo_mult");
            AutomatedRacks.MISSILE_TURN_RATE_MULT = getFloat(statsData, "missile_turn_rate_mult");

            statsData = hullmodData.getJSONObject("ash_beam_combiners");
            BeamCombiners.BEAM_DAMAGE_MULT = getFloat(statsData, "beam_damage_mult");
            BeamCombiners.BEAM_FLUX_COST_MULT = getFloat(statsData, "beam_flux_cost_mult");

            statsData = hullmodData.getJSONObject("ash_circuit_breakers");
            CircuitBreakers.OVERLOAD_TIME_MULT = getFloat(statsData, "overload_time_mult");
            CircuitBreakers.EMP_DAMAGE_TAKEN_MULT = getFloat(statsData, "emp_damage_taken_mult");

            statsData = hullmodData.getJSONObject("ash_convicted_crewmates");
            ConvictedCrewmates.FRIGATE_PPT_MULT = getFloat(statsData,"frigate_ppt_mult");
            ConvictedCrewmates.DESTROYER_PPT_MULT = getFloat(statsData,"destroyer_ppt_mult");
            ConvictedCrewmates.MAX_CR_MOD = getFloat(statsData,"max_cr_mod");
            ConvictedCrewmates.KILL_TIMER = getFloat(statsData,"kill_timer");
            ConvictedCrewmates.PPT_GAIN.put(ShipAPI.HullSize.FRIGATE, getFloat(statsData, "ppt_gain" , 0));
            ConvictedCrewmates.PPT_GAIN.put(ShipAPI.HullSize.DESTROYER, getFloat(statsData, "ppt_gain", 1));
            ConvictedCrewmates.PPT_GAIN.put(ShipAPI.HullSize.CRUISER, getFloat(statsData, "ppt_gain", 2));
            ConvictedCrewmates.PPT_GAIN.put(ShipAPI.HullSize.CAPITAL_SHIP, getFloat(statsData, "ppt_gain", 3));

            statsData = hullmodData.getJSONObject("ash_external_cargo_holds");
            ExternalCargoHolds.FLUX_DISSIPATION_MULT = getFloat(statsData, "flux_dissipation_mult");
            ExternalCargoHolds.SENSOR_PROFILE_MULT = getFloat(statsData, "sensor_profile_mult");
            ExternalCargoHolds.ARMOR_DAMAGE_TAKEN_MULT = getFloat(statsData, "armor_damage_taken_mult");
            ExternalCargoHolds.CARGO_MOD.put(ShipAPI.HullSize.FRIGATE, getFloat(statsData, "cargo_mod" , 0));
            ExternalCargoHolds.CARGO_MOD.put(ShipAPI.HullSize.DESTROYER, getFloat(statsData, "cargo_mod", 1));
            ExternalCargoHolds.CARGO_MOD.put(ShipAPI.HullSize.CRUISER, getFloat(statsData, "cargo_mod", 2));
            ExternalCargoHolds.CARGO_MOD.put(ShipAPI.HullSize.CAPITAL_SHIP, getFloat(statsData, "cargo_mod", 3));

            statsData = hullmodData.getJSONObject("ash_external_cryo_pods");
            ExternalCryoPods.FLUX_DISSIPATION_MULT = getFloat(statsData, "flux_dissipation_mult");
            ExternalCryoPods.SENSOR_PROFILE_MULT = getFloat(statsData, "sensor_profile_mult");
            ExternalCryoPods.CREW_LOST_MULT = getFloat(statsData, "crew_lost_mult");
            ExternalCryoPods.CREW_MOD.put(ShipAPI.HullSize.FRIGATE, getFloat(statsData, "crew_mod" , 0));
            ExternalCryoPods.CREW_MOD.put(ShipAPI.HullSize.DESTROYER, getFloat(statsData, "crew_mod", 1));
            ExternalCryoPods.CREW_MOD.put(ShipAPI.HullSize.CRUISER, getFloat(statsData, "crew_mod", 2));
            ExternalCryoPods.CREW_MOD.put(ShipAPI.HullSize.CAPITAL_SHIP, getFloat(statsData, "crew_mod", 3));

            statsData = hullmodData.getJSONObject("ash_external_fuel_tanks");
            ExternalFuelTanks.FLUX_DISSIPATION_MULT = getFloat(statsData, "flux_dissipation_mult");
            ExternalFuelTanks.SENSOR_PROFILE_MULT = getFloat(statsData, "sensor_profile_mult");
            ExternalFuelTanks.HIGH_EXPLOSIVE_DAMAGE_TAKEN_MULT = getFloat(statsData, "high_explosive_damage_taken_mult");
            ExternalFuelTanks.FUEL_MOD.put(ShipAPI.HullSize.FRIGATE, getFloat(statsData, "fuel_mod" , 0));
            ExternalFuelTanks.FUEL_MOD.put(ShipAPI.HullSize.DESTROYER, getFloat(statsData, "fuel_mod", 1));
            ExternalFuelTanks.FUEL_MOD.put(ShipAPI.HullSize.CRUISER, getFloat(statsData, "fuel_mod", 2));
            ExternalFuelTanks.FUEL_MOD.put(ShipAPI.HullSize.CAPITAL_SHIP, getFloat(statsData, "fuel_mod", 3));

            statsData = hullmodData.getJSONObject("ash_fighter_targeting_uplink");
            FighterTargetingUplink.FIGHTER_DAMAGE_MULT = getFloat(statsData, "fighter_damage_mult");
            FighterTargetingUplink.MIN_EFFECTIVE_RANGE = getFloat(statsData, "min_effective_range");
            FighterTargetingUplink.MAX_EFFECTIVE_RANGE = getFloat(statsData, "max_effective_range");

            statsData = hullmodData.getJSONObject("ash_flux_amplifiers");
            FluxAmplifiers.WEAPON_DAMAGE_MULT = getFloat(statsData, "weapon_damage_mult");
            FluxAmplifiers.WEAPON_FLUX_MULT = getFloat(statsData, "weapon_flux_mult");
            FluxAmplifiers.WEAPON_FIRE_RATE_MULT = getFloat(statsData, "weapon_fire_rate_mult");

            statsData = hullmodData.getJSONObject("ash_flux_limiters");
            FluxLimiters.WEAPON_FLUX_MULT = getFloat(statsData, "weapon_flux_mult");
            FluxLimiters.WEAPON_DAMAGE_MULT = getFloat(statsData, "weapon_damage_mult");
            FluxLimiters.WEAPON_FIRE_RATE_MULT = getFloat(statsData, "weapon_fire_rate_mult");

            statsData = hullmodData.getJSONObject("ash_front_loaded_armor");
            FrontLoadedArmor.POSITIVE_ARMOR_VALUE_MULT = getFloat(statsData, "positive_armor_value_mult");
            FrontLoadedArmor.NEGATIVE_ARMOR_VALUE_MULT = getFloat(statsData, "negative_armor_value_mult");

            statsData = hullmodData.getJSONObject("ash_fuel_ramscoop");
            FuelRamscoop.DAYS_TO_GENERATE_FUEL = getFloat(statsData, "days_to_generate_fuel");
            FuelRamscoop.FUEL_TO_GENERATE.put(ShipAPI.HullSize.FRIGATE, getFloat(statsData, "fuel_to_generate" , 0));
            FuelRamscoop.FUEL_TO_GENERATE.put(ShipAPI.HullSize.DESTROYER, getFloat(statsData, "fuel_to_generate", 1));
            FuelRamscoop.FUEL_TO_GENERATE.put(ShipAPI.HullSize.CRUISER, getFloat(statsData, "fuel_to_generate", 2));
            FuelRamscoop.FUEL_TO_GENERATE.put(ShipAPI.HullSize.CAPITAL_SHIP, getFloat(statsData, "fuel_to_generate", 3));
            FuelRamscoop.SMOD_FUEL_TO_GENERATE.put(ShipAPI.HullSize.FRIGATE, getFloat(statsData, "smod_fuel_to_generate" , 0));
            FuelRamscoop.SMOD_FUEL_TO_GENERATE.put(ShipAPI.HullSize.DESTROYER, getFloat(statsData, "smod_fuel_to_generate", 1));
            FuelRamscoop.SMOD_FUEL_TO_GENERATE.put(ShipAPI.HullSize.CRUISER, getFloat(statsData, "smod_fuel_to_generate", 2));
            FuelRamscoop.SMOD_FUEL_TO_GENERATE.put(ShipAPI.HullSize.CAPITAL_SHIP, getFloat(statsData, "smod_fuel_to_generate", 3));

            statsData = hullmodData.getJSONObject("ash_graviton_attunement_drive");
            GravitonAttunementDrive.SUPPLIES_PER_MONTH_MULT = getFloat(statsData, "supplies_per_month_mult");
            GravitonAttunementDrive.SENSOR_PROFILE_MULT = getFloat(statsData, "sensor_profile_mult");
            GravitonAttunementDrive.FLEET_BURN_MOD.put(ShipAPI.HullSize.FRIGATE, getFloat(statsData, "fleet_burn_mod" , 0));
            GravitonAttunementDrive.FLEET_BURN_MOD.put(ShipAPI.HullSize.DESTROYER, getFloat(statsData, "fleet_burn_mod", 1));
            GravitonAttunementDrive.FLEET_BURN_MOD.put(ShipAPI.HullSize.CRUISER, getFloat(statsData, "fleet_burn_mod", 2));
            GravitonAttunementDrive.FLEET_BURN_MOD.put(ShipAPI.HullSize.CAPITAL_SHIP, getFloat(statsData, "fleet_burn_mod", 3));

            statsData = hullmodData.getJSONObject("ash_industrial_machine_forge");
            IndustrialMachineForge.DAYS_TO_GENERATE_HEAVY_MACHINERY = getFloat(statsData, "days_to_generate_heavy_machinery");
            IndustrialMachineForge.HEAVY_MACHINERY_TO_GENERATE.put(ShipAPI.HullSize.FRIGATE, getInt(statsData, "heavy_machinery_to_generate" , 0));
            IndustrialMachineForge.HEAVY_MACHINERY_TO_GENERATE.put(ShipAPI.HullSize.DESTROYER, getInt(statsData, "heavy_machinery_to_generate", 1));
            IndustrialMachineForge.HEAVY_MACHINERY_TO_GENERATE.put(ShipAPI.HullSize.CRUISER, getInt(statsData, "heavy_machinery_to_generate", 2));
            IndustrialMachineForge.HEAVY_MACHINERY_TO_GENERATE.put(ShipAPI.HullSize.CAPITAL_SHIP, getInt(statsData, "heavy_machinery_to_generate", 3));
            IndustrialMachineForge.METALS_TO_CONSUME.put(ShipAPI.HullSize.FRIGATE, getInt(statsData, "metals_to_consume" , 0));
            IndustrialMachineForge.METALS_TO_CONSUME.put(ShipAPI.HullSize.DESTROYER, getInt(statsData, "metals_to_consume", 1));
            IndustrialMachineForge.METALS_TO_CONSUME.put(ShipAPI.HullSize.CRUISER, getInt(statsData, "metals_to_consume", 2));
            IndustrialMachineForge.METALS_TO_CONSUME.put(ShipAPI.HullSize.CAPITAL_SHIP, getInt(statsData, "metals_to_consume", 3));

            statsData = hullmodData.getJSONObject("ash_invasion_package");
            InvasionPackage.PLANETARY_OPERATION_CASUALTIES_MULT.put(ShipAPI.HullSize.FRIGATE, getFloat(statsData, "casualties_mult" , 0));
            InvasionPackage.PLANETARY_OPERATION_CASUALTIES_MULT.put(ShipAPI.HullSize.DESTROYER, getFloat(statsData, "casualties_mult", 1));
            InvasionPackage.PLANETARY_OPERATION_CASUALTIES_MULT.put(ShipAPI.HullSize.CRUISER, getFloat(statsData, "casualties_mult", 2));
            InvasionPackage.PLANETARY_OPERATION_CASUALTIES_MULT.put(ShipAPI.HullSize.CAPITAL_SHIP, getFloat(statsData, "casualties_mult", 3));
            InvasionPackage.PLANETARY_OPERATIONS_MULT.put(ShipAPI.HullSize.FRIGATE, getFloat(statsData, "operations_mult" , 0));
            InvasionPackage.PLANETARY_OPERATIONS_MULT.put(ShipAPI.HullSize.DESTROYER, getFloat(statsData, "operations_mult", 1));
            InvasionPackage.PLANETARY_OPERATIONS_MULT.put(ShipAPI.HullSize.CRUISER, getFloat(statsData, "operations_mult", 2));
            InvasionPackage.PLANETARY_OPERATIONS_MULT.put(ShipAPI.HullSize.CAPITAL_SHIP, getFloat(statsData, "operations_mult", 3));
            InvasionPackage.CARGO_CAPACITY_MULT = getFloat(statsData, "cargo_capacity_mult");

            statsData = hullmodData.getJSONObject("ash_makeshift_missile_autoforge");
            MakeshiftMissileAutoforge.MISSILE_AMMO_RELOAD_SIZE_MOD = getFloat(statsData, "missile_ammo_reload_size_mod");
            MakeshiftMissileAutoforge.MISSILE_AMMO_PER_SECOND_MOD = getFloat(statsData, "missile_ammo_per_second_mod");

            statsData = hullmodData.getJSONObject("ash_marine_training_facility");
            MarineTrainingFacility.DAYS_TO_GENERATE_MARINES = getInt(statsData, "days_to_generate_marines");
            MarineTrainingFacility.MARINES_TO_GENERATE.put(ShipAPI.HullSize.FRIGATE, getInt(statsData, "marines_to_generate" , 0));
            MarineTrainingFacility.MARINES_TO_GENERATE.put(ShipAPI.HullSize.DESTROYER, getInt(statsData, "marines_to_generate", 1));
            MarineTrainingFacility.MARINES_TO_GENERATE.put(ShipAPI.HullSize.CRUISER, getInt(statsData, "marines_to_generate", 2));
            MarineTrainingFacility.MARINES_TO_GENERATE.put(ShipAPI.HullSize.CAPITAL_SHIP, getInt(statsData, "marines_to_generate", 3));
            MarineTrainingFacility.MARINES_TO_LEVEL.put(ShipAPI.HullSize.FRIGATE, getInt(statsData, "marines_to_level" , 0));
            MarineTrainingFacility.MARINES_TO_LEVEL.put(ShipAPI.HullSize.DESTROYER, getInt(statsData, "marines_to_level", 1));
            MarineTrainingFacility.MARINES_TO_LEVEL.put(ShipAPI.HullSize.CRUISER, getInt(statsData, "marines_to_level", 2));
            MarineTrainingFacility.MARINES_TO_LEVEL.put(ShipAPI.HullSize.CAPITAL_SHIP, getInt(statsData, "marines_to_level", 3));
            MarineTrainingFacility.SMOD_MARINES_TO_LEVEL.put(ShipAPI.HullSize.FRIGATE, getInt(statsData, "smod_marines_to_level" , 0));
            MarineTrainingFacility.SMOD_MARINES_TO_LEVEL.put(ShipAPI.HullSize.DESTROYER, getInt(statsData, "smod_marines_to_level", 1));
            MarineTrainingFacility.SMOD_MARINES_TO_LEVEL.put(ShipAPI.HullSize.CRUISER, getInt(statsData, "smod_marines_to_level", 2));
            MarineTrainingFacility.SMOD_MARINES_TO_LEVEL.put(ShipAPI.HullSize.CAPITAL_SHIP, getInt(statsData, "smod_marines_to_level", 3));
            MarineTrainingFacility.MAX_MARINES_TO_GENERATE.put(ShipAPI.HullSize.FRIGATE, getInt(statsData, "max_marines_to_generate" , 0));
            MarineTrainingFacility.MAX_MARINES_TO_GENERATE.put(ShipAPI.HullSize.DESTROYER, getInt(statsData, "max_marines_to_generate", 1));
            MarineTrainingFacility.MAX_MARINES_TO_GENERATE.put(ShipAPI.HullSize.CRUISER, getInt(statsData, "max_marines_to_generate", 2));
            MarineTrainingFacility.MAX_MARINES_TO_GENERATE.put(ShipAPI.HullSize.CAPITAL_SHIP, getInt(statsData, "max_marines_to_generate", 3));

            statsData = hullmodData.getJSONObject("ash_mineral_refinery");
            MineralRefinery.DAYS_TO_GENERATE_ALLOYS = getInt(statsData, "days_to_generate_alloys");
            MineralRefinery.ALLOYS_TO_GENERATE.put(ShipAPI.HullSize.FRIGATE, getInt(statsData, "alloys_to_generate" , 0));
            MineralRefinery.ALLOYS_TO_GENERATE.put(ShipAPI.HullSize.DESTROYER, getInt(statsData, "alloys_to_generate", 1));
            MineralRefinery.ALLOYS_TO_GENERATE.put(ShipAPI.HullSize.CRUISER, getInt(statsData, "alloys_to_generate", 2));
            MineralRefinery.ALLOYS_TO_GENERATE.put(ShipAPI.HullSize.CAPITAL_SHIP, getInt(statsData, "alloys_to_generate", 3));
            MineralRefinery.MINERALS_TO_CONSUME.put(ShipAPI.HullSize.FRIGATE, getInt(statsData, "minerals_to_consume" , 0));
            MineralRefinery.MINERALS_TO_CONSUME.put(ShipAPI.HullSize.DESTROYER, getInt(statsData, "minerals_to_consume", 1));
            MineralRefinery.MINERALS_TO_CONSUME.put(ShipAPI.HullSize.CRUISER, getInt(statsData, "minerals_to_consume", 2));
            MineralRefinery.MINERALS_TO_CONSUME.put(ShipAPI.HullSize.CAPITAL_SHIP, getInt(statsData, "minerals_to_consume", 3));

            statsData = hullmodData.getJSONObject("ash_missile_bay");
            MissileBay.MISSILE_AMMO_MULT = getFloat(statsData, "missile_ammo_mult");
            MissileBay.FIGHTER_BAY_MOD = getFloat(statsData, "fighter_bay_mod");
            MissileBay.DEPLOYMENT_POINTS_MOD.put(ShipAPI.HullSize.FRIGATE, getFloat(statsData, "deployment_points_mod" , 0));
            MissileBay.DEPLOYMENT_POINTS_MOD.put(ShipAPI.HullSize.DESTROYER, getFloat(statsData, "deployment_points_mod", 1));
            MissileBay.DEPLOYMENT_POINTS_MOD.put(ShipAPI.HullSize.CRUISER, getFloat(statsData, "deployment_points_mod", 2));
            MissileBay.DEPLOYMENT_POINTS_MOD.put(ShipAPI.HullSize.CAPITAL_SHIP, getFloat(statsData, "deployment_points_mod", 3));

            statsData = hullmodData.getJSONObject("ash_reactive_subsystems");
            ReactiveSubsystems.MAX_CR_MOD = getFloat(statsData, "max_cr_mod");
            ReactiveSubsystems.DEPLOYMENT_POINTS_MOD.put(ShipAPI.HullSize.FRIGATE, getFloat(statsData, "deployment_points_mod" , 0));
            ReactiveSubsystems.DEPLOYMENT_POINTS_MOD.put(ShipAPI.HullSize.DESTROYER, getFloat(statsData, "deployment_points_mod", 1));
            ReactiveSubsystems.DEPLOYMENT_POINTS_MOD.put(ShipAPI.HullSize.CRUISER, getFloat(statsData, "deployment_points_mod", 2));
            ReactiveSubsystems.DEPLOYMENT_POINTS_MOD.put(ShipAPI.HullSize.CAPITAL_SHIP, getFloat(statsData, "deployment_points_mod", 3));

            statsData = hullmodData.getJSONObject("ash_reactor_bay");
            ReactorBay.FLUX_DISSIPATION_MULT = getFloat(statsData, "flux_dissipation_mult");
            ReactorBay.FLUX_CAPACITY_MULT = getFloat(statsData, "flux_capacity_mult");
            ReactorBay.FIGHTER_BAY_MOD = getFloat(statsData, "fighter_bay_mod");
            ReactorBay.DEPLOYMENT_POINTS_MOD.put(ShipAPI.HullSize.FRIGATE, getFloat(statsData, "deployment_points_mod" , 0));
            ReactorBay.DEPLOYMENT_POINTS_MOD.put(ShipAPI.HullSize.DESTROYER, getFloat(statsData, "deployment_points_mod", 1));
            ReactorBay.DEPLOYMENT_POINTS_MOD.put(ShipAPI.HullSize.CRUISER, getFloat(statsData, "deployment_points_mod", 2));
            ReactorBay.DEPLOYMENT_POINTS_MOD.put(ShipAPI.HullSize.CAPITAL_SHIP, getFloat(statsData, "deployment_points_mod", 3));

            statsData = hullmodData.getJSONObject("ash_swift_retreat_protocol");
            SwiftRetreatProtocol.FIGHTER_MOVEMENT_MULT = getFloat(statsData, "fighter_movement_mult");
            SwiftRetreatProtocol.FIGHTER_ENGINE_DMG_TAKEN_MULT = getFloat(statsData, "fighter_engine_damage_taken_mult");

            statsData = hullmodData.getJSONObject("ash_targeting_transceiver");
            TargetingTransceiver.WEAPON_RANGE_MOD = getFloat(statsData, "weapon_range_mod");
            TargetingTransceiver.AUTOFIRE_ACCURACY_MOD = getFloat(statsData, "autofire_accuracy_mod");
            TargetingTransceiver.MIN_EFFECTIVE_RANGE = getFloat(statsData, "min_effective_range");
            TargetingTransceiver.MAX_EFFECTIVE_RANGE = getFloat(statsData, "max_effective_range");

            statsData = hullmodData.getJSONObject("ash_temporal_flux_reactor");
            TemporalFluxReactor.TIME_FLOW_MULT = getFloat(statsData, "time_flow_mult");

            statsData = hullmodData.getJSONObject("ash_venting_overdrive");
            VentingOverdrive.SHIP_MOVEMENT_MULT = getFloat(statsData, "ship_movement_mult");

            statsData = hullmodData.getJSONObject("ash_volatile_warheads");
            VolatileWarheads.MISSILE_DAMAGE_MULT = getFloat(statsData, "missile_damage_mult");
            VolatileWarheads.MISSILE_SPEED_MULT = getFloat(statsData, "missile_speed_mult");
            VolatileWarheads.MISSILE_HEALTH_MULT = getFloat(statsData, "missile_health_mult");
        } catch (IOException | JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public int getInt(JSONObject data, String statId) throws JSONException {
        return data.getInt(statId);
    }

    public int getInt(JSONObject data, String statId, int index) throws JSONException {
        return data.getJSONArray(statId).getInt(index);
    }

    public float getFloat(JSONObject data, String statId) throws JSONException {
        return (float) data.getDouble(statId);
    }

    public float getFloat(JSONObject data, String statId, int index) throws JSONException {
        return (float) data.getJSONArray(statId).getDouble(index);
    }
}
