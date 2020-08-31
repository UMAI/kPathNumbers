package com.omalakhov.exception;

public class TreeException extends Exception {
    public enum Type {
        ROOT_NOT_FOUND("A root (a vertex without a parent) was not found"),
        WRONG_INPUT_FILE_FORMAT("Wrong input file format at line: {0}");

        private String message;

        Type(String message) {
            this.message = message;
        }

        String getMessage(String... messageParams) {
            String messageTemplate = message;
            for (int i = 0; i < messageParams.length; i++) {
                messageTemplate = messageTemplate.replace("{" + i + "}", messageParams[i]);
            }
            return messageTemplate;
        }
    }

    public TreeException(Type type, String... messageParams) {
        super(type.getMessage(messageParams));
    }

    public TreeException(Throwable exception, Type type, String... messageParams) {
        super(type.getMessage(messageParams), exception);
    }
}
