import java.io.*;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Scanner;

public class Client {
    private final Scanner user_scanner = new Scanner(System.in);
    private ObjectOutputStream pw;
    private ObjectInputStream server_stream;
    private Socket socket;
    private String address;
    private int port;
    
    public static void main(String args[]){
        Client user = new Client("127.0.0.1",1107);
        user.Connect();
        user.Welcome();
    }

    private void Connect() {
        System.out.println("Starting Client connection to "+address+":"+port);
        try {
            socket = new Socket(address,port);
            System.out.println("Started Client connection to "+address+":"+port);

            this.pw = new ObjectOutputStream(socket.getOutputStream());
            this.server_stream = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void Welcome(){
        while(true){
            System.out.println("Hi welcome on VolleyTransf!\nChoose an option to start:\n1) Login\n2) Register\nInsert number of your choise: ");
            int choise = user_scanner.nextInt();
            user_scanner.nextLine();
            while(choise!=1 && choise!=2){
                System.out.println("Wrong choise!\n1) Login\n2) Register\nInsert number of your choise: ");
                choise = user_scanner.nextInt();
                user_scanner.nextLine();
            }
            switch (choise){
                case 1:
                    try {
                        User tmp = Login();
                        if(tmp == null){break;}
                        switch (tmp.getClass().getName()){
                            case "Athlete":
                                AthleteClient((Athlete) tmp);
                                return;
                            case "Manager":
                                ManagerClient((Manager) tmp);
                                return;
                        }

                    }
                    catch (UnsupportedEncodingException | NoSuchAlgorithmException e ) {e.printStackTrace();}
                    catch (IOException e) {e.printStackTrace();}
                    catch (ClassNotFoundException e) {e.printStackTrace();}
                    break;
                case 2:
                    Register();
                    break;
            }
        }
    }

    private void Register(){
        User tmp = null;
        System.out.println("Welcome to registration page!\nFirst of all set your username: ");
        String user = user_scanner.nextLine();
        try {
            pw.writeObject(Protocol.CHECK_USERNAME);
            pw.writeObject(user);
            String response = (String) server_stream.readObject();
            while(response.equals(Protocol.ALREADYUSEDUSERNAME)){
                System.out.println("Username already in use, choose another one: ");
                user = user_scanner.nextLine();
                pw.writeObject(Protocol.CHECK_USERNAME);
                pw.writeObject(user);
                response = (String) server_stream.readObject();}
        } catch (IOException | ClassNotFoundException e) {e.printStackTrace();}
        System.out.println("Perfect! now choose a password: ");
        String pass = user_scanner.nextLine();
        System.out.println("Insert Name: ");
        String name = user_scanner.nextLine();
        System.out.println("Insert Surname: ");
        String surname = user_scanner.nextLine();
        System.out.println("Insert Gender: (M/F) ");
        String gender = user_scanner.nextLine().toUpperCase();
        while(!gender.startsWith("M") && !gender.startsWith("F")){
            System.out.println("Insert Gender: (M/F) ");
            gender = user_scanner.nextLine().toUpperCase();
        }
        System.out.println("Insert Birthdate: (GG/MM/YYYY) ");
        String birthdate = user_scanner.nextLine();
        while(!birthdate.contains("/") || birthdate.length()!=10){
            System.out.println("Something wrong with your date, try again: (GG/MM/YYYY) ");
            birthdate = user_scanner.nextLine();
        }
        System.out.println("Insert Email: ");
        String email = user_scanner.nextLine();
        while(!email.contains("@")){
            System.out.println("Something wrong with your Email, try again: ");
            email = user_scanner.nextLine();
        }
        System.out.println("Insert Phone Number: ");
        String phone = user_scanner.nextLine();
        System.out.println("Insert the City where you live: ");
        String city = user_scanner.nextLine();
        System.out.println("Insert Province: (CT,ME,MI,TO...)");
        String province = user_scanner.nextLine().toUpperCase();
        while(province.length()!=2){
            System.out.println("Use only two letters to indicate province!Try again: ");
            province = user_scanner.nextLine().toUpperCase();
        }
        System.out.println("Insert ZIP code: ");
        String ZIP = user_scanner.nextLine();
        while(ZIP.length()!=5){
            System.out.println("Something wrong with your ZIP Code, try again: ");
            ZIP = user_scanner.nextLine();
        }

        System.out.println("OK! Now choose you profile type:\n-Manager\n-Athlete\n: ");
        String type = user_scanner.nextLine().toUpperCase();
        while(!type.equals("MANAGER") && !type.equals("ATHLETE")){
            System.out.println("Something wrong!Choose you profile type:\n - Manager\n-Athlete\n: ");
            type = user_scanner.nextLine().toUpperCase();
        }

        switch (type){
            case "MANAGER":
                System.out.println("Insert the team you represent: ");
                String team = user_scanner.nextLine();
                tmp = new Manager(name,surname,gender.charAt(0),user,pass,email,phone,city,ZIP,province, LocalDate.of(Integer.valueOf(birthdate.substring(6,10)),Integer.valueOf(birthdate.substring(3,5)),Integer.valueOf(birthdate.substring(0,2))),team);
                break;
            case "ATHLETE":
                System.out.println("Insert your favourite Role: ");
                String favrole = user_scanner.nextLine();
                while(!Protocol.AdmittedRoles.contains(favrole)){
                    System.out.println("Admitted Roles: "+Protocol.AdmittedRoles.toString());
                    System.out.println("Insert your favourite Role:");
                    favrole = user_scanner.nextLine();
                }
                tmp = new Athlete(name,surname,gender.charAt(0),user,pass,email,phone,city,ZIP,province, LocalDate.of(Integer.valueOf(birthdate.substring(6,10)),Integer.valueOf(birthdate.substring(3,5)),Integer.valueOf(birthdate.substring(0,2))),favrole);
                break;
        }
        String response=null;
        try {
            pw.writeObject(Protocol.REGISTER);
            pw.writeObject(tmp.getClass().getName());
            pw.writeObject(tmp);
            response  = (String) server_stream.readObject();
            switch (response){
                case Protocol.REGISTER_SUCCESS:
                    System.out.println("Successfully Registered! Login to start using VolleyTransfer!");
                    break;
                case Protocol.REGISTER_ERROR:
                    System.out.println("Something went wrong! Try Again Later Please!");
            }
        } catch (IOException | ClassNotFoundException e) {e.printStackTrace();}
    }

    private User Login() throws UnsupportedEncodingException, NoSuchAlgorithmException {
        System.out.println("Welcome back!\nInsert username: ");
        String username = user_scanner.nextLine();
        System.out.println("Insert password: ");
        String pass = User.MD5(user_scanner.nextLine());

        //invio credenziali al server e attesa risposta
        try {
            pw.writeObject(Protocol.LOGIN);
            pw.writeObject(username);
            pw.writeObject(pass);

            String response = (String) server_stream.readObject();
            switch (response){
                case Protocol.LOGIN_SUCCESS:
                    User user = (User) server_stream.readObject();
                    return user;
                case Protocol.ALREADYACTIVEUSER:
                    System.out.println("User is already active!");
                    return null;
                case Protocol.USERORPASS_ERROR:
                    System.out.println("Wrong Username or Password! Try Again!");
                    return null;
            }
            } catch (IOException | ClassNotFoundException e) {e.printStackTrace();}
        return null;
    }

    private void AthleteClient(Athlete user) throws IOException {
        while(true){
            System.out.println("ATHLETE CLIENT: ");
            System.out.println("Cosa vuoi fare?\n1) Guarda le tue informazioni\n2) Modifica Profilo\n3) Aggiorna Curriculum Sportivo\n4) Esci\nScegli: ");
            int choise = user_scanner.nextInt();
            user_scanner.nextLine();
            switch (choise){
                case 1:
                    System.out.println(((Athlete) user).toString());
                    break;
                case 2:
                    ProfileUpdate(user);
                    break;
                case 3:
                    CurriculumUpdate(user);
                    break;
                case 4:
                    user.setActive(false);
                    pw.writeObject(Protocol.USER_UPDATE);
                    pw.writeObject(user);
                    Quit();
                    return;
                default:
                    System.out.println("Comando inserito non esistente!");
            }
        }
    }

    private void CurriculumUpdate(Athlete user) {
        while(true) {
            System.out.println("Benvenuto nella pagina di Aggiornamento del curriculum sportivo: Cosa vuoi fare?");
            System.out.println("1) Visualizza Curriculum\n2) Aggiungi Stagione\n3) Rimuovi Stagione\n4)Indietro\nScegli:");
            int choise = user_scanner.nextInt();
            user_scanner.nextLine();
            switch (choise) {
                case 1:
                    System.out.println(user.getCurriculum().toString());
                    break;
                case 2:
                    System.out.println("Inserisci i dati della stagione");
                    System.out.println("Anno: (formato: 2012/2013): ");
                    String year =  user_scanner.nextLine();
                    System.out.println("Squadra: ");
                    String team =  user_scanner.nextLine();
                    System.out.println("Ruolo: ");
                    String role =  user_scanner.nextLine();
                    System.out.println("Serie: (Serie/girone: Serie B/H,Serie A3/B, 1a Divisione/A ecc): ");
                    String league =  user_scanner.nextLine();
                    System.out.println("Nazione: ");
                    String country =  user_scanner.nextLine();
                    System.out.println("Partite Giocate: ");
                    int games =  user_scanner.nextInt();
                    user_scanner.nextLine();
                    System.out.println("Punti Personali Fatti: ");
                    int points =  user_scanner.nextInt();
                    user_scanner.nextLine();
                    System.out.println("Posizione Finale Della Squadra: ");
                    int rank =  user_scanner.nextInt();
                    user_scanner.nextLine();

                    Season tmp = new Season(year,team,role,league,country,games,points,rank);
                    user.AddSeason(tmp);
                    break;
                case 3:
                    while(true) {
                        try {
                            System.out.println("Inserisci l'annata da rimuovere: (2013/2014)");
                            String yeartr = user_scanner.nextLine();
                            Integer num = Integer.parseInt(yeartr.substring(0, 4));
                            user.RemoveSeason(num);
                            System.out.println("Annata "+yeartr+" Rimossa!");
                            break;
                        } catch (Exception e) {
                            System.out.println("Formato annata inserita errato");
                        }
                    }
                    break;
                case 4:
                    return;
                default:
                    System.out.println("Comando inserito non esistente!");
                    break;
            }
        }
    }

    private void ProfileUpdate(User user) {
        while(true){
            System.out.println("Benvenuto nella pagina di Aggiornamento del profilo: Cosa vuoi fare?");
            System.out.println( "1) Modifica Password\n" +
                                "2) Modifica Residenza\n" +
                                "3) Modifica Contatti");
            if(user.getClass().getName().equals("Athlete")) System.out.println("4) Modifica disponibilità al trasferimento");
            else System.out.println("4) Modifica squadra rappresentata");
            System.out.println( "5) Indietro\n" +
                                "Scegli: ");
            int choose = user_scanner.nextInt();
            user_scanner.nextLine();
            switch (choose){
                case 1:
                    System.out.println("Inserisci La tua password attuale: ");
                    try {
                        String pass = User.MD5(user_scanner.nextLine());
                        if(user.Login(user.getUsername(),pass)){
                            System.out.println("Inserisci la nuova password: ");
                            pass = user_scanner.nextLine();
                            user.setPassword(pass);
                        }
                        else System.out.println("Password Inserita Errata!");
                    } catch (UnsupportedEncodingException | NoSuchAlgorithmException e) {e.printStackTrace();}
                    break;
                case 2:
                    System.out.println("Inserisci la nuova città di residenza: ");
                    String city = user_scanner.nextLine();
                    System.out.println("Inserisci la nuova provincia: (CT,ME,MI,TO...)");
                    String province = user_scanner.nextLine();
                    while(province.length()!=2){
                        System.out.println("Use only two letters to indicate province!Try again: ");
                        province = user_scanner.nextLine().toUpperCase();
                    }
                    System.out.println("Inserisci il nuovo CAP: ");
                    String ZIP = user_scanner.nextLine();
                    while(ZIP.length()!=5){
                        System.out.println("Something wrong with your ZIP Code, try again: ");
                        ZIP = user_scanner.nextLine();
                    }
                    City newcity = new City(city,ZIP,province);
                    user.setLocation(newcity);
                    System.out.println("Aggiornamento esguito con successo!");
                    break;
                case 3:
                    System.out.println("Inserisci la nuova email o scrivi 'NO' per non modificarla: ");
                    String email = user_scanner.nextLine();
                    if(!email.equals("NO")){
                        while(!email.contains("@")){
                            System.out.println("Something wrong with your Email, try again: ");
                            email = user_scanner.nextLine();}
                        user.setEmail(email);
                    }

                    System.out.println("Inserisci il nuovo contatto telefonico o scrivi 'NO' per non modificarlo: ");
                    String phone = user_scanner.nextLine();
                    if(!phone.equals("NO")){
                        user.setPhoneNumber(phone);
                    }
                    break;
                case 4:
                    switch (user.getClass().getName()){
                        case "Athlete":
                            if(((Athlete)user).isLoockingforTeam()){
                                System.out.println("Attualmente il tuo stato è DISPONIBILE al trasferimento, vuoi cambiarlo? (S/N)");
                                String choise = user_scanner.nextLine().toUpperCase();
                                while(!choise.startsWith("S") && !choise.startsWith("N")){
                                    System.out.println("Attualmente il tuo stato è DISPONIBILE al trasferimento, vuoi cambiarlo? (S/N)");
                                    choise = user_scanner.nextLine().toUpperCase();
                                }
                                if(choise.startsWith("S"))((Athlete) user).changeLoockingforTeam();
                            }
                            else {
                                System.out.println("Attualmente il tuo stato è NON DISPONIBILE al trasferimento, vuoi cambiarlo? (S/N)");
                                String choise = user_scanner.nextLine().toUpperCase();
                                while(!choise.startsWith("S") && !choise.startsWith("N")){
                                    System.out.println("Attualmente il tuo stato è DISPONIBILE al trasferimento, vuoi cambiarlo? (S/N)");
                                    choise = user_scanner.nextLine().toUpperCase();
                                }
                                if(choise.startsWith("S"))((Athlete) user).changeLoockingforTeam();
                            }
                            break;
                        case "Manager":
                            System.out.println("Inserisci il nuovo Team che rappresenti: ");
                            String newteam = user_scanner.nextLine();
                            ((Manager) user).setTeamname(newteam);
                            break;
                    }
                    break;
                case 5:
                    return;
                default:
                    System.out.println("Comando inserito non esistente!");
                    break;
            }
        }

    }

    private void ManagerClient(Manager user) throws IOException, ClassNotFoundException {
        while(true){
            System.out.println("MANAGER CLIENT: ");
            System.out.println("Cosa vuoi fare?\n1) Guarda le tue informazioni\n2) Modifica Profilo\n3) Cerca Giocatori\n4) Esci\nScegli: ");
            int choise = user_scanner.nextInt();
            user_scanner.nextLine();
            switch (choise){
                case 1:
                    System.out.println(((Manager) user).toString());
                    break;
                case 2:
                    ProfileUpdate(user);
                    break;
                case 3:
                    PlayerSearch();
                    break;
                case 4:
                    user.setActive(false);
                    pw.writeObject(Protocol.USER_UPDATE);
                    pw.writeObject(user);
                    Quit();
                    return;
                default:
                    System.out.println("Comando inserito non esistente!");
            }
        }
    }

    private void PlayerSearch() throws IOException, ClassNotFoundException {
        while(true) {
            System.out.println("Benvenuto nella pagina di Ricerca: Cosa vuoi fare?");
            System.out.println("1) Ricerca per Ruolo\n2) Ricerca per Età\n3) Ricerca per Provincia\n4)Indietro\nScegli:");
            int choise = user_scanner.nextInt();
            user_scanner.nextLine();
            switch (choise) {
                case 1:
                    System.out.println("Sei interessato ad atleti del maschile o del femminile? (M/F)");
                    String rolegender = user_scanner.nextLine().toUpperCase();
                    while(!rolegender.startsWith("M") && !rolegender.startsWith("F")){
                        System.out.println("Insert Gender: (M/F) ");
                        rolegender = user_scanner.nextLine().toUpperCase();
                    }
                    System.out.println("A che ruolo sei interessato? ");
                    String role = user_scanner.nextLine();
                    while(!Protocol.AdmittedRoles.contains(role)){
                        System.out.println("Admitted Roles: "+Protocol.AdmittedRoles.toString());
                        System.out.println("A che ruolo sei interessato? ");
                        role = user_scanner.nextLine();
                    }
                    pw.writeObject(Protocol.ROLE_SEARCH);
                    pw.writeObject(rolegender.charAt(0));
                    pw.writeObject(role);
                    ArrayList<AthShow> atleti = (ArrayList<AthShow>) server_stream.readObject();
                    if(atleti.size() != 0){
                    int i=0;
                    while(i<atleti.size()){
                        i++;
                        atleti.sort(new Comparator<AthShow>() {
                            @Override
                            public int compare(AthShow athShow, AthShow t1) {
                                return athShow.getAge().compareTo(t1.getAge());
                            }
                        });
                        System.out.println(i+") "+atleti.get(i-1));
                    }
                    }
                    else System.out.println("Nessun Atleta corrisponde ai parametri selezionati");
                    break;
                case 2:
                    System.out.println("Sei interessato ad atleti del maschile o del femminile? (M/F)");
                    String agegender = user_scanner.nextLine().toUpperCase();
                    while(!agegender.startsWith("M") && !agegender.startsWith("F")){
                        System.out.println("Insert Gender: (M/F) ");
                        agegender = user_scanner.nextLine().toUpperCase();
                    }
                    System.out.println("Inserisci l'età minima: ");
                    int minage = user_scanner.nextInt();
                    user_scanner.nextLine();
                    System.out.println("Inserisci l'età massima: ");
                    int maxage = user_scanner.nextInt();
                    user_scanner.nextLine();

                    pw.writeObject(Protocol.AGE_SEARCH);
                    pw.writeObject(agegender.charAt(0));
                    pw.writeObject(minage);
                    pw.writeObject(maxage);
                    ArrayList<AthShow> ageatleti = (ArrayList<AthShow>) server_stream.readObject();
                    if(ageatleti.size() != 0){
                        int i=0;
                        while(i<ageatleti.size()){
                            i++;
                            ageatleti.sort(new Comparator<AthShow>() {
                                @Override
                                public int compare(AthShow athShow, AthShow t1) {
                                    return athShow.getRole().compareTo(t1.getRole());
                                }
                            });
                            System.out.println(i+") "+ageatleti.get(i-1));
                        }
                    }
                    else System.out.println("Nessun Atleta corrisponde ai parametri selezionati");
                    break;
                case 3:
                    System.out.println("Sei interessato ad atleti del maschile o del femminile? (M/F)");
                    String placegender = user_scanner.nextLine().toUpperCase();
                    while(!placegender.startsWith("M") && !placegender.startsWith("F")){
                        System.out.println("Insert Gender: (M/F) ");
                        placegender = user_scanner.nextLine().toUpperCase();
                    }
                    System.out.println("A che provincia sei interessato? (CT,ME,TO,PA..)");
                    String province = user_scanner.nextLine().toUpperCase();
                    while(province.length()!=2){
                        System.out.println("Use only two letters to indicate province!Try again: ");
                        province = user_scanner.nextLine().toUpperCase();
                    }
                    pw.writeObject(Protocol.PLACE_SEARCH);
                    pw.writeObject(placegender.charAt(0));
                    pw.writeObject(province);
                    ArrayList<AthShow> placeatleti = (ArrayList<AthShow>) server_stream.readObject();
                    if(placeatleti.size() != 0){
                        int i=0;
                        while(i<placeatleti.size()){
                            i++;
                            placeatleti.sort(new Comparator<AthShow>() {
                                @Override
                                public int compare(AthShow athShow, AthShow t1) {
                                    return athShow.getRole().compareTo(t1.getRole());
                                }
                            });
                            System.out.println(i+") "+placeatleti.get(i-1));
                        }
                    }
                    else System.out.println("Nessun Atleta corrisponde ai parametri selezionati");
                    break;
                case 4:
                    return;
                default:
                    System.out.println("Comando inserito non esistente!");
                    break;
            }
        }
    }

    public Client(String address, int port) {
        this.address = address;
        this.port = port;
    }

    private void Quit(){
        try {
            pw.writeObject(Protocol.QUIT);
            socket.close();
        } catch (IOException e) {e.printStackTrace();}
    }
    }