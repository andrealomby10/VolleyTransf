import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.util.*;

public class ClientManager implements Runnable{
    private Socket client_socket;
    HashMap<String,User> list;

    @Override
    public void run() {
        String tid = Thread.currentThread().getName();
        System.out.println(tid+"-> Accepted connection from " + client_socket.getRemoteSocketAddress());

        ObjectInputStream client_scanner = null;
        ObjectOutputStream pw = null;

        try {
            client_scanner = new ObjectInputStream(client_socket.getInputStream());
            pw = new ObjectOutputStream(client_socket.getOutputStream());
        } catch (IOException e) {e.printStackTrace();}
        boolean go = true;
        while (go) {
            String message = null;
            try {
                message = (String) client_scanner.readObject();
                System.out.println("Server Received: " + message);

                switch (message){
                    case Protocol.REGISTER:
                        message = (String) client_scanner.readObject();
                        switch (message){
                            case "Athlete":
                                Athlete newath = (Athlete) client_scanner.readObject();
                                list.put(newath.getUsername(),newath);
                                pw.writeObject(Protocol.REGISTER_SUCCESS);
                                break;
                            case "Manager":
                                Manager newman = (Manager) client_scanner.readObject();
                                list.put(newman.getUsername(),newman);
                                pw.writeObject(Protocol.REGISTER_SUCCESS);
                                break;
                        }
                        System.out.println(list.toString());
                        break;
                    case Protocol.LOGIN:
                        String user  = (String) client_scanner.readObject();
                        String pass  = (String) client_scanner.readObject();
                        User u = list.get(user);
                            if(u!=null && u.Login(user,pass)){
                                if(u.isActive()){
                                    pw.writeObject(Protocol.ALREADYACTIVEUSER);
                                }
                                else{
                                u.setActive(true);
                                pw.writeObject(Protocol.LOGIN_SUCCESS);
                                pw.writeObject(u);
                                break;}
                            }
                        pw.writeObject(Protocol.USERORPASS_ERROR);
                        break;
                    case Protocol.CHECK_USERNAME:
                        String username = (String) client_scanner.readObject();
                        if(list.containsKey(username))pw.writeObject(Protocol.ALREADYUSEDUSERNAME);
                        else pw.writeObject(Protocol.USERNAME_OK);
                        break;
                    case Protocol.USER_UPDATE:
                        User tmp = (User) client_scanner.readObject();
                        list.remove(tmp.getUsername());
                        list.put(tmp.getUsername(),tmp);
                        break;
                    case Protocol.ROLE_SEARCH:
                        char rolegender = (char) client_scanner.readObject();
                        String role = (String) client_scanner.readObject();
                        ArrayList<AthShow> rolelist = new ArrayList<AthShow>();
                        for(Map.Entry<String,User> entry : list.entrySet()){
                            if(entry.getValue().getClass().getName() == "Athlete" && entry.getValue().getGender()==rolegender)
                                if(((Athlete) entry.getValue()).getRole().equals(role))rolelist.add(new AthShow((Athlete) entry.getValue()));
                        }
                        pw.writeObject(rolelist);
                        break;
                    case Protocol.AGE_SEARCH:
                        char agegender = (char) client_scanner.readObject();
                        int minage = (int) client_scanner.readObject();
                        int maxage = (int) client_scanner.readObject();
                        ArrayList<AthShow> agelist = new ArrayList<AthShow>();
                        for(Map.Entry<String,User> entry : list.entrySet()){
                            if(entry.getValue().getClass().getName() == "Athlete" && entry.getValue().getGender()==agegender)
                                if(((Athlete) entry.getValue()).getAge()>minage && ((Athlete) entry.getValue()).getAge()<maxage)agelist.add(new AthShow((Athlete) entry.getValue()));
                        }
                        pw.writeObject(agelist);
                        break;
                    case Protocol.PLACE_SEARCH:
                        char placegender = (char) client_scanner.readObject();
                        String place = (String) client_scanner.readObject();
                        ArrayList<AthShow> placelist = new ArrayList<AthShow>();
                        for(Map.Entry<String,User> entry : list.entrySet()){
                            if(entry.getValue().getClass().getName() == "Athlete" && entry.getValue().getGender()==placegender)
                                if(entry.getValue().getLocation().Province.equals(place))placelist.add(new AthShow((Athlete) entry.getValue()));
                        }
                        pw.writeObject(placelist);
                        break;
                    case Protocol.QUIT:
                        System.out.println("Server: Closing connection to "+client_socket.getRemoteSocketAddress());
                        client_socket.close();
                        go = false;
                        break;
                    }
            }
                    catch (IOException | ClassNotFoundException e) {e.printStackTrace();break;}
                    }
        }

    public ClientManager(Socket myclient,HashMap<String,User> list) {
        client_socket = myclient;
        this.list = list;
    }

    }
