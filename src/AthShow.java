import java.io.Serializable;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Arrays;

public class AthShow implements Serializable {
    private final String Username;
    private final String Name,Surname,City,Province,Role,email,phone;
    private final Integer Age;
    private final ArrayList<Season> Curriculum;

    public AthShow(Athlete ath){
        Username = ath.getUsername();
        Name = ath.getName();
        Surname = ath.getSurname();
        City = ath.getLocation().Name;
        Province = ath.getLocation().Province;
        Role = ath.getRole();
        Age = ath.getAge();
        email = ath.getEmail();
        phone = ath.getPhoneNumber();
        Curriculum = ath.getCurriculum();
    }

    @Override
    public String toString() {
        String show = Surname + " " + Name + ", " + Role + ", "+Age+","+City+"("+Province+"), Contacts: Email:"+email+" Number:"+phone+"\n" +
                "Recent Seasons:\n";
        int i = 0;
        while (i < 3 && i<Curriculum.size()){
            show += Curriculum.get(i).toString()+"\n";
            i++;
        }
        if (i==0) show+="Not Avaibles";

        return show;
    }

    public String getProvince() {
        return Province;
    }

    public Integer getAge() {
        return Age;
    }

    public String getRole(){
        return Role;
    }
}
