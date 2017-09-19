package com.ayzl.controller;

import com.ayzl.model.User;
import com.ayzl.service.ToutiaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.*;

import static com.ayzl.aspect.LogAspect.logger;

@Controller
public class IndexController {
    @Autowired
    private ToutiaoService toutiaoService;

    @RequestMapping(path = {"/", "/index"})
    @ResponseBody
    public String index(HttpSession session){
        logger.info("Visit Index");
        return "Hello world " + session.getAttribute("msg") + "<br>" + toutiaoService.say();
    }

    @RequestMapping(value = {"/profile/{groupId}/{userId}"})
    @ResponseBody
    public String profile(@PathVariable("groupId") String groupId,
                          @PathVariable("userId") int userId,
                          @RequestParam(value = "type", defaultValue = "1") int type,
                          @RequestParam(value = "key", defaultValue = "ayzl") String key){
        return String.format("{%s}:{%d}, [%d]:[%s]", groupId, userId, type, key);
    }

    @RequestMapping(value = {"/vm"})
    public String news(Model model){
        model.addAttribute("value1", "vvl");
        List<String> color = Arrays.asList(new String[] {"red", "yellow", "green"});
        Map<String, String> map = new HashMap<>();
        for (int i = 0; i < color.size(); i++) {
            map.put(String.valueOf(i), color.get(i));
        }
        model.addAttribute("maps", map);
        model.addAttribute("colors", color);
        model.addAttribute("user", new User("UKOP"));
        return "news";
    }

    @RequestMapping(value = {"/request"})
    @ResponseBody
    public String request(HttpServletRequest request,
                          HttpServletResponse response,
                          HttpSession session){
        StringBuilder sb = new StringBuilder();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()){
            String name = headerNames.nextElement();
            sb.append(name + ":" + request.getHeader(name) + "<br>");
        }
        for(Cookie cookie : request.getCookies()){
            sb.append("Cookie");
            sb.append(cookie.getName());
            sb.append(":");
            sb.append(cookie.getValue());
            sb.append("<br>");
        }
        sb.append("getMethod:" + request.getMethod() + "<br>");
        sb.append("getPathInfo:" + request.getPathInfo() + "<br>");
        sb.append("getQueryString" + request.getQueryString() + "<br>");
        sb.append("getRequestURI:" + request.getRequestURI() + "<br>");
        return sb.toString();
    }

    @RequestMapping(value = {"/response"})
    @ResponseBody
    public String response(@CookieValue(value = "ayzlId", defaultValue = "556") String ayzlId,
                           @RequestParam(value = "key", defaultValue = "key") String key,
                           @RequestParam(value = "value", defaultValue = "value") String value,
                           HttpServletResponse response){
        response.addCookie(new Cookie(key, value));
        response.addHeader(key, value);
        response.addDateHeader("date", new Date().getTime());
        return "sessionId from Cookie:" + ayzlId;
    }

    @RequestMapping(value = {"/redirect/{code}"})
    public RedirectView redirect(@PathVariable("code") int code,
                                 HttpSession session){
        RedirectView redirectView = new RedirectView("/", true);
        if(code == 301){
            redirectView.setStatusCode(HttpStatus.MOVED_PERMANENTLY);
        }
        session.setAttribute("msg", "jump from redirect");
        return redirectView;
    }

    @RequestMapping("/admin")
    @ResponseBody
    public String admin(@RequestParam(value = "key", required = false) String key){
        if("admin".equals(key)){
            return "hello admin";
        }
        throw new IllegalArgumentException("key错误!");
    }

    @ExceptionHandler
    @ResponseBody
    public String error(Exception e){
        return "error! " + e.getMessage();
    }
}
