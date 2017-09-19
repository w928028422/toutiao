package com.ayzl.service;

import com.ayzl.dao.NewsDAO;
import com.ayzl.model.HostHolder;
import com.ayzl.model.News;
import com.ayzl.util.ToutiaoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class NewsService {
    @Autowired
    private NewsDAO newsDAO;

    @Autowired
    private HostHolder hostHolder;

    public List<News> getLatestNews(int userId, int offset, int limit){
        return newsDAO.selectByUserIdAndOffset(userId, offset, limit);
    }

    public News getById(int id){
        return newsDAO.getById(id);
    }

    public String saveImage(MultipartFile file) throws IOException{
        int npos = file.getOriginalFilename().lastIndexOf('.');
        if(npos < 0)
            return null;
        String fileExt = file.getOriginalFilename().substring(npos+1).toLowerCase();
        if(!ToutiaoUtil.isFileAllowed(fileExt))
            return null;

        String fileName = UUID.randomUUID().toString().replaceAll("-","") + "." + fileExt;
        Files.copy(file.getInputStream(), new File(ToutiaoUtil.IMAGE_DIR + fileName).toPath(),
                StandardCopyOption.REPLACE_EXISTING);
        return ToutiaoUtil.TOUTIAO_DOMIAN + "image?name=" + fileName;
    }

    public String addNews(String image, String title, String link){
        News news = new News();
        news.setLink(link);
        news.setTitle(title);
        news.setImage(image);
        news.setCreatedDate(new Date());
        if(hostHolder.getUser() != null){
            news.setUserId(hostHolder.getUser().getId());
        }
        else{
            news.setUserId(0);
        }
        newsDAO.addNews(news);
        return "添加成功!";
    }

    public void updateCommentCount(int newsId, int count){
        newsDAO.updateCommentCount(newsId, count);
    }

    public void updateLikeCount(int newsId, int count){
        newsDAO.updateLikeCount(newsId, count);
    }
}
