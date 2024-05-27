package Expense.Tracker.service.factory;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;

@Component
public class AnswermethodFactory {

    public SendMessage getSendMessage(Long chatId,
                                      String text,
                                      ReplyKeyboard keyboard){
        return SendMessage.builder()
                .chatId(chatId)
                .text(text)
                .replyMarkup(keyboard)
                .build();

    }
}
