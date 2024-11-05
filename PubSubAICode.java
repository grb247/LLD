import java.util.*;  
import java.util.concurrent.ConcurrentHashMap;  
import java.util.concurrent.ExecutorService;  
import java.util.concurrent.Executors;  
  
// Publisher Interface  
interface Publisher {  
    void publish(String topic, Message message);  
}  
  
// Subscriber Interface  
interface Subscriber {  
    void receive(Message message);  
}  
  
// Message Class  
class Message {  
    private String content;  
  
    public Message(String content) {  
        this.content = content;  
    }  
  
    public String getContent() {  
        return content;  
    }  
}  
  
// Abstract Publisher  
abstract class AbstractPublisher implements Publisher {  
    protected MessageBroker broker;  
  
    public AbstractPublisher(MessageBroker broker) {  
        this.broker = broker;  
    }  
}  
  
// Abstract Subscriber  
abstract class AbstractSubscriber implements Subscriber {  
    protected MessageBroker broker;  
  
    public AbstractSubscriber(MessageBroker broker) {  
        this.broker = broker;  
    }  
  
    public void subscribe(String topic) {  
        broker.subscribe(topic, this);  
    }  
}  
  
// Message Broker  
class MessageBroker {  
    private final Map<String, List<Subscriber>> subscribers = new ConcurrentHashMap<>();  
    private final ExecutorService executorService = Executors.newCachedThreadPool();  
  
    public void subscribe(String topic, Subscriber subscriber) {  
        subscribers.computeIfAbsent(topic, k -> new ArrayList<>()).add(subscriber);  
    }  
  
    public void publish(String topic, Message message) {  
        List<Subscriber> topicSubscribers = subscribers.get(topic);  
        if (topicSubscribers != null) {  
            for (Subscriber subscriber : topicSubscribers) {  
                executorService.submit(() -> subscriber.receive(message));  
            }  
        }  
    }  
}  
  
// Concrete Publisher  
class ConcretePublisher extends AbstractPublisher {  
  
    public ConcretePublisher(MessageBroker broker) {  
        super(broker);  
    }  
  
    @Override  
    public void publish(String topic, Message message) {  
        broker.publish(topic, message);  
    }  
}  
  
// Concrete Subscriber  
class ConcreteSubscriber extends AbstractSubscriber {  
  
    public ConcreteSubscriber(MessageBroker broker) {  
        super(broker);  
    }  
  
    @Override  
    public void receive(Message message) {  
        System.out.println("Received message: " + message.getContent());  
    }  
}  
  
// Main Class to Test the System  
public class PubSubSystem {  
  
    public static void main(String[] args) {  
        MessageBroker broker = new MessageBroker();  
  
        Publisher publisher1 = new ConcretePublisher(broker);  
        Publisher publisher2 = new ConcretePublisher(broker);  
  
        Subscriber subscriber1 = new ConcreteSubscriber(broker);  
        Subscriber subscriber2 = new ConcreteSubscriber(broker);  
  
        subscriber1.subscribe("topic1");  
        subscriber2.subscribe("topic1");  
  
        publisher1.publish("topic1", new Message("Hello from publisher1"));  
        publisher2.publish("topic1", new Message("Hello from publisher2"));  
    }  
}  
