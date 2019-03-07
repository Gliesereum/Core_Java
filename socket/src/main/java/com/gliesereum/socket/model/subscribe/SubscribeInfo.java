package com.gliesereum.socket.model.subscribe;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author yvlasiuk
 * @version 1.0
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubscribeInfo {

    private String subscribeType;

    private String pointName;

    private String subscribeDestination;

    private String subscribeProperties;
}
