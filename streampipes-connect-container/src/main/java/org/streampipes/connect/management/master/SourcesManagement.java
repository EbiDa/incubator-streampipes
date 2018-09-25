/*
 * Copyright 2018 FZI Forschungszentrum Informatik
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package org.streampipes.connect.management.master;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.streampipes.config.backend.BackendConfig;
import org.streampipes.connect.config.ConnectContainerConfig;
import org.streampipes.connect.exception.AdapterException;
import org.streampipes.container.html.JSONGenerator;
import org.streampipes.container.html.model.DataSourceDescriptionHtml;
import org.streampipes.container.html.model.Description;
import org.streampipes.model.SpDataSet;
import org.streampipes.model.SpDataStream;
import org.streampipes.model.connect.adapter.AdapterDescription;
import org.streampipes.model.connect.adapter.AdapterSetDescription;
import org.streampipes.model.connect.adapter.AdapterStreamDescription;
import org.streampipes.model.graph.DataSourceDescription;
import org.streampipes.model.grounding.EventGrounding;
import org.streampipes.model.grounding.TransportProtocol;
import org.streampipes.sdk.helpers.Formats;
import org.streampipes.sdk.helpers.Protocols;
import org.streampipes.sdk.helpers.SupportedFormats;
import org.streampipes.sdk.helpers.SupportedProtocols;
import org.streampipes.storage.couchdb.impl.AdapterStorageImpl;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SourcesManagement {

    private Logger logger = LoggerFactory.getLogger(SourcesManagement.class);

    private AdapterStorageImpl adapterStorage;
    private String connectHost = null;

    public SourcesManagement(AdapterStorageImpl adapterStorage) {
        this.adapterStorage = adapterStorage;
    }

    public SourcesManagement() {
        this.adapterStorage = new AdapterStorageImpl();
    }

    public void addAdapter(String baseUrl, String streamId, SpDataSet dataSet) throws AdapterException {
        AdapterSetDescription adapterDescription = (AdapterSetDescription) this.adapterStorage.getAdapter(streamId);
        adapterDescription.setDataSet(dataSet);

        WorkerRestClient.invokeSetAdapter(baseUrl, adapterDescription);
    }

    public void detachAdapter(String baseUrl, String streamId, String runningInstanceId) throws AdapterException {
        AdapterSetDescription adapterDescription = (AdapterSetDescription) this.adapterStorage.getAdapter(streamId);

        WorkerRestClient.stopSetAdapter(baseUrl, adapterDescription);
    }


    public String getAllAdaptersInstallDescription(String user) throws AdapterException {
        String host = getConnectHost();

        List<AdapterDescription> allAdapters = adapterStorage.getAllAdapters();
        List<Description> allAdapterDescriptions = new ArrayList<>();

        for (AdapterDescription ad : allAdapters) {
            URI uri = null;
            String uriString = null;
            try {
//                uriString = "http://" + host + "/streampipes-connect/api/v1/" + user + "/master/adapters/" + ad.getId();
                uriString = "http://" + host + "/streampipes-connect/api/v1/" + user + "/master/sources/" + ad.getId();
                uri = new URI(uriString);
            } catch (URISyntaxException e) {
                logger.error("URI for the sources endpoint is not correct: " + uriString, e);
                throw new AdapterException("Username " + user + " not allowed");
            }


            List<Description> streams = new ArrayList<>();
            Description d = new Description(ad.getName(), "", uri);
            d.setType("set");
            streams.add(d);
            DataSourceDescriptionHtml dsd = new DataSourceDescriptionHtml("Adapter Stream",
                    "This stream is generated by an StreamPipes Connect adapter. ID of adapter: " + ad.getId(), uri, streams);
            dsd.setType("source");
            allAdapterDescriptions.add(dsd);
        }

        JSONGenerator json = new JSONGenerator(allAdapterDescriptions);

        return json.buildJson();
    }

    public DataSourceDescription getAdapterDataSource(String id) throws AdapterException {

        AdapterDescription adapterDescription = new AdapterStorageImpl().getAdapter(id);

        SpDataStream ds;
        if (adapterDescription instanceof AdapterSetDescription) {
            ds = ((AdapterSetDescription) adapterDescription).getDataSet();
            EventGrounding eg = new EventGrounding();
            eg.setTransportProtocol(SupportedProtocols.kafka());
            eg.setTransportFormats(Arrays.asList(SupportedFormats.jsonFormat()));
            ((SpDataSet) ds).setSupportedGrounding(eg);
        } else {
            ds = ((AdapterStreamDescription) adapterDescription).getDataStream();


            String topic = adapterDescription.getEventGrounding().getTransportProtocol().getTopicDefinition().getActualTopicName();

            TransportProtocol tp = Protocols.kafka(BackendConfig.INSTANCE.getKafkaHost(), BackendConfig.INSTANCE.getKafkaPort(), topic);
            EventGrounding eg = new EventGrounding();
            eg.setTransportFormats(Arrays.asList(Formats.jsonFormat()));
            eg.setTransportProtocol(tp);

            ds.setEventGrounding(eg);
        }


        String url = adapterDescription.getUri().toString() + "/" + adapterDescription.getId();

        ds.setName(adapterDescription.getName());
        ds.setDescription("Description");

        ds.setUri(url + "/streams");

        DataSourceDescription dataSourceDescription = new DataSourceDescription(
                url, "Adaper Data Source",
                "This data source contains one data stream from the adapters");

        dataSourceDescription.addEventStream(ds);

        return dataSourceDescription;
    }

    public String getConnectHost() {
        if (connectHost == null) {
            return ConnectContainerConfig.INSTANCE.getBackendHost() + ":" + ConnectContainerConfig.INSTANCE.getBackendPort();
        } else {
            return connectHost;
        }
    }

    public void setConnectHost(String connectHost) {
        this.connectHost = connectHost;
    }
}