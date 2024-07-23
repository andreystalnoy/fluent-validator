package software.matteria.util;

import software.matteria.util.exception.SafeRunnerException;
import software.matteria.util.task.SafeTask;
import software.matteria.util.task.Task;
import software.matteria.util.exception.UnexpectedBehaviorException;
import software.matteria.util.task.BoundedSafeTask;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public class SafeRunner {
    private final List<SafeTask> tasks = new ArrayList<>();

    private SafeRunner() {}
    public static SafeRunner configure() {
        return new SafeRunner();
    }

    @SafeVarargs
    public final SafeRunner task(final Task task, final Class<? extends Exception>... expectedExceptions) {
        this.tasks.add(new BoundedSafeTask(task, expectedExceptions));
        return this;
    }

    public final SafeRunner task(final Task task) {
        this.tasks.add(new SafeTask(task));
        return this;
    }

    public void run() throws SafeRunnerException {
        List<Exception> exceptions = process(tasks);
        if (!exceptions.isEmpty()) {
            throw new SafeRunnerException("Tasks execution failed", exceptions);
        }
    }

    public void run(Consumer<SafeRunnerException> safeRunnerExceptionConsumer) {
        List<Exception> exceptions = process(tasks);
        if (!exceptions.isEmpty()) {
            safeRunnerExceptionConsumer.accept(new SafeRunnerException("Tasks execution failed", exceptions));
        }
    }

    private List<Exception> process(List<SafeTask> tasks) {
        List<Exception> exceptions = new ArrayList<>();
        for (SafeTask task : tasks) {
            try {
                task.getTask().execute();
            } catch (Exception e) {
                if (task instanceof BoundedSafeTask) {
                    BoundedSafeTask boundedTask = (BoundedSafeTask) task;
                    boolean isExpected = Arrays.stream(boundedTask.getExpectedExceptions())
                            .anyMatch(exp -> exp.isInstance(e));
                    if (!isExpected) {
                        throw new UnexpectedBehaviorException(String.format(
                                "An unexpected exception occurred. Expected: %s, Found: %s",
                                Arrays.toString(boundedTask.getExpectedExceptions()), e.getClass().getSimpleName()), e);
                    }
                }
                exceptions.add(e);
            }
        }
        return exceptions;
    }
}
