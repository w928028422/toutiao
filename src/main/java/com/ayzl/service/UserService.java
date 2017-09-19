package com.ayzl.service;

import com.ayzl.dao.LoginTicketDAO;
import com.ayzl.dao.UserDAO;
import com.ayzl.model.LoginTicket;
import com.ayzl.model.User;
import com.ayzl.util.ToutiaoUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService {
    @Autowired
    private UserDAO userDAO;

    @Autowired
    private LoginTicketDAO loginTicketDAO;

    public User getUser(int id){
        return userDAO.selectById(id);
    }

    public Map<String, Object> register(String username, String password){
        Map<String, Object> map = new HashMap<>();
        if(StringUtils.isBlank(username)){
            map.put("msgname", "用户名不能为空");
            return map;
        }
        if(StringUtils.isBlank(password)){
            map.put("msgpwd", "密码不能为空");
            return map;
        }

        User user = userDAO.selectByName(username);
        if(user != null){
            map.put("msgname", "用户名已经存在!");
            return map;
        }

        user = new User();
        user.setName(username);
        user.setSalt(UUID.randomUUID().toString().replaceAll("-", "").substring(0, 5));
        user.setPassword(ToutiaoUtil.MD5(password + user.getSalt()));
        user.setHeadUrl(String.format("https://images.nowcoder.com/head/%dt.png", new Random().nextInt(1000)));
        userDAO.addUser(user);

        map.put("ticket", addLoginTicket(user.getId()));
        return map;
    }

    public void logout(String ticket){
        loginTicketDAO.updateStatus(ticket, 1);
    }


    public Map<String, Object> login(String username, String password){
        Map<String, Object> map = new HashMap<>();
        if(StringUtils.isBlank(username)){
            map.put("msgname", "用户名不能为空");
            return map;
        }
        if(StringUtils.isBlank(password)){
            map.put("msgpwd", "密码不能为空");
            return map;
        }

        User user = userDAO.selectByName(username);
        if(user == null){
            map.put("msgname", "用户名不存在!");
            return map;
        }

        if(!ToutiaoUtil.MD5(password + user.getSalt()).equals(user.getPassword())){
            map.put("msgpwd", "密码错误!");
            return map;
        }

        map.put("userId", user.getId());
        map.put("ticket", addLoginTicket(user.getId()));
        return map;
    }

    private String addLoginTicket(int userId){
        LoginTicket ticket = new LoginTicket();
        ticket.setTicket(UUID.randomUUID().toString().replaceAll("-", ""));
        Date date = new Date();
        date.setTime(date.getTime() + 1000*3600*12);
        ticket.setExpired(date);
        ticket.setUserId(userId);
        ticket.setStatus(0);
        loginTicketDAO.addTicket(ticket);
        return ticket.getTicket();
    }
}
