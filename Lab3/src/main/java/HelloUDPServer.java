import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by pryaly on 12/11/2015.
 */
public class HelloUDPServer {

    private final int port;
    private final int numberOfThreads;
    private ExecutorService executor;
    private DatagramSocket socket;

    public HelloUDPServer(int port, int numberOfThreads) {
        this.port = port;
        this.numberOfThreads = numberOfThreads;
    }

    private class Replier implements Runnable {

        private final DatagramPacket messagePacket;

        public Replier(DatagramPacket messagePacket) {
            this.messagePacket = messagePacket;
        }

        @Override
        public void run() {
            try (DatagramSocket socket = new DatagramSocket()) {

                byte[] data = messagePacket.getData();
                String message = new String(data, 0, data.length);

                System.out.println("Receive new message:  " + message);

                String response = "Hello, " + message;

                byte[] outBuf = response.getBytes();
                DatagramPacket responsePacket = new DatagramPacket(outBuf, 0, outBuf.length, messagePacket.getSocketAddress());
                socket.send(responsePacket);
                System.out.println("Response was sent to client: " + response);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void start() throws Exception {
        executor = Executors.newFixedThreadPool(numberOfThreads);
        try {
            socket = new DatagramSocket(port);
            while (!Thread.interrupted()) {
                try {
                    byte[] inBuf = new byte[1024];
                    DatagramPacket messagePacket = new DatagramPacket(inBuf, 0, 1024);
                    socket.receive(messagePacket);
                    executor.execute(new Replier(messagePacket));
                } catch (IOException e) {
                    System.err.println("Problem with receiving packet: " + e.getMessage());
                }
            }
        } catch (SocketException e) {
            throw new Exception("Could not bind server to port "+ port, e);
        }
        stop();
    }

    public void stop() {
        executor.shutdown();
        socket.close();
    }

    public void restart() throws Exception {
        stop();
        start();
    }

    public static void main(String args[]) throws Exception {
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
