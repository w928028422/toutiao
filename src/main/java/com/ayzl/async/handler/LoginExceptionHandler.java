package com.ayzl.async.handler;

import com.ayzl.async.EventHandler;
import com.ayzl.async.EventModel;
import com.ayzl.async.EventType;
import com.ayzl.service.MessageService;
import com.ayzl.util.MailSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class LoginExceptionHandler implements EventHandler {
    @Autowired
    private MessageService messageService;

    @Autowired
    private MailSender mailSender;

    @Override
    public void doHandle(EventModel model) {
        String content = "你上次的登录ip异常";
        messageService.addMessage(1, model.getActorId(), content);
        /*Map<String, Object> map = new HashMap<>();
        map.put("username", model.getExt("username"));
        mailSender.sendWithHTMLTemplate(model.getExt("email"), "登录异常", "welcome.ftl", map);*/
    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(EventType.LOGIN);
    }
}
