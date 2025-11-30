package com.example.carespawbe.service.Forum;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class ViewLimiterService {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    public boolean isAllowedToView(Long postId, String ipAddress) {
        String key = "view:" + postId + ":" + ipAddress;

        boolean isExist = stringRedisTemplate.hasKey(key);
        if (isExist) {
            return false;
        }

        stringRedisTemplate.opsForValue().set(key, "1", Duration.ofHours(24));
        return true;
    }
}
