/**
 * This class represents a user's profile
 */
public class Profile {
    private final String username;
    private final String first;
    private final String last;
    private final int flashGrade;
    private final int projGrade;
    private final int heightFeet;
    private final int heightInches;
    private final int ape;
    private final String gender;

    public Profile(String username, String first, String last, int flashGrade, int projGrade, int heightFeet, int heightInches, int ape, String gender) {
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
        this("", "", "",0,1,5,0,0,"Other");
    }
}
