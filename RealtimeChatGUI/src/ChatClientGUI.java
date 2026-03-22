import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ChatClientGUI extends JFrame {

    private JTextPane chatPane;
    private JTextField messageField;
    private JTextField usernameField;
    private JTextField roomField;
    private JTextField serverField;
    private JTextField portField;

    private JButton connectButton;
    private JButton sendButton;
    private JButton switchRoomButton;
    private JButton disconnectButton;

    private DefaultListModel<String> userListModel;
    private JList<String> usersList;
    private JLabel roomLabel;
    private JLabel statusLabel;

    private Socket socket;
    private BufferedReader reader;
    private PrintWriter writer;

    private String currentUser = "";
    private String currentRoom = "";

    public ChatClientGUI() {
        setTitle("Advanced Real-Time Chat Application");
        setSize(950, 620);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(12, 12));
        mainPanel.setBackground(new Color(245, 247, 255));
        mainPanel.setBorder(new EmptyBorder(12, 12, 12, 12));

        // Top panel
        JPanel topPanel = new JPanel(new GridLayout(3, 4, 10, 10));
        topPanel.setBackground(new Color(245, 247, 255));

        usernameField = new JTextField();
        roomField = new JTextField("Room1");
        serverField = new JTextField("localhost");
        portField = new JTextField("5000");

        connectButton = createStyledButton("Connect", new Color(76, 132, 255));
        switchRoomButton = createStyledButton("Switch Room", new Color(255, 153, 51));
        disconnectButton = createStyledButton("Disconnect", new Color(230, 76, 76));

        topPanel.add(new JLabel("Username:"));
        topPanel.add(usernameField);
        topPanel.add(new JLabel("Room Name:"));
        topPanel.add(roomField);

        topPanel.add(new JLabel("Server IP:"));
        topPanel.add(serverField);
        topPanel.add(new JLabel("Port:"));
        topPanel.add(portField);

        topPanel.add(connectButton);
        topPanel.add(switchRoomButton);
        topPanel.add(disconnectButton);
        topPanel.add(new JLabel(""));

        // Center panel
        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));
        centerPanel.setBackground(new Color(245, 247, 255));

        chatPane = new JTextPane();
        chatPane.setEditable(false);
        chatPane.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        chatPane.setBackground(Color.WHITE);
        JScrollPane chatScrollPane = new JScrollPane(chatPane);

        JPanel rightPanel = new JPanel(new BorderLayout(8, 8));
        rightPanel.setPreferredSize(new Dimension(220, 0));
        rightPanel.setBackground(new Color(245, 247, 255));

        JLabel usersTitle = new JLabel("Online Users");
        usersTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));

        userListModel = new DefaultListModel<>();
        usersList = new JList<>(userListModel);
        usersList.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        JScrollPane userScrollPane = new JScrollPane(usersList);

        roomLabel = new JLabel("Current Room: -");
        roomLabel.setFont(new Font("Segoe UI", Font.BOLD, 15));

        statusLabel = new JLabel("Status: Not connected");
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        rightPanel.add(usersTitle, BorderLayout.NORTH);
        rightPanel.add(userScrollPane, BorderLayout.CENTER);

        JPanel infoPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        infoPanel.setBackground(new Color(245, 247, 255));
        infoPanel.add(roomLabel);
        infoPanel.add(statusLabel);
        rightPanel.add(infoPanel, BorderLayout.SOUTH);

        centerPanel.add(chatScrollPane, BorderLayout.CENTER);
        centerPanel.add(rightPanel, BorderLayout.EAST);

        // Bottom panel
        JPanel bottomPanel = new JPanel(new BorderLayout(10, 10));
        bottomPanel.setBackground(new Color(245, 247, 255));

        messageField = new JTextField();
        sendButton = createStyledButton("Send", new Color(76, 181, 104));

        bottomPanel.add(messageField, BorderLayout.CENTER);
        bottomPanel.add(sendButton, BorderLayout.EAST);

        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(mainPanel);

        disconnectButton.setEnabled(false);
        switchRoomButton.setEnabled(false);
        sendButton.setEnabled(false);

        connectButton.addActionListener(this::connectToServer);
        sendButton.addActionListener(this::sendMessage);
        switchRoomButton.addActionListener(this::switchRoom);
        disconnectButton.addActionListener(this::disconnectFromServer);
        messageField.addActionListener(this::sendMessage);
    }

    private JButton createStyledButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        return button;
    }

    private void connectToServer(ActionEvent e) {
        String username = usernameField.getText().trim();
        String room = roomField.getText().trim();
        String server = serverField.getText().trim();
        String portText = portField.getText().trim();

        if (username.isEmpty() || room.isEmpty() || server.isEmpty() || portText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields.");
            return;
        }

        int port;
        try {
            port = Integer.parseInt(portText);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Port must be a number.");
            return;
        }

        try {
            socket = new Socket(server, port);
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(socket.getOutputStream(), true);

            writer.println(username);
            writer.println(room);

            currentUser = username;
            currentRoom = room;

            appendSystemMessage("Connected to server.");
            updateRoomLabel(room);
            statusLabel.setText("Status: Connected as " + username);

            connectButton.setEnabled(false);
            disconnectButton.setEnabled(true);
            switchRoomButton.setEnabled(true);
            sendButton.setEnabled(true);

            usernameField.setEditable(false);
            serverField.setEditable(false);
            portField.setEditable(false);

            Thread receiveThread = new Thread(() -> {
                try {
                    String message;
                    while ((message = reader.readLine()) != null) {
                        handleIncomingMessage(message);
                    }
                } catch (IOException ex) {
                    SwingUtilities.invokeLater(() -> appendSystemMessage("Disconnected from server."));
                } finally {
                    SwingUtilities.invokeLater(this::resetUIAfterDisconnect);
                }
            });

            receiveThread.start();

        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Could not connect to server.");
        }
    }

    private void handleIncomingMessage(String message) {
        SwingUtilities.invokeLater(() -> {
            if (message.startsWith("CHAT:")) {
                // CHAT:time|sender|text
                String data = message.substring(5);
                String[] parts = data.split("\\|", 3);
                if (parts.length == 3) {
                    appendChatMessage(parts[0], parts[1], parts[2]);
                }
            } else if (message.startsWith("SYSTEM:")) {
                // SYSTEM:time|text
                String data = message.substring(7);
                String[] parts = data.split("\\|", 2);
                if (parts.length == 2) {
                    appendSystemMessage("[" + parts[0] + "] " + parts[1]);
                } else {
                    appendSystemMessage(data);
                }
            } else if (message.startsWith("USERS:")) {
                updateUsersList(message.substring(6));
            } else if (message.startsWith("ROOM:")) {
                currentRoom = message.substring(5);
                updateRoomLabel(currentRoom);
            } else if (message.equals("ERROR_USERNAME_EXISTS")) {
                JOptionPane.showMessageDialog(this, "Username already exists in this room.");
                disconnectFromServer(null);
            } else {
                appendSystemMessage(message);
            }
        });
    }

    private void sendMessage(ActionEvent e) {
        String message = messageField.getText().trim();

        if (writer == null || message.isEmpty()) {
            return;
        }

        writer.println(message);
        messageField.setText("");
    }

    private void switchRoom(ActionEvent e) {
        String newRoom = roomField.getText().trim();

        if (writer == null) {
            JOptionPane.showMessageDialog(this, "Not connected.");
            return;
        }

        if (newRoom.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter a room name.");
            return;
        }

        writer.println("/join " + newRoom);
    }

    private void disconnectFromServer(ActionEvent e) {
        try {
            if (writer != null) {
                writer.println("/exit");
            }
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        } catch (IOException ex) {
            appendSystemMessage("Error while disconnecting.");
        } finally {
            resetUIAfterDisconnect();
        }
    }

    private void appendChatMessage(String time, String sender, String message) {
        String prefix = sender.equalsIgnoreCase(currentUser) ? "(You)" : sender;
        chatPane.setText(chatPane.getText() + "[" + time + "] " + prefix + ": " + message + "\n");
    }

    private void appendSystemMessage(String message) {
        chatPane.setText(chatPane.getText() + "[SYSTEM] " + message + "\n");
    }

    private void updateUsersList(String users) {
        userListModel.clear();
        if (users.trim().isEmpty()) return;

        String[] userArray = users.split(",");
        for (String user : userArray) {
            if (user.equalsIgnoreCase(currentUser)) {
                userListModel.addElement(user + " (You)");
            } else {
                userListModel.addElement(user);
            }
        }
    }

    private void updateRoomLabel(String room) {
        roomLabel.setText("Current Room: " + room);
    }

    private void resetUIAfterDisconnect() {
        connectButton.setEnabled(true);
        disconnectButton.setEnabled(false);
        switchRoomButton.setEnabled(false);
        sendButton.setEnabled(false);

        usernameField.setEditable(true);
        serverField.setEditable(true);
        portField.setEditable(true);

        statusLabel.setText("Status: Not connected");
        roomLabel.setText("Current Room: -");
        userListModel.clear();

        writer = null;
        reader = null;
        socket = null;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ChatClientGUI().setVisible(true));
    }
}