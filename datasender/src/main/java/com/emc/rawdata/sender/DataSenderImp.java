package com.emc.rawdata.sender;

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
import java.util.concurrent.ExecutionException;

public class DataSenderImp implements DataSender {
    private URI controllerURI;
    private String scope = "defaultScope";
    private String streamName = "defaultStream";
    private String routingKey = "defaultRoutingKey";
    EventStreamWriter<String> writer;
    public DataSenderImp(String url) throws Exception {
        this.controllerURI = new URI(url);

        StreamManager streamManager = StreamManager.create(controllerURI);

        final boolean scopeCreation = streamManager.createScope(scope);
        StreamConfiguration streamConfig = StreamConfiguration.builder()
                .scalingPolicy(ScalingPolicy.fixed(10))
                .build();
        final boolean streamCreation = streamManager.createStream(scope, streamName, streamConfig);

        ClientFactory clientFactory = ClientFactory.withScope(scope, controllerURI);
        writer = clientFactory.createEventWriter(streamName,
                     new JavaSerializer<String>(),
                     EventWriterConfig.builder().build());
    }
    @Override
    public void send (String message){
        final CompletableFuture<Void> writeFuture = writer.writeEvent(routingKey, message);
        System.out.println("has writed message:" + message );
    }

    @Override
    public void close() {
        writer.close();
    }

    public static void main(String[] args) throws Exception {
        DataSenderImp dataSenderImp = new DataSenderImp("tcp://10.247.101.104:9090");
        for (int i = 0; i < 100; i++) {
            dataSenderImp.send("helloworld");
        }
        dataSenderImp.close();
        System.exit(0);
    }
}
