package com.dellemc.rawdata;

import com.dellemc.rawdata.analyze.Analyzer;
import com.dellemc.rawdata.analyze.FakeAnalyzer;
import com.dellemc.rawdata.receiver.DataReceiverImp;

import java.net.URISyntaxException;

public class Main {
    public static void main(String[] args) throws URISyntaxException {
        if (args == null || args.length == 0){
            System.out.println("null args");
        }
        String url = "tcp://" + args[0] + ":9090";
        DataReceiverImp dataReceiverImp = new DataReceiverImp(url);
        Analyzer analyzer = new FakeAnalyzer();
        while (true){
            String message = dataReceiverImp.receive();
            if (message == null){
                break;
            }
            analyzer.analyze(message);
        }
        dataReceiverImp.close();
        System.exit(0);
    }
}
