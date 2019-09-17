package com.emc.rawdata.sender;

import io.pravega.client.admin.StreamManager;
import io.pravega.client.stream.ScalingPolicy;
import io.pravega.client.stream.Stream;
import io.pravega.client.stream.StreamConfiguration;
import io.pravega.connectors.flink.FlinkPravegaWriter;
import io.pravega.connectors.flink.PravegaConfig;
import io.pravega.connectors.flink.PravegaWriterMode;
import io.pravega.connectors.flink.serialization.PravegaSerialization;

import java.net.URI;

public class FlinkAppDataSender {
    private URI controllerURI = null;
    private String scope = "defaultScope";
    private String streamName = "defaultStream";
    private String routingKey = "defaultRoutingKey";
    private PravegaConfig pravegaConfig;
    public FlinkAppDataSender() throws Exception {
        controllerURI = new  URI("");
        pravegaConfig = PravegaConfig.fromDefaults().withControllerURI(controllerURI);
    }
    public Stream createStream(){
        Stream stream = pravegaConfig.resolve("");
        StreamConfiguration streamConfig = StreamConfiguration.builder()
                .scalingPolicy(ScalingPolicy.fixed(10))
                .build();
        try(StreamManager streamManager = StreamManager.create(pravegaConfig.getClientConfig())) {
            // create the requested stream based on the given stream configuration
            streamManager.createStream(stream.getScope(), stream.getStreamName(), streamConfig);
        }
        return stream;
    }
    public void createScope(){
        try(StreamManager streamManager = StreamManager.create(controllerURI)) {
            streamManager.createScope(scope);
        }
    }
    public FlinkPravegaWriter<String> createPravegaWriter() {
        Stream pravegaStream = createStream();

        FlinkPravegaWriter.Builder<String> builder = FlinkPravegaWriter.<String>builder()
                .withPravegaConfig(pravegaConfig)
                .forStream(pravegaStream)
                .withEventRouter(new MessageRouter())
                .withSerializationSchema(PravegaSerialization.serializationFor(String.class));
        builder.withWriterMode(PravegaWriterMode.EXACTLY_ONCE);
        return builder.build();
    }
}
