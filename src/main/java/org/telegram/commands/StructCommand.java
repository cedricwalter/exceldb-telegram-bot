package org.telegram.commands;

import com.cedricwalter.telegram.exceldbbot.ExcelHelper;
import org.telegram.BotConfig;
import org.telegram.telegrambots.api.objects.Chat;
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.bots.AbsSender;
import org.telegram.telegrambots.logging.BotLogger;

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

            Set<String> names = excelHelper.getStruct(botConfig.getExcel());
            List<String> sorted = new ArrayList<>(names.size());
            sorted.addAll(names);
            Collections.sort(sorted);

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