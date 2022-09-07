package com.sikugeon.damda.jwt;

import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Map;
import java.util.Random;

/**
 * JWT Key를 제공하고 조회합니다.
 */
@Component
public class JwtKey {
    private static Map<String, String> SECRET_KEY_SET;
    private static String[] KID_SET;
    private static Random randomIndex = new Random();

    private static String key1, key2, key3;

    @Value("${jwt.key1}")
    private void setKey1(String key3) {
        this.key1 = key3;
    }

    @Value("${jwt.key2}")
    private void setKey2(String key3) {
        this.key2 = key3;
    }

    @Value("${jwt.key3}")
    private void setKey3(String key3) {
        this.key3 = key3;
    }

    @PostConstruct
    public void setUp() {
        this.SECRET_KEY_SET = Map.of("key1", key1, "key2", key2, "key3", key3);
        this.KID_SET = SECRET_KEY_SET.keySet().toArray(new String[0]);
    }

    /**
     * SECRET_KEY_SET 에서 랜덤한 KEY 가져오기
     *
     * @return kid와 key Pair
     */
    public static Pair<String, Key> getRandomKey() {
        String kid = KID_SET[randomIndex.nextInt(KID_SET.length)];
        String secretKey = SECRET_KEY_SET.get(kid);
        return Pair.of(kid, Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8)));
    }

    /**
     * kid로 Key찾기
     *
     * @param kid kid
     * @return Key
     */
    public static Key getKey(String kid) {
        String key = SECRET_KEY_SET.getOrDefault(kid, null);
        if (key == null)
            return null;
        return Keys.hmacShaKeyFor(key.getBytes(StandardCharsets.UTF_8));
    }
}