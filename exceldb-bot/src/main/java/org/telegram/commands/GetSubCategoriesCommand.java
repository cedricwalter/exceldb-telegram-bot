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
import java.util.Set;

public class GetSubCategoriesCommand extends BotCommand {

    private static final String LOGTAG = "GETCATEGORIESCOMMAND";

    public GetSubCategoriesCommand() {
        super("GetSubCat", "get all startup sub-categories");
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] arguments) {
        SendMessage answer = new SendMessage();
        answer.setChatId(chat.getId().toString());
        try {
            BotConfig botConfig = new BotConfig();
            if (!botConfig.isInWhiteList(user)) {
                answer.setText("You are not authorized to use this bot and need to be whitelisted first, contact an Admin");
                absSender.sendMessage(answer);
                return;
            }
            ExcelHelper excelHelper = new ExcelHelper();
            Set<String> cat = excelHelper.getUniqueColumnValues(botConfig.getExcel(), 4);

            answer.setText(cat.toString());
            absSender.sendMessage(answer);


        } catch (TelegramApiException e) {
            BotLogger.error(LOGTAG, e);
        }
        catch (IOException e) {
            BotLogger.error(LOGTAG, e);
        }
    }
}