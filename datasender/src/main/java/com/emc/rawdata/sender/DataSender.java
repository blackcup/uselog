package com.emc.rawdata.sender;

public interface DataSender {
    /**
     * 发送给pravega
     * @param message
     */
    void send(String message);
}
