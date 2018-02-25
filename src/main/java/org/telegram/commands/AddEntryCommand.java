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

public class AddEntryCommand extends WhiteListedUserBotCommand {

    private static final String LOGTAG = "ADDENTRYCOMMAND";

    public AddEntryCommand() {
        super("addEntry", "Add entry new startup to database");
    }

    @Override
    public void executeWhiteListedUser(AbsSender absSender, User user, Chat chat, String[] arguments) {
        SendMessage answer = new SendMessage();
        answer.setChatId(chat.getId().toString());
        try {
            BotConfig botConfig = new BotConfig();
            ExcelHelper excelHelper = new ExcelHelper();

            if (arguments != null && arguments.length > 0) {
                if (arguments.length != 4) {
                    answer.setChatId(chat.getId().toString());
                    answer.setText("You need to provide name category subcategory url like \n 'cedric gmbh' 'blockchain finance' 'education and training' 'www.cedricwalter.com'");
                }
                String name = arguments[0];
                String category = arguments[1];
                String subCategory = arguments[2];
                String url = arguments[3];

                System.out.println(String.format("AddEntry from %s : %s %s %s %s", user.getUserName(), name, category, subCategory, url));

                excelHelper.addEntry(botConfig.getExcel(), name, category, subCategory, url);
                answer.setText("Success");
                absSender.sendMessage(answer);
            } else {
                answer.setChatId(chat.getId().toString());
                answer.setText("You need to provide name category subcategory url like \n 'cedric gmbh' 'blockchain finance' 'education and training' 'www.cedricwalter.com'");
            }

            absSender.sendMessage(answer);
        } catch (TelegramApiException e) {
            BotLogger.error(LOGTAG, e);
        } catch (IOException e) {
            BotLogger.error(LOGTAG, e);
        }
    }
}