package com.cedricwalter.telegram.exceldbbot.commands;

import com.cedricwalter.telegram.exceldbbot.BotConfig;
import com.cedricwalter.telegram.exceldbbot.database.ExcelHelper;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.logging.BotLogger;

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
            Set<String> subCategories = excelHelper.getUniqueColumnValues(2);

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