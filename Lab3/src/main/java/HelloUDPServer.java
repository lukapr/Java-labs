import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by pryaly on 12/11/2015.
 */
public class HelloUDPServer {

    private final int port;
    private final int numberOfThreads;
    private final List<Thread> threads;

    public HelloUDPServer(int port, int numberOfThreads) {
        this.port = port;
        this.numberOfThreads = numberOfThreads;
        threads = new ArrayList<>();
    }

    private class Replier implements Runnable {

        @Override
        public void run() {
            try(DatagramSocket socket = new DatagramSocket(port)) {
                byte[] buf = new byte[65536];
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                socket.receive(packet);
                String message = new String(packet.getData());
                System.out.println("New message received: " + message);

                String response = "Hello, " + message;
                byte[] outBuf = response.getBytes();
                DatagramPacket responsePacket = new DatagramPacket(outBuf, outBuf.length, packet.getSocketAddress());
                socket.send(responsePacket);
                System.out.println("Response was sent to client: " + response);
            } catch (IOException e) {
                //TODO Exception
                e.printStackTrace();
            }
        }
    }

    public void start() {
        for (int i = 0; i < numberOfThreads; i++) {
            threads.add(new Thread(new Replier()));
        }
        threads.stream().forEach(Thread::start);
    }

    public void stop() {
        threads.stream().forEach(Thread::interrupt);
    }

    public void restart() {
        stop();
        start();
    }

    public static void main(String args[]) throws UnknownHostException {
        if (args == null || args.length != 2) {
            System.err.println("Use: java HelloUDPServer port numberOfThreads");
        } else {
            int port = Integer.valueOf(args[0]);
            int numberOfThreads = Integer.valueOf(args[1]);
            HelloUDPServer helloUDPServer = new HelloUDPServer(port, numberOfThreads);
            helloUDPServer.start();
        }
    }
}
