package com.cedricwalter.telegram.exceldbbot.commands;

import com.cedricwalter.telegram.exceldbbot.BotConfig;
import com.cedricwalter.telegram.exceldbbot.database.ExcelHelper;
import com.cedricwalter.telegram.exceldbbot.database.ExcelIndexes;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.logging.BotLogger;

import java.io.File;
import java.io.IOException;
import java.util.Set;

public class InCVLCommand extends WhiteListedUserBotCommand {

    private static final String LOGTAG = "CVLCOMMAND";
    private final BotConfig botConfig;
    private final ExcelHelper excelHelper;

    public InCVLCommand() {
        super("inCVL", "return list of companies in CryptoValley Labs");
        botConfig = new BotConfig();
        excelHelper = new ExcelHelper();
    }

    @Override
    public void executeWhiteListedUser(AbsSender absSender, User user, Chat chat, String[] arguments) {
        try {
            StringBuilder messageTextBuilder = new StringBuilder();

            Set<String> names = excelHelper.getNameForColumnMatching(ExcelIndexes.IN_CVL_COLUMN_INDEX, "TRUE");
            int i = 1;
            for (String name : names) {
                messageTextBuilder.append(" " + i++ + " " + name.trim()).append("\n");

                if (i == 10) {
                    sendMessage(absSender, chat, "In CryptoValley Labs:\n" + messageTextBuilder.toString());
                    messageTextBuilder = new StringBuilder();
                } else if (i % 10 == 0) {
                    sendMessage(absSender, chat, messageTextBuilder.toString());
                    messageTextBuilder = new StringBuilder();
                }

            }
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