package org.telegram.commands;

import com.cedricwalter.telegram.exceldbbot.ExcelHelper;
import com.cedricwalter.telegram.exceldbbot.ExcelIndexes;
import org.telegram.BotConfig;
import org.telegram.telegrambots.api.objects.Chat;
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.bots.AbsSender;
import org.telegram.telegrambots.logging.BotLogger;

import java.io.File;
import java.io.IOException;
import java.util.Set;

public class InLPCommand extends WhiteListedUserBotCommand {

    private static final String LOGTAG = "CVLCOMMAND";
    private final BotConfig botConfig;
    private final ExcelHelper excelHelper;

    public InLPCommand() {
        super("inlp", "return list of companies in LakeSide Partners");
        botConfig = new BotConfig();
        excelHelper = new ExcelHelper();
    }

    @Override
    public void executeWhiteListedUser(AbsSender absSender, User user, Chat chat, String[] arguments) {
        try {
            StringBuilder messageTextBuilder = new StringBuilder();

            Set<String> names = excelHelper.getNameForColumnMatching(botConfig.getExcel(), ExcelIndexes.IN_LP_COLUMN_INDEX, true);
            int i = 1;
            for (String name : names) {
                messageTextBuilder.append(" " + i++ + " " + name.trim()).append("\n");
            }
            String message = messageTextBuilder.toString();
            if (message.length() > 0) {
                sendMessage(absSender, chat, "In LakeSide Partners:\n" + message);
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