package org.streampipes.manager.execution.http;

import java.util.List;

import org.streampipes.model.client.pipeline.PipelineOperationStatus;
import org.streampipes.model.base.InvocableStreamPipesEntity;

public class GraphSubmitter {

	private List<InvocableStreamPipesEntity> graphs;
	private String pipelineId;
	private String pipelineName;
	
	public GraphSubmitter(String pipelineId, String pipelineName, List<InvocableStreamPipesEntity> graphs)
	{
		this.graphs = graphs;
		this.pipelineId = pipelineId;
		this.pipelineName = pipelineName;
	}
	
	public PipelineOperationStatus invokeGraphs()
	{
		PipelineOperationStatus status = new PipelineOperationStatus();
		status.setPipelineId(pipelineId);
		status.setPipelineName(pipelineName);
	
		
		graphs.forEach(g -> status.addPipelineElementStatus(new HttpRequestBuilder(g).invoke()));
		status.setSuccess(!status.getElementStatus().stream().anyMatch(s -> !s.isSuccess()));
		
		if (status.isSuccess()) status.setTitle("Pipeline " +pipelineName +" successfully started");
		else status.setTitle("Could not start pipeline " +pipelineName +".");
		return status;
	}
	
	public PipelineOperationStatus detachGraphs()
	{
		PipelineOperationStatus status = new PipelineOperationStatus();
		status.setPipelineId(pipelineId);
		
		graphs.forEach(g -> status.addPipelineElementStatus(new HttpRequestBuilder(g).detach()));
		status.setSuccess(!status.getElementStatus().stream().anyMatch(s -> !s.isSuccess()));
		
		if (status.isSuccess()) status.setTitle("Pipeline " +pipelineName +" successfully stopped");
		else status.setTitle("Could not stop all pipeline elements of pipeline " +pipelineName +".");
		
		return status;
	}
}
