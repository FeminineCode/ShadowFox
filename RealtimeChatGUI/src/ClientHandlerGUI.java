import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ClientHandlerGUI implements Runnable {
    private Socket socket;
    private BufferedReader reader;
    private PrintWriter writer;
    private String userName;
    private String roomName;

    public ClientHandlerGUI(Socket socket) {
        this.socket = socket;
        try {
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            System.out.println("Error setting up streams: " + e.getMessage());
        }
    }

    @Override
    public void run() {
        try {
            // Protocol:
            // 1st line -> username
            // 2nd line -> room
            userName = reader.readLine();
            roomName = reader.readLine();

            if (userName == null || roomName == null || userName.trim().isEmpty() || roomName.trim().isEmpty()) {
                sendSystemToSelf("Invalid username or room name.");
                disconnect();
                return;
            }

            userName = userName.trim();
            roomName = roomName.trim();

            if (!joinRoom(roomName)) {
                sendCommand("ERROR_USERNAME_EXISTS");
                disconnect();
                return;
            }

            sendSystemToSelf("Welcome " + userName + "! You joined room [" + roomName + "]");
            broadcastSystem(userName + " joined the room.", false);
            sendUserListToRoom();

            String message;
            while ((message = reader.readLine()) != null) {
                message = message.trim();

                if (message.equalsIgnoreCase("/exit")) {
                    break;
                } else if (message.startsWith("/join ")) {
                    String newRoom = message.substring(6).trim();
                    if (!newRoom.isEmpty()) {
                        changeRoom(newRoom);
                    } else {
                        sendSystemToSelf("Room name cannot be empty.");
                    }
                } else if (!message.isEmpty()) {
                    broadcastChat(userName, message);
                }
            }

        } catch (IOException e) {
            System.out.println("Connection lost with client: " + userName);
        } finally {
            disconnect();
        }
    }

    private boolean joinRoom(String room) {
        ChatServerGUI.chatRooms.putIfAbsent(room, ConcurrentHashMap.newKeySet());

        Set<ClientHandlerGUI> clients = ChatServerGUI.chatRooms.get(room);

        synchronized (clients) {
            for (ClientHandlerGUI client : clients) {
                if (client.userName != null && client.userName.equalsIgnoreCase(userName)) {
                    return false;
                }
            }
            clients.add(this);
        }

        roomName = room;
        return true;
    }

    private void leaveRoom() {
        if (roomName != null && ChatServerGUI.chatRooms.containsKey(roomName)) {
            Set<ClientHandlerGUI> clients = ChatServerGUI.chatRooms.get(roomName);

            clients.remove(this);

            if (!clients.isEmpty()) {
                for (ClientHandlerGUI client : clients) {
                    client.sendSystem("[" + roomName + "] " + userName + " left the room.");
                }
                sendUserListToSpecificRoom(roomName);
            } else {
                ChatServerGUI.chatRooms.remove(roomName);
            }
        }
    }

    private void changeRoom(String newRoom) {
        String oldRoom = roomName;

        leaveRoom();

        if (!joinRoom(newRoom)) {
            joinRoom(oldRoom);
            sendSystemToSelf("Username already exists in room [" + newRoom + "].");
            sendUserListToRoom();
            return;
        }

        sendCommand("ROOM:" + roomName);
        sendSystemToSelf("You switched from [" + oldRoom + "] to [" + newRoom + "]");
        broadcastSystem(userName + " joined the room.", false);
        sendUserListToSpecificRoom(oldRoom);
        sendUserListToRoom();
    }

    private void broadcastChat(String sender, String message) {
        String time = getCurrentTime();
        String fullMessage = "CHAT:" + time + "|" + sender + "|" + message;

        Set<ClientHandlerGUI> clients = ChatServerGUI.chatRooms.get(roomName);
        if (clients != null) {
            for (ClientHandlerGUI client : clients) {
                client.writer.println(fullMessage);
            }
        }
    }

    private void broadcastSystem(String message, boolean includeSelf) {
        String time = getCurrentTime();
        String fullMessage = "SYSTEM:" + time + "|" + message;

        Set<ClientHandlerGUI> clients = ChatServerGUI.chatRooms.get(roomName);
        if (clients != null) {
            for (ClientHandlerGUI client : clients) {
                if (includeSelf || client != this) {
                    client.writer.println(fullMessage);
                }
            }
        }
    }

    private void sendSystem(String message) {
        String time = getCurrentTime();
        writer.println("SYSTEM:" + time + "|" + message);
    }

    private void sendSystemToSelf(String message) {
        sendSystem(message);
    }

    private void sendCommand(String command) {
        writer.println(command);
    }

    private void sendUserListToRoom() {
        sendUserListToSpecificRoom(roomName);
    }

    private void sendUserListToSpecificRoom(String room) {
        if (room == null) return;

        Set<ClientHandlerGUI> clients = ChatServerGUI.chatRooms.get(room);
        if (clients == null) return;

        StringBuilder sb = new StringBuilder("USERS:");
        boolean first = true;

        for (ClientHandlerGUI client : clients) {
            if (!first) sb.append(",");
            sb.append(client.userName);
            first = false;
        }

        String userListMessage = sb.toString();

        for (ClientHandlerGUI client : clients) {
            client.writer.println("ROOM:" + room);
            client.writer.println(userListMessage);
        }
    }

    private String getCurrentTime() {
        return new SimpleDateFormat("hh:mm a").format(new Date());
    }

    private void disconnect() {
        String oldRoom = roomName;
        leaveRoom();

        try {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
            System.out.println((userName != null ? userName : "Unknown user") + " disconnected.");
        } catch (IOException e) {
            System.out.println("Error closing socket.");
        }

        sendUserListToSpecificRoom(oldRoom);
    }
}