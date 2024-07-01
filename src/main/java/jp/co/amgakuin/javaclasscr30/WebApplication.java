package jp.co.amgakuin.javaclasscr30;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class WebApplication implements CommandLineRunner {
    private DatagramSocket socket;

	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(WebApplication.class, args);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            SpringApplication.exit(context, () -> 0);
        }));
	}
	
    @Override
    public void run(String... args) throws Exception {
        socket = new DatagramSocket(12345);
        byte[] buffer = new byte[512];
        System.out.println("UDPサーバーがポート12345で起動しました。");

        try {
            while (true) {
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);
                String receivedData = new String(packet.getData(), 0, packet.getLength());
                System.out.println("受信: " + receivedData);

                // 受信データをそのまま送り返す
                InetAddress clientAddress = packet.getAddress();
                int clientPort = packet.getPort();
                DatagramPacket responsePacket = new DatagramPacket(packet.getData(), packet.getLength(), clientAddress, clientPort);
                socket.send(responsePacket);
            }
        } finally {
            if (socket != null && !socket.isClosed()) {
                socket.close();
                System.out.println("ソケットがクローズされました。");
            }
        }
    }

}
