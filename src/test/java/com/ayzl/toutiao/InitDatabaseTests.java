package com.ayzl.toutiao;

import com.ayzl.ToutiaoApplication;
import com.ayzl.dao.CommentDAO;
import com.ayzl.dao.LoginTicketDAO;
import com.ayzl.dao.NewsDAO;
import com.ayzl.dao.UserDAO;
import com.ayzl.model.*;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Random;
import java.util.UUID;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ToutiaoApplication.class)
@Sql("/init-schema.sql")
public class InitDatabaseTests {
    @Autowired
    private UserDAO userDAO;

    @Autowired
    private NewsDAO newsDAO;

    @Autowired
    private LoginTicketDAO loginTicketDAO;

    @Autowired
    private CommentDAO commentDAO;

    @Test
    public void initData() {
        Random random = new Random();
        for (int i = 0; i < 30; ++i) {
            User user = new User();
            user.setHeadUrl(String.format("http://images.nowcoder.com/head/%dt.png", random.nextInt(1000)));
            user.setName(String.format("USER%d", i));
            user.setPassword("");
            user.setSalt("");
            userDAO.addUser(user);

            user.setPassword("new" + String.valueOf(i));
            userDAO.updatePassword(user);

            News news = new News();
            news.setCommentCount(3);
            Date date = new Date();
            date.setTime(date.getTime() + 1000*3600*i*4);
            news.setCreatedDate(date);
            news.setImage(String.format("http://images.nowcoder.com/head/%dm.png", random.nextInt(1000)));
            news.setLikeCount(random.nextInt(100));
            news.setUserId(i+1);
            news.setTitle(String.format("Title{%d}", i));
            news.setLink(String.format("http://www.nowcoder.com/%d.html", i));
            newsDAO.addNews(news);

            LoginTicket loginTicket = new LoginTicket();
            loginTicket.setStatus(0);
            loginTicket.setUserId(i+1);
            loginTicket.setExpired(date);
            loginTicket.setTicket(UUID.randomUUID().toString());
            loginTicketDAO.addTicket(loginTicket);

            for(int j = 0; j < 3; j++){
                Comment comment = new Comment();
                comment.setStatus(0);
                comment.setCreatedDate(new Date());
                comment.setEntityType(EntityType.ENTITY_NEWS);
                comment.setEntityId(news.getId());
                comment.setContent(UUID.randomUUID().toString());
                comment.setUserId(user.getId());
                commentDAO.addComment(comment);
            }
        }

        Assert.assertEquals("new0", userDAO.selectById(1).getPassword());
    }

}
