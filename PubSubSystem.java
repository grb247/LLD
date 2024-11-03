//https://github.com/ashishps1/awesome-low-level-design/tree/main/solutions/java/src/pubsubsystem
interface Subscriber {
    void onMessage(Message message);
}

class PrintSubscriber implements Subscriber {
    private final String name;

    public PrintSubscriber(String name) {
        this.name = name;
    }

    @Override
    public void onMessage(Message message) {
        System.out.println("Subscriber " + name + " received message: " + message.getContent());
    }
}
 class Topic {
    private final String name;
    private final Set<Subscriber> subscribers = new CopyOnWriteArraySet<>();

    public Topic(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void addSubscriber(Subscriber subscriber) {
        subscribers.add(subscriber);
    }

    public void removeSubscriber(Subscriber subscriber) {
        subscribers.remove(subscriber);
    }

    public void publish(Message message) {
        for (Subscriber subscriber : subscribers) {
            subscriber.onMessage(message);
        }
    }
}
class Publisher {
    private final Set<Topic> topics;

    public Publisher() {
        this.topics = new HashSet<>();
    }

    public void registerTopic(Topic topic) {
        topics.add(topic);
    }

    public void publish(Topic topic, Message message) {
        if(!topics.contains(topic)) {
            System.out.println("This publisher can't publish to topic: " + topic.getName());
            return;
        }
        topic.publish(message);
    }
}
class Message {
    private final String content;

    public Message(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }
}
public class PubSubSystemDemo {
    public static void run() {
        // Create topics
        Topic topic1 = new Topic("Topic1");
        Topic topic2 = new Topic("Topic2");

        // Create publishers
        Publisher publisher1 = new Publisher();
        Publisher publisher2 = new Publisher();

        // Create subscribers
        Subscriber subscriber1 = new PrintSubscriber("Subscriber1");
        Subscriber subscriber2 = new PrintSubscriber("Subscriber2");
        Subscriber subscriber3 = new PrintSubscriber("Subscriber3");

        publisher1.registerTopic(topic1);
        publisher2.registerTopic(topic2);

        // Subscribe to topics
        topic1.addSubscriber(subscriber1);
        topic1.addSubscriber(subscriber2);
        topic2.addSubscriber(subscriber2);
        topic2.addSubscriber(subscriber3);

        // Publish messages
        publisher1.publish(topic1, new Message("Message1 for Topic1"));
        publisher1.publish(topic1, new Message("Message2 for Topic1"));
        publisher2.publish(topic2, new Message("Message1 for Topic2"));


        // Unsubscribe from a topic
        topic1.removeSubscriber(subscriber2);

        // Publish more messages
        publisher1.publish(topic1, new Message("Message3 for Topic1"));
        publisher2.publish(topic2, new Message("Message2 for Topic2"));
    }
}
