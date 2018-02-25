package org.telegram.commands;

import org.telegram.BotConfig;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Chat;
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.bots.AbsSender;
import org.telegram.telegrambots.bots.commands.BotCommand;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import org.telegram.telegrambots.logging.BotLogger;

public abstract class WhiteListedUserBotCommand extends BotCommand {

    private static final String LOGTAG = "USERPERMISSIONS";

    public WhiteListedUserBotCommand(String commandIdentifier, String description) {
        super(commandIdentifier, description);
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        try {
            BotConfig botConfig = new BotConfig();
            if (!botConfig.isInWhiteList(user)) {
                sendMessage(absSender, chat, "You are not authorized to use this bot and need to be whitelisted first, contact @CedricWalter and define an @username!");
                return;
            }

            executeWhiteListedUser(absSender, user, chat, strings);

        } catch (TelegramApiException e) {
            BotLogger.error(LOGTAG, e);
        }
    }

    public abstract void executeWhiteListedUser(AbsSender absSender, User user, Chat chat, String[] strings);

    protected void sendMessage(AbsSender absSender, Chat chat, String text) throws TelegramApiException {
        SendMessage answer = new SendMessage();
        answer.setChatId(chat.getId().toString());
        answer.setText(text);
        absSender.sendMessage(answer);
    }
}
