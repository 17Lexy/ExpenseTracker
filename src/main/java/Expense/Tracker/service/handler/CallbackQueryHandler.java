package Expense.Tracker.service.handler;

import Expense.Tracker.entity.BotState;
import Expense.Tracker.entity.Car;
import Expense.Tracker.entity.Expense;
import Expense.Tracker.entity.User;
import Expense.Tracker.repository.CarRepository;
import Expense.Tracker.repository.ExpenseRepository;
import Expense.Tracker.repository.UserRepository;
import Expense.Tracker.service.data.CallbackData;
import Expense.Tracker.service.factory.AnswermethodFactory;
import Expense.Tracker.service.factory.KeyboardFactory;
import Expense.Tracker.service.manager.AddManager;
import Expense.Tracker.service.manager.GarageManager;
import Expense.Tracker.service.manager.StartManager;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static Expense.Tracker.entity.BotState.*;
import static Expense.Tracker.service.data.CallbackData.*;
import static Expense.Tracker.service.data.CallbackData.GARAGE;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CallbackQueryHandler {

    private final GarageManager garageManager;
    private final StartManager startManager;
    private final AddManager addManager;
    private final UserRepository userRepository;
    private final CarRepository carRepository;

    final private AnswermethodFactory answermethodFactory;
    final private KeyboardFactory keyboardFactory;
    private final ExpenseRepository expenseRepository;


    @Autowired
    public CallbackQueryHandler(GarageManager garageManager, StartManager startManager, AddManager addManager, UserRepository userRepository, CarRepository carRepository, AnswermethodFactory answermethodFactory, KeyboardFactory keyboardFactory, ExpenseRepository expenseRepository) {
        this.garageManager = garageManager;
        this.startManager = startManager;
        this.addManager = addManager;
        this.userRepository = userRepository;
        this.carRepository = carRepository;
        this.answermethodFactory = answermethodFactory;
        this.keyboardFactory = keyboardFactory;
        this.expenseRepository = expenseRepository;
    }

    public SendMessage handleUserInput(Message message) {
        Long userId = message.getChatId();
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        switch (user.getState()) {
            case MAIN_MENU:
                if ("Мой гараж".equals(message.getText())) {
                    user.setState(BotState.GARAGE);
                } else if ("Добавить транспорт".equals(message.getText())) {
                    user.setState(ADDING_CAR);
                }
                break;

            case ADDING_CAR:
                Car car = new Car();
                car.setCarName(message.getText());
                car.setUser(user);
                carRepository.save(car);
                user.setState(BotState.MAIN_MENU);
                userRepository.save(user);
                String answer = "Автомобиль \"" + message.getText() + "\" успешно добавлен!";
                SendMessage sendMessage = answermethodFactory.getSendMessage(
                        message.getChatId(),
                        answer,
                        keyboardFactory.getInlineKeyboard(
                                List.of("Мой гараж", "Добавить транспорт"),
                                List.of(2),
                                List.of(GARAGE, ADD)
                        )
                );
                return sendMessage;

            case ADDING_EXPENSE:
                Expense expense = new Expense();
                DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yy");

                expense.setSum(Long.valueOf(message.getText()));
                expense.setCar(user.getSelectedCar());
                expense.setDate(LocalDateTime.now());
                expenseRepository.save(expense);

                user.setState(BotState.GARAGE);
                userRepository.save(user);

                SendMessage sendMessage1 = answermethodFactory.getSendMessage(
                        message.getChatId(),
                        "Трата успешно добавлена",
                        keyboardFactory.getInlineKeyboard(
                                List.of("История", "Добавить трату", "Гараж"),
                                List.of(3),
                                List.of(CallbackData.EXPENSE_HISTORY, ADD_EXPENSE, GARAGE)
                        ));

                return sendMessage1;

            case IN_CAR:
                SendMessage sendMessage2 = answermethodFactory.getSendMessage(
                        message.getChatId(),
                        "Стейт чуз карз",
                        keyboardFactory.getInlineKeyboard(
                                List.of("Посмотреть историю", "Добавить еще трату", "Вернуться к выбору авто"),
                                List.of(3),
                                List.of(CallbackData.EXPENSE_HISTORY, ADD_EXPENSE, GARAGE)
                        ));
                return sendMessage2;

            case EXPENSE_CATEGORY:



            case GARAGE:

                break;
        }

        return null;
    }

//    public BotApiMethod<?> answer(CallbackQuery callbackQuery, Bot bot) {
//        Long userId = callbackQuery.getMessage().getChatId();
//        String data = callbackQuery.getData();
//
//        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
//
//        if (data.equals("ADD")) {
//            user.setState(BotState.ADDING_CAR);
//            userRepository.save(user);
//            return answermethodFactory.getSendMessage(user.getChatId(), "Введите название автомобиля:", null);
//        }
//
//        return null;
//    }
}
