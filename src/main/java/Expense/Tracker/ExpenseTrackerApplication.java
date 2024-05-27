package Expense.Tracker;


import Expense.Tracker.telegram.TelegramProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;


@SpringBootApplication
@EnableConfigurationProperties(TelegramProperties.class)
public class ExpenseTrackerApplication {

	public static void main(String[] args) throws TelegramApiException {
		SpringApplication.run(ExpenseTrackerApplication.class, args);
	}
}
