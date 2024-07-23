package ru.aphecoculture.tgbot.gitlab.handler.callbackquerydataprocessor;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.gitlab4j.api.models.MergeRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.aphecoculture.ecovision.tgbot.commons.callbackquery.CallbackQueryProcessor;
import ru.aphecoculture.ecovision.tgbot.commons.exception.BotApplicationException;
import ru.aphecoculture.tgbot.gitlab.service.GitlabService;
import ru.aphecoculture.tgbot.gitlab.utils.CallbackDataUtils;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class SelectProjectProcessor implements CallbackQueryProcessor {

    @Autowired
    GitlabService gitlabService;


    @Override
    public boolean doesProcess(CallbackQuery callbackQuery) {
        return callbackQuery.getData().startsWith("select_projectId_");
    }

    @SneakyThrows
    @Override
    public List<BotApiMethod> processCallbackQuery(CallbackQuery callbackQuery) throws BotApplicationException {
        Long userId = callbackQuery.getFrom().getId();
        String data = callbackQuery.getData();
        Long projectId = CallbackDataUtils.getProjectIdFromCallback(data);

        List<MergeRequest> releaseMRs = gitlabService.getLatestRelease(projectId);

        List<BotApiMethod> response = new ArrayList<>();

        String responseText;
        if (releaseMRs.isEmpty()) {
            responseText = "Не смог найти последний релиз, убедитесь, что вы создали MR, который начинается с \"Release\" и попробуйте заново";

            response.add(SendMessage.builder()
                    .text(responseText)
                    .chatId(userId)
                    .build());
        } else if (releaseMRs.size() == 1) {
            responseText = ("К сожалению, я смог найти только один релизный МР: %s \n" +
                    "Для формирования границ релиза нужно минимум 2 МРа, которые содержат слово \"release\" \n" +
                    "Попробуйте изменить название предыдущего мра, добавив в него \"release\" или попробуйте" +
                    " воспользоваться функционалом при формировании следующего релиза")
                    .formatted(releaseMRs.getFirst().getTitle());
            response.add(SendMessage.builder()
                    .text(responseText)
                    .chatId(userId)
                    .build());
        } else {
            responseText = ("Последний релиз: %s. Если все так, то после нажатия кнопки \"Сформировать\" " +
                    "появится отчет включающий все МРы между %s и %s").formatted(
                    releaseMRs.get(0).getTitle(), releaseMRs.get(0).getTitle(), releaseMRs.get(1).getTitle()
            );

            List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
            InlineKeyboardButton button = new InlineKeyboardButton();
            button.setText("Сформировать");
            button.setCallbackData("generate_report_projectId_%d_fromMRId_%d_toMRId_%d".formatted(projectId, releaseMRs.get(0).getIid(), releaseMRs.get(1).getIid()));
            keyboard.add(List.of(button));

            response.add(SendMessage.builder()
                    .text(responseText)
                    .chatId(userId)
                    .replyMarkup(
                            InlineKeyboardMarkup.builder()
                                    .keyboard(keyboard)
                                    .build())
                    .build());
        }


        return response;
    }


}
