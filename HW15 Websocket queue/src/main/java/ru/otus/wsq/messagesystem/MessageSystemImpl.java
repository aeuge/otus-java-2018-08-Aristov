package ru.otus.wsq.messagesystem;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class MessageSystemImpl implements MessageSystem{
    private final static Logger logger = Logger.getLogger(MessageSystem.class.getName());

    private final Map<Address, Thread> workers;
    private final Map<Address, LinkedBlockingQueue<Message>> messagesMap;
    private final Map<Address, Addressee> addresseeMap;

    public MessageSystemImpl() {
        workers = new HashMap<>();
        messagesMap = new HashMap<>();
        addresseeMap = new HashMap<>();
        logger.log(Level.INFO,"ms started");
    }

    public void addAddressee(Addressee addressee) {
        addresseeMap.put(addressee.getAddress(), addressee);
        messagesMap.put(addressee.getAddress(), new LinkedBlockingQueue<>());
        startThread(addressee);
    }

    public void sendMessage(Message message) {
        Address address = message.getTo();
        messagesMap.get(address).add(message);
        System.out.println("put message to db queue");
    }

    private void startThread(Addressee addressee) {
        String name = "MS-worker-" + addressee.getAddress().getId();
        Thread thread = new Thread(() -> {
            LinkedBlockingQueue<Message> queue = messagesMap.get(addressee.getAddress());
            while (true) {
                try {
                    Message message = queue.take();
                    message.exec(addressee);
                    System.out.println("exec message for " + name);
                } catch (InterruptedException e) {
                    logger.log(Level.INFO, "Thread interrupted. Finishing: " + name);
                    return;
                }
            }
        });
        thread.setName(name);
        thread.start();
        workers.put(addressee.getAddress(),thread);
        logger.log(Level.INFO,"thread " + name + " started");
    }

    public void endThread(Addressee addressee) {
        workers.get(addressee.getAddress()).interrupt();
        messagesMap.remove(addressee.getAddress());
        addresseeMap.remove(addressee.getAddress());
        logger.log(Level.INFO,"thread " + addressee.getAddress() + " interrupted");
    }

    public void dispose() {
        for (Map.Entry<Address, Thread> entry : workers.entrySet()) {
            entry.getValue().interrupt();
        }
    }
}
