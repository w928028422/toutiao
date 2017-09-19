package com.ayzl.service;

import com.ayzl.dao.MessageDAO;
import com.ayzl.model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class MessageService {
    @Autowired
    private MessageDAO messageDAO;

    public int addMessage(int fromId, int toId, String content){
        Message message = new Message();
        message.setContent(content);
        message.setFromId(fromId);
        message.setToId(toId);
        message.setCreatedDate(new Date());
        message.setHasRead(0);
        message.setConversationId(String.format("%d_%d", Math.min(fromId, toId), Math.max(fromId, toId)));
        messageDAO.addMessage(message);
        return 0;
    }

    public List<Message> getConversationDetail(String conversationId, int offset, int limit){
        return messageDAO.getConversationDetail(conversationId, offset, limit);
    }

    public List<Message> getConversationList(int userId, int offset, int limit){
        return messageDAO.getConversationList(userId, offset, limit);
    }

    public int getUnreadCount(int userId, String conversationId){
        return messageDAO.getUnreadCount(userId, conversationId);
    }
}
