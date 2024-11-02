import java.util.concurrent.PriorityBlockingQueue;  
import java.util.Comparator;
import java.lang.Thread;
  
public class MyClass {  
    public static void main(String[] args) {  
        PriorityBlockingQueue<Task> pq = new PriorityBlockingQueue<>(11, new Compare());  
        pq.add(new Task(4, "Low Priority"));  
        pq.add(new Task(2, "Moderate Priority"));  
        pq.add(new Task(8, "Lowest Priority"));  
        PubSub ps = new PubSub(pq);
        Thread t = new Thread(ps);  
        t.start();  
        ps.add(new Task(11,"added new"));
    }  
}  
  
class Compare implements Comparator<Task> {  
    @Override  
    public int compare(Task t1, Task t2) {  
        return Integer.compare(t1.priority, t2.priority);  
    }  
}  
  
class Task {  
    int priority;  
    String task;  
    public Task(int priority, String task) {  
        this.priority = priority;  
        this.task = task;  
    }  
}  
  
class PubSub implements Runnable {  
    
    PriorityBlockingQueue<Task> pq;  
    public PubSub(PriorityBlockingQueue<Task> pq) {  
        this.pq = pq;  
    }  
  
    public void add(Task t)
    {
        pq.add(t);
    }
    @Override  
    public void run() {  
        try {  
            while (true) {  
                Task task = pq.take(); // This will block if the queue is empty  
                System.out.println("Priority is " + task.priority + ", task: " + task.task);  
            }  
        } catch (InterruptedException e) {  
            Thread.currentThread().interrupt(); // Restore the interrupted status  
        }  
    }
}  

