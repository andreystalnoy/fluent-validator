package software.matteria.saferunner.task;

public class SafeTask {
    private final Task task;
    public SafeTask(Task task) {
        this.task = task;
    }

    public Task getTask() {
        return task;
    }
}
