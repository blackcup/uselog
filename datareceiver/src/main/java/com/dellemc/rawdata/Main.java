package com.dellemc.rawdata;

import com.dellemc.rawdata.analyze.Analyzer;
import com.dellemc.rawdata.analyze.FakeAnalyzer;
import com.dellemc.rawdata.receiver.DataReceiverImp;

import java.net.URISyntaxException;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) throws Exception {
        if (args == null || args.length == 0){
            System.out.println("null args");
        }
        String url = "tcp://" + args[0] + ":9090";
        DataReceiverImp dataReceiverImp = new DataReceiverImp(url);
        Analyzer analyzer = new FakeAnalyzer();
        try {
            while (true){
                String message = dataReceiverImp.receive();
                if (message == null){
                    continue;
                }
                try {
                    analyzer.analyze(message);
                }catch (Exception e){
                    System.out.println(e);
                }

                TimeUnit.MILLISECONDS.sleep(500);
            }
        }finally {
            dataReceiverImp.close();
            analyzer.close();
            System.exit(0);
        }
    }
}
