import java.util.*;
// Card class;
// Bank class;
// Person
// ATM class
// Buttons
class User {
    private String name;
    private String id;
    private String email;
    public User(String name, String Id, String email)
    {
        this.name = name;
        this.id = id;
        this.email = email;
    }
    public String getId()
    {
        return id;
    }
}
enum TaskStatus {
    PENDING, COMPLETED, INPROGRESS
}

class Task {
    private String taskId;
    private String discription;
    private User user;
    private Date dueDate;
    private TaskStatus taskStatus;
    
    public Task(String taskId, String des, User user, Date dueDate, TaskStatus taskStatus)
    {
        this.taskId = taskId;
        this.discription = des;
        this.user = user;
        this.dueDate = dueDate;
        this.taskStatus = taskStatus;
    }
    
    public String getTaskId()
    {
        return taskId;
    }
    public String getDiscription()
    {
        return discription;
    }
    public User getUser()
    {
        return user;
    }
    public Date getDueDate()
    {
        return dueDate;
    }
    public TaskStatus getTaskStatus()
    {
        return taskStatus;
    }
    public void setTaskStatus(TaskStatus status)
    {
        taskStatus = status;
    }
    public void setDueDate(Date date)
    {
        dueDate = date;
    }
}
class TaskManager {
    //createtask
    //assigntask
    //updateuser
    //update status
    //user map
    //get task
    //get in range task
    private static TaskManager instance;
    private final Map<String, Task> tasks;
    private final Map<String, List<Task>> userTasks;
    
    private TaskManager()
    {
        tasks = new ConcurrentHashMap<>();
        userTasks = new ConcurrentHashMap<>();
    }
    public static synchronized TaskManager getInstance()
    {
        if(instance == null)
        instance = new TaskManager();
        return instance;
    }
    public void createtask(Task task)
    {
        tasks.put(task.getTaskId(), task);
        assignTasktoUser(task.getUser(), task);
    }
    public void deleteTask(Task task)
    {
        tasks.remove(task.getTaskId());
        List<Task> userTask = userTasks.get(task.getUser().getId());
        userTask.remove(task);
    }
    public void updateTask(Task task)
    {
        
    }
    public void markTaskAsCompleted(String taskId)
    {
        Task task = tasks.get(taskId);
        if(task != null)
        {
            synchronized(task) {
                task.setTaskStatus(TaskStatus.COMPLETED);
            }
        }
    }
    private void assignTasktoUser(User user, Task task)
    {
        List<Task> taskList = userTasks.getOrDefault(user.getId(), new ArrayList<>());
        taskList.add(task);
        userTasks.put(user.getId(), taskList);
    }
}
class Solution { 
    public static void main(String[] args) {
        //Demo here we write
        TaskManager taskManager = TaskManager.getInstance();
        User user1 = new User("Grb", "47", "email@12");
        User user2 = new User("Grb2", "48", "email@13");
        User user3 = new User("Grb3", "49", "email@11");
        Task task1 = new Task("12", "Task is task1", user1, new Date(), TaskStatus.PENDING);
        Task task2 = new Task("13", "Task is task2", user2, new Date(), TaskStatus.PENDING);
        Task task3 = new Task("14", "Task is task3", user3, new Date(), TaskStatus.PENDING);
        taskManager.createtask(task1);
        taskManager.createtask(task2);
        taskManager.createtask(task3);
        // here we can check for others as well
    }
}
