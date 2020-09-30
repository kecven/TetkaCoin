package it.kebab.coin.network;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class Node extends Thread {
    private static final Logger log = LoggerFactory.getLogger(Node.class);

    private SocketChannel socketChannel;
    private String remoteAddress;
    private ObjectOutputStream objectOutputStream;
    private ObjectInputStream objectInputStream;


    public Node(){}

    public Node(SocketChannel socketChannel) throws IOException {
        init(socketChannel);
    }

    public Node(String ip) throws IOException {
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.configureBlocking(true);

        if (socketChannel.connect(new InetSocketAddress(ip, NetworkServer.PORT))) {
            init(socketChannel);
        } else {
            throw new RuntimeException("Can't open connect with address = " + ip + ":" + NetworkServer.PORT);
        }
    }

    private void init(SocketChannel socketChannel) throws IOException {
        this.socketChannel = socketChannel;
        objectOutputStream = new ObjectOutputStream(socketChannel.socket().getOutputStream());
        objectInputStream = new ObjectInputStream(socketChannel.socket().getInputStream());
        remoteAddress = socketChannel.getRemoteAddress().toString();

        NetworkServer.NODES.put(remoteAddress, this);

        log.info("New connection with address = " + remoteAddress);
    }

    public void send(Model model) throws IOException {
        objectOutputStream.writeObject(model);
    }

    public void run() {
        Model model;
        try {
            while (true) {
                model = (Model) objectInputStream.readObject();
                getMsg(model);
            }
        } catch (IOException | ClassNotFoundException e) {
            log.error("Error handling client: " + remoteAddress + ": " + e);
        } finally {
            NetworkServer.NODES.remove(remoteAddress);
            try {
                socketChannel.close();
            } catch (IOException e) {
                log.error("Couldn't close a socket with remote address: " + remoteAddress);
            }
            log.warn("Connection with client: " + remoteAddress + " closed");
        }
    }

    private void getMsg(Model model){
        System.out.println(model);
    }

    @Override
    public int hashCode() {
        return remoteAddress.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return remoteAddress.equals(obj);
    }
}
