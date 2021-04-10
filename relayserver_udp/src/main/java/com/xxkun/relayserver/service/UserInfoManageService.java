package com.xxkun.relayserver.service;

import com.xxkun.relayserver.dao.UserIdentifier;
import com.xxkun.relayserver.dao.UserSession;
import com.xxkun.relayserver.dao.UserInfo;

public interface UserInfoManageService {

    UserSession getUserSessionFromToken(String token);

    UserInfo getUserInfoFromUserId(long userId);

    UserInfo getUserInfoFromUserSession(UserSession userSession);

    UserInfo refreshUserSession(UserSession userSession);

    boolean isUserSessionExpire(UserSession userSession);
}
