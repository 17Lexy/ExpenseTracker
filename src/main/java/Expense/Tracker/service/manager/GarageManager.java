package Expense.Tracker.service.manager;

import Expense.Tracker.entity.Car;
import Expense.Tracker.repository.CarRepository;
import Expense.Tracker.service.factory.AnswermethodFactory;
import Expense.Tracker.service.factory.KeyboardFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.List;

import static Expense.Tracker.service.data.CallbackData.*;

@Component
public class GarageManager {

    private final KeyboardFactory keyboardFactory;

    private final AnswermethodFactory methodFactory;

    private final CarRepository carRepository;


    @Autowired
    public GarageManager(KeyboardFactory keyboardFactory, AnswermethodFactory methodFactory, CarRepository carRepository) {
        this.keyboardFactory = keyboardFactory;
        this.methodFactory = methodFactory;
        this.carRepository = carRepository;
    }



    public SendMessage answerCallbackQuery(Message message) {
        Long userId = message.getChatId();
        List<Car> cars = carRepository.findCarsByUser_ChatId(userId);
        if (cars.isEmpty()) {
            return methodFactory.getSendMessage(
                    message.getChatId(),
                    "Ваш гараж пуст!",
                    keyboardFactory.getInlineKeyboard(
                            List.of("Добавить авто"),
                            List.of(1),
                            List.of(ADD)
                    )
            );
        } else {
            return methodFactory.getSendMessage(
                    message.getChatId(), "Добро пожаловать в гараж! Выберите авто:",
                    keyboardFactory.getInlineKeyboard(
                            List.of("Добавить авто"),
                            List.of(1),
                            List.of(ADD)
                    ));
        }
    }
//
//    public BotApiMethod<?> answerCallbackQuery (Message message){
//        return methodFactory.getSendMessage(
//                message.getChatId(), "Добро пожаловать в гараж! Выберите авто:",
//                keyboardFactory.getInlineKeyboard(
//                        List.of("Добавить авто",addManager.cars.get(0)),
//                        cars,
//                        List.of(ADD)
//                ));
//
//
//    }

//    public BotApiMethod<?> garage (Message message){
//        return SendMessage.builder()
//                .chatId(message.getChatId())
//                .replyMarkup(keyboardFactory.getInlineKeyboard(
//                        List.of("Добавить трату", "Посмотреть статистику"),
//                        List.of(2),
//                        List.of("add Expense","show statistic")
//                ))
//                .text("""
//                        Добро пожаловать в гараж! Выберите авто:
//                        """).build();
//    }


//    public BotApiMethod<?> answerCallbackQuery (CallbackQuery callbackQuery){
//        return EditMessageText.builder()
//                .chatId(callbackQuery.getMessage().getChatId())
//                .messageId(callbackQuery.getMessage().getMessageId())
//
//                .text("""
//                        Добро пожаловать в гараж! Выберите авто:
//                        """)
//                .build();
//    }
}
