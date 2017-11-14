package org.streampipes.pe.sinks.standalone.samples.file;

import org.streampipes.model.graph.DataSinkInvocation;
import org.streampipes.wrapper.params.binding.EventSinkBindingParams;

public class FileParameters extends EventSinkBindingParams {

  private String path;

  public FileParameters(DataSinkInvocation graph, String path) {
    super(graph);
    this.path = path;
  }

  public String getPath() {
    return path;
  }

}
