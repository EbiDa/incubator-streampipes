package org.streampipes.pe.processors.esper.aggregate.avg;

import java.util.List;

import org.streampipes.model.graph.DataProcessorInvocation;
import org.streampipes.wrapper.params.binding.EventProcessorBindingParams;

public class AggregationParameter extends EventProcessorBindingParams {

	private AggregationType aggregationType;
	private int outputEvery;
	private int timeWindowSize;
	private String aggregate;
	private List<String> groupBy;
	private List<String> selectProperties;
	
	public AggregationParameter(DataProcessorInvocation graph, AggregationType aggregationType, int outputEvery, List<String> groupBy, String aggregate, int timeWindowSize, List<String> selectProperties) {
		super(graph);
		this.aggregationType = aggregationType;
		this.outputEvery = outputEvery;
		this.groupBy = groupBy;
		this.timeWindowSize = timeWindowSize;
		this.aggregate = aggregate;
		this.selectProperties = selectProperties;
	}

	public AggregationType getAggregationType() {
		return aggregationType;
	}

	public void setAggregationType(AggregationType aggregationType) {
		this.aggregationType = aggregationType;
	}

	public int getOutputEvery() {
		return outputEvery;
	}

	public void setOutputEvery(int outputEvery) {
		this.outputEvery = outputEvery;
	}

	public List<String> getGroupBy() {
		return groupBy;
	}

	public void setGroupBy(List<String> groupBy) {
		this.groupBy = groupBy;
	}

	public int getTimeWindowSize() {
		return timeWindowSize;
	}

	public void setTimeWindowSize(int timeWindowSize) {
		this.timeWindowSize = timeWindowSize;
	}

	public String getAggregate() {
		return aggregate;
	}

	public void setAggregate(String aggregate) {
		this.aggregate = aggregate;
	}

	public List<String> getSelectProperties() {
		return selectProperties;
	}

	public void setSelectProperties(List<String> selectProperties) {
		this.selectProperties = selectProperties;
	}	

}
