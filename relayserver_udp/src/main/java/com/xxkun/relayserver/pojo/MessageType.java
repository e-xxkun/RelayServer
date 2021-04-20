package com.xxkun.relayserver.pojo;

import com.xxkun.relayserver.component.exception.MessageResolutionException;
import com.xxkun.relayserver.component.handler.MessageHandler;
import com.xxkun.relayserver.pojo.request.Message;
import com.xxkun.relayserver.pojo.request.Request;
import com.xxkun.relayserver.pojo.request.message.HeartbeatMessage;
import com.xxkun.relayserver.pojo.request.message.PunchMessage;

public class MessageType {

    public enum PUT{
        UNKNOWN(3, "");


        private final int code;
        private final String info;

        PUT(int code, String info) {
            this.code = code;
            this.info = info;
        }

        static {
            for (MessageType type : MessageType.values()) {
                typeMap.put(type.code, type);
            }
        }

        @Override
        public String getInfo() {
            return info;
        }

        @Override
        public int getCode() {
            return code;
        }

        @Override
        public Message createMessage(Request request) throws MessageResolutionException {
            return null;
        }

        @Override
        public MessageHandler getMessageHandler() {
            return MessageHandler.getInstance(this);
        }
    }

    public enum GET {
        PUNCH(0, "") {
            @Override
            public Message createMessage(Request request) throws MessageResolutionException {
                return new PunchMessage(request);
            }
        },
        HEARTBEAT(1, ""){
            @Override
            public Message createMessage(Request request) throws MessageResolutionException {
                return new HeartbeatMessage(request);
            }
        },
        REPLY(2, ""),
        UNKNOWN(3, "");


        private final int code;
        private final String info;

        GET(int code, String info) {
            this.code = code;
            this.info = info;
        }

//        static {
//            for (MessageType type : MessageType.values()) {
//                typeMap.put(type.code, type);
//            }
//        }

        @Override
        public String getInfo() {
            return info;
        }

        @Override
        public int getCode() {
            return code;
        }

        @Override
        public Message createMessage(Request request) throws MessageResolutionException {
            return null;
        }

        @Override
        public MessageHandler getMessageHandler() {
            return MessageHandler.getInstance(this);
        }
    }
}
