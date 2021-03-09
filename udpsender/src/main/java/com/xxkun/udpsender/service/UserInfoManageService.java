package com.xxkun.udpsender.service;

import com.xxkun.udpsender.dao.UserIdentifier;
import com.xxkun.udpsender.dao.UserSession;
import com.xxkun.udpsender.dao.UserInfo;

public interface UserInfoManageService {

    UserSession getUserSessionFromToken(String token);

    String getUserInfoFromUserId(String userId);

    UserInfo getUserInfoFromUserSession(UserInfo userInfo);

    void updateUserSession(UserSession userSession);

    UserIdentifier updateUserInfo(UserSession userInfo);

    void setUserSession(UserSession userSession);

    Long getExpire(UserSession key);
}
