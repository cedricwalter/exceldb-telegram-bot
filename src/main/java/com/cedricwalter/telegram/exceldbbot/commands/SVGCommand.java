package com.cedricwalter.telegram.exceldbbot.commands;

import com.cedricwalter.telegram.exceldbbot.BotConfig;
import com.cedricwalter.telegram.exceldbbot.database.ExcelHelper;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.logging.BotLogger;

import java.io.File;
import java.io.IOException;
import java.util.Set;

public class SVGCommand extends WhiteListedUserBotCommand {

    private static final String LOGTAG = "SVGCOMMAND";
    private final BotConfig botConfig;
    private final ExcelHelper excelHelper;

    public SVGCommand() {
        super("svg", "return missing for poster");
        botConfig = new BotConfig();
        excelHelper = new ExcelHelper();
    }

    @Override
    public void executeWhiteListedUser(AbsSender absSender, User user, Chat chat, String[] arguments) {
        try {
            StringBuilder messageTextBuilder = new StringBuilder();

            Set<String> names = excelHelper.getUniqueColumnValues(0);
            for (String name : names) {
                if (!fileExistsCaseSensitive(botConfig.getSVGPath() + name.trim() + ".svg")) {
                    System.out.println(name.trim());
                    messageTextBuilder.append(name.trim()).append("\n");
                }
            }
            String message = messageTextBuilder.toString();
            if (message.length() > 0) {
                sendMessage(absSender, chat, "missing logo:\n" + message);
            } else {
                sendMessage(absSender, chat, "Well done!!! All logo were found!");
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