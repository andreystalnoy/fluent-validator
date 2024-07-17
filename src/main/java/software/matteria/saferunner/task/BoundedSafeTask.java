package software.matteria.saferunner.task;

public class BoundedSafeTask extends SafeTask {
    private final Class<? extends Exception>[] expectedExceptions;

    public BoundedSafeTask(Task task, Class<? extends Exception>[] expectedExceptions) {
        super(task);
        this.expectedExceptions = expectedExceptions;
    }

    public Class<? extends Exception>[] getExpectedExceptions() {
        return expectedExceptions;
    }
}
