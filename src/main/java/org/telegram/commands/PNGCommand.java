package org.telegram.commands;

import com.cedricwalter.telegram.exceldbbot.ExcelHelper;
import org.telegram.BotConfig;
import org.telegram.telegrambots.api.objects.Chat;
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.bots.AbsSender;
import org.telegram.telegrambots.logging.BotLogger;

import java.io.File;
import java.io.IOException;
import java.util.Set;

public class PNGCommand extends WhiteListedUserBotCommand {

    private static final String LOGTAG = "LOGOCOMMAND";
    private final BotConfig botConfig;
    private final ExcelHelper excelHelper;

    public PNGCommand() {
        super("logo", "return logo missing");
        botConfig = new BotConfig();
        excelHelper = new ExcelHelper();
    }

    @Override
    public void executeWhiteListedUser(AbsSender absSender, User user, Chat chat, String[] arguments) {
        try {
            StringBuilder messageTextBuilder = new StringBuilder();

            Set<String> names = excelHelper.getUniqueColumnValues( 0);
            for (String name : names) {
                if (!fileExistsCaseSensitive(botConfig.getLogoPath() + name.trim() + ".png")) {
                    messageTextBuilder.append(name.trim() + ".png").append("\n");
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