package de.fzi.cep.sepa.units;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.github.jqudt.Unit;
import com.github.jqudt.onto.UnitFactory;

public class UnitCollector {

	private static final String namespace = "http://qudt.org/schema/qudt#";
	
	private static final String[] unitTypeClasses = new String[] {
		"DerivedUnit",
		"unit",
		"DimensionlessUnit",
		"ScienceAndEngineeringUnit",
		"ResourceUnit",
		"BaseUnit",
		"SIBaseUnit",
		"DerivedUnit",
		"PhysicalUnit",
		"ComputingUnit",
		"CommunicationsUnit",
		"BiomedicalUnit",
		"RadiologyUnit",
		"NonSIUnit",
		"ChemistryUnit",
		"LogarithmicUnit",
		"SIUnit",
		"AtomicPhysicsUnit",
		"MechanicsUnit",
		"SpaceAndTimeUnit",
		"ElectricityAndMagnetismUnit",
		"ThermodynamicsUnit",
		"PerMeter",
		"SquareMeterKelvin",
		"PerCubicMeter",
		"CurvatureUnit",
		"LengthUnit",
		"VolumeUnit",
		"AngleUnit",
		"AreaAngleUnit",
		"TimeUnit",
		"AccelerationUnit",
		"TimeSquaredUnit",
		"FrequencyUnit",
		"VelocityUnit",
		"TimeAreaUnit",
		"AreaUnit",
		"VolumePerTimeUnit",
		"AreaTimeTemperatureUnit",
		"SpecificHeatVolumeUnit",
		"ThermalEnergyUnit",
		"TemperaturePerTimeUnit",
		"MolarHeatCapacityUnit",
		"LengthTemperatureTimeUnit",
		"ThermalEnergyLengthUnit",
		"ThermalResistivityUnit",
		"TemperatureUnit",
		"ThermalDiffusivityUnit",
		"ThermalConductivityUnit",
		"MassTemperatureUnit",
		"ThermalExpansionUnit",
		"CoefficientOfHeatTransferUnit",
		"SpecificHeatPressureUnit",
		"HeatFlowRateUnit",
		"ThermalResistanceUnit",
		"HeatCapacityAndEntropyUnit",
		"ThermalInsulanceUnit",
		"LengthTemperatureUnit",
		"AreaTemperatureUnit",
		"SpecificHeatCapacityUnit",
		"LinearVelocityUnit",
		"AngularVelocityUnit"
	};
	
	private Set<Unit> availableUnits = new HashSet<>();
	private Set<Unit> availableUnitTypes = new HashSet<>();
	
	public UnitCollector() {
		collect();
	}
	
	private void collect() {
		for(String newUri : unitTypeClasses) {
			try {
				Unit unitType = UnitFactory.getInstance().getUnit(namespace +newUri);
				if (unitType != null) availableUnitTypes.add(unitType);
				List<String> uris = UnitFactory.getInstance().getURIs(namespace +newUri);
				for(String uri : uris) {
					Unit unit = UnitFactory.getInstance().getUnit(uri);
					if (unit != null) availableUnits.add(unit);
				}
			} catch (Exception e) {
				System.out.println("No entry found");
			}
		}
	}

	public Set<Unit> getAvailableUnits() {
		return availableUnits;
	}	
	
	public Set<Unit> getAvailableUnitTypes() {
		return availableUnitTypes;
	}
	
	
	
}
