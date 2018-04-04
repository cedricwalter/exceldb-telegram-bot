package org.telegram.updateshandlers;

import org.telegram.BotConfig;
import org.telegram.commands.*;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.bots.TelegramLongPollingCommandBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import org.telegram.telegrambots.logging.BotLogger;

public class CommandsHandler extends TelegramLongPollingCommandBot {

    public static final String LOGTAG = "COMMANDSHANDLER";
    private BotConfig botConfig = new BotConfig();

    public CommandsHandler() {
        register(new HelloCommand());
        register(new HasEntryCommand());
        register(new AddEntryCommand());
        register(new GetCategoriesCommand());
        register(new GetSubCategoriesCommand());
        register(new CountCommand());
        register(new StatsCommand());
        register(new PNGCommand());
        register(new SVGCommand());
        register(new StructCommand());
        register(new Top30Command());

        HelpCommand helpCommand = new HelpCommand(this);
        register(helpCommand);

        registerDefaultAction((absSender, message) -> {
            SendMessage commandUnknownMessage = new SendMessage();
            commandUnknownMessage.setChatId(message.getChatId());
            commandUnknownMessage.setText("The command '" + message.getText() + "' is not known by this bot. Here comes some help ");
            try {
                absSender.sendMessage(commandUnknownMessage);
            } catch (TelegramApiException e) {
                BotLogger.error(LOGTAG, e);
            }
            helpCommand.execute(absSender, message.getFrom(), message.getChat(), new String[]{});
        });
    }

    @Override
    public void processNonCommandUpdate(Update update) {

        if (update.hasMessage()) {
            Message message = update.getMessage();

            if (message.hasText()) {
                SendMessage echoMessage = new SendMessage();
                echoMessage.setChatId(message.getChatId());

                BotConfig botConfig = new BotConfig();
                User from = message.getFrom();
                if (botConfig.getWhiteList().contains(from)) {

                    String result = "OK";

                    echoMessage.setText(result);
                } else {
                    echoMessage.setText("you're not authorized to use this Bot");
                }
                try {
                    sendMessage(echoMessage);
                } catch (TelegramApiException e) {
                    BotLogger.error(LOGTAG, e);
                }
            }
        }
    }

    @Override
    public String getBotUsername() {
        BotConfig botConfig = new BotConfig();

        return botConfig.getUser();
    }

    @Override
    public String getBotToken() {
        BotConfig botConfig = new BotConfig();

        return botConfig.getToken();
    }
}