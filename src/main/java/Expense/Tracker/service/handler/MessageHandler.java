package Expense.Tracker.service.handler;
import Expense.Tracker.entity.User;
import Expense.Tracker.repository.UserRepository;
import Expense.Tracker.telegram.Bot;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;

@Service
public class MessageHandler {
    private final UserRepository userRepository;

    private final CommandHandler commandHandler;

    private final CallbackQueryHandler callbackQueryHandler;

    public MessageHandler(UserRepository userRepository, CommandHandler commandHandler, CallbackQueryHandler callbackQueryHandler) {
        this.userRepository = userRepository;
        this.commandHandler = commandHandler;
        this.callbackQueryHandler = callbackQueryHandler;
    }

    public BotApiMethod<?> handleMessage(Message message, Bot bot) {
        Long userId = message.getChatId();
        User user = userRepository.findById(userId)
                .orElseGet(() -> userRepository.save(User.builder().chatId(userId).build()));

        String text = message.getText();
        if (text.startsWith("/")) {

            return commandHandler.answer(message, bot);
        } else {
            return callbackQueryHandler.handleUserInput(message);
        }
    }
}
