import java.io.Serializable;

public class Season implements Comparable<Season>, Serializable {
    private final String Year,Team,Role,League,Country;
    private final int Games,Points,Finalrank;

    public Season(String year, String team, String role, String league, String country, int games, int points, int finalrank) {
        Year = year;
        Team = team;
        Role = role;
        League = league;
        Country = country;
        Games = games;
        Points = points;
        Finalrank = finalrank;
    }

    public String getYear() {
        return Year;
    }

    @Override
    public int compareTo(Season season) {
        Integer num = Integer.parseInt(this.Year.substring(0,4));
        return num.compareTo(Integer.parseInt(season.Year.substring(0,4)));
        }

    @Override
    public String toString() {
        return  "Year: '" + Year + '\'' +
                ", Team: '" + Team + '\'' +
                ", Role: '" + Role + '\'' +
                ", League: '" + League + '\'' +
                "(" + Country + ")" +
                ", Games: " + Games +
                ", Points: " + Points +
                ", Finalrank: " + Finalrank;
    }
}
