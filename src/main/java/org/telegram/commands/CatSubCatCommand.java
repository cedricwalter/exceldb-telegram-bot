package org.telegram.commands;

import com.cedricwalter.telegram.exceldbbot.ExcelHelper;
import org.telegram.BotConfig;
import org.telegram.telegrambots.api.objects.Chat;
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.bots.AbsSender;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import org.telegram.telegrambots.logging.BotLogger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;

public class CatSubCatCommand extends WhiteListedUserBotCommand {

    private static final String LOGTAG = "CATSUBCATCOMMAND";
    private final BotConfig botConfig;
    private final ExcelHelper excelHelper;

    public CatSubCatCommand() {
        super("struct", "Display the structure category vs subcategory");
        excelHelper = new ExcelHelper();
        botConfig = new BotConfig();
    }

    @Override
    public void executeWhiteListedUser(AbsSender absSender, User user, Chat chat, String[] arguments) {
        try {

            InputStream inputStream = CatSubCatCommand.class.getResourceAsStream("/category-subcategory.txt");
            String s = readFromInputStream(inputStream);

            sendMessage(absSender, chat, s);
        } catch (TelegramApiException e) {
            BotLogger.error(LOGTAG, e);
        } catch (IOException e) {
            BotLogger.error(LOGTAG, e);
        }
    }


    private String readFromInputStream(InputStream inputStream)
            throws IOException {
        StringBuilder resultStringBuilder = new StringBuilder();
        try (BufferedReader br
                     = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = br.readLine()) != null) {
                resultStringBuilder.append(line).append("\n");
            }
        }
        return resultStringBuilder.toString();
    }
}