package com.emc.rawdata.generate;
import java.io.*;
import java.util.*;
import java.lang.*;

public class DataGenerators implements DataGenerator, DataCollector{
    private static ArrayList<String> list;

    private static File f;
    private int index = 0;


    @Override
    public void collect(String path) {
        list = (ArrayList<String>) readTxtFileIntoStringArrList(path);
    }

    @Override
    public boolean hasNext() {
        if (index >= list.size())
            return false;
        else
            return true;
    }

    @Override
    public String generator() {
        if (hasNext()) {
            index = index + 1;
            return list.get(index - 1);

        }
        else
            return null;
    }
    public static List<String> readTxtFileIntoStringArrList(String filePath) {
        List<String> list = new ArrayList<String>();
        try {
            String encoding = "UTF-8";
            File file = new File(filePath);
            if (file.isFile() && file.exists()) { // check whether the file exist
                InputStreamReader read = new InputStreamReader(
                        new FileInputStream(file), encoding);
                BufferedReader bufferedReader = new BufferedReader(read);
                String lineTxt = null;

                while ((lineTxt = bufferedReader.readLine()) != null) {
                    list.add(lineTxt);
                }
                bufferedReader.close();
                read.close();
            } else {
                System.out.println("Cannot find the target file.");
            }
        } catch (Exception e) {
            System.out.println("Reading file error.");
            e.printStackTrace();
        }

        return list;
    }

}
