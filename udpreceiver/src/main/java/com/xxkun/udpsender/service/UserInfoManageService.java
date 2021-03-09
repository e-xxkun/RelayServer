package com.xxkun.udpsender.service;

import com.xxkun.udpsender.dao.UserIdentifier;
import com.xxkun.udpsender.dao.UserSession;
import com.xxkun.udpsender.dao.UserInfo;

public interface UserInfoManageService {

    public UserSession getUserSessionFromToken(String token);

    public String getUserInfoFromUserId(String userId);

    public UserInfo getUserInfoFromUserSession(UserInfo userInfo);

    public void updateUserSession(UserSession userSession);

    public UserIdentifier updateUserInfo(UserSession userInfo);

    public void setUserSession(UserSession userSession);
}
