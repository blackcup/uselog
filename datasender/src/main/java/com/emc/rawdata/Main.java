package com.emc.rawdata;

import com.emc.rawdata.generate.DataGenerators;
import com.emc.rawdata.sender.DataSender;
import com.emc.rawdata.sender.DataSenderImp;

import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) throws Exception {
        if (args.length == 0){
            System.out.println("args is null");
            System.exit(1);
        }
        if (args.length !=2){
            System.out.println("args length doesn't meet 2");
        }
        String url = "tcp://" + args[0] + ":9090";
        String path = args[1];
        DataGenerators dataGenerators = new DataGenerators();
        dataGenerators.collect(path);
        DataSender dataSender = new DataSenderImp(url);
        try {
            while (dataGenerators.hasNext()){
                TimeUnit.MILLISECONDS.sleep(500);
                dataSender.send(dataGenerators.generator());
            }
        }finally {
            dataSender.close();
        }
        System.exit(0);
    }
}
