package Expense.Tracker.service.handler;

import Expense.Tracker.entity.User;
import Expense.Tracker.service.data.CallbackData;
import Expense.Tracker.service.factory.KeyboardFactory;
import Expense.Tracker.service.manager.GarageManager;
import Expense.Tracker.service.manager.StartManager;
import Expense.Tracker.telegram.Bot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.List;

import static Expense.Tracker.service.data.CallbackData.GARAGE;

@Service
public class CommandHandler {

    final private KeyboardFactory keyboardFactory;

    final private GarageManager garageManager;

    final private CallbackData callbackData;

    final private StartManager startManager;

    @Autowired
    public CommandHandler(KeyboardFactory keyboardFactory, GarageManager garageManager, CallbackData callbackData, StartManager startManager) {
        this.keyboardFactory = keyboardFactory;
        this.garageManager = garageManager;
        this.callbackData = callbackData;
        this.startManager = startManager;
    }


    public BotApiMethod<?> answer(Message message, Bot bot){
        String command = message.getText();
        switch (command){
            case "/start" -> {
                return startManager.answerCommand(message);
            }
            default -> {
                return defaultAnswer(message);
            }
        }
    }

//    private BotApiMethod<?> start (Message message){
//        return SendMessage.builder()
//                .chatId(message.getChatId())
//                .replyMarkup(keyboardFactory.getInlineKeyboard(
//                        List.of("Мой гараж","Добавить автомобиль"),
//                        List.of(2),
//                        List.of(GARAGE,"add auto")
//                ))
//                .text("""
//                        Привет! Я твой личный помощник по учету расходов на автомобиль.
//                        Со мной ты можешь легко отслеживать все свои расходы на топливо,
//                        обслуживание, ремонт и другие затраты.
//                        """).build();
//    }
    private BotApiMethod<?> defaultAnswer (Message message){
        return SendMessage.builder()
                .chatId(message.getChatId())
                .text("""
                        Я не знаю такую команду:( \n
                        Попробуй написaть /start
                        """).build();
    }

}
