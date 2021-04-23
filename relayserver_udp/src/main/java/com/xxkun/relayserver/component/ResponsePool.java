package com.xxkun.relayserver.component;

import com.xxkun.relayserver.pojo.response.UpdateTokenResponse;
import com.xxkun.relayserver.pojo.response.PunchResponse;
import com.xxkun.relayserver.pojo.response.UserExceptionResponse;

import java.net.InetSocketAddress;

public class ResponsePool {
    public static UpdateTokenResponse createHeartbeatResponse(InetSocketAddress socketAddress) {
        return null;
    }

    public static UserExceptionResponse createUserExceptionResponse(InetSocketAddress socketAddress) {
        return null;
    }

    public static PunchResponse createPunchResponse(InetSocketAddress socketAddress) {
        return null;
    }
}
