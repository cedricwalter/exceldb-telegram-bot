package org.telegram.commands;

import com.cedricwalter.telegram.exceldbbot.ExcelHelper;
import org.apache.poi.ss.usermodel.Row;
import org.telegram.BotConfig;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Chat;
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.bots.AbsSender;
import org.telegram.telegrambots.bots.commands.BotCommand;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import org.telegram.telegrambots.logging.BotLogger;

import java.io.IOException;
import java.util.List;

public class HasEntryCommand extends BotCommand {

    private static final String LOGTAG = "HASENTRYCOMMAND";

    public HasEntryCommand() {
        super("hasEntry", "Check if database contains company name or company url already, if found returns them. e.g /hasentry google");
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] arguments) {
        SendMessage answer = new SendMessage();

        answer.setChatId(chat.getId().toString());
        try {
            BotConfig botConfig = new BotConfig();
            if (!botConfig.isInWhiteList(user)) {
                answer.setText("You are not authorized to use this bot and need to be whitelisted first, contact @CedricWalter and define an @username!");
                absSender.sendMessage(answer);
                return;
            }

            ExcelHelper excelHelper = new ExcelHelper();

            if (arguments != null && arguments.length > 0) {
                String entry = arguments[0];

                System.out.println(String.format("hasEntry %s from %s", entry, user.getUserName()));

                List<Row> rows = excelHelper.hasEntry(botConfig.getExcel(), entry);

                if (rows.size() > 0) {
                    answer.setText(ExcelHelper.toString(rows));
                    absSender.sendMessage(answer);
                } else {
                    answer.setText("Found no entries " + entry + "you may want to add this entry using /addEntry [name] [categorie] [subcategorie] [url]");
                    absSender.sendMessage(answer);
                }
            }
            else {
                answer.setText("Provide at least a search pattern, e.g /hasentry google");
                absSender.sendMessage(answer);
            }
        } catch (TelegramApiException e) {
            BotLogger.error(LOGTAG, e);
        } catch (IOException e) {
            BotLogger.error(LOGTAG, e);
        }
    }
}