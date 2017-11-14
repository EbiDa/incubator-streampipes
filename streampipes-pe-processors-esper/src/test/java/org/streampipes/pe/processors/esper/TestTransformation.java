package org.streampipes.pe.processors.esper;


import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.eclipse.rdf4j.repository.RepositoryException;
import org.eclipse.rdf4j.rio.RDFParseException;
import org.eclipse.rdf4j.rio.UnsupportedRDFormatException;

import org.streampipes.model.graph.DataProcessorDescription;
import org.streampipes.serializers.jsonld.JsonLdTransformer;

public class TestTransformation {

	public static void main(String[] args)
	{
		//List<SEPA> sepas = StorageManager.INSTANCE.getStorageAPI().getAllSEPAs();
		
		try {
			//graph = new JsonLdTransformer().toJsonLd(sepas.get(0));
			String test = FileUtils.readFileToString(new File("src/test/resources/TestSepaSerialization.jsonld"), "UTF-8");
			//Graph graph;
			
			//System.out.println(Utils.asString(graph));
			
			DataProcessorDescription sepa = new JsonLdTransformer().fromJsonLd(test, DataProcessorDescription.class);
			System.out.println(sepa.getElementId());
			
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RDFParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedRDFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RepositoryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}
}
