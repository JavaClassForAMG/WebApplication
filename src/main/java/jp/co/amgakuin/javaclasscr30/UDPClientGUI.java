package jp.co.amgakuin.javaclasscr30;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class UDPClientGUI extends JFrame {
    private DatagramSocket socket;
    private InetAddress serverAddress;
    private int serverPort;
    private boolean running;
    
    // GUIコンポーネントの宣言
    private JTextArea displayArea;
    private JTextField inputField;
    private JButton connectButton;
    private JButton sendButton;
    private JButton disconnectButton;

    // コンストラクタ
    public UDPClientGUI() {
        // Nimbus Look and Feelの設定
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // ウィンドウの設定
        setTitle("UDP Client");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // データ表示エリアの設定
        displayArea = new JTextArea();
        displayArea.setEditable(false);
        displayArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(displayArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Received Data"));
        
        // テキスト入力エリアの設定
        inputField = new JTextField(30);
        
        // ボタンの設定
        connectButton = new JButton("Connect");
        sendButton = new JButton("Send");
        disconnectButton = new JButton("Disconnect");

        // パネルにコンポーネントを追加
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(connectButton, gbc);

        gbc.gridx = 1;
        panel.add(new JLabel("Message:"), gbc);

        gbc.gridx = 2;
        panel.add(inputField, gbc);

        gbc.gridx = 3;
        panel.add(sendButton, gbc);

        gbc.gridx = 4;
        panel.add(disconnectButton, gbc);

        // フレームにコンポーネントを追加
        add(scrollPane, BorderLayout.CENTER);
        add(panel, BorderLayout.SOUTH);

        // 接続ボタンのアクションリスナー
        connectButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                connectToServer();
            }
        });

        // 送信ボタンのアクションリスナー
        sendButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                sendMessage();
            }
        });

        // 終了ボタンのアクションリスナー
        disconnectButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                disconnectFromServer();
            }
        });
    }

    // サーバーに接続するメソッド
    private void connectToServer() {
        try {
            // サーバーのIPアドレスとポート番号を設定
            serverAddress = InetAddress.getByName("192.168.5.51");
            serverPort = 12345;
            
            // ソケットを作成
            socket = new DatagramSocket();
            running = true;

            // 受信スレッドを開始
            new Thread(new ReceiveTask()).start();
            
            displayArea.append("Connected to server.\n");
        } catch (Exception e) {
            displayArea.append("Connection error: " + e.getMessage() + "\n");
        }
    }

    // メッセージを送信するメソッド
    private void sendMessage() {
        try {
            if (socket == null || socket.isClosed()) {
                displayArea.append("Not connected.\n");
                return;
            }

            // 送信するバイナリーデータを準備
            String message = inputField.getText();
            byte[] sendData = message.getBytes();
            if (sendData.length > 512) {
                displayArea.append("Data size exceeds 512 bytes.\n");
                return;
            }

            // パケットを作成して送信
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, serverAddress, serverPort);
            socket.send(sendPacket);
            displayArea.append("Sent: " + message + "\n");
            inputField.setText(""); // 入力フィールドをクリア

        } catch (Exception e) {
            displayArea.append("Send error: " + e.getMessage() + "\n");
        }
    }

    // サーバーから切断するメソッド
    private void disconnectFromServer() {
        running = false; // 受信スレッドを停止
        if (socket != null && !socket.isClosed()) {
            socket.close();
            displayArea.append("Disconnected from server.\n");
        }
    }

    // 受信タスクを実行するクラス（Runnableを実装）
    private class ReceiveTask implements Runnable {
        public void run() {
            try {
                while (running) {
                    // 受信するバッファを準備
                    byte[] receiveData = new byte[512];
                    
                    // パケットを受信
                    DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                    socket.receive(receivePacket);
                    
                    // 受信したデータを表示
                    String receivedMessage = new String(receivePacket.getData(), 0, receivePacket.getLength());
                    displayArea.append("Received from server: " + receivedMessage + "\n");
                }
            } catch (Exception e) {
                if (running) {
                    displayArea.append("Receive error: " + e.getMessage() + "\n");
                }
            }
        }
    }

    // メインメソッド（プログラムのエントリーポイント）
    public static void main(String[] args) {
        // GUIをイベントディスパッチスレッドで起動
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new UDPClientGUI().setVisible(true);
            }
        });
    }
}
