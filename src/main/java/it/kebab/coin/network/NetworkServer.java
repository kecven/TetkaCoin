package it.kebab.coin.network;

import it.kebab.coin.util.NetworkUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class NetworkServer {
    private static final Logger log = LoggerFactory.getLogger(NetworkServer.class);

    public static final int PORT = 9798;
    public static final Map<String, Node> NODES = new ConcurrentHashMap();

    @Autowired
    private ApplicationContext context;
    @Autowired
    private NetworkUtil networkUtil;


    @PostConstruct
    public void init() throws IOException, InterruptedException {
        serverUp();
    }

    public void serverUp() throws IOException {
        ServerSocketChannel ssChannel = ServerSocketChannel.open();
        ssChannel.configureBlocking(true);
        ssChannel.socket().bind(new InetSocketAddress(PORT));
        while (true) {
            SocketChannel sChannel = ssChannel.accept();
            context.getBean(Node.class, sChannel).run();
            new Node(sChannel).start();
        }
    }

    public void sendAllNodes(final Model model){
        NODES.forEach((String address, Node client) -> {
            try {
                client.send(model);
            } catch (IOException e) {
                log.warn("Can't send to user with address: " + address, e);
            }
        });
    }


}
