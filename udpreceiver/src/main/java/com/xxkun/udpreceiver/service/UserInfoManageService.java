package com.xxkun.udpreceiver.service;

import com.xxkun.udpreceiver.dao.UserIdentifier;
import com.xxkun.udpreceiver.dao.UserSession;
import com.xxkun.udpreceiver.dao.UserInfo;

public interface UserInfoManageService {

    public UserSession getUserSessionFromToken(String token);

    public String getUserInfoFromUserId(String userId);

    public UserInfo getUserInfoFromUserSession(UserInfo userInfo);

    public void updateUserSession(UserSession userSession);

    public UserIdentifier updateUserInfo(UserSession userInfo);

    public void setUserSession(UserSession userSession);
}
