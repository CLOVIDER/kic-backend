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
    private final RedisTemplate<String, String> emailTemplate;

    @Autowired
    public RedisUtil(@Qualifier("refreshTemplate") RedisTemplate<String, String> refreshTemplate,
            @Qualifier("blackListTemplate") RedisTemplate<String, String> blackListTemplate,
            @Qualifier("emailTemplate") RedisTemplate<String, String> emailTemplate) {
        this.refreshTemplate = refreshTemplate;
        this.blackListTemplate = blackListTemplate;
        this.emailTemplate = emailTemplate;
    }

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

    // email
    public String getEmailCode(String key) {
        return emailTemplate.opsForValue().get(key);
    }

    public void setEmailCode(String key, String value) {
        emailTemplate.opsForValue().set(key, value, 3, TimeUnit.MINUTES);
    }

    public boolean hasKeyEmail(String key) {
        return Boolean.TRUE.equals(emailTemplate.hasKey(key));
    }


}
