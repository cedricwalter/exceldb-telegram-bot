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
        super("add", "Add entry new startup to database\n" +
                "/add acme-inc         or\n" +
                "/add acme-inc category-with-space subcategory-with-space www.acme.com\n" +
                Emoji.NO_ENTRY_SIGN + " don't use space, replace them with -");
        botConfig = new BotConfig();
        excelHelper = new ExcelHelper();
    }

    @Override
    public void executeWhiteListedUser(AbsSender absSender, User user, Chat chat, String[] arguments) {
        try {
            int argumentLength = arguments.length;

            if (arguments != null && argumentLength > 0) {
                if (argumentLength <= 1) {
                    sendMessage(absSender, chat, "You need to provide at least a name\n" +
                            "But you can give more like: category subcategory url like \n/add acme-inc category-with-space subcategory-with-space www.acme.com\n" + Emoji.NO_ENTRY_SIGN + " don't use space, replace them with -");
                }
                String name = arguments[0];
                String category = argumentLength >= 1 ? arguments[1] : "na";
                String subCategory = argumentLength >= 2 ? arguments[2] : "na";
                String url = argumentLength >= 3 ? arguments[3] : "na";

                System.out.println(String.format("AddEntry from %s : %s %s %s %s", user.getUserName(), name, category, subCategory, url));

                excelHelper.addEntry(botConfig.getExcel(), name, category, subCategory, url);
                sendMessage(absSender, chat, "Success");
            } else {
                sendMessage(absSender, chat, "You need to provide at least a name\n" +
                        "But you can give more like: category subcategory url like \n/add acme-inc category-with-space subcategory-with-space www.acme.com\n" + Emoji.NO_ENTRY_SIGN + " don't use space, replace them with -");
            }
        } catch (TelegramApiException e) {
            BotLogger.error(LOGTAG, e);
        } catch (IOException e) {
            BotLogger.error(LOGTAG, e);
        }
    }
}