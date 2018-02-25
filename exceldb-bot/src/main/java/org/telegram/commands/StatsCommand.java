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
    public void executeWhiteListedUser(AbsSender absSender, User user, Chat chat, String[] arguments) {
        try {
            Map<String, String> stats = excelHelper.getStats(botConfig.getExcel());
            StringBuilder stringBuilder = new StringBuilder();
            for (String key : stats.keySet()) {
                stringBuilder.append(key).append("=").append(String.valueOf(stats.get(key))).append("\n");
            }

            SendMessage answer = new SendMessage();
            answer.setChatId(chat.getId().toString());
            answer.setText(stringBuilder.toString());
            absSender.sendMessage(answer);

        } catch (TelegramApiException e) {
            BotLogger.error(LOGTAG, e);
        }
        catch (IOException e) {
            BotLogger.error(LOGTAG, e);
        }
    }
}