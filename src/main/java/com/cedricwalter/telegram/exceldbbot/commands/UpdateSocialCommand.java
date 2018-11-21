package com.cedricwalter.telegram.exceldbbot.commands;

import com.cedricwalter.telegram.exceldbbot.commands.WhiteListedUserBotCommand;
import com.cedricwalter.telegram.exceldbbot.database.ExcelHelper;
import com.cedricwalter.telegram.exceldbbot.BotConfig;
import com.cedricwalter.telegram.exceldbbot.database.ExcelIndexes;
import org.telegram.telegrambots.api.objects.Chat;
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.bots.AbsSender;
import org.telegram.telegrambots.logging.BotLogger;

import java.io.File;
import java.io.IOException;
import java.util.Set;

public class UpdateSocialCommand extends WhiteListedUserBotCommand {

    private static final String LOGTAG = "UPDATE_SOCIAL";
    private final BotConfig botConfig;
    private final ExcelHelper excelHelper;

    public UpdateSocialCommand() {
        super("updateSocial", "if you're a super user, fill up the excel with all social account using a scraper robot");
        botConfig = new BotConfig();
        excelHelper = new ExcelHelper();
    }

    @Override
    public void executeWhiteListedUser(AbsSender absSender, User user, Chat chat, String[] arguments) {
        try {

            Set<String> categories = excelHelper.getUniqueColumnValues(ExcelIndexes.URL_COLUMN_INDEX);

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