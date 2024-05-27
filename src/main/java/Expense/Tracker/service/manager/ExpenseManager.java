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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.time.format.DateTimeFormatter;
import java.util.List;

import static Expense.Tracker.service.data.CallbackData.*;

@Component
public class ExpenseManager {
    private final UserRepository userRepository;
    private final CarRepository carRepository;
    private final AnswermethodFactory methodFactory;
    private final ExpenseRepository expenseRepository;
    private final KeyboardFactory keyboardFactory;

    @Autowired
    public ExpenseManager(UserRepository userRepository, CarRepository carRepository, AnswermethodFactory methodFactory, ExpenseRepository expenseRepository, KeyboardFactory keyboardFactory) {
        this.userRepository = userRepository;
        this.carRepository = carRepository;
        this.methodFactory = methodFactory;
        this.expenseRepository = expenseRepository;
        this.keyboardFactory = keyboardFactory;
    }

    public SendMessage handleCarSelection(Long userId, String carId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Car selectedCar = carRepository.findById(Long.parseLong(carId)).orElseThrow(() -> new RuntimeException("Car not found"));

        user.setSelectedCar(selectedCar);
        user.setState(BotState.IN_CAR);
        userRepository.save(user);
        System.out.println("Changing user state to CHOOSE_CAR for userId: " + userId);

        return methodFactory.getSendMessage(
                userId,
                "Вы выбрали автомобиль: " + selectedCar.getCarName() + ".",
                keyboardFactory.getInlineKeyboard(
                        List.of("История", "Добавить трату", "Гараж", "Удалить авто"),
                        List.of(3,1),
                        List.of(CallbackData.EXPENSE_HISTORY, ADD_EXPENSE, GARAGE, DELETE_CAR)
                ));
    }

    public SendMessage handleViewHistory(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Car selectedCar = user.getSelectedCar();

        if (selectedCar == null) {
            return methodFactory.getSendMessage(userId, "Не выбран автомобиль.", null);
        }

        List<Expense> expenses = expenseRepository.findByCar(selectedCar);
        if (expenses.isEmpty()) {
            return methodFactory.getSendMessage(userId, "История трат пуста.", null);
        }

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yy");

        // Формирование сообщения с историей трат
        StringBuilder historyMessage = new StringBuilder("История трат для автомобиля: " + selectedCar.getCarName() + "\n\n");

        for (Expense expense : expenses) {
            historyMessage
                    .append(dateFormatter.format(expense.getDate()))
                    .append(": ")
                    .append(expense.getSum())
                    .append("₽.\n");
        }

        Long totalExpense =expenseRepository.getTotalExpenseByCarId(user.getSelectedCar().getCarId());

        historyMessage.append("\n Итого: ").append(totalExpense).append("₽.");


        return methodFactory.getSendMessage(
                userId,
                historyMessage.toString(),
                keyboardFactory.getInlineKeyboard(
                        List.of("Добавить трату", "Гараж"),
                        List.of(2),
                        List.of(ADD_EXPENSE, GARAGE)
                ));
    }

}
