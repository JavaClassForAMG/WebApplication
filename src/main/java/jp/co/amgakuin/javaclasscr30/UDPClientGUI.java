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

public class UDPClientGUI extends JFrame {
    // GUIコンポーネントの宣言
    // 受信データを表示するテキストエリア
	private JTextArea displayArea;
    // 送信データを入力するテキストフィールド
	private JTextField inputField; 
    // 接続ボタン
	private JButton connectButton;
    // 送信ボタン
	private JButton sendButton; 
    // 終了ボタン
	private JButton disconnectButton; 
    
    // ソケット通信のための変数
    // データグラムソケット
	private DatagramSocket socket; 
    // サーバーのアドレス
	private InetAddress serverAddress;
    // サーバーのポート番号
	private int serverPort = 12345;
    // 受信スレッドの制御用
	private boolean running; 

    public UDPClientGUI() {
        super("UDP Client");

        // ウィンドウのレイアウトを設定
        setLayout(new BorderLayout());

        // データ表示エリア
        displayArea = new JTextArea();
        displayArea.setEditable(false); // 編集不可に設定
        JScrollPane scrollPane = new JScrollPane(displayArea); // スクロール可能にする
        add(scrollPane, BorderLayout.CENTER);

        // 下部パネルに接続ボタン、送信ボタン、テキスト入力エリア、終了ボタンを配置
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());

        connectButton = new JButton("接続");
        sendButton = new JButton("送信");
        inputField = new JTextField(20);
        disconnectButton = new JButton("終了");

        panel.add(connectButton);
        panel.add(inputField);
        panel.add(sendButton);
        panel.add(disconnectButton);

        add(panel, BorderLayout.SOUTH);

        // 各ボタンのアクションリスナーを設定
        connectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                connectToServer();
            }
        });

        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessage();
            }
        });

        disconnectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                disconnectFromServer();
            }
        });

        // ウィンドウ設定
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    // サーバーに接続するメソッド
    private void connectToServer() {
        try {
            serverAddress = InetAddress.getByName("192.168.5.51"); // サーバーのアドレスを取得
            socket = new DatagramSocket(); // ソケットを作成
            running = true; // 受信スレッドを動作状態にする
            new Thread(new ReceiveRunnable()).start(); // 受信スレッドを開始
            displayArea.append("サーバーに接続しました\n"); // 接続成功メッセージを表示
        } catch (Exception e) {
            displayArea.append("接続エラー: " + e.getMessage() + "\n"); // エラーメッセージを表示
        }
    }

    // サーバーにメッセージを送信するメソッド
    private void sendMessage() {
        try {
            String message = inputField.getText(); // テキストフィールドからメッセージを取得
            byte[] sendData = message.getBytes(); // メッセージをバイト配列に変換
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, serverAddress, serverPort); // データグラムパケットを作成
            socket.send(sendPacket); // パケットを送信
            displayArea.append("送信しました: " + message + "\n"); // 送信メッセージを表示
        } catch (Exception e) {
            displayArea.append("送信エラー: " + e.getMessage() + "\n"); // エラーメッセージを表示
        }
    }

    // サーバーとの接続を切断するメソッド
    private void disconnectFromServer() {
        running = false; // 受信スレッドを停止状態にする
        if (socket != null && !socket.isClosed()) {
            socket.close(); // ソケットを閉じる
            displayArea.append("サーバーとの接続を切断しました\n"); // 切断メッセージを表示
        }
    }

    // 非同期でデータを受信するためのRunnable
    private class ReceiveRunnable implements Runnable {
        @Override
        public void run() {
            try {
                while (running) {
                    byte[] receiveData = new byte[512]; // 受信用バッファを作成
                    DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length); // 受信用パケットを作成
                    socket.receive(receivePacket); // パケットを受信
                    String receivedMessage = new String(receivePacket.getData(), 0, receivePacket.getLength()); // 受信データを文字列に変換
                    displayArea.append("受信しました: " + receivedMessage + "\n"); // 受信メッセージを表示
                }
            } catch (Exception e) {
                if (running) { // runningがtrueの場合のみエラーメッセージを表示
                    displayArea.append("受信エラー: " + e.getMessage() + "\n"); // エラーメッセージを表示
                }
            }
        }
    }

    // メインメソッド
    public static void main(String[] args) {
        new UDPClientGUI(); // GUIを作成して表示
    }
}

