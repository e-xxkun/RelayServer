package com.xxkun.udpreceiver.component;


import com.xxkun.udpreceiver.UDPReceiveLoopThread;
import com.xxkun.udpreceiver.dao.UMessage;
import com.xxkun.udpreceiver.dao.UserIdentifier;
import com.xxkun.udpreceiver.dao.UserSession;
import com.xxkun.udpreceiver.service.UserInfoManageService;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.SocketAddress;

public class MessageManager implements UDPReceiveLoopThread.OnMessage {

    @Autowired
    private UserInfoManageService userInfoManageService;

    @Autowired
    private MsgQueueSender msgQueueSender;

    @Override
    public void onMessage(SocketAddress from, UMessage msg) {
        UserSession userSession = userInfoManageService.getUserSessionFromToken(msg.getToken());
        if (userSession == null) {
            return;
        }
        userInfoManageService.updateUserSession(userSession);
        UserIdentifier userIdentifier = userInfoManageService.updateUserInfo(userSession);
        msg.setIdentifier(userIdentifier);

        switch (msg.getType()) {

            case MSGT_HEARTBEAT:
                msgQueueSender.sendHeartbeatMessage(msg);
                break;
            case MSGT_REPLY:
                break;
            case MSGT_TEXT:
                break;
            case MSGT_UNKNOWN:
                break;
            case NPT_FULL_OR_RESTRICTED_CONE_NAT:
                break;
            case NPT_SYMMETRIC_NAT:
                break;
            case NPT_START:
                break;
            case NPT_STEP_1:
                break;
            case NPT_STEP_2:
                break;
        }
//        switch (msg.getType()) {
//            case MSGT_LOGIN:
//                if (!clientPool.containsKey(getAddressStr(from))) {
//                    clientPool.put(getAddressStr(from), new Server.ClientInfo(from));
//                    System.out.println("Client " + getAddressStr(from) + " logged in");
//                    TransferUtil.udpSendMsg(server, from, Message.MessageType.MSGT_LOGIN, null);
//                } else {
//                    System.out.println("Client " + getAddressStr(from) + " failed to login");
//                    TransferUtil.udpSendMsg(server, from, Message.MessageType.MSGT_REPLY, "Login failed");
//                }
//                break;
//            case MSGT_LOGOUT:
//                if (clientPool.remove(getAddressStr(from)) != null) {
//                    System.out.println("Client " + getAddressStr(from) + " logged out");
//                    TransferUtil.udpSendMsg(server, from, Message.MessageType.MSGT_REPLY, "Logout success");
//                } else {
//                    System.out.println("Client " + getAddressStr(from) + " failed to logout");
//                    TransferUtil.udpSendMsg(server, from, Message.MessageType.MSGT_REPLY, "Logout failed");
//                }
//                break;
//            case MSGT_LIST:
//                System.out.println("Client " + getAddressStr(from) + " query list ");
//                String fromStr = getAddressStr(from);
//                String text = clientPool.keySet().stream().filter(s -> !s.equals(fromStr)).collect(Collectors.joining(","));
//                TransferUtil.udpSendMsg(server, from, Message.MessageType.MSGT_REPLY, "[" + text + "]");
//                break;
//            case MSGT_PUNCH:
//                SocketAddress other = TransferUtil.getSocketAddressFromString(msg.getBody());
//                if (other == null) {
//                    System.out.println("Address format error");
//                    break;
//                }
//                System.out.println("Client " + getAddressStr(from) + " punching to " + getAddressStr(other));
//                TransferUtil.udpSendMsg(server, other, Message.MessageType.MSGT_PUNCH, getAddressStr(from));
//                TransferUtil.udpSendMsg(server, from, Message.MessageType.MSGT_TEXT, "punch request sent");
//                break;
//            case MSGT_HEARTBEAT:
//                Server.ClientInfo client = clientPool.get(getAddressStr(from));
//                if (client != null) {
//                    client.setLastConnectDate(new Date());
//                    System.out.println("Client " + getAddressStr(from) + " alive");
//                }
//                break;
//            default:
//                TransferUtil.udpSendMsg(server, from, Message.MessageType.MSGT_REPLY, "Unknown command");
//                break;
//        }
    }
}
