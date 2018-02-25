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

public class HasEntryCommand extends WhiteListedUserBotCommand {

    private static final String LOGTAG = "HASENTRYCOMMAND";
    private final BotConfig botConfig;
    private final ExcelHelper excelHelper;

    public HasEntryCommand() {
        super("hasEntry", "Check if database contains company name or company url already, if found returns them each as a unique message. e.g /hasentry inacta");
        botConfig = new BotConfig();
        excelHelper = new ExcelHelper();
    }

    @Override
    public void executeWhiteListedUser(AbsSender absSender, User user, Chat chat, String[] arguments) {
        try {
            if (arguments != null && arguments.length > 0) {
                String entry = arguments[0];

                System.out.println(String.format("hasEntry %s from %s", entry, user.getUserName()));

                List<Row> rows = excelHelper.hasEntry(botConfig.getExcel(), entry);

                if (rows.size() > 0) {
                    // Only return ten first to not spam the users
                    List<String> rowList = ExcelHelper.toString(rows);
                    if (rowList.size() > 10) {

                        SendMessage answer = new SendMessage();
                        answer.setChatId(chat.getId().toString());
                        answer.setText("Found too much entries " + rowList.size() + " returning only 10 first entries.");
                        absSender.sendMessage(answer);

                        rowList = rowList.subList(0, 10);
                    }

                    for (String result : rowList) {
                        SendMessage answer = new SendMessage();
                        answer.setChatId(chat.getId().toString());
                        answer.setText(result);
                        absSender.sendMessage(answer);
                    }
                } else {
                    SendMessage answer = new SendMessage();
                    answer.setChatId(chat.getId().toString());
                    answer.setText("Found no entries " + entry + "you may want to add this entry using \n /addEntry [name] [categorie] [subcategorie] [url] \n if you don't have a category yet use empty string like this\n /addEntry [name] \"\" \"\" [url]");
                    absSender.sendMessage(answer);
                }
            }
            else {
                SendMessage answer = new SendMessage();
                answer.setChatId(chat.getId().toString());
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