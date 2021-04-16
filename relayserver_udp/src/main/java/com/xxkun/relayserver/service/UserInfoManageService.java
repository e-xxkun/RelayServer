package com.xxkun.relayserver.service;

import com.xxkun.relayserver.pojo.user.UserSession;
import com.xxkun.relayserver.pojo.user.UserInfo;

public interface UserInfoManageService {

    UserSession getUserSessionFromToken(String token);

    UserInfo getUserInfoFromUserId(long userId);

    UserInfo getUserInfoFromUserSession(UserSession userSession);

    UserInfo refreshUserSession(UserSession userSession);

    boolean isUserSessionExpire(UserSession userSession);
}
