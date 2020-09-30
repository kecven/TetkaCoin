package it.kebab.coin.util;

import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

@Component
public class NetworkUtil {

    public static Map<String, Long> allAddress;

    @PostConstruct
    private void init() throws IOException {
        allAddress = getAllAddress();
    }

    public Map<String, Long> getAllAddress() throws IOException {
        Map<String, Long> result = new HashMap<>();


        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(Const.NODE_ADDRESS_FILENAME))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] addresses = line.split(" ");
                if (addresses.length != 2){
                    continue;
                }
                String address = addresses[0];
                Long timeAddress = new Long(addresses[1]);

                result.put(address, timeAddress);
            }
        }
        return result;
    }

    public void updateAddress(Map<String, Long> updateAddress) {
        Long nowMs = System.currentTimeMillis();

        updateAddress.forEach((address, time) -> {
            if (time > nowMs){
                time = nowMs;
            } else if (nowMs - time > Const.OLD_ADDRESS_TIME_MS) {
                return; //continue
            }
            if (allAddress.containsKey(address)){
                if (allAddress.get(address) < time){
                    allAddress.put(address, time);
                }
            } else {
                allAddress.put(address, time);
            }
        });
    }

    public void clearOldAddress() {
        Long nowMs = System.currentTimeMillis();

        allAddress.forEach((address, time) -> {
            if (nowMs - time > Const.OLD_ADDRESS_TIME_MS) {
                allAddress.remove(address);
            }
        });
    }

    public void save() throws IOException {
        clearOldAddress();

        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(Const.NODE_ADDRESS_FILENAME))) {
            allAddress.forEach((address, time) -> {
                try {
                    bufferedWriter.write(address + " " + time);
                    bufferedWriter.newLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
    }

}
