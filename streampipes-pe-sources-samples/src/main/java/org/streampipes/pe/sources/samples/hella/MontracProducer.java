package org.streampipes.pe.sources.samples.hella;

import java.util.ArrayList;
import java.util.List;

import org.streampipes.container.declarer.EventStreamDeclarer;
import org.streampipes.container.declarer.SemanticEventProducerDeclarer;
import org.streampipes.model.graph.DataSourceDescription;

public class MontracProducer implements SemanticEventProducerDeclarer {

	@Override
	public DataSourceDescription declareModel() {
		
		DataSourceDescription sep = new DataSourceDescription("source-montrac", "Montrac (Replay)", "Provides streams generated by the Montrac transportation system");
		
		return sep;
	}

	
	@Override
	public List<EventStreamDeclarer> getEventStreams() {
	
		List<EventStreamDeclarer> streams = new ArrayList<EventStreamDeclarer>();
		
		streams.add(new MaterialMovementStream());
		
		return streams;
	}
}
