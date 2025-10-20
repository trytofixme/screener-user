package ru.screener.user.builder.request.send;

public class SendMessageRequestText {

    public static final String START_MESSAGE = """
    Приветствую!
    RSI Alert Bot - это бот, который оповещает об изменениях индикатора RSI.
    Для получения доступа к функционалу нажми "💳 Доступ"
    """;
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

    //to send custom emojis need to purchase 5k TON to allow bot to attach custom usernames (https://stackoverflow.com/questions/74437942/how-to-send-custom-emoji-from-bot-in-telegram)
    public static final String GET_ACCESS_MESSAGE =
        """
        ![⚠️](tg://emoji?id=5420323339723881652) Как часто ты узнавал о __прибыльных возможностях__\\, но было уже поздно и ты не успевал ими воспользоваться\\, испытывая *разочарование* и острое *FOMO*⁉️
        
        ![📈](tg://emoji?id=5244837092042750681) *RSI Alert Bot* поможет тебе __избежать таких проблем__\\.
        
        ![✅](tg://emoji?id=5206607081334906820) Мы *мониторим* весь крипторынок за тебя\\, и пришлем уведомление\\, если *индикатор относительной силы \\(RSI\\)* превысит твое значение\\.
        
        ![😎](tg://emoji?id=5240476433221953356) *Все просто* \\- __после получения доступа__ тебе станут доступны настройки\\, с помощью которых ты сможешь выбрать значения RSI\\, при достижении которых бот немедленно пришлет уведомление\\.
        
        ![👨‍💻](tg://emoji?id=5012808793910674567) Мы постоянно улучшаем и дополняем бота полезными функциями\\, так что __следи за обновлениями__ \\- скоро появятся еще более *широкие возможности*\\!
        
        Будем рады видеть тебя в нашей команде трейдеров\\!
        
        ![✅](tg://emoji?id=5206607081334906820) *Выбери один из тарифных планов и оплати с помощью карты*\\.
        Доступ откроется автоматически в течение 1 минуты\\.
        """;
    public static final String ACCESS_PERMITTED = "Подписка уже оформлена";
}
