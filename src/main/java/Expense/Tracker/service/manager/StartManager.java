package Expense.Tracker.service.manager;

import Expense.Tracker.service.factory.AnswermethodFactory;
import Expense.Tracker.service.factory.KeyboardFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.List;

import static Expense.Tracker.service.data.CallbackData.ADD;
import static Expense.Tracker.service.data.CallbackData.GARAGE;

@Component
public class StartManager {
    final private AnswermethodFactory answermethodFactory;

    final private KeyboardFactory keyboardFactory;

    @Autowired
    public StartManager(AnswermethodFactory answermethodFactory, KeyboardFactory keyboardFactory) {
        this.answermethodFactory = answermethodFactory;
        this.keyboardFactory = keyboardFactory;
    }


    public SendMessage answerCommand(Message message) {
        User user = message.getFrom();
        String name = user.getFirstName();
        String welcomeMessage = String.format("Привет,  %s \uD83D\uDC4B  \n" +
                "\n" +
                "Я твой личный помощник по учету расходов на автомобиль.\n" +
                "Со мной ты можешь легко отслеживать все свои расходы на топливо," +
                "обслуживание, ремонт и другие затраты.", name);
        return answermethodFactory.getSendMessage(
                message.getChatId(),
                welcomeMessage,
                keyboardFactory.getInlineKeyboard(
                        List.of("Добавить автомобиль"),
                        List.of(1),
                        List.of(ADD)
                ));

    }


}
