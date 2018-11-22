package com.cedricwalter.telegram.exceldbbot.commands;

import com.cedricwalter.telegram.exceldbbot.BotConfig;
import com.cedricwalter.telegram.exceldbbot.database.ExcelHelper;
import com.cedricwalter.telegram.exceldbbot.database.GoogleSheet;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.logging.BotLogger;

import java.util.Map;

public class StatsCommand extends WhiteListedUserBotCommand {

    private static final String LOGTAG = "STATSCOMMAND";
    private final BotConfig botConfig;
    private final ExcelHelper excelHelper;

    public StatsCommand() {
        super("stats", "Return some statistics");
        botConfig = new BotConfig();
        excelHelper = new ExcelHelper();
    }

    @Override
    public void executeWhiteListedUser(AbsSender absSender, User user, Chat chat, String[] arguments) throws Exception {
        try {
            send(absSender, chat, excelHelper.getStats(GoogleSheet.getSwissRows()), "Switzerland: ");
            send(absSender, chat, excelHelper.getStats(GoogleSheet.getSingaporeRows()), "Singapore: ");
        } catch (TelegramApiException e) {
            BotLogger.error(LOGTAG, e);
        }
    }

    private void send(AbsSender absSender, Chat chat, Map<String, String> stats, String text) throws TelegramApiException {
        for (String key : stats.keySet()) {
            StringBuilder stringBuilder = new StringBuilder(text);
            stringBuilder.append(key).append("=").append(String.valueOf(stats.get(key))).append("\n");
            sendMessage(absSender, chat, stringBuilder.toString());
        }
    }
}