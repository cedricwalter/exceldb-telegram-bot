package com.cedricwalter.telegram.exceldbbot.commands;

import com.cedricwalter.telegram.exceldbbot.BotConfig;
import com.cedricwalter.telegram.exceldbbot.database.ExcelHelper;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.logging.BotLogger;

public class CountCommand extends WhiteListedUserBotCommand {

    private static final String LOGTAG = "COUNTCOMMAND";
    private final BotConfig botConfig;
    private final ExcelHelper excelHelper;

    public CountCommand() {
        super("count", "Return the number of entries in excel");
        excelHelper = new ExcelHelper();
        botConfig = new BotConfig();
    }

    @Override
    public void executeWhiteListedUser(AbsSender absSender, User user, Chat chat, String[] arguments) {
        try {
            int count = excelHelper.count();

            sendMessage(absSender, chat, "We have currently " + count + " entrie(s) in excel.");
        } catch (TelegramApiException e) {
            BotLogger.error(LOGTAG, e);
        } catch (Exception e) {
            BotLogger.error(LOGTAG, e);
        }
    }
}