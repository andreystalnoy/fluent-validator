package software.matteria.util;

import software.matteria.util.exception.SafeRunnerException;
import software.matteria.util.exception.UnexpectedBehaviorException;
import software.matteria.util.task.Task;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;

class SafeRunnerTest {
    private final Task[] tasks = new Task[] {
            () -> { throw new IllegalArgumentException("Test Exception"); },
            () -> { /* empty */ }
    };

    @Test
    void testExpectedExceptionHandling() {
        SafeRunner runner = SafeRunner.configure()
                .task(tasks[0], IllegalArgumentException.class);

        assertThrows(SafeRunnerException.class, runner::run);
    }

    @Test
    void testUnexpectedExceptionHandling() {
        SafeRunner runner = SafeRunner.configure()
                .task(tasks[0], NullPointerException.class);

        assertThrows(UnexpectedBehaviorException.class, runner::run);
    }

    @Test
    void testNoExceptionScenario() {
        SafeRunner runner = SafeRunner.configure()
                .task(tasks[1]);

        assertDoesNotThrow(() -> runner.run());
    }

    @Test
    void testRunAndCatchScenario() {
        SafeRunner runner = SafeRunner.configure()
                .task(tasks[1], IllegalArgumentException.class);

        assertDoesNotThrow(() -> {
            runner.run((e) -> e.getExecutionExceptions().forEach(ex -> {
                /* Some errors handling */
            }));
        });
    }
}
