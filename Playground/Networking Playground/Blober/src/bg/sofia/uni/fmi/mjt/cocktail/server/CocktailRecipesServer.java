package bg.sofia.uni.fmi.mjt.cocktail.server;

import java.io.IOException;
import java.net.InetSocketAddress;

import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class CocktailRecipesServer {

    public static final int SERVER_PORT = 7777;

    private static final String SERVER_HOST = "localhost";

    private static final int BUFFER_SIZE = 1024;

    public static void main(String[] args) {
        try (ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
             ) {
            serverSocketChannel.bind(new InetSocketAddress(SERVER_HOST, SERVER_PORT));
            serverSocketChannel.configureBlocking(false);

            Selector selector = Selector.open();
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

            ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);

            while (true) {
                int readyChannels = selector.select();
                if (readyChannels == 0) {
                    continue;
                }

                Set<SelectionKey> selectionKey = selector.selectedKeys();
                Iterator<SelectionKey> iteratorKey = selectionKey.iterator();

                while (iteratorKey.hasNext()) {
                    SelectionKey key = iteratorKey.next();

                    if (key.isReadable()) {
                        SocketChannel sc = (SocketChannel) key.channel();

                        buffer.clear();// getting ready for reading

                        int flag = sc.read(buffer);
                        if (flag < 0 ) {
                            System.out.println("Client has closed the connection");
                            sc.close();
                            continue;
                        }



                        buffer.flip();
                        sc.write(buffer);

                    }else if (key.isAcceptable()) {
                        ServerSocketChannel sockChannel = (ServerSocketChannel) key.channel();
                        SocketChannel accept = sockChannel.accept();
                        accept.configureBlocking(false);
                        accept.register(selector, SelectionKey.OP_READ);
                    }

                    iteratorKey.remove();
                }

            }








        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
