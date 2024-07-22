package clovider.clovider_be.global.util;

import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class RedisUtil {

    private final RedisTemplate<String, String> refreshTemplate;
    private final RedisTemplate<String, String> blackListTemplate;

    @Autowired
    public RedisUtil(@Qualifier("refreshTemplate") RedisTemplate<String, String> refreshTemplate,
            @Qualifier("blackListTemplate") RedisTemplate<String, String> blackListTemplate) {
        this.refreshTemplate = refreshTemplate;
        this.blackListTemplate = blackListTemplate;
    }

    // TODO: Email 공간 추가

    public void set(String key, String value, Long expiration) {
        refreshTemplate.opsForValue().set(key, value, expiration, TimeUnit.SECONDS);
    }

    public String get(String key) {
        return (String) refreshTemplate.opsForValue().get(key);
    }

    public boolean delete(String key) {
        return Boolean.TRUE.equals(refreshTemplate.delete(key));
    }

    public boolean hasKey(String key) {
        return Boolean.TRUE.equals(refreshTemplate.hasKey(key));
    }

    // 블랙 리스트 관련
    public void setBlackList(String key, String value, Long expiration) {
        blackListTemplate.opsForValue().set(key, value, expiration, TimeUnit.SECONDS);
    }

    public String getBlackList(String key) {
        return blackListTemplate.opsForValue().get(key);
    }

    public boolean deleteBlackList(String key) {
        return Boolean.TRUE.equals(blackListTemplate.delete(key));
    }

    public boolean hasKeyBlackList(String key) {
        return Boolean.TRUE.equals(blackListTemplate.hasKey(key));
    }

}
