import java.time.LocalDate;

public class Manager extends User{
    String Teamname;

    public Manager(String name, String surname, char gender, String username, String password, String email, String phone, String cityname, String ZIP, String province, LocalDate birthdate, String teamname) {
        super(name, surname,gender, username, password, email, phone, cityname, ZIP, province, birthdate);
        Teamname=teamname;
    }

    @Override
    public String toString() {
        return "Manager{" + super.toString() +", Team='" + Teamname + "\'}";
    }

    public String getTeamname() { return Teamname; }

    public void setTeamname(String teamname) {
        Teamname = teamname;
        UpdateLastEdit();
    }
}
