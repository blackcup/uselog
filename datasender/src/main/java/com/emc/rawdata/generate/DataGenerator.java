package com.emc.rawdata.generate;

public interface DataGenerator {
    /**
     * 返回当前数据集是否还有数据
     * @return
     */
    boolean hasNext();

    /**
     * 调用一次，返回一条符合规范的log,用于发送给pravega
     * @return
     */
    String generator();
}
