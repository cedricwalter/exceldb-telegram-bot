package com.cedricwalter.telegram.exceldbbot.commands;

import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.logging.BotLogger;

public class HelloCommand extends WhiteListedUserBotCommand {

    private static final String LOGTAG = "HELLOCOMMAND";

    public HelloCommand() {
        super("hello", "Say hallo to this bot");
    }

    @Override
    public void executeWhiteListedUser(AbsSender absSender, User user, Chat chat, String[] arguments) {
        String userName = chat.getUserName();
        if (userName == null || userName.isEmpty()) {
            userName = user.getFirstName() + " " + user.getLastName();
        }

        StringBuilder messageTextBuilder = new StringBuilder("Hello ").append(userName);
        if (arguments != null && arguments.length > 0) {
            messageTextBuilder.append("\n");
            messageTextBuilder.append("Thank you so much for your kind " + arguments.length + "words:\n");
            messageTextBuilder.append(String.join(" ", arguments));
        }

        try {
            sendMessage(absSender, chat, messageTextBuilder.toString());

        } catch (TelegramApiException e) {
            BotLogger.error(LOGTAG, e);
        }
    }
}