package ar.edu.itba.paw.webapp.forms;

public class RegexUtils {

    public static final String FILE_REGEX = "^(?!([ ,\\-_.]+)$)[a-zA-Z0-9áéíóúÁÉÍÓÚñÑüÜ .,\\-_]+$";

    public static final String PASSWORD_REGEX = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9]).+$";

    public static final String USERNAME_REGEX = "^(?![\\-_0-9.]+$)[a-zA-Z0-9.\\-_]+$";

    public static final String NAME_REGEX = "([a-zA-ZáéíóúÁÉÍÓÚñÑüÜ]+[ ]?)*";

    public static final String EMAIL_REGEX = "^(?=.{1,64}@)[A-Za-z0-9\\+_-]+(\\.[A-Za-z0-9\\+_-]+)*@[^-][A-Za-z0-9\\+-]+(\\.[A-Za-z0-9\\+-]+)*(\\.[A-Za-z]{2,})$";

    public static final String CHALLENGE_CODE_REGEX = "^[0-9]{6}$";

    public static final String AVAILABLE_FOLDER_COLORS_REGEX = "(BBBBBB|16A765|4986E7|CD35A6)";

    public static final String CATEGORY_REGEX = "theory|practice|exam|other";

    public static final String SEARCHABLE_REDIRECT = "/|/notes/.*|/directory/.*";

    private RegexUtils() {
    }


}
