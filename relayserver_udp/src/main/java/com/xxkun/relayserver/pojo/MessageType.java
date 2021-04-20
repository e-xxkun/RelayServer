package com.xxkun.relayserver.pojo;

import com.xxkun.relayserver.component.exception.MessageResolutionException;
import com.xxkun.relayserver.component.handler.MessageHandler;
import com.xxkun.relayserver.pojo.request.Message;
import com.xxkun.relayserver.pojo.request.Request;
import com.xxkun.relayserver.pojo.request.message.HeartbeatMessage;
import com.xxkun.relayserver.pojo.request.message.PunchMessage;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

public class MessageType {

    private static EnumMap<RequestType, Map<Integer, IMessageType>> typeMap;

    {
        typeMap = new EnumMap<>(RequestType.class);
        for (RequestType type : RequestType.values()) {
            typeMap.put(type, new HashMap<>());
        }
    }

    public static IMessageType fromTypeCode(RequestType type, int code) {
        return typeMap.get(type).get(code);
    }

    private static void init(IMessageType[] types, RequestType requestType) {
        Map<Integer, IMessageType> map = typeMap.get(requestType);
        for (IMessageType type : types) {
            map.put(type.getCode(), type);
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
        public int getCode() {
            return code;
        }
        @Override
        public MessageHandler getMessageHandler() {
            return MessageHandler.getInstance(this);
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
        public int getCode() {
            return code;
        }
        @Override
        public MessageHandler getMessageHandler() {
            return MessageHandler.getInstance(this);
        }
    }
}
