package de.fzi.cep.sepa.html.page;

import java.net.URI;
import java.util.List;

import de.fzi.cep.sepa.desc.declarer.SemanticEventConsumerDeclarer;
import de.fzi.cep.sepa.html.WelcomePageGenerator;
import de.fzi.cep.sepa.html.model.AgentDescription;
import de.fzi.cep.sepa.html.model.Description;

public class EventConsumerWelcomePage extends WelcomePageGenerator<SemanticEventConsumerDeclarer>{

	
	public EventConsumerWelcomePage(String baseUri, List<SemanticEventConsumerDeclarer> declarers)
	{
		super(baseUri, declarers);
	}
	
	@Override
	public List<Description> buildUris() {
		for(SemanticEventConsumerDeclarer declarer : declarers)
		{
			Description producer = new AgentDescription();
			producer.setName(declarer.declareModel().getName());
			producer.setDescription(declarer.declareModel().getDescription());
			producer.setUri(URI.create(baseUri +declarer.declareModel().getUri().replaceFirst("[a-zA-Z]{4}://[a-zA-Z\\.]+:\\d+/", "")));
			descriptions.add(producer);
		}
		return descriptions;	
	}
}