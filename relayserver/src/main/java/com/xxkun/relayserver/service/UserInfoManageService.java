package com.xxkun.relayserver.service;

import com.xxkun.relayserver.dao.UserInfo;
import com.xxkun.relayserver.dao.UserSession;
import com.xxkun.relayserver.dao.mbg.model.User;

public interface UserInfoManageService {


    boolean isUserLogin(long id);

    boolean isExistUserToken(String token);

    UserInfo setUser(User user);

    UserSession getUserSessionFromToken(String token);

    void removeUserFromSession(UserSession userSession);
}
