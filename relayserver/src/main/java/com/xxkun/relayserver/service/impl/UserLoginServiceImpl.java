package com.xxkun.relayserver.service.impl;

import cn.hutool.json.JSONObject;
import com.xxkun.relayserver.dao.UserInfo;
import com.xxkun.relayserver.dao.UserSession;
import com.xxkun.relayserver.dao.mbg.mapper.UserMapper;
import com.xxkun.relayserver.dao.mbg.model.User;
import com.xxkun.relayserver.dao.mbg.model.UserExample;
import com.xxkun.relayserver.service.RedisService;
import com.xxkun.relayserver.service.UserInfoManageService;
import com.xxkun.relayserver.service.UserLoginService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserLoginServiceImpl implements UserLoginService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserLoginServiceImpl.class);

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserInfoManageService userInfoManageService;

    @Override
    public String login(String userId, String password) {
        UserExample userExample = new UserExample();
        userExample.createCriteria().andUseridEqualTo(userId);
        userExample.createCriteria().andPasswordEqualTo(password);
        List<User> userList = userMapper.selectByExample(userExample);
        if (userList != null && userList.size() > 0) {
            User user = userList.get(0);
            if (userInfoManageService.isUserLogin(user.getId())) {
                return null;
            }
            UserInfo userInfo = userInfoManageService.setUser(user);
            JSONObject jsonObject = new JSONObject();
            jsonObject.putOnce("token", userInfo.getSession().getToken());
            jsonObject.putOnce("identification", userInfo.getIdentifier().toString());
            return jsonObject.toString();
        }
        return null;
    }

    @Override
    public boolean logout(String token) {
        if (!userInfoManageService.isExistUserToken(token)) {
            return false;
        }
        UserSession userSession = userInfoManageService.getUserSessionFromToken(token);
        if (userInfoManageService.isUserLogin(userSession.getUserId())) {
            return false;
        }
        userInfoManageService.removeUserFromSession(userSession);
        return true;
    }
}
