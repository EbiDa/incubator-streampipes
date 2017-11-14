package org.streampipes.pe.sinks.standalone.samples.kafka;

import org.streampipes.model.graph.DataSinkDescription;
import org.streampipes.model.graph.DataSinkInvocation;
import org.streampipes.pe.sinks.standalone.config.ActionConfig;
import org.streampipes.sdk.builder.DataSinkBuilder;
import org.streampipes.sdk.extractor.DataSinkParameterExtractor;
import org.streampipes.sdk.helpers.EpRequirements;
import org.streampipes.sdk.helpers.Labels;
import org.streampipes.sdk.helpers.OntologyProperties;
import org.streampipes.sdk.helpers.SupportedFormats;
import org.streampipes.sdk.helpers.SupportedProtocols;
import org.streampipes.wrapper.ConfiguredEventSink;
import org.streampipes.wrapper.runtime.EventSink;
import org.streampipes.wrapper.standalone.declarer.StandaloneEventSinkDeclarer;

public class KafkaController extends StandaloneEventSinkDeclarer<KafkaParameters> {

	private static final String KAFKA_BROKER_SETTINGS_KEY = "broker-settings";
	private static final String TOPIC_KEY = "topic";

	private static final String KAFKA_HOST_URI = "http://schema.org/kafkaHost";
	private static final String KAFKA_PORT_URI = "http://schema.org/kafkaPort";

	@Override
	public DataSinkDescription declareModel() {
		return DataSinkBuilder.create("kafka", "Kafka Publisher", "Forwards an event to a Kafka Broker")
						.iconUrl(ActionConfig.getIconUrl("kafka_logo"))
						.requiredPropertyStream1(EpRequirements.anyProperty())
						.requiredTextParameter(Labels.from(TOPIC_KEY, "Kafka Topic", "Select a Kafka " +
										"topic"), false, false)
						.requiredOntologyConcept(Labels.from(KAFKA_BROKER_SETTINGS_KEY, "Kafka Broker Settings", "Provide" +
														" settings of the Kafka broker to connect with."),
										OntologyProperties.mandatory(KAFKA_HOST_URI),
										OntologyProperties.mandatory(KAFKA_PORT_URI))
						.supportedFormats(SupportedFormats.jsonFormat())
						.supportedProtocols(SupportedProtocols.kafka())
						.build();
	}

	@Override
	public ConfiguredEventSink<KafkaParameters, EventSink<KafkaParameters>> onInvocation(DataSinkInvocation graph) {
		DataSinkParameterExtractor extractor = DataSinkParameterExtractor.from(graph);
		String topic = extractor.singleValueParameter(TOPIC_KEY,
						String.class);

		String kafkaHost = extractor.supportedOntologyPropertyValue(KAFKA_BROKER_SETTINGS_KEY, KAFKA_HOST_URI,
						String.class);
		Integer kafkaPort = extractor.supportedOntologyPropertyValue(KAFKA_BROKER_SETTINGS_KEY, KAFKA_PORT_URI,
						Integer.class);

		KafkaParameters params = new KafkaParameters(graph, kafkaHost, kafkaPort, topic);

		return new ConfiguredEventSink<>(params, KafkaPublisher::new);
	}

}
