package com.dellemc.rawdata.receiver;

import io.pravega.client.ClientFactory;
import io.pravega.client.admin.ReaderGroupManager;
import io.pravega.client.admin.StreamManager;
import io.pravega.client.stream.*;
import io.pravega.client.stream.impl.JavaSerializer;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.UUID;

public class DataReceiverImp implements DataReceiver{
    private URI controllerURI;
    private String scope = "defaultScope";
    private String streamName = "defaultStream";
    private String readerGroup ;
    EventStreamReader<String> reader;
    public DataReceiverImp(String url) throws URISyntaxException {
        this.controllerURI = new URI(url);
        StreamManager streamManager = StreamManager.create(this.controllerURI);

        final boolean scopeIsNew = streamManager.createScope(scope);
        StreamConfiguration streamConfig = StreamConfiguration.builder()
                .scalingPolicy(ScalingPolicy.fixed(10))
                .build();
        readerGroup = UUID.randomUUID().toString().replace("_","");
        String readerName = UUID.randomUUID().toString().replace("_","");
        final boolean streamIsNew = streamManager.createStream(scope, streamName, streamConfig);
        final ReaderGroupConfig readerGroupConfig = ReaderGroupConfig.builder()
                .stream(Stream.of(scope, streamName))
                .build();
        ReaderGroupManager readerGroupManager = ReaderGroupManager.withScope(scope, controllerURI) ;
            readerGroupManager.createReaderGroup(readerGroup, readerGroupConfig);

        ClientFactory clientFactory = ClientFactory.withScope(scope, controllerURI);
        reader = clientFactory.createReader(readerName,
                readerGroup,
                new JavaSerializer<String>(),
                ReaderConfig.builder().build());
        EventRead<String> stringEventRead = reader.readNextEvent(30000);
    }

    public String receive() {
        EventRead<String> stringEventRead = reader.readNextEvent(30000);
        if (stringEventRead == null){
            return null;
        }
        return stringEventRead.getEvent();
    }

    @Override
    public void close() {
        reader.close();
    }

    public static void main(String[] args) throws Exception {
        DataReceiverImp dataReceiverImp = new DataReceiverImp("tcp://10.247.101.104:9090");
        while(true){
            String receive = dataReceiverImp.receive();
            if (receive == null){
                break;
            }
            System.out.println(receive);
        }
        dataReceiverImp.close();
        System.exit(0);
    }
}
