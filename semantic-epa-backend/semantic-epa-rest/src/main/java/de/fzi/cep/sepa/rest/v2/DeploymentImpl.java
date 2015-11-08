package de.fzi.cep.sepa.rest.v2;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.openrdf.rio.RDFHandlerException;

import com.clarkparsia.empire.annotation.InvalidRdfException;
import com.sun.jersey.multipart.FormDataParam;

import de.fzi.cep.sepa.commons.Utils;
import de.fzi.cep.sepa.commons.config.ConfigurationManager;
import de.fzi.cep.sepa.manager.generation.ArchetypeManager;
import de.fzi.cep.sepa.model.client.deployment.DeploymentConfiguration;
import de.fzi.cep.sepa.model.impl.graph.SepaDescription;
import de.fzi.cep.sepa.model.transform.JsonLdTransformer;
import de.fzi.cep.sepa.model.util.GsonSerializer;
import de.fzi.cep.sepa.rest.api.AbstractRestInterface;


@Path("/v2/users/{username}/deploy")
public class DeploymentImpl extends AbstractRestInterface {

	@POST
	@Path("/implementation")
	@Produces("application/zip")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response getFile(@FormDataParam("config") String deploymentConfig, @FormDataParam("model") String model) {
	    
		DeploymentConfiguration config = fromJson(deploymentConfig, DeploymentConfiguration.class);
		
		SepaDescription sepa = GsonSerializer.getGsonWithIds().fromJson(model, SepaDescription.class);
		
		File f = new ArchetypeManager(config, sepa).getGeneratedFile();

	    if (!f.exists()) {
	        throw new WebApplicationException(404);
	    }

	    return Response.ok(f)
	            .header("Content-Disposition",
	                    "attachment; filename=" +f.getName()).build();
	}
	
	@POST
	@Path("/description")
	@Produces("application/json")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response getDescription(@FormDataParam("config") String deploymentConfig, @FormDataParam("model") String model) {
		
		SepaDescription sepa = GsonSerializer.getGsonWithIds().fromJson(model, SepaDescription.class);
		
		File file = new File(ConfigurationManager.getStreamPipesConfigFileLocation() +RandomStringUtils.randomAlphabetic(8) +File.separator +makeName(sepa.getName()) +".jsonld");
		
		try {
			FileUtils.write(file, Utils.asString(new JsonLdTransformer().toJsonLd(sepa)));
			return Response.ok(file).header("Content-Disposition",
	                "attachment; filename=" +file.getName()).build();
		} catch (RDFHandlerException | IllegalAccessException
				| IllegalArgumentException | InvocationTargetException
				| SecurityException | ClassNotFoundException | IOException
				| InvalidRdfException e) {
			e.printStackTrace();
			return Response.serverError().build();
		}	
	}
	
	private String makeName(String name)
	{
		return name.replaceAll(" ", "_").toLowerCase();
	}
}
