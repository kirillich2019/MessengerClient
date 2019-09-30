package com.wg.messengerclient.models.server_answers;

public enum Errors {
    SERVER_ACCESS_ERROR(-2, "Ошибка доступа к серверу."),
    UNKNOWN_ERROR(-1, "UNKNOWN_ERROR"),
    NO_ERROR(0, ""),
    WRONG_ARGS(1, "Неверные аргументы."),
    UNKNOWN_TOKEN(2, "Нейзвестный токен."),
    UNKNOWN_ID(3, "Нейзвестный id."),
    ACCOUNT_IS_NOT_FOUND(400, "Аккаунт не найден"),
    ALREADY_IN_FRIENDS(401, "Пользователь уже у вас в друзьях"),
    ALREADY_SENT_REQUEST(402, "Запрос уже был отправлен"),
    CANT_SEND_REQUEST_TO_YOURSELF(404, "Разве вы не дружите с собой?");

    private int code;
    private String message;

    public String getMessage() {
        return message;
    }

    Errors(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public static Errors getErrorByCode(int errorCode) {
        for(Errors e: Errors.values()) {
            if(e.code == errorCode) {
                return e;
            }
        }

        return null;
    }
}
