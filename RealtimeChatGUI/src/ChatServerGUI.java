import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ChatServerGUI {
    public static final int PORT = 5000;

    // room name -> clients in that room
    public static final Map<String, Set<ClientHandlerGUI>> chatRooms = new ConcurrentHashMap<>();

    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println(" Advanced Chat Server Started");
        System.out.println(" Port: " + PORT);
        System.out.println("========================================");

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("New client connected: " + socket.getInetAddress().getHostAddress());

                ClientHandlerGUI clientHandler = new ClientHandlerGUI(socket);
                Thread thread = new Thread(clientHandler);
                thread.start();
            }
        } catch (IOException e) {
            System.out.println("Server error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}