package org.telegram.commands;

import com.cedricwalter.telegram.exceldbbot.ExcelHelper;
import org.telegram.BotConfig;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Chat;
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.bots.AbsSender;
import org.telegram.telegrambots.bots.commands.BotCommand;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import org.telegram.telegrambots.logging.BotLogger;

import java.io.IOException;
import java.util.Set;

public class GetCategoriesCommand extends WhiteListedUserBotCommand {

    private static final String LOGTAG = "GETCATEGORIESCOMMAND";
    private final BotConfig botConfig;
    private final ExcelHelper excelHelper;

    public GetCategoriesCommand() {
        super("GetCat", "get all startup categories");
        botConfig = new BotConfig();
        excelHelper = new ExcelHelper();
    }

    @Override
    public void executeWhiteListedUser(AbsSender absSender, User user, Chat chat, String[] arguments) {
        try {
            System.out.println(String.format("GetCat from %s", user.getUserName()));
            Set<String> categories = excelHelper.getUniqueColumnValues(botConfig.getExcel(), 3);

            StringBuilder stringBuilder = new StringBuilder();
            for (String category : categories) {
                stringBuilder.append(category.trim().replaceAll(" ", "-")).append("\n");
            }

            sendMessage(absSender, chat, stringBuilder.toString());
        } catch (TelegramApiException e) {
            BotLogger.error(LOGTAG, e);
        }
        catch (IOException e) {
            BotLogger.error(LOGTAG, e);
        }
    }
}