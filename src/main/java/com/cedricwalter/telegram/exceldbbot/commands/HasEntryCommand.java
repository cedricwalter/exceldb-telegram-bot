package com.cedricwalter.telegram.exceldbbot.commands;

import com.cedricwalter.telegram.exceldbbot.BotConfig;
import com.cedricwalter.telegram.exceldbbot.database.ExcelHelper;
import com.cedricwalter.telegram.exceldbbot.database.GoogleSheet;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.logging.BotLogger;

import java.util.List;
import java.util.Set;

public class HasEntryCommand extends WhiteListedUserBotCommand {

    private static final String LOGTAG = "HASENTRYCOMMAND";
    private final BotConfig botConfig;
    private final ExcelHelper excelHelper;

    public HasEntryCommand() {
        super("has", "Check if database contains company name or company url already, if found returns them each as a unique message (up to 10). e.g /has acme");
        botConfig = new BotConfig();
        excelHelper = new ExcelHelper();
    }

    @Override
    public void executeWhiteListedUser(AbsSender absSender, User user, Chat chat, String[] arguments) {
        try {
            if (arguments != null && arguments.length > 0) {
                String entry = arguments[0];

                System.out.println(String.format("/has %s from %s", entry, user.getUserName()));

                Set<List<Object>> SwissRows = excelHelper.hasEntry(entry, GoogleSheet.getSwissRows());
                Set<List<Object>> singaporeRows = excelHelper.hasEntry(entry, GoogleSheet.getSingaporeRows());
                Set<List<Object>> dubaiRows = excelHelper.hasEntry(entry, GoogleSheet.getDubaiRows());

                boolean found = false;

                if (SwissRows.size() > 0) {
                    found = display(absSender, chat, SwissRows, "In Switzerland, found too much entries ", "In Switzerland, found :");
                }

                if (singaporeRows.size() > 0) {
                    found = display(absSender, chat, singaporeRows, "In Singapore, found too much entries ", "In Singapore, found :");
                }

                if (dubaiRows.size() > 0) {
                    found = display(absSender, chat, dubaiRows, "In Dubai, found too much entries ", "In Singapore, found :");
                }

                if (!found) {
                    String newEntry = entry.replaceAll(" ", "-");
                    sendMessage(absSender, chat, "Found no entries " + entry + " you may want to add this entry using \n" +
                            "https://docs.google.com/forms/d/e/1FAIpQLSf9vE5sTtYjTSjQGdTOI5Lvv3FniBhH9Y4ROxyRqDgu6a777Q/viewform?usp=sf_link");
                }
            } else {
                sendMessage(absSender, chat, "Provide at least a search pattern, e.g /has google");
            }
        } catch (TelegramApiException e) {
            BotLogger.error(LOGTAG, e);
        } catch (Exception e) {
            BotLogger.error(LOGTAG, e);
        }
    }

    private boolean display(AbsSender absSender, Chat chat, Set<List<Object>> swissRows, String s, String s2) throws TelegramApiException {
        boolean found;
        found = true;
        // Only return ten first to not spam the users
        List<String> rowList = ExcelHelper.toString(swissRows);
        if (rowList.size() > 10) {

            String text = s + rowList.size() + " returning only 10 first entries.";
            sendMessage(absSender, chat, text);

            rowList = rowList.subList(0, 10);
        }

        sendMessage(absSender, chat, s2);
        for (String result : rowList) {
            sendMessage(absSender, chat, result);
        }
        return found;
    }

}