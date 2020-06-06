import java.io.Serializable;

public class City implements Serializable {
    String Name, ZIP , Province;

    public City(String name, String ZIP, String province) {
        Name = name;
        this.ZIP = ZIP;
        Province = province;
    }

    @Override
    public String toString() {
        return "City{" +
                "Name='" + Name + '\'' +
                ", ZIP='" + ZIP + '\'' +
                ", Province='" + Province + '\'' +
                '}';
    }
}
