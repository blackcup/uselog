package com.emc.rawdata.sender;

import io.pravega.connectors.flink.PravegaEventRouter;

public class MessageRouter implements PravegaEventRouter<String> {
    @Override
    public String getRoutingKey(String event) {
        return event;
    }
}
