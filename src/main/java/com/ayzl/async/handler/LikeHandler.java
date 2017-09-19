package com.ayzl.async.handler;

import com.ayzl.async.EventHandler;
import com.ayzl.async.EventModel;
import com.ayzl.async.EventType;
import com.ayzl.model.User;
import com.ayzl.service.MessageService;
import com.ayzl.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class LikeHandler implements EventHandler {
    @Autowired
    private MessageService messageService;

    @Autowired
    private UserService userService;

    @Override
    public void doHandle(EventModel model) {
        User user = userService.getUser(model.getActorId());
        String content = String.format("用户%s赞了你的资讯,http://127.0.0.1:8080/news/%d", user.getName(), model.getEntityId());
        messageService.addMessage(1, model.getEntityOwnerId(), content);
    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(EventType.LIKE);
    }
}
