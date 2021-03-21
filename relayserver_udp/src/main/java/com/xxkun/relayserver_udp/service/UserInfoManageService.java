package com.xxkun.relayserver_udp.service;

import com.xxkun.relayserver_udp.dao.UserIdentifier;
import com.xxkun.relayserver_udp.dao.UserSession;
import com.xxkun.relayserver_udp.dao.UserInfo;

public interface UserInfoManageService {

    public UserSession getUserSessionFromToken(String token);

    public String getUserInfoFromUserId(String userId);

    public UserInfo getUserInfoFromUserSession(UserInfo userInfo);

    public void updateUserSession(UserSession userSession);

    public UserIdentifier updateUserInfo(UserSession userInfo);

    public void setUserSession(UserSession userSession);
}
