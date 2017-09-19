package com.ayzl.controller;

import com.ayzl.model.*;
import com.ayzl.service.*;
import com.ayzl.util.ToutiaoUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
public class NewsController {
    @Autowired
    private NewsService newsService;

    @Autowired
    private UserService userService;

    @Autowired
    private QiniuService qiniuService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private LikeService likeService;

    @Autowired
    private HostHolder hostHolder;
    private static final Logger logger = LoggerFactory.getLogger(NewsController.class);

    @RequestMapping(value = {"/user/addNews/"}, method = {RequestMethod.POST})
    @ResponseBody
    public String addNews(@RequestParam("image") String image,
                          @RequestParam("title") String title,
                          @RequestParam("link") String link){
        try{
            String msg = newsService.addNews(image,title,link);
            return ToutiaoUtil.getJSONString(0, msg);
        }catch (Exception e){
            logger.error("添加失败!");
            return ToutiaoUtil.getJSONString(1, "添加失败");
        }
    }

    @RequestMapping(value = {"/news/{newsId}"}, method = {RequestMethod.GET})
    public String detailNews(@PathVariable("newsId")int newsId, Model model){
        News news = newsService.getById(newsId);
        List<ViewObject> commentVOs = new ArrayList<>();
        int localUserId = hostHolder.getUser() == null ? 0 : hostHolder.getUser().getId();
        if(news != null){
            List<Comment> comments = commentService.getCommentsByEntity(newsId, EntityType.ENTITY_NEWS);
            for(Comment comment: comments){
                ViewObject vo = new ViewObject();
                vo.set("comment", comment);
                vo.set("user", userService.getUser(comment.getUserId()));
                commentVOs.add(vo);
            }
        }
        if(localUserId != 0){
            model.addAttribute("like", likeService.getLikeStatus(localUserId, newsId, EntityType.ENTITY_NEWS));
        }
        else {
            model.addAttribute("like", 0);
        }
        model.addAttribute("news", news);
        model.addAttribute("owner", userService.getUser(news.getUserId()));
        model.addAttribute("comments", commentVOs);
        return "detail";
    }

    @RequestMapping(value = {"/addComment/"}, method = {RequestMethod.POST})
    @ResponseBody
    public String addComment(@RequestParam("content") String content,
                             @RequestParam("newsId") int newsId){
        try {
            Map<String, Object> commentMap = commentService.addComment(content, newsId, EntityType.ENTITY_NEWS);
            int count = commentService.getCommentCount(newsId, EntityType.ENTITY_NEWS);
            newsService.updateCommentCount(newsId, count);
            return ToutiaoUtil.getJSONString(0, commentMap);
        }catch (Exception e){
            logger.error("提交评论出错!" + e.getMessage());
            return ToutiaoUtil.getJSONString(1, "提交评论出错!");
        }
    }

    @RequestMapping(value = {"/uploadImage/"}, method = {RequestMethod.POST})
    @ResponseBody
    public String uploadImage(@RequestParam("file") MultipartFile file){
        try{
            //String fileUrl = newsService.saveImage(file);
            String fileUrl = qiniuService.saveImage(file);
            if(fileUrl == null)
                return ToutiaoUtil.getJSONString(1, "图片上传失败!");
            return ToutiaoUtil.getJSONString(0, fileUrl);
        }catch (Exception e){
            logger.error("图片上传失败!");
            return ToutiaoUtil.getJSONString(1, "图片上传失败!");
        }
    }

    @RequestMapping(value = {"/image"}, method = {RequestMethod.GET})
    @ResponseBody
    public void getImage(@RequestParam("name") String imageName,
                           HttpServletResponse response){
        response.setContentType("image/jpeg");
        try{
            StreamUtils.copy(new FileInputStream(
                    new File(ToutiaoUtil.IMAGE_DIR + imageName)),
                    response.getOutputStream());
        }catch (Exception e){
            logger.error("图片错误", e.getMessage());
        }
    }
}
