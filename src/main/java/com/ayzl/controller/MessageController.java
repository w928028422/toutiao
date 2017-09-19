package com.ayzl.controller;

import com.ayzl.model.HostHolder;
import com.ayzl.model.Message;
import com.ayzl.model.User;
import com.ayzl.model.ViewObject;
import com.ayzl.service.MessageService;
import com.ayzl.service.UserService;
import com.ayzl.util.ToutiaoUtil;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
public class MessageController {
    @Autowired
    private MessageService messageService;

    @Autowired
    private UserService userService;

    @Autowired
    private HostHolder hostHolder;

    private static final Logger logger = LoggerFactory.getLogger(MessageController.class);

    @RequestMapping(value = {"/msg/addMessage"}, method = {RequestMethod.POST})
    @ResponseBody
    public String addMessage(@RequestParam("fromId") int fromId,
                             @RequestParam("toId") int toId,
                             @RequestParam("content") String content){
        try {
            messageService.addMessage(fromId, toId, content);
            return ToutiaoUtil.getJSONString(0);
        }catch (Exception e){
            logger.error("添加消息错误!" + e.getMessage());
            return ToutiaoUtil.getJSONString(1, "添加消息错误!");
        }
    }

    @RequestMapping(value = {"/msg/detail"}, method = {RequestMethod.GET})
    public String conversationDetail(@Param("conversationId") String conversationId, Model model){
        List<ViewObject> messages = new ArrayList<>();
        try {
             List<Message> messageList = messageService.getConversationDetail(conversationId, 0, 10);
             for(Message msg: messageList){
                 ViewObject vo = new ViewObject();
                 vo.set("message", msg);
                 User user = userService.getUser(msg.getFromId());
                 if(user == null){
                     continue;
                 }
                 vo.set("userId", user.getId());
                 vo.set("headUrl", user.getHeadUrl());
                 messages.add(vo);
             }
        }catch (Exception e){
            logger.error("获取消息失败!");
        }
        model.addAttribute("messages", messages);
        return "letterDetail";
    }

    @RequestMapping(value = {"/msg/list"}, method = {RequestMethod.GET})
    public String conversationList(Model model){
        List<ViewObject> conversations = new ArrayList<>();
        try{
            int userId = hostHolder.getUser().getId();
            List<Message> messages = messageService.getConversationList(userId, 0, 10);
            for(Message msg: messages){
                ViewObject vo = new ViewObject();
                vo.set("conversation", msg);
                int targetId = (userId == msg.getFromId())? msg.getToId():msg.getFromId();
                User user = userService.getUser(targetId);
                vo.set("headUrl", user.getHeadUrl());
                vo.set("userName", user.getName());
                vo.set("userId", targetId);
                vo.set("unreadCount", messageService.getUnreadCount(userId, msg.getConversationId()));
                conversations.add(vo);
            }
        }catch (Exception e){
            logger.error("获取消息列表失败!" + e.getMessage());
        }
        model.addAttribute("conversations", conversations);
        return "letter";
    }

}
