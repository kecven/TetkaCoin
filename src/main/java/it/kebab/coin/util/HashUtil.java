package it.kebab.coin.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Component
public class HashUtil {

    private static final Logger log = LoggerFactory.getLogger(HashUtil.class);

    public String generate(String input) {
        return generate(input.getBytes());
    }

    public String generate(byte[] input) {
        MessageDigest mDigest = null;
        try {
            mDigest = MessageDigest.getInstance(Const.ALGORITHM_FOR_HASH);
        } catch (NoSuchAlgorithmException e) {
            log.error(Const.ALGORITHM_FOR_HASH + " not found", e);
        }
        byte[] result = mDigest.digest(input);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < result.length; i++) {
            sb.append(Integer.toString((result[i] & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();
    }
}
