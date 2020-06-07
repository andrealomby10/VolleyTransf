import java.io.*;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class UserHashMap implements Serializable{
    private HashMap<String,User> Users;
    private File memory;
    private LocalDateTime LastBackup;

    public UserHashMap(String backup_path) throws IOException, ClassNotFoundException {
        memory = new File(backup_path);
        if(memory.createNewFile())Users = new HashMap<>();
        else Users = Load();
        Save();
    }

    public UserHashMap() throws IOException, ClassNotFoundException {
        memory = new File("backup.vt");
        if(memory.createNewFile())Users = new HashMap<>();
        else Users = Load();
        Save();
    }

    public synchronized void Save() throws IOException {
        FileOutputStream writer = new FileOutputStream(memory);
        ObjectOutputStream memstream = new ObjectOutputStream(writer);

        memstream.writeObject(Users);
        memstream.close();
        LastBackup = LocalDateTime.now();
    }

    private synchronized HashMap<String,User> Load() throws IOException, ClassNotFoundException {
        HashMap<String,User> users = new HashMap<>();
        FileInputStream reader = new FileInputStream(memory);
        ObjectInputStream memstream = new ObjectInputStream(reader);

        users = (HashMap<String, User>) memstream.readObject();
        memstream.close();
        return users;
    }

    public synchronized String put (User u){
        if(Users.containsKey(u.getUsername()))return Protocol.ALREADYUSEDUSERNAME;
        else{
        Users.put(u.getUsername(),u);
        return Protocol.REGISTER_SUCCESS;}
    }

    public User get (String key){
        return Users.get(key);
    }

    public synchronized void update(User tmp){
        Users.remove(tmp.getUsername());
        Users.put(tmp.getUsername(),tmp);
    }

    public synchronized boolean containsKey(String key){
        return Users.containsKey(key);
    }

    public Set<Map.Entry<String, User>> entrySet(){
        return Users.entrySet();
    }
}
