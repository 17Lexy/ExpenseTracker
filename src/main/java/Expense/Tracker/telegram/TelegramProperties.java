package Expense.Tracker.telegram;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
@ConfigurationProperties("application.properties")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TelegramProperties {

    String botName;


    String botToken;


    String path;

}
