package com.cedricwalter.telegram.exceldbbot.commands;

import com.cedricwalter.telegram.exceldbbot.BotConfig;
import com.cedricwalter.telegram.exceldbbot.database.ExcelHelper;
import directory.cryptovalley.country.SingaporeGenerator;
import directory.cryptovalley.country.SwissGenerator;
import directory.cryptovalley.generator.ImageHelper;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.logging.BotLogger;

import java.io.File;
import java.io.IOException;

public class DeploySingaporeCommand extends WhiteListedUserBotCommand {

    private static final String LOGTAG = "DEPLOY";
    private final BotConfig botConfig;
    private final ExcelHelper excelHelper;

    public DeploySingaporeCommand() {
        super("deploySingapore", "if you're a super user, you can deploy latest cvmap.ch updates");
        botConfig = new BotConfig();
        excelHelper = new ExcelHelper();
    }

    @Override
    public void executeWhiteListedUser(AbsSender absSender, User user, Chat chat, String[] arguments) {
        try {
            sendMessage(absSender, chat, "Start deployment of Singapore cvmaps.ch");

            new SingaporeGenerator().main(null);

            if (ImageHelper.errors.length() == 0) {
                sendMessage(absSender, chat, "deployment Singapore cvmaps.ch completed!");
            } else {
                sendMessage(absSender, chat, "deployment failed!");
                sendMessage(absSender, chat, ImageHelper.errors.toString());
            }


        } catch (Exception e) {
            BotLogger.error(LOGTAG, e);
        }
    }

    public static boolean fileExistsCaseSensitive(String path) {
        try {
            File file = new File(path);
            return file.exists() && file.getCanonicalFile().getName().equals(file.getName());
        } catch (IOException e) {
            return false;
        }
    }


}