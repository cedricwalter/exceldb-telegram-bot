package org.telegram;

import org.telegram.telegrambots.api.objects.User;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class BotConfig {

    Properties properties = new Properties();

    public BotConfig(String configFileName) {
        try {
            properties.load(BotConfig.class.getResourceAsStream(configFileName));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public BotConfig() {
        this("/config.properties");
    }

    private String getProperty(String token) {
        return properties.getProperty(token);
    }

    public String getUser() {
        return getProperty("user");
    }

    public String getExcel() {
        return getProperty("excel");
    }

    public String getToken() {
        return getProperty("token");
    }

    public boolean isInWhiteList(User user) {
        List<String> whiteList = getWhiteList();
        for (String name : whiteList) {
            if (user.getUserName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    public List<String> getWhiteList() {
        return Arrays.asList(getProperty("whitelisted").split(","));
    }

    public String getExcelImages() {
        return getProperty("excelImages");
    }
}
