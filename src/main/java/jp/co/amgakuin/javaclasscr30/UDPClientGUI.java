package jp.co.amgakuin.javaclasscr30;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class UDPClientGUI {

    private static DatagramSocket socket;
    private static InetAddress serverAddress;
    private static int serverPort = 12345;
    private static JTextArea displayArea;
    private static JTextField inputField;
    private static Thread receiveThread;

    public static void main(String[] args) {
        JFrame frame = new JFrame("UDP Client");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);
        frame.setLayout(new BorderLayout());

        // 上部のパネル（ボタンとテキスト入力エリア）
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout());

        JButton connectButton = new JButton("接続");
        JButton sendButton = new JButton("送信");
        JButton disconnectButton = new JButton("終了");
        inputField = new JTextField(20);

        topPanel.add(connectButton);
        topPanel.add(inputField);
        topPanel.add(sendButton);
        topPanel.add(disconnectButton);

        frame.add(topPanel, BorderLayout.NORTH);

        // 中央のテキストエリア（スクロール可能）
        displayArea = new JTextArea();
        displayArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(displayArea);
        frame.add(scrollPane, BorderLayout.CENTER);

        // ボタンのアクションリスナーを設定
        connectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                connectToServer();
            }
        });

        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendData();
            }
        });

        disconnectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                disconnectFromServer();
            }
        });

        frame.setVisible(true);
    }

    private static void connectToServer() {
        try {
            serverAddress = InetAddress.getByName("192.168.5.51");
            socket = new DatagramSocket();
            displayMessage("サーバーに接続しました");

            // データ受信用のスレッドを開始
            receiveThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    receiveData();
                }
            });
            receiveThread.start();
        } catch (Exception e) {
            displayMessage("接続エラー: " + e.getMessage());
        }
    }

    private static void sendData() {
        try {
            if (socket == null || serverAddress == null) {
                displayMessage("まずサーバーに接続してください");
                return;
            }
            String message = inputField.getText();
            byte[] sendData = message.getBytes();
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, serverAddress, serverPort);
            socket.send(sendPacket);
            displayMessage("送信: " + message);
        } catch (Exception e) {
            displayMessage("送信エラー: " + e.getMessage());
        }
    }

    private static void receiveData() {
        try {
            while (!socket.isClosed()) {
                byte[] receiveData = new byte[512];
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                socket.receive(receivePacket);
                String receivedMessage = new String(receivePacket.getData(), 0, receivePacket.getLength());
                displayMessage("受信: " + receivedMessage);
            }
        } catch (Exception e) {
            if (!socket.isClosed()) {
                displayMessage("受信エラー: " + e.getMessage());
            }
        }
    }

    private static void disconnectFromServer() {
        if (socket != null && !socket.isClosed()) {
            socket.close();
            displayMessage("サーバーとの接続を切断しました");
        }
        if (receiveThread != null && receiveThread.isAlive()) {
            receiveThread.interrupt();
        }
    }

    private static void displayMessage(String message) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                displayArea.append(message + "\n");
            }
        });
    }
}
