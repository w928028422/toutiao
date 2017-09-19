package com.ayzl.service;

import com.ayzl.dao.CommentDAO;
import com.ayzl.model.Comment;
import com.ayzl.model.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CommentService {
    @Autowired
    private CommentDAO commentDAO;

    @Autowired
    private HostHolder hostHolder;

    public List<Comment> getCommentsByEntity(int entityId, int entityType){
        return commentDAO.selectByEntity(entityId, entityType);
    }

    public Map<String, Object> addComment(String content, int entityId, int entityType){
        Map<String, Object> commentMap = new HashMap<>();
        Comment comment = new Comment();
        comment.setUserId(hostHolder.getUser().getId());
        comment.setContent(content);
        comment.setEntityId(entityId);
        comment.setEntityType(entityType);
        comment.setCreatedDate(new Date());
        comment.setStatus(0);
        commentDAO.addComment(comment);
        commentMap.put("content", content);
        commentMap.put("headUrl", hostHolder.getUser().getHeadUrl());
        commentMap.put("createdDate", comment.getCreatedDate());
        commentMap.put("userName", hostHolder.getUser().getName());
        return commentMap;
    }

    public int getCommentCount(int entityId, int entityType){
        return commentDAO.getCommentCount(entityId, entityType);
    }

    public int deleteComment(int id){
        commentDAO.deleteComment(id);
        return 0;
    }
}
