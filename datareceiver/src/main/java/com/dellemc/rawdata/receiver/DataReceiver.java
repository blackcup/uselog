package com.dellemc.rawdata.receiver;

public interface DataReceiver {
    /**
     * 接收pravega的数据，每次调用都会返回一条message/log
     * @return
     */
    String receive();
}
