package com.cedricwalter.telegram.exceldbbot.commands;

import com.cedricwalter.telegram.exceldbbot.BotConfig;
import com.cedricwalter.telegram.exceldbbot.database.ExcelHelper;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.logging.BotLogger;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class StructCommand extends WhiteListedUserBotCommand {

    private static final String LOGTAG = "STRUCTCOMMAND";
    private final BotConfig botConfig;
    private final ExcelHelper excelHelper;

    public StructCommand() {
        super("struct", "return structure");
        botConfig = new BotConfig();
        excelHelper = new ExcelHelper();
    }

    @Override
    public void executeWhiteListedUser(AbsSender absSender, User user, Chat chat, String[] arguments) {
        try {
            StringBuilder messageTextBuilder = new StringBuilder();

            Set<String> names = excelHelper.getStruct();
            List<String> sorted = new ArrayList<>(names.size());
            sorted.addAll(names);
            Collections.sort(sorted);
            sorted.remove("category|sub-category");

            for (String name : sorted) {
                messageTextBuilder.append(name).append("\n");
            }
            String message = messageTextBuilder.toString();


            sendMessage(absSender, chat, "Structure:\n" + message);

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