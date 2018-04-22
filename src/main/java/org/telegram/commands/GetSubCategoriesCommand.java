package org.telegram.commands;

import com.cedricwalter.telegram.exceldbbot.ExcelHelper;
import org.telegram.BotConfig;
import org.telegram.telegrambots.api.objects.Chat;
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.bots.AbsSender;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import org.telegram.telegrambots.logging.BotLogger;

import java.io.IOException;
import java.util.Set;

public class GetSubCategoriesCommand extends WhiteListedUserBotCommand {

    private static final String LOGTAG = "GETCATEGORIESCOMMAND";
    private final BotConfig botConfig;
    private final ExcelHelper excelHelper;

    public GetSubCategoriesCommand() {
        super("GetSubCat", "get all startup sub-categories");
        botConfig = new BotConfig();
        excelHelper = new ExcelHelper();
    }

    @Override
    public void executeWhiteListedUser(AbsSender absSender, User user, Chat chat, String[] arguments) {
        try {
            System.out.println(String.format("GetSubCat from %s", user.getUserName()));
            Set<String> subCategories = excelHelper.getUniqueColumnValues( 2);

            StringBuilder stringBuilder = new StringBuilder();
            for (String subCategory : subCategories) {
                stringBuilder.append(subCategory.trim().replaceAll(" ", "-")).append("\n");
            }

            sendMessage(absSender, chat, stringBuilder.toString());
        } catch (TelegramApiException e) {
            BotLogger.error(LOGTAG, e);
        } catch (Exception e) {
            BotLogger.error(LOGTAG, e);
        }
    }
}