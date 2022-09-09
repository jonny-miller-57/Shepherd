/**
 * This class represents a user's profile
 */
public class Profile {
    private final String username;
    private final String first;
    private final String last;
    private final String flashGrade;
    private final String projGrade;
    private final int heightFeet;
    private final int heightInches;
    private final int ape;
    private final String gender;

    public Profile(String username, String first, String last, String flashGrade, String projGrade, int heightFeet, int heightInches, int ape, String gender) {
        this.username = username;
        this.first = first;
        this.last = last;
        this.flashGrade = flashGrade;
        this.projGrade = projGrade;
        this.ape = ape;
        this.heightFeet = heightFeet;
        this.heightInches = heightInches;
        this.gender = gender;
    }

    public Profile() {
        this("", "", "","V0","V1",5,0,0,"Other");
    }

    public String getUsername() {
        return username;
    }
}
