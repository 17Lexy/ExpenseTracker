package Expense.Tracker.service.manager;

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
import Expense.Tracker.telegram.Bot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static Expense.Tracker.entity.BotState.EXPENSE_HISTORY;
import static Expense.Tracker.service.data.CallbackData.*;

@Component
public class AddManager {
    private final KeyboardFactory keyboardFactory;

    private final AnswermethodFactory methodFactory;
    private final GarageManager garageManager;

    private final ExpenseManager expenseManager;
    private final UserRepository userRepository;
    private final CarRepository carRepository;

    private final ExpenseRepository expenseRepository;


    @Autowired
    public AddManager(KeyboardFactory keyboardFactory, AnswermethodFactory methodFactory, GarageManager garageManager, ExpenseManager expenseManager, UserRepository userRepository, CarRepository carRepository, ExpenseRepository expenseRepository) {
        this.keyboardFactory = keyboardFactory;
        this.methodFactory = methodFactory;
        this.garageManager = garageManager;
        this.expenseManager = expenseManager;
        this.userRepository = userRepository;
        this.carRepository = carRepository;
        this.expenseRepository = expenseRepository;
    }

    public SendMessage addAuto(Message message) {
        return methodFactory.getSendMessage(
                message.getChatId(),
                "Введите название автомобиля",
                null);
    }

    public SendMessage answerCallbackQuery(CallbackQuery callbackQuery, Bot bot) {
        Long userId = callbackQuery.getMessage().getChatId();
        String data = callbackQuery.getData();
        Message message = callbackQuery.getMessage();

        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        if (data.equals(ADD)) {
            user.setState(BotState.ADDING_CAR);
            userRepository.save(user);
            return methodFactory.getSendMessage(
                    userId,
                    "Введите название автомобиля:",
                    null
            );
        }
        if (data.equals(GARAGE)) {
            user.setState(BotState.GARAGE);
            userRepository.save(user);


            List<Car> cars = carRepository.findActiveCarsByUser(user);
            if (cars.isEmpty()) {
                return methodFactory.getSendMessage(
                        userId,
                        "У вас пока нет автомобилей.",
                        null
                );
            }
            List<String> carNames = new ArrayList<>();
            List<String> carIds = new ArrayList<>();
            for (Car car : cars) {
                carNames.add(car.getCarName());
                carIds.add(car.getCarId().toString());
            }

            // Создание и отправка сообщения с кнопками автомобилей
            return methodFactory.getSendMessage(
                    userId,
                    "Добро пожаловать в гараж! Выберите авто:",
                    keyboardFactory.getInlineKeyboard(carNames, Collections.nCopies(carNames.size(), 1), carIds)
            );


        }
        if(data.equals(DELETE_CAR)){
            Car selectedCar = user.getSelectedCar();
            selectedCar.setActive(false);
            carRepository.save(selectedCar);
            return methodFactory.getSendMessage(
                    userId,
                    "Авто успешно удален из гаража",
                    keyboardFactory.getInlineKeyboard(
                            List.of("Гараж", "Добавить авто"),
                            List.of(2),
                            List.of(GARAGE, ADD)
                    ));

        }

        if(data.equals(ADD_EXPENSE)){
            user.setState(BotState.EXPENSE_CATEGORY);
            userRepository.save(user);



            return methodFactory.getSendMessage(
                    userId,
                    "Выберите категорию: ",
                    keyboardFactory.getInlineKeyboard(
                            List.of("Заправки", "Запчасти", "Иное"),
                            List.of(3),
                            List.of(ZAPRAVKA, ZAPCHASTI, INOE)
                    ));
        }
        if(user.getState() == BotState.EXPENSE_CATEGORY){

        }
        if (data.equals(CallbackData.EXPENSE_HISTORY)) {

            return expenseManager.handleViewHistory(userId);

        }

        if (user.getState() == BotState.GARAGE && data.matches("\\d+")) {
            return expenseManager.handleCarSelection(userId, data);
        }

        return null;
    }


//    public BotApiMethod<?> answer(Message message, Bot bot) {
//        String userInput = message.getText();
//        return methodFactory.getSendMessage(
//                message.getChatId(),
//                "Автомобиль успешно добавлен!",
//                keyboardFactory.getInlineKeyboard(
//                        List.of("Вернуться в гараж", "Добавить еще автомобиль"),
//                        List.of(2),
//                        List.of(GARAGE, ADD)
//                )
//        );
//    }
}

