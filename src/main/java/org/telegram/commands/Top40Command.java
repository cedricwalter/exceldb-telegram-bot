package org.telegram.commands;

import com.cedricwalter.telegram.exceldbbot.ExcelHelper;
import com.cedricwalter.telegram.exceldbbot.ExcelIndexes;
import com.cedricwalter.telegram.exceldbbot.GoogleSheet;
import org.telegram.BotConfig;
import org.telegram.telegrambots.api.objects.Chat;
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.bots.AbsSender;
import org.telegram.telegrambots.logging.BotLogger;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Top40Command extends WhiteListedUserBotCommand {

    private static final String LOGTAG = "SVGCOMMAND";
    private final BotConfig botConfig;
    private final ExcelHelper excelHelper;

    public Top40Command() {
        super("top40", "return top 40 companies");
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
                sendMessage(absSender, chat, "Top30:\n" + message);
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