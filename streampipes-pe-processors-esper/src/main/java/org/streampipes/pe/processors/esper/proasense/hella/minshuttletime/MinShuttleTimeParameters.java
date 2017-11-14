package org.streampipes.pe.processors.esper.proasense.hella.minshuttletime;

import org.streampipes.model.graph.DataProcessorInvocation;
import org.streampipes.pe.processors.esper.proasense.hella.shuttletime.MouldingMachine;
import org.streampipes.wrapper.params.binding.EventProcessorBindingParams;

import java.util.ArrayList;
import java.util.List;

public class MinShuttleTimeParameters extends EventProcessorBindingParams {

private static final long serialVersionUID = 4319341875274736697L;
	
	private List<String> selectProperties = new ArrayList<>();
	
	private List<MouldingMachine> mouldingMachines = new ArrayList<>();
	
	private String shuttleIdEventName;
	private String lacqueringLineIdEventName;
	private String mouldingMachineIdEventName;
	private String timestampEventName;
	
	public MinShuttleTimeParameters(DataProcessorInvocation graph, List<String> selectProperties, String lacqueringLineIdEventName, String mouldingMachineIdEventName, String shuttleIdEventName, String timestampEventName) {
		super(graph);
		this.selectProperties = selectProperties;
		
		mouldingMachines.add(new MouldingMachine("IMM1", "Arrive", "IMM 1"));
		mouldingMachines.add(new MouldingMachine("IMM2", "Arrive", "IMM 1"));
		mouldingMachines.add(new MouldingMachine("IMM 3", "Arrive", "IMM1"));
		mouldingMachines.add(new MouldingMachine("IMM 4", "Arrive", "IMM1"));
		mouldingMachines.add(new MouldingMachine("IMM 5", "Arrive",  "IMM1"));
		
		this.shuttleIdEventName = shuttleIdEventName;
		this.lacqueringLineIdEventName = lacqueringLineIdEventName;
		this.mouldingMachineIdEventName = mouldingMachineIdEventName;
		this.timestampEventName = timestampEventName;
	}

	public List<String> getSelectProperties() {
		return selectProperties;
	}

	public List<MouldingMachine> getMouldingMachines()
	{
		return mouldingMachines;
	}

	public String getShuttleIdEventName() {
		return shuttleIdEventName;
	}

	public String getLacqueringLineIdEventName() {
		return lacqueringLineIdEventName;
	}

	public String getMouldingMachineIdEventName() {
		return mouldingMachineIdEventName;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getTimestampEventName() {
		return timestampEventName;
	}
	
	
	
}
