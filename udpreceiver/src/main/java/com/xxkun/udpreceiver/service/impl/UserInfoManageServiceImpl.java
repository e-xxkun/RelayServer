package com.xxkun.udpreceiver.service.impl;

import com.xxkun.udpreceiver.dao.UserIdentifier;
import com.xxkun.udpreceiver.dao.UserInfo;
import com.xxkun.udpreceiver.dao.UserSession;
import com.xxkun.udpreceiver.service.UserInfoManageService;

public class UserInfoManageServiceImpl implements UserInfoManageService {
    @Override
    public UserSession getUserSessionFromToken(String token) {
        return null;
    }

    @Override
    public String getUserInfoFromUserId(String userId) {
        return null;
    }

    @Override
    public UserInfo getUserInfoFromUserSession(UserInfo userInfo) {
        return null;
    }

    @Override
    public void updateUserSession(UserSession userSession) {

    }

    @Override
    public UserIdentifier updateUserInfo(UserSession userInfo) {

        return null;
    }

    @Override
    public void setUserSession(UserSession userSession) {

    }
}
