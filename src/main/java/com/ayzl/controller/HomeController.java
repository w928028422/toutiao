package com.ayzl.controller;

import com.ayzl.model.EntityType;
import com.ayzl.model.HostHolder;
import com.ayzl.model.News;
import com.ayzl.model.ViewObject;
import com.ayzl.service.LikeService;
import com.ayzl.service.NewsService;
import com.ayzl.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
public class HomeController {
    @Autowired
    NewsService newsService;

    @Autowired
    UserService userService;

    @Autowired
    LikeService likeService;

    @Autowired
    HostHolder hostHolder;

    private List<ViewObject> getViews(int userId, int offset, int limit){
        List<News> newsList = newsService.getLatestNews(userId, offset, limit);
        int localUserId = hostHolder.getUser() == null ? 0 : hostHolder.getUser().getId();
        List<ViewObject> viewObjects = new ArrayList<>();
        for(News news : newsList){
            ViewObject viewObject = new ViewObject();
            viewObject.set("news", news);
            viewObject.set("user", userService.getUser(news.getUserId()));
            if(localUserId != 0){
                viewObject.set("like", likeService.getLikeStatus(localUserId, news.getId(), EntityType.ENTITY_NEWS));
            }
            else{
                viewObject.set("like", 0);
            }
            viewObjects.add(viewObject);
        }
        return viewObjects;
    }

    @RequestMapping(value = {"/", "/index"}, method = {RequestMethod.GET, RequestMethod.POST})
    public String index(Model model, @RequestParam(value = "pop", defaultValue = "0") int pop,
                        @RequestParam(value = "url", defaultValue = "") String url){
        model.addAttribute("views", getViews(0, 0, 10));
        model.addAttribute("pop", pop);
        model.addAttribute("url", url);
        return "home";
    }

    @RequestMapping(value = {"/user/{userId}"}, method = {RequestMethod.GET, RequestMethod.POST})
    public String user(Model model, @PathVariable("userId") int userId){
        model.addAttribute("views", getViews(userId, 0, 10));
        return "home";
    }
}
