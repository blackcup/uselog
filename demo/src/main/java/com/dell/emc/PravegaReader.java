package com.dell.emc;

import io.pravega.client.ClientFactory;
import io.pravega.client.admin.ReaderGroupManager;
import io.pravega.client.admin.StreamManager;
import io.pravega.client.stream.*;
import io.pravega.client.stream.impl.JavaSerializer;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.UUID;


public class PravegaReader {
    private URI controllerURI;
    private String scope;
    private String streamName;

    public PravegaReader(String controllerURI, String scope, String streamName) throws URISyntaxException {
        this.controllerURI = new URI(controllerURI);
        this.scope = scope;
        this.streamName = streamName;
    }
    public void receive() {
        StreamManager streamManager = StreamManager.create(controllerURI);

        final boolean scopeIsNew = streamManager.createScope(scope);
        StreamConfiguration streamConfig = StreamConfiguration.builder()
                .scalingPolicy(ScalingPolicy.fixed(1))
                .build();
        final boolean streamIsNew = streamManager.createStream(scope, streamName, streamConfig);

        final String readerGroup = UUID.randomUUID().toString().replace("-", "");
        final ReaderGroupConfig readerGroupConfig = ReaderGroupConfig.builder()
                .stream(Stream.of(scope, streamName))
                .build();
        try (ReaderGroupManager readerGroupManager = ReaderGroupManager.withScope(scope, controllerURI)) {
            readerGroupManager.createReaderGroup(readerGroup, readerGroupConfig);
        }

        try (ClientFactory clientFactory = ClientFactory.withScope(scope, controllerURI);
             EventStreamReader<String> reader = clientFactory.createReader("reader",
                     readerGroup,
                     new JavaSerializer<String>(),
                     ReaderConfig.builder().build())) {
            System.out.format("Reading all the events from %s/%s%n", scope, streamName);
            EventRead<String> event = null;
            do {
                try {
                    event = reader.readNextEvent(1000);
                    System.out.format("Read event '%s'%n", event.getEvent());
                } catch (ReinitializationRequiredException e) {
                    //There are certain circumstances where the reader needs to be reinitialized
                    e.printStackTrace();
                }
            } while (event.getEvent() != null);
            reader.close();
            System.out.format("No more events from %s/%s%n", scope, streamName);
        }
        System.exit(0);
    }

    public static void main(String[] args) throws URISyntaxException {
        new PravegaReader("tcp://10.247.101.104:9090","demo","demo").receive();
    }
}
