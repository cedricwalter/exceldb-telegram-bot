package com.cedricwalter.telegram.exceldbbot.commands;

import com.cedricwalter.telegram.exceldbbot.scrapper.social.SocialScrapper;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.logging.BotLogger;

public class UpdateSocialCommand extends SuperUserBotCommand {

    private static final String LOGTAG = "UPDATE_SOCIAL";

    public UpdateSocialCommand() {
        super("updateSocial", "if you're a super user, fill up the excel with all social account using a scraper bot");
    }

    @Override
    public void executeSuperdUser(AbsSender absSender, User user, Chat chat, String[] arguments) {
        try {
            sendMessage(absSender, chat, "Starting Import");
            //SocialScrapper.main(null);
            sendMessage(absSender, chat, "Import done");
        } catch (Exception e) {
            BotLogger.error(LOGTAG, e);
        }
    }


}