package it.kebab.coin.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;


@Component
public class Setting {

    private static final Logger log = LoggerFactory.getLogger(Setting.class);

    private int userTransactionNumber;

    public int getUserTransactionNumber() {
        return userTransactionNumber;
    }

    public void setUserTransactionNumber(int userTransactionNumber) {
        this.userTransactionNumber = userTransactionNumber;
    }

    public void setUserTransactionNumberAndSave(int userTransactionNumber) throws IOException {
        setUserTransactionNumber(userTransactionNumber);
        save();
    }

    @PostConstruct
    private void init() throws IOException {
    }

    private void setDefaultSetting(){
        userTransactionNumber = 0;
    }

    public void save() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        mapper.writeValue(new FileOutputStream(Const.SETTING_FILENAME), this);
    }

    public void read() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Setting setting = (Setting) mapper.readValue(new FileInputStream(Const.SETTING_FILENAME),
                Setting.class);

        userTransactionNumber = setting.userTransactionNumber;
    }


}
