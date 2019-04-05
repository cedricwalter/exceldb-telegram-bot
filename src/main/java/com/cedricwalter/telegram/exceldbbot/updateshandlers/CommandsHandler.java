package com.cedricwalter.telegram.exceldbbot.updateshandlers;

import com.cedricwalter.telegram.exceldbbot.BotConfig;
import com.cedricwalter.telegram.exceldbbot.commands.*;
import org.telegram.telegrambots.extensions.bots.commandbot.TelegramLongPollingCommandBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.logging.BotLogger;

public class CommandsHandler extends TelegramLongPollingCommandBot {

    public static final String LOGTAG = "COMMANDSHANDLER";
    private final BotConfig botConfig;


    public CommandsHandler(String botUsername, BotConfig botConfig) {
        super(botUsername);
        this.botConfig = botConfig;

        register(new HelloCommand());
        register(new HasEntryCommand());
        register(new CountCommand());
        register(new StatsCommand());
        register(new StructCommand());
        register(new Top50Command());
        register(new InCVLCommand());
        register(new InLPCommand());
        register(new CantonCommand());

        //register(new UpdateSocialCommand());
//        register(new DeploySwissCommand());
//        register(new DeploySingaporeCommand());

        HelpCommand helpCommand = new HelpCommand(this);
        register(helpCommand);

        registerDefaultAction((absSender, message) -> {
            SendMessage commandUnknownMessage = new SendMessage();
            commandUnknownMessage.setChatId(message.getChatId());
            commandUnknownMessage.setText("The command '" + message.getText() + "' is not known by this bot. Here comes some help ");
            try {
                absSender.execute(commandUnknownMessage);
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
                    execute(echoMessage);
                } catch (TelegramApiException e) {
                    BotLogger.error(LOGTAG, e);
                }
            }
        }
    }

    public static void sendMessage(AbsSender absSender, Chat chat, String text) throws TelegramApiException {
        SendMessage answer = new SendMessage();
        answer.setChatId(chat.getId().toString());
        answer.enableHtml(true);
        answer.setText(text);
        absSender.execute(answer);
    }

    @Override
    public String getBotToken() {
        BotConfig botConfig = new BotConfig();

        return botConfig.getToken();
    }
}