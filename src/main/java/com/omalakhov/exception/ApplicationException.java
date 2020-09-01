package com.omalakhov.exception;

public class ApplicationException extends Exception {
    public enum Type {
        TREE_ROOT_NOT_FOUND("A root (a vertex without a parent) of a tree was not found"),
        WRONG_INPUT_FILE_FORMAT("Wrong input file format");

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

    public ApplicationException(Type type, String... messageParams) {
        super(type.getMessage(messageParams));
    }

    public ApplicationException(Throwable exception, Type type, String... messageParams) {
        super(type.getMessage(messageParams), exception);
    }
}
