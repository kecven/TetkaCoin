package it.kebab.coin.util;

import java.util.ArrayList;
import java.util.List;

public class Const {

    public static final String DATA_FOLDER = "./data/";
    public static final String SETTING_FILENAME = DATA_FOLDER + "setting.js";

    public static final String PRIVATE_KEY_FILENAME = DATA_FOLDER + "private.key";
    public static final String PUBLIC_KEY_FILENAME = DATA_FOLDER + "public.key";
    public static final String NODE_ADDRESS_FILENAME = DATA_FOLDER + "nodes.txt";

    public static final String ALGORITHM_FOR_HASH = "SHA-256";

    public long DIFFICULT = Long.MAX_VALUE;


    /**
     * String to hold name of the encryption algorithm.
     */
    public static final String ALGORITHM_FOR_DIGITAL_SIGNATURE = "RSA";

    public static final int KEY_SIZE = 2048;

    public static final long OLD_ADDRESS_TIME_MS = 1000 * 60 * 60 * 8;  //Адреса устаривают через 8 часов

    /**
     * алгоритм цифровой подписи
     */
    public static final String SING_ALGORITHM_FOR_DIGITAL_SIGNATURE = "SHA256withRSA";

    public static final List<String> DEFAULT_ADDRESS = new ArrayList<>(){{
        add("127.0.0.1");

        add("89.223.88.67");
    }};
}
