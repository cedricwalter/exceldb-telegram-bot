package com.cedricwalter.telegram.exceldbbot.commands;

import com.cedricwalter.telegram.exceldbbot.BotConfig;
import com.cedricwalter.telegram.exceldbbot.database.ExcelHelper;
import com.cedricwalter.telegram.exceldbbot.database.ExcelIndexes;
import com.cedricwalter.telegram.exceldbbot.database.GoogleSheet;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.logging.BotLogger;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Top50Command extends WhiteListedUserBotCommand {

    private static final String LOGTAG = "SVGCOMMAND";
    private final BotConfig botConfig;
    private final ExcelHelper excelHelper;

    public Top50Command() {
        super("top50", "return top 50 companies");
        botConfig = new BotConfig();
        excelHelper = new ExcelHelper();
    }

    @Override
    public void executeWhiteListedUser(AbsSender absSender, User user, Chat chat, String[] arguments) {
        try {
            StringBuilder messageTextBuilder = new StringBuilder();

            Set<String> names = getNameForColumnMatching(ExcelIndexes.TOP30_COLUMN_INDEX, true);
            int i = 1;
            for (String name : names) {
                messageTextBuilder.append(" " + i++ + " " + name.trim()).append("\n");
            }
            String message = messageTextBuilder.toString();
            if (message.length() > 0) {
                sendMessage(absSender, chat, "Top40:\n" + message);
            }
            sendMessage(absSender, chat, "see https://docs.google.com/spreadsheets/d/1cum9GOnjKZ-WiR_AiynmgjA5Jy8gL2QcMtPi974C-HU for more details");


        } catch (Exception e) {
            BotLogger.error(LOGTAG, e);
        }
    }

    public Set<String> getNameForColumnMatching(int columnIndex, boolean value) throws Exception {
        Set<String> potential = new HashSet<>();

        List<List<Object>> rows = GoogleSheet.getTop30();
        for (List<Object> row : rows) {
            potential.add(ExcelHelper.getValueSafe(row, ExcelIndexes.NAME_COLUMN_INDEX) + " (" +
                    ExcelHelper.getValueSafe(row, ExcelIndexes.CATEGORY_COLUMN_INDEX) + "/" +
                    ExcelHelper.getValueSafe(row, ExcelIndexes.SUBCATEGORY_COLUMN_INDEX) + ")"
            );
        }
        return potential;
    }


}