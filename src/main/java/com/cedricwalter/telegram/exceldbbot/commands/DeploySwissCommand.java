package com.cedricwalter.telegram.exceldbbot.commands;

import com.cedricwalter.telegram.exceldbbot.BotConfig;
import com.cedricwalter.telegram.exceldbbot.database.ExcelHelper;
import directory.cryptovalley.country.SwissGenerator;
import directory.cryptovalley.generator.ImageHelper;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.logging.BotLogger;

import java.io.File;
import java.io.IOException;

public class DeploySwissCommand extends WhiteListedUserBotCommand {

    private static final String LOGTAG = "DEPLOY";
    private final BotConfig botConfig;
    private final ExcelHelper excelHelper;

    public DeploySwissCommand() {
        super("deploySwiss", "if you're a super user, you can deploy latest cvmap.ch updates");
        botConfig = new BotConfig();
        excelHelper = new ExcelHelper();
    }

    @Override
    public void executeWhiteListedUser(AbsSender absSender, User user, Chat chat, String[] arguments) {
        try {
            sendMessage(absSender, chat, "Start deployment of swiss cvmaps.ch");

            new SwissGenerator().main(null);

            if (ImageHelper.errors.length() == 0) {
                sendMessage(absSender, chat, "deployment Swiss cvmaps.ch completed!");
            } else {
                sendMessage(absSender, chat, "deployment Swiss cvmaps.ch failed!");
                sendMessage(absSender, chat, ImageHelper.errors.toString());
            }
        } catch (Exception e) {
            BotLogger.error(LOGTAG, e);
        }
    }
}