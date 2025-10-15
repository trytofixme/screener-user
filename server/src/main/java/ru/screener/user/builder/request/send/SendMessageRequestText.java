package ru.screener.user.builder.request.send;

public class SendMessageRequestText {

    public static final String START_MESSAGE = "Приветствую!";
    public static final String MAIN_MENU_MESSAGE = "Главное меню";
    public static final String CURRENT_SETTINGS_MESSAGE = "⚙️ *Текущие настройки:*";
    public static final String UNKNOWN_COMMAND_MESSAGE = "Прости, я тебя не понимаю";
    public static final String TIME_FRAME_COMMAND_MESSAGE = "Выберите временной интервал";
    public static final String RSI_VALUE_COMMAND_MESSAGE = "Введите значение RSI";
    public static final String DUMP_VALUE_COMMAND_MESSAGE = "Введите значение дампа";
    public static final String FORMAT_ERROR_MESSAGE = "❗ Неверный формат. Попробуй снова.";
    public static final String AFTER_UPDATE_SETTINGS_MESSAGE = "✅ Готово. Текущие настройки:";
    public static final String RSI_HELP_MESSAGE =
        """
        🔴 *Прислать шортовый сигнал, если RSI монеты превысил значение X*;
        🟢 *Прислать лонговый сигнал, если RSI монеты меньше значения X*.
        """;
    public static final String DUMP_HELP_MESSAGE =
        """
        🔴 *Прислать сигнал о дампе на шорте, если RSI монеты после сигнала изменился на значение X*;
        🟢 *Прислать сигнал о дампе на лонге, если RSI монеты после сигнала изменился на значение X*.
        """;

    public static final String GET_ACCESS_MESSAGE =
        """
        ✅ Выбери тариф и оплати удобным способом.
        Доступ откроется автоматически в течение 1 минуты.
        """;
    public static final String ACCESS_PERMITTED = "Подписка уже оформлена";
}
