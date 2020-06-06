import java.util.Arrays;
import java.util.List;

public class Protocol {
    public static final String REGISTER = "ADDNEWUSR";
    public static final String ALREADYUSEDUSERNAME = "USERNOTAVB";
    public static final String CHECK_USERNAME = "USRCK";
    public static final String USERNAME_OK = "USROK";
    public static final String REGISTER_SUCCESS = "ADDOK";
    public static final String REGISTER_ERROR = "ADDERR";

    public static final String LOGIN = "LGN";
    public static final String LOGIN_SUCCESS = "LGNOK";
    public static final String USERORPASS_ERROR = "LGNERR1";
    public static final String ALREADYACTIVEUSER = "LNGERR2";
    public static final String LOGIN_ERROR = "LGNERR";

    public static final String USER_UPDATE = "USRUPD";
    public static final String QUIT = "QUIT";

    public static final String ROLE_SEARCH = "ROLSRC";
    public static final String AGE_SEARCH="AGESRC";
    public static final String PLACE_SEARCH = "PLCSRC";

    public static final List<String> AdmittedRoles = Arrays.asList("Palleggiatore", "Banda", "Opposto", "Libero", "Centrale", "Universale");
}
