package Expense.Tracker.service;

import Expense.Tracker.service.handler.MessageHandler;
import Expense.Tracker.service.manager.AddManager;
import Expense.Tracker.telegram.Bot;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateDispatcher {

    final MessageHandler messageHandler;


    final AddManager addManager;

    @Autowired
    public UpdateDispatcher(MessageHandler messageHandler, AddManager addManager) {
        this.messageHandler = messageHandler;
        this.addManager = addManager;
    }


    public BotApiMethod<?> distribute(Update update, Bot bot) {
        if (update.hasCallbackQuery()) {
            return addManager.answerCallbackQuery(update.getCallbackQuery(), bot);
        }
        if (update.hasMessage() && update.getMessage().hasText()) {
            return messageHandler.handleMessage(update.getMessage(), bot);
        }

        return null;
    }


}
