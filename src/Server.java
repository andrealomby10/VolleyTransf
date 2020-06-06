import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDate;
import java.util.HashMap;

public class Server {
    public static final Athlete Andrea = new Athlete("Andrea","Lombardo",'M',"andrealomby10","pass","lomby@gmail.com","3489875451","Fiumefreddo di Sicilia",
            "95013","CT", LocalDate.of(1994,06,30),"Palleggiatore");
    public static final Athlete Davide = new Athlete("Davide","Lombardo",'M',"davidelomby7","pass","lomby@gmail.com","3489875451","Fiumefreddo di Sicilia",
            "95013","CT", LocalDate.of(1999,02,26),"Palleggiatore");
    public static final Athlete Simone = new Athlete("Simone","Lombardo",'M',"simonelomby13","pass","lomby@gmail.com","3489875451","Fiumefreddo di Sicilia",
            "95013","CT", LocalDate.of(1995,06,06),"Libero");
    public static final Manager Saro = new Manager("Saro","Pomecio",'M',"saropom","pass","lomby@gmail.com","3489875451","Fiumefreddo di Sicilia",
            "95013","CT", LocalDate.now(),"ASD Papiro Volley");

    private ServerSocket socket;
    private Socket client_socket;
    private int port;
    int client_id = 0;
    HashMap<String,User> list = new HashMap<>();

    public static void main(String args[]){
        Server server = new Server(1107);
        server.start();
    }

    public Server(int port){
        System.out.println("Initializing server with port "+port);
        this.port = port;
        list.put(Andrea.getUsername(),Andrea);
        list.put(Saro.getUsername(),Saro);
        list.put(Davide.getUsername(),Davide);
        list.put(Simone.getUsername(),Simone);


    }

    public void start() {
        try {
            System.out.println("Starting server on port "+port);
            socket = new ServerSocket(port);
            System.out.println("Started server on port "+port);
            while (true) {
                System.out.println("Listening on port " + port);
                client_socket = socket.accept();
                System.out.println("Accepted connection from " + client_socket.getRemoteSocketAddress());

                ClientManager cm = new ClientManager(client_socket,list);
                Thread t = new Thread(cm,"client_"+client_id);
                client_id++;
                t.start();
            }

        } catch (IOException e) {
            System.out.println("Could not start server on port "+port);
            e.printStackTrace();
        }

    }
}
