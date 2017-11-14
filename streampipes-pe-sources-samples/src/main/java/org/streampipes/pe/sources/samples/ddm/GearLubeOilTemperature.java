package org.streampipes.pe.sources.samples.ddm;

import java.util.ArrayList;
import java.util.List;

import org.streampipes.pe.sources.samples.util.Utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.streampipes.container.declarer.EventStreamDeclarer;
import org.streampipes.model.grounding.EventGrounding;
import org.streampipes.model.schema.EventProperty;
import org.streampipes.model.schema.EventPropertyPrimitive;
import org.streampipes.model.schema.EventSchema;
import org.streampipes.model.SpDataStream;
import org.streampipes.model.grounding.TransportFormat;
import org.streampipes.model.graph.DataSourceDescription;
import org.streampipes.vocabulary.MessageFormat;
import org.streampipes.vocabulary.XSD;
import org.streampipes.pe.sources.samples.config.AkerVariables;
import org.streampipes.pe.sources.samples.config.ProaSenseSettings;
import org.streampipes.pe.sources.samples.config.SourcesConfig;

public class GearLubeOilTemperature implements EventStreamDeclarer {

	private static final Logger logger = LoggerFactory
			.getLogger("GearLubeOilTemperature");
	
	private String topicName;
	
	@Override
	public SpDataStream declareModel(DataSourceDescription sep) {
		
		SpDataStream stream = new SpDataStream();
		
		EventSchema schema = new EventSchema();
		List<EventProperty> eventProperties = new ArrayList<EventProperty>();
		eventProperties.add(new EventPropertyPrimitive(XSD._long.toString(), "variable_type", "", org.streampipes.commons.Utils.createURI("http://schema.org/Text")));
		eventProperties.add(new EventPropertyPrimitive(XSD._string.toString(), "variable_timestamp", "", org.streampipes.commons.Utils.createURI("http://schema.org/DateTime")));
		eventProperties.add(new EventPropertyPrimitive(XSD._double.toString(), "value", "", org.streampipes.commons.Utils.createURI("http://schema.org/Number")));
		
		
		EventGrounding grounding = new EventGrounding();
		grounding.setTransportProtocol(ProaSenseSettings.standardProtocol(AkerVariables.GearLubeOilTemperature.topic()));
		grounding.setTransportFormats(org.streampipes.commons.Utils.createList(new TransportFormat(MessageFormat.Json)));
		
		this.topicName = grounding.getTransportProtocol().getTopicName();

		stream.setEventGrounding(grounding);
		schema.setEventProperties(eventProperties);
		stream.setEventSchema(schema);
		stream.setName(AkerVariables.GearLubeOilTemperature.eventName());
		stream.setDescription(AkerVariables.GearLubeOilTemperature.description());
		stream.setUri(sep.getUri() + "/gearLubeTemp");
		stream.setIconUrl(SourcesConfig.iconBaseUrl + "/Temperature_Icon" +"_HQ.png");
		
		return stream;
	}

	@Override
	public void executeStream() {
        long[] variables = {AkerVariables.GearLubeOilTemperature.tagNumber()};
        String cont = Utils.performRequest(variables, topicName, "121213123", "212342134");
        logger.info(cont);
	}

	@Override
	public boolean isExecutable() {
		return true;
	}
}
