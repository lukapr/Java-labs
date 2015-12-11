import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by pryaly on 12/11/2015.
 */
public class HelloUDPClient {

    private final List<Thread> threads;
    private final SocketAddress address;
    private final String prefix;
    private final int numberOfThreads;
    private final int numberOfQueries;

    public HelloUDPClient(String host, int port, String prefix, int numberOfThreads, int numberOfQueries) throws UnknownHostException {
        this.address = new InetSocketAddress(InetAddress.getByName(host), port);
        this.prefix = prefix;
        this.numberOfQueries = numberOfQueries;
        this.numberOfThreads = numberOfThreads;
        this.threads = new ArrayList<>();
    }

    private class Sender implements Runnable {

        private int threadNumber;

        public Sender(int threadNumber) {
            this.threadNumber = threadNumber;
        }

        @Override
        public void run() {
            try(DatagramSocket socket = new DatagramSocket()) {
                for (int messageNumber = 0; messageNumber < numberOfQueries; messageNumber++) {
                    String message = prefix + threadNumber + "_" + messageNumber;
                    byte[] outBuf = message.getBytes();
                    DatagramPacket messagePacket = new DatagramPacket(outBuf, outBuf.length, address);
                    socket.send(messagePacket);
                    System.out.println("New message was sent to server: " + message);

                    byte[] inBuf = new byte[65536];
                    DatagramPacket responsePacket = new DatagramPacket(inBuf, inBuf.length);
                    socket.receive(responsePacket);
                    String response = new String(responsePacket.getData());
                    System.out.println("New response received \"" + response + "\"");
                }
            } catch (IOException e) {
                //TODO add Exception
                e.printStackTrace();
            }
        }
    }

    public void start() {
        for (int i = 0; i < numberOfThreads; i++) {
            threads.add(new Thread(new Sender(i)));
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
        if (args == null || args.length != 5) {
            System.err.println("Use: java HelloUDPClient host port prefix numberOfThreads numberOfQueries");
        } else {
            String host = args[0];
            int port = Integer.valueOf(args[1]);
            String prefix = args[2];
            int numberOfThreads = Integer.valueOf(args[3]);
            int numberOfQueries = Integer.valueOf(args[4]);
            HelloUDPClient helloUDPClient = new HelloUDPClient(host, port, prefix, numberOfThreads,numberOfQueries);
            helloUDPClient.start();
        }
    }
}
