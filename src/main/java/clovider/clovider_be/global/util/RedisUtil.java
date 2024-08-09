package clovider.clovider_be.global.util;

import java.util.Set;
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
    private final RedisTemplate<String, String> deduplicationTemplate;

    private static final String VIEWED_NOTICES_PREFIX = "noticeView:";

    @Autowired
    public RedisUtil(@Qualifier("refreshTemplate") RedisTemplate<String, String> refreshTemplate,
            @Qualifier("blackListTemplate") RedisTemplate<String, String> blackListTemplate,
            @Qualifier("emailTemplate") RedisTemplate<String, String> emailTemplate,
            @Qualifier("deduplicationTemplate") RedisTemplate<String, String> deduplicationTemplate
            ) {
        this.refreshTemplate = refreshTemplate;
        this.blackListTemplate = blackListTemplate;
        this.emailTemplate = emailTemplate;
        this.deduplicationTemplate = deduplicationTemplate;

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

    // 직원 ID를 받아서 해당 직원이 공지사항을 조회했는지 확인
    public boolean hasViewedNotice(Long noticeId, Long employeeId) {
        String key = getRedisKey(employeeId);
        Set<String> viewedNotices = deduplicationTemplate.opsForSet().members(key);
        return viewedNotices != null && viewedNotices.contains(noticeId.toString());
    }

    // 직원 ID를 받아서 공지사항을 조회한 것으로 표시
    public void markNoticeAsViewed(Long noticeId, Long employeeId) {
        String key = getRedisKey(employeeId);
        deduplicationTemplate.opsForSet().add(key, noticeId.toString());
        deduplicationTemplate.expire(key, 1, TimeUnit.DAYS); // 1일 후 만료
    }

    // 직원 ID를 사용하여 Redis 키 생성
    private String getRedisKey(Long employeeId) {
        return VIEWED_NOTICES_PREFIX + employeeId;
    }


}
