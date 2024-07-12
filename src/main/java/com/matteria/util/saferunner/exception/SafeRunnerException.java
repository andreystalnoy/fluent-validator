package com.matteria.util.saferunner.exception;

import java.util.List;

public class SafeRunnerException extends Throwable {
    private final List<Exception> executionExceptions;

    public SafeRunnerException(String message, List<Exception> executionExceptions) {
        super(message);
        this.executionExceptions = executionExceptions;
    }


    public List<Exception> getExecutionExceptions() {
        return executionExceptions;
    }
}
