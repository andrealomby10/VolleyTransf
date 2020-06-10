import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.time.LocalDateTime;


public abstract class User implements Serializable{
private String Name,Surname,Username,Email, PhoneNumber;
private char Gender;
private City Location;
private String Password;
private LocalDateTime LastEdit,RegistrationDate;
private LocalDate Birthdate;
private Boolean Active;

    public User(String name, String surname,char gender, String username, String password, String email, String phone, String cityname, String ZIP, String province, LocalDate birthdate) {
        Name = name;
        Surname = surname;
        Username = username;
        Gender=gender;
        Email = email;
        PhoneNumber = phone;
        Location = new City(cityname,ZIP,province);
        Birthdate = birthdate;
        Active = false;

        try {
            Password = MD5(password);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        LastEdit = null;
        RegistrationDate = LocalDateTime.now();
    }

    public static String MD5 (String password) throws UnsupportedEncodingException, NoSuchAlgorithmException{
        byte[] bytesOfMessage = password.getBytes("UTF-8");
        MessageDigest md = MessageDigest.getInstance("MD5");
        String myHash = new BigInteger(1,md.digest(bytesOfMessage)).toString(16);
        return myHash;
    }

    public boolean Login (String username, String md5password){
        if (username.equals(Username) && md5password.equals(Password)) return true;
        else return false;
    }

    public void setPassword(String password) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        Password = MD5(password);
        LastEdit = LocalDateTime.now();
        }

    public String getName() {
        return Name;
    }
    public void setName(String name) {
        Name = name;
        UpdateLastEdit();
    }


    public String getSurname() {
        return Surname;
    }
    public void setSurname(String surname) {
        Surname = surname;
        UpdateLastEdit();
    }


    public String getUsername() {
        return Username;
    }
    public void setUsername(String username) {
        Username = username;
        UpdateLastEdit();
    }


    public String getEmail() {
        return Email;
    }
    public void setEmail(String email) {
        Email = email;
        UpdateLastEdit();
    }

    public String getPhoneNumber() { return PhoneNumber; }
    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
        UpdateLastEdit();
    }

    public City getLocation() {
        return Location;
    }
    public void setLocation(City location) {
        Location = location;
        UpdateLastEdit();
    }

    public synchronized Boolean isActive() { return Active; }
    public synchronized void setActive(Boolean active) { Active = active; }

    public LocalDateTime getLastEdit() {
        return LastEdit;
    }
    public void UpdateLastEdit(){
        LastEdit = LocalDateTime.now();
    }

    public LocalDateTime getRegistrationDate() {
        return RegistrationDate;
    }

    public LocalDate getBirthdate(){ return Birthdate; }

    public char getGender() {
        return Gender;
    }

    @Override
    public String toString() {
        return  Username+": "+Surname + " " + Name + ", "+Gender+", "+Birthdate+"\nLocated in: "+Location.toString()+"\nContacts:\nEmail: "+Email+"\nPhone: "+PhoneNumber +
                "\nUser since: "+RegistrationDate +"\nLast Profile Edit: "+LastEdit;
    }

}
