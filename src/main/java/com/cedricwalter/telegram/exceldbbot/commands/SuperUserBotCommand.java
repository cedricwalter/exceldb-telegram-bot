package com.cedricwalter.telegram.exceldbbot.commands;

import com.cedricwalter.telegram.exceldbbot.BotConfig;
import com.cedricwalter.telegram.exceldbbot.updateshandlers.CommandsHandler;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.logging.BotLogger;

public abstract class SuperUserBotCommand extends BotCommand {

    private static final String LOGTAG = "USERPERMISSIONS";

    public SuperUserBotCommand(String commandIdentifier, String description) {
        super(commandIdentifier, description);
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        try {
            BotConfig botConfig = new BotConfig();
            if (!botConfig.isSuperUser(user)) {
                sendMessage(absSender, chat, "You are not a superuser and can not use this command, contact @CedricWalter if you think you should be able to run this command");
                return;
            }

            executeSuperdUser(absSender, user, chat, strings);

        } catch (Exception e) {
            BotLogger.error(LOGTAG, e);
        }
    }

    public abstract void executeSuperdUser(AbsSender absSender, User user, Chat chat, String[] strings) throws Exception;

    protected void sendMessage(AbsSender absSender, Chat chat, String text) throws TelegramApiException {
        CommandsHandler.sendMessage(absSender,chat, text);
    }
}
