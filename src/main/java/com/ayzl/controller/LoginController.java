package com.ayzl.controller;

import com.ayzl.async.EventModel;
import com.ayzl.async.EventProducer;
import com.ayzl.async.EventType;
import com.ayzl.service.UserService;
import com.ayzl.util.ToutiaoUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@Controller
public class LoginController {
    @Autowired
    private UserService userService;

    @Autowired
    private EventProducer eventProducer;
    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @RequestMapping(value = {"/reg/"}, method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String register(Model model,
                           @RequestParam("username") String username,
                           @RequestParam("password") String password,
                           @RequestParam(value = "remember", defaultValue = "0") int rememberme,
                           HttpServletResponse response){
        try{
            Map<String, Object> map = userService.register(username, password);
            if(map.containsKey("ticket")){
                Cookie cookie = new Cookie("ticket", map.get("ticket").toString());
                cookie.setPath("/");
                if(rememberme > 0){
                    cookie.setMaxAge(3600*24*7);
                }
                response.addCookie(cookie);
                return ToutiaoUtil.getJSONString(0, "注册成功!");
            }else{
                return ToutiaoUtil.getJSONString(1, map);
            }

        }catch (Exception e){
            logger.error("注册异常!" + e.getMessage());
            return ToutiaoUtil.getJSONString(1, "注册异常!");
        }
    }

    @RequestMapping(value = {"/login/"}, method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String login(Model model,
                           @RequestParam("username") String username,
                           @RequestParam("password") String password,
                           @RequestParam(value = "remember", defaultValue = "0") int rememberme,
                           HttpServletResponse response){
        //try{
            Map<String, Object> map = userService.login(username, password);
            if(map.containsKey("ticket")){
                Cookie cookie = new Cookie("ticket", map.get("ticket").toString());
                cookie.setPath("/");
                if(rememberme > 0){
                    cookie.setMaxAge(3600*24*7);
                }
                response.addCookie(cookie);
                eventProducer.fireEvent(new EventModel(EventType.LOGIN).setActorId((int)map.get("userId"))
                        .setExt("username", username).setExt("email", "928028422@qq.com")
                );
                return ToutiaoUtil.getJSONString(0, "登录成功!");
            }else{
                return ToutiaoUtil.getJSONString(1, map);
            }

        /*}catch (Exception e){
            logger.error("登录异常!" + e.getMessage());
            return ToutiaoUtil.getJSONString(1, "登录异常!");
        }*/
    }

    @RequestMapping(value = {"/logout/"})
    public String logout(@CookieValue("ticket") String ticket){
        userService.logout(ticket);
        return "redirect:/";
    }
}
