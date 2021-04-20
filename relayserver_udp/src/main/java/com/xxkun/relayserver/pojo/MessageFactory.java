package com.xxkun.relayserver.pojo;

import com.xxkun.relayserver.component.exception.MessageResolutionException;
import com.xxkun.relayserver.pojo.request.Message;
import com.xxkun.relayserver.pojo.request.Request;
import com.xxkun.relayserver.pojo.request.message.HeartbeatMessage;
import com.xxkun.relayserver.pojo.request.message.PunchMessage;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

@Component
public class MessageFactory {

    private static EnumMap<RequestType, Map<Integer, IMessageType>> typeMap;

    static {
        typeMap = new EnumMap<>(RequestType.class);
        for (RequestType type : RequestType.values()) {
            typeMap.put(type, new HashMap<>());
        }
    }

    public static IMessageType fromTypeCode(RequestType type, int code) {
        return typeMap.get(type).get(code);
    }

    private static void init(IMessageType[] types, RequestType type) {
        Map<Integer, IMessageType> map = typeMap.get(type);
        for (IMessageType messageType : types) {
            map.put(messageType.getCode(), messageType);
        }
    }

    public enum PUT implements IMessageType {
        UNKNOWN(3) {
            @Override
            public Message createMessage(Request request) {
                return null;
            }
        };

        private final int code;

        static {
            init(values(), RequestType.PUT);
        }

        PUT(int code) {
            this.code = code;
        }
        @Override
        public boolean isPUT() {
            return true;
        }
        @Override
        public int getCode() {
            return code;
        }
    }

    public enum GET implements IMessageType {
        PUNCH(0) {
            @Override
            public Message createMessage(Request request) throws MessageResolutionException {
                return new PunchMessage(request);
            }
        },
        HEARTBEAT(1){
            @Override
            public Message createMessage(Request request) throws MessageResolutionException {
                return new HeartbeatMessage(request);
            }
        },
        REPLY(2) {
            @Override
            public Message createMessage(Request request) throws MessageResolutionException {
                return null;
            }
        },
        UNKNOWN(3) {
            @Override
            public Message createMessage(Request request) throws MessageResolutionException {
                return null;
            }
        };

        private final int code;

        GET(int code) {
            this.code = code;
        }

        static {
            init(values(), RequestType.GET);
        }

        @Override
        public boolean isPUT() {
            return false;
        }
        @Override
        public int getCode() {
            return code;
        }
    }
}
