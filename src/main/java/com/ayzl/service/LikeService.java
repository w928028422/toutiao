package com.ayzl.service;

import com.ayzl.util.JedisAdapter;
import com.ayzl.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LikeService {
    @Autowired
    private JedisAdapter jedisAdapter;

    /**
     * 喜欢返回1，不喜欢返回-1，否则返回0
     * @param userId
     * @param entityId
     * @param entityType
     * @return
     */
    public int getLikeStatus(int userId, int entityId, int entityType){
        String likeKey = RedisKeyUtil.getLikeKey(entityId, entityType);
        if(jedisAdapter.sismember(likeKey, String.valueOf(userId))){
            return 1;
        }
        String disLikeKey = RedisKeyUtil.getDisLikeKey(entityId, entityType);
        if(jedisAdapter.sismember(disLikeKey, String.valueOf(userId))){
            return -1;
        }
        return 0;
    }

    public long like(int userId, int entityId, int entityType){
        String likeKey = RedisKeyUtil.getLikeKey(entityId, entityType);
        jedisAdapter.sadd(likeKey, String.valueOf(userId));
        String disLikeKey = RedisKeyUtil.getDisLikeKey(entityId, entityType);
        jedisAdapter.srem(disLikeKey, String.valueOf(userId));
        return jedisAdapter.scard(likeKey);
    }

    public long disLike(int userId, int entityId, int entityType){
        String disLikeKey = RedisKeyUtil.getDisLikeKey(entityId, entityType);
        jedisAdapter.sadd(disLikeKey, String.valueOf(userId));
        String likeKey = RedisKeyUtil.getLikeKey(entityId, entityType);
        jedisAdapter.srem(likeKey, String.valueOf(userId));
        return jedisAdapter.scard(likeKey);
    }
}
