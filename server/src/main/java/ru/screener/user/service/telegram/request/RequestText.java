package ru.screener.user.service.telegram.request;

public class RequestText {

    public static final String START_MESSAGE = "Приветствую!";
    public static final String MAIN_MENU_MESSAGE = "Главное меню:";
    public static final String CURRENT_SETTINGS_MESSAGE = "⚙️ *Текущие настройки:*";
    public static final String UNKNOWN_COMMAND_MESSAGE = "Прости, я тебя не понимаю";
    public static final String TIME_FRAME_COMMAND_MESSAGE = "Выберите временной интервал";
    public static final String RSI_VALUE_COMMAND_MESSAGE = "Введите значение RSI";
    public static final String FORMAT_ERROR_MESSAGE = "❗ Неверный формат. Попробуй снова.";
    public static final String AFTER_UPDATE_SETTINGS_MESSAGE = "✅ Готово. Текущие настройки:";
    public static final String RSI_HELP_MESSAGE =
        """
        🔴 *Шорт*: пришлём сигнал, если RSI монеты превысил значение X (индикатор шорта);
        🟢 *Лонг*: пришлём сигнал, если RSI монеты меньше значения X (индикатор лонга).
        """;
}
