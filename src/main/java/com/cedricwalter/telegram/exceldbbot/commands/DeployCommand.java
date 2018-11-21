package com.cedricwalter.telegram.exceldbbot.commands;

import com.cedricwalter.telegram.exceldbbot.commands.WhiteListedUserBotCommand;
import com.cedricwalter.telegram.exceldbbot.database.ExcelHelper;
import com.cedricwalter.telegram.exceldbbot.BotConfig;
import org.telegram.telegrambots.api.objects.Chat;
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.bots.AbsSender;
import org.telegram.telegrambots.logging.BotLogger;

import java.io.File;
import java.io.IOException;

public class DeployCommand extends WhiteListedUserBotCommand {

    private static final String LOGTAG = "DEPLOY";
    private final BotConfig botConfig;
    private final ExcelHelper excelHelper;

    public DeployCommand() {
        super("deploy", "if you're a super user, you can deploy latest cvmap.ch updates");
        botConfig = new BotConfig();
        excelHelper = new ExcelHelper();
    }

    @Override
    public void executeWhiteListedUser(AbsSender absSender, User user, Chat chat, String[] arguments) {
        try {
            StringBuilder messageTextBuilder = new StringBuilder();
            sendMessage(absSender, chat, messageTextBuilder.toString());
            messageTextBuilder = new StringBuilder();
        } catch (Exception e) {
            BotLogger.error(LOGTAG, e);
        }
    }

    public static boolean fileExistsCaseSensitive(String path) {
        try {
            File file = new File(path);
            return file.exists() && file.getCanonicalFile().getName().equals(file.getName());
        } catch (IOException e) {
            return false;
        }
    }


}