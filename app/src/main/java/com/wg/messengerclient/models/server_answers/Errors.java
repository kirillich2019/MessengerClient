package com.wg.messengerclient.models.server_answers;

public enum Errors {
    LOGIN_TOO_SHORT(102),
    LOGIN_TOO_LONG(103),
    LOGIN_CONTAINS_WRONG_SYMBOLS(104),
    PASSWORD_TOO_SHORT(105),
    NAME_WRONG_LENGTH(106),
    NAME_CONTAINS_WRONG_SYMBOLS(107),
    SURNAME_WRONG_LENGTH(108),
    SURNAME_CONTAINS_WRONG_SYMBOLS(109);

    private int code;
    private String message;

    public String getMessage() {
        return message;
    }

    public boolean isMessageInClient() {
        return messageInClient;
    }

    private boolean messageInClient;

    Errors(int code) {
        this(code, "");
        messageInClient = false;
    }

    Errors(int code, String message) {
        this.code = code;
        this.message = message;
        messageInClient = true;
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
