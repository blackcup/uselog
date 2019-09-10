package com.dell.emc;

import io.pravega.client.ClientFactory;
import io.pravega.client.admin.StreamManager;
import io.pravega.client.stream.EventStreamWriter;
import io.pravega.client.stream.EventWriterConfig;
import io.pravega.client.stream.ScalingPolicy;
import io.pravega.client.stream.StreamConfiguration;
import io.pravega.client.stream.impl.JavaSerializer;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.CompletableFuture;

public class PravegaWriter {
    private URI controllerURI;
    private String scope;
    private String streamName;

    public PravegaWriter(   String controllerURI, String scope, String streamName) throws URISyntaxException {
        this.controllerURI = new URI(controllerURI);
        this.scope = scope;
        this.streamName = streamName;
    }
    public void send(String routingKey, String message) {
        StreamManager streamManager = StreamManager.create(controllerURI);

        final boolean scopeCreation = streamManager.createScope(scope);
        StreamConfiguration streamConfig = StreamConfiguration.builder()
                .scalingPolicy(ScalingPolicy.fixed(1))
                .build();
        final boolean streamCreation = streamManager.createStream(scope, streamName, streamConfig);

        try (ClientFactory clientFactory = ClientFactory.withScope(scope, controllerURI);
             EventStreamWriter<String> writer = clientFactory.createEventWriter(streamName,
                     new JavaSerializer<String>(),
                     EventWriterConfig.builder().build())) {

            System.out.format("Writing message: '%s' with routing-key: '%s' to stream '%s / %s'%n",
                    message, routingKey, scope, streamName);
            final CompletableFuture<Void> writeFuture = writer.writeEvent(routingKey, message);
        }
        System.exit(0);
    }

    public static void main(String[] args) throws URISyntaxException {
        new PravegaWriter("tcp://10.247.101.104:9090","demo","demo").send("123","hello world");
    }
}
