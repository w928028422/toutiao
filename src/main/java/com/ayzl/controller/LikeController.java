package com.ayzl.controller;

import com.ayzl.async.EventModel;
import com.ayzl.async.EventProducer;
import com.ayzl.async.EventType;
import com.ayzl.model.EntityType;
import com.ayzl.model.HostHolder;
import com.ayzl.service.LikeService;
import com.ayzl.service.NewsService;
import com.ayzl.util.ToutiaoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class LikeController {
    @Autowired
    private LikeService likeService;

    @Autowired
    private NewsService newsService;

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private EventProducer eventProducer;

    @RequestMapping(value = {"/like"}, method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String like(@RequestParam("newsId") int newsId){
        int userId = hostHolder.getUser().getId();
        long likeCount = likeService.like(userId, newsId, EntityType.ENTITY_NEWS);
        newsService.updateLikeCount(newsId, (int)likeCount);

        eventProducer.fireEvent(new EventModel(EventType.LIKE)
                .setActorId(hostHolder.getUser().getId()).setEntityId(newsId)
                .setEntityType(EntityType.ENTITY_NEWS).setEntityOwnerId(newsService.getById(newsId).getUserId())
        );
        return ToutiaoUtil.getJSONString(0, String.valueOf(likeCount));
    }

    @RequestMapping(value = {"/dislike"}, method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String dislike(@RequestParam("newsId") int newsId){
        int userId = hostHolder.getUser().getId();
        long likeCount = likeService.disLike(userId, newsId, EntityType.ENTITY_NEWS);
        newsService.updateLikeCount(newsId, (int)likeCount);
        return ToutiaoUtil.getJSONString(0, String.valueOf(likeCount));
    }
}
