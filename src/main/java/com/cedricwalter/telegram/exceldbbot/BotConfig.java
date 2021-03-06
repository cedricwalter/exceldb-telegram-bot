package com.cedricwalter.telegram.exceldbbot;

import org.telegram.telegrambots.meta.api.objects.User;

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

    public String getToken() {
        return getProperty("token");
    }

    public boolean isInWhiteList(User user) {
        return isInList(user, getWhiteList());
    }

    public boolean isSuperUser(User user) {
        return isInList(user, getSuperUsers());
    }

    private boolean isInList(User user, List<String> whiteList) {
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

    public List<String> getSuperUsers() {
        return Arrays.asList(getProperty("superUsers").split(","));
    }

}
