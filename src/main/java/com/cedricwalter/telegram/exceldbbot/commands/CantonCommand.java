package com.cedricwalter.telegram.exceldbbot.commands;

import com.cedricwalter.telegram.exceldbbot.database.ExcelHelper;
import com.cedricwalter.telegram.exceldbbot.database.ExcelIndexes;
import com.cedricwalter.telegram.exceldbbot.BotConfig;
import org.telegram.telegrambots.api.objects.Chat;
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.bots.AbsSender;
import org.telegram.telegrambots.logging.BotLogger;

import java.io.File;
import java.io.IOException;
import java.util.Set;

public class CantonCommand extends WhiteListedUserBotCommand {

    private static final String LOGTAG = "LPCOMMAND";
    private final BotConfig botConfig;
    private final ExcelHelper excelHelper;

    public CantonCommand() {
        super("incanton", "return list of companies in Canton, ex: /incanton ZH");
        botConfig = new BotConfig();
        excelHelper = new ExcelHelper();
    }

    @Override
    public void executeWhiteListedUser(AbsSender absSender, User user, Chat chat, String[] arguments) {
        try {
            StringBuilder messageTextBuilder = new StringBuilder();

            if (arguments != null && arguments.length > 0) {
                String canton = arguments[0];
                Set<String> names = excelHelper.getNameForColumnMatching(ExcelIndexes.CANTON_COLUMN_INDEX, canton);
                int size = names.size();
                if (size > 0) {
                    int i = 1;
                    for (String name : names) {
                        messageTextBuilder.append(" " + i++ + " " + name.trim()).append("\n");

                        if (i % 10 == 0) {
                            String message = messageTextBuilder.toString();
                            sendMessage(absSender, chat, message);
                            messageTextBuilder = new StringBuilder();
                        } else if (size < 10){
                            String message = messageTextBuilder.toString();
                            sendMessage(absSender, chat, message);
                        }
                    }
                } else {
                    sendMessage(absSender, chat, "No companies found in canton " + canton);
                }

            } else {
                sendMessage(absSender, chat, "Provide at least a search pattern, e.g /incanton ZH");
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