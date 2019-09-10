package com.emc.rawdata.generate;

public interface DataCollector {
    /**
     * 给定一个log文件，做一些简单处理。optional
     * @param path
     */
    void collect(String path);
}
