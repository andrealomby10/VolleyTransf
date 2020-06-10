import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Collections;


public class Athlete extends User{
    private boolean LoockingforTeam;
    private ArrayList<Season> Curriculum;
    private String FavouriteRole;




    public Athlete(String name, String surname,char gender, String username, String password, String email,String phone, String cityname, String ZIP, String province, LocalDate birthdate,String favrole) {
        super(name, surname, gender ,username, password, email, phone, cityname, ZIP, province, birthdate);
        LoockingforTeam = false;
        Curriculum = new ArrayList<>();
        FavouriteRole = favrole;
    }

    public ArrayList<Season> getCurriculum() {
        return Curriculum;
    }

    public String getRole() { return FavouriteRole; }
    public void setRole(String role) { this.FavouriteRole = role; this.UpdateLastEdit(); }

    public boolean isLoockingforTeam() {
        return LoockingforTeam;
    }
    public void setLoockingforTeam(boolean loockingforTeam) {
        LoockingforTeam = loockingforTeam;
    }
    public void changeLoockingforTeam() {
        LoockingforTeam = !LoockingforTeam;
    }

    public int getAge(){
       return Period.between(getBirthdate(), LocalDate.now()).getYears();
    }

    public void AddSeason(Season newseason){
           Curriculum.add(newseason);
           Collections.sort(Curriculum);
     }

     public void RemoveSeason(Integer year){
         for (Season s:Curriculum) {
          if(Integer.valueOf(s.getYear().substring(0,4)) == year){
          Curriculum.remove(s);
          return;}
         }

     }

    @Override
    public String toString() {
        return super.toString()+"\nFavourite Role: "+FavouriteRole;
    }


}
