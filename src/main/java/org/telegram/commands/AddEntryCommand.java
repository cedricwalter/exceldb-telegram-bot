package org.telegram.commands;

import com.cedricwalter.telegram.exceldbbot.ExcelHelper;
import org.apache.poi.ss.usermodel.Row;
import org.telegram.BotConfig;
import org.telegram.services.Emoji;
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
    private final BotConfig botConfig;
    private final ExcelHelper excelHelper;

    public AddEntryCommand() {
        super("addEntry", "Add entry new startup to database\n\n/addentry acme-inc category-with-space subcategory-with-space www.acme.com\n"+ Emoji.NO_ENTRY_SIGN+" don't use space, replace them with -");
        botConfig = new BotConfig();
        excelHelper = new ExcelHelper();
    }

    @Override
    public void executeWhiteListedUser(AbsSender absSender, User user, Chat chat, String[] arguments) {
        try {
            if (arguments != null && arguments.length > 0) {
                if (arguments.length != 4) {
                    sendMessage(absSender, chat, "You need to provide name category subcategory url like \n/addentry acme-inc category-with-space subcategory-with-space www.acme.com\n"+ Emoji.NO_ENTRY_SIGN+" don't use space, replace them with -");
                }
                String name = arguments[0];
                String category = arguments[1];
                String subCategory = arguments[2];
                String url = arguments[3];

                System.out.println(String.format("AddEntry from %s : %s %s %s %s", user.getUserName(), name, category, subCategory, url));

                excelHelper.addEntry(botConfig.getExcel(), name, category, subCategory, url);
                sendMessage(absSender, chat, "Success");
            } else {
                sendMessage(absSender, chat, "You need to provide name category subcategory url like \n/addentry acme-inc Blockchain-Service-Provider entertainment www.acme.com");
            }
        } catch (TelegramApiException e) {
            BotLogger.error(LOGTAG, e);
        } catch (IOException e) {
            BotLogger.error(LOGTAG, e);
        }
    }
}