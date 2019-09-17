package com.emc.rawdata;

import com.emc.rawdata.generate.DataGenerators;
import com.emc.rawdata.sender.DataSender;
import com.emc.rawdata.sender.DataSenderImp;
import com.emc.rawdata.sender.FlinkAppDataSender;
import io.pravega.client.admin.StreamManager;
import io.pravega.client.stream.ScalingPolicy;
import io.pravega.client.stream.Stream;
import io.pravega.client.stream.StreamConfiguration;
import io.pravega.connectors.flink.FlinkPravegaWriter;
import io.pravega.connectors.flink.PravegaConfig;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.LocalStreamEnvironment;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import java.net.URI;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) throws Exception {
//        if (args.length == 0){
//            System.out.println("args is null");
//            System.exit(1);
//        }
//        if (args.length !=2){
//            System.out.println("args length doesn't meet 2");
//        }
//        String url = "tcp://" + args[0] + ":9090";
//        String path = args[1];
//        DataGenerators dataGenerators = new DataGenerators();
//        dataGenerators.collect(path);
//        DataSender dataSender = new DataSenderImp(url);
//        try {
//            while (dataGenerators.hasNext()){
//                TimeUnit.MILLISECONDS.sleep(500);
//                dataSender.send(dataGenerators.generator());
//            }
//        }finally {
//            dataSender.close();
//        }
//        System.exit(0);
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        FlinkAppDataSender flinkAppDataSender = new FlinkAppDataSender();
        // When running locally the Scope may need to be created (Nautilus creates it in a cluster)
        flinkAppDataSender.createScope();

        DataGenerators dataGenerators = new DataGenerators();
        dataGenerators.collect("/gc_test.txt");
        FlinkPravegaWriter<String> pravegaWriter = flinkAppDataSender.createPravegaWriter();

        /** Construct Graph **/
        DataStream<String> generatedEventStream = env.addSource(dataGenerators)
                .uid("source-generator");

        generatedEventStream.addSink(pravegaWriter)
                .uid("log-sender");

        env.execute("message-sender");

    }
}
