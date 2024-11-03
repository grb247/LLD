public interface ExecutionContext {

    void execute();
}

public abstract class ScheduledTask {

    public final ExecutionContext context;

    public ScheduledTask (ExecutionContext context) {
        this.context = context;
    }

    abstract boolean isRecurring();

    void execute() {
        context.execute();
    }

    abstract Optional<ScheduledTask> nextScheduledTask();

    abstract long getNextExecutionTime();
}

public class OneTimeTask extends ScheduledTask {

    private final long executionTime;

    public OneTimeTask(ExecutionContext context, long executionTime) {
    super(context);
        this.executionTime = executionTime;
    }

    @Override
    public long getNextExecutionTime() {
        return executionTime;
    }

    @Override
    public boolean isRecurring() {
        return false;
    }

    @Override
    public Optional<ScheduledTask> nextScheduledTask() {
        return Optional.empty();
    }
}

public class RecurringTask extends ScheduledTask {

    private final long executionTime;

    private final long interval;

    public RecurringTask(ExecutionContext context, long executionTime, long interval) {
        super(context);
        this.executionTime = executionTime;
        this.interval = interval;
    }

    @Override
    public long getNextExecutionTime() {
        return executionTime;
    }

    @Override
    public boolean isRecurring() {
        return true;
    }

    @Override
    public Optional<ScheduledTask> nextScheduledTask() {
        return Optional.of(new RecurringTask(context, executionTime + interval, interval));
    }
}

public interface TaskStore {  
    ScheduledTask peek();  
  
    ScheduledTask poll();  
  
    void add(ScheduledTask task);  
  
    boolean remove(ScheduledTask task);  
  
    boolean isEmpty();  
}  

public class PriorityBlockingQueueTaskStore implements TaskStore<ScheduledTask> {

    private final PriorityBlockingQueue<ScheduledTask> taskQueue;

    private final Set<ScheduledTask> tasks;

    public PriorityBlockingQueueTaskStore(Comparator<ScheduledTask> comparator, Integer queueSize) {
        this.taskQueue = new PriorityBlockingQueue<>(queueSize, comparator);
        this.tasks = new HashSet<>();
    }

    @Override
    public void add(ScheduledTask task) {
        taskQueue.offer(task);
    }

    @Override
    public ScheduledTask poll() {
        return taskQueue.poll();
    }

    @Override
    public ScheduledTask peek() {
        return taskQueue.peek();
    }

    @Override
    public boolean remove(ScheduledTask task) {
        if (tasks.contains(task)) {
            taskQueue.remove(task);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean isEmpty() {
        return taskQueue.isEmpty();
    }
}

public class TaskRunner implements Runnable {

    private final TaskStore<ScheduledTask> taskStore;

    private volatile boolean running;

    public TaskRunner(TaskStore<ScheduledTask> taskStore) {
        this.taskStore = taskStore;
        this.running = true;
    }

    public void run() {
        while (running && !Thread.currentThread().isInterrupted()) {
            try {
                ScheduledTask scheduledTask = taskStore.take(); // Block until a task is available
                long delay = scheduledTask.getNextExecutionTime() - Instant.now().toEpochMilli();
                if (delay > 0) {
                    // Re-queue the task and wait until it is ready
                    taskStore.put(scheduledTask);
                    synchronized (this) {
                        wait(delay);
                    }
                } else {
                    scheduledTask.execute();
                    if (scheduledTask.isRecurring()) {
                        taskStore.put(scheduledTask.nextScheduledTask());
                    }
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // Preserve the interrupted status
            }
        }
    }

    public void stop() {
        this.running = false;
        synchronized (this) {
            notify(); // Notify the thread to wake up and stop
        }
    }
}

public class ExecutorConfig {

    private int numThreads;

    public ExecutorConfig(int numThreads) {
        this.numThreads = numThreads;
    }

    public int getNumThreads() {
        return numThreads;
    }

    public void setNumThreads(int numThreads) {
        this.numThreads = numThreads;
    }
}

public class TaskScheduler {

    private final List<Thread> threads;

    private final List<TaskRunner> taskRunners;

    private final TaskStore<ScheduledTask> taskStore;

    public TaskScheduler(ExecutorConfig executorConfig, TaskStore<ScheduledTask> taskStore) {
        this.threads = new ArrayList<>();
        this.taskRunners = new ArrayList<>();
        this.taskStore = taskStore;
        for (int i = 0; i < executorConfig.getNumThreads(); i++) {
            TaskRunner taskRunner = new TaskRunner(taskStore);
            Thread thread = new Thread(taskRunner);
            thread.start();
            threads.add(thread);
            taskRunners.add(taskRunner);
        }
    }

    public void stop() {
        taskRunners.forEach(TaskRunner::stop);
        threads.forEach(t -> {
            t.interrupt();
            try {
                t.join(); // Wait for the thread to finish
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
    }
}
