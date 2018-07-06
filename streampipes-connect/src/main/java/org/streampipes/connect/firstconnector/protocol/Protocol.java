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

package org.streampipes.connect.firstconnector.protocol;

import org.streampipes.model.modelconnect.GuessSchema;
import org.streampipes.model.modelconnect.ProtocolDescription;
import org.streampipes.model.schema.EventSchema;
import org.streampipes.connect.firstconnector.format.Format;
import org.streampipes.connect.firstconnector.format.Parser;

import java.util.List;
import java.util.Map;


public abstract class Protocol {

    public abstract Protocol getInstance(ProtocolDescription protocolDescription, Parser parser, Format format);

    public abstract ProtocolDescription declareModel();

    public abstract GuessSchema getGuessSchema();

    public abstract List<Map<String, Object>> getNElements(int n);

    /*
        This method is used when the adapter is started to send constantly events to Kafka
     */
    public abstract void run(String broker, String topic);

    /*
       Stops the running protocol. Mainly relevant for streaming protocols
     */
    public abstract void stop();

    public abstract String getId();
}