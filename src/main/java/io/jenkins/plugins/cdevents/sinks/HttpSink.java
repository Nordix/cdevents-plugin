/**
 * Copyright FMR LLC <opensource@fidelity.com>
 * SPDX-License-Identifier: Apache-2.0
 */

package io.jenkins.plugins.cdevents.sinks;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import hudson.slaves.Cloud;
import io.cloudevents.CloudEvent;
import io.cloudevents.core.format.EventFormat;
import io.cloudevents.core.format.EventSerializationException;
import io.cloudevents.core.provider.EventFormatProvider;
import io.cloudevents.core.format.ContentType;
import io.cloudevents.jackson.JsonFormat;
import io.jenkins.plugins.cdevents.CDEventsGlobalConfig;
import io.jenkins.plugins.cdevents.CDEventsSink;
import jenkins.model.Jenkins;
import org.apache.http.HttpHost;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.SerializableEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import java.io.Serializable;

import hudson.ProxyConfiguration;

import javax.management.relation.RoleUnresolvedList;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HttpSink extends CDEventsSink {

    private static final Logger LOGGER = Logger.getLogger("HttpSink");
    private static final String sinkUrl = CDEventsGlobalConfig.get().getHttpSinkUrl();
    private final ProxyConfiguration proxy = Jenkins.get().proxy;

    @Override
    public void sendCloudEvent(CloudEvent cloudEvent) throws IOException {
        LOGGER.log(Level.INFO, "Now attempting to send to the HTTP endpoint " + sinkUrl
                + " the following CloudEvent " + cloudEvent);
        HttpPost httpPost = new HttpPost(sinkUrl);
        // TODO need to test the string conversion and using the Apache HTTP library is
        // successful
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            byte[] serialized = new JsonFormat().serialize(cloudEvent);
            String rawJson = new String(serialized, StandardCharsets.UTF_8);
            Object jsonObj = objectMapper.readValue(rawJson, Object.class);
            String json = objectMapper.writeValueAsString(jsonObj);
            StringEntity entity = new StringEntity(json);
            httpPost.setEntity(entity);
        } catch (EventSerializationException e) {
            e.printStackTrace();
        }



        httpPost.setHeader("Accept", "application/json");
        httpPost.setHeader("Ce-Type", cloudEvent.getType());
        httpPost.setHeader("Content-type", "application/cloudevents+json; charset=UTF-8");

        HttpHost httpProxy = (proxy != null ? new HttpHost(proxy.name, proxy.port) : null);
        try (CloseableHttpClient client = HttpClientBuilder.create()
                .useSystemProperties()
                .setProxy(httpProxy)
                .build();
             CloseableHttpResponse response = client.execute(httpPost)) {
            JSONObject sinkResponse = new JSONObject(EntityUtils.toString(response.getEntity(), "UTF-8"));
            LOGGER.log(Level.INFO, "Response from HTTP Sink Endpoint: " + sinkResponse);
        }
    }
}