package com.scor.sas.exception;

import java.io.Serializable;

public class SasException extends Exception implements Serializable {

    private static final long serialVersionUID = -973189080258779842L;

    public SasException() {
        super();
    }

    public SasException(String message, Throwable cause) {
        super(message, cause);
    }

    public SasException(String message) {
        super(message);
    }

    public SasException(Throwable cause) {
        super(cause);
    }

}
