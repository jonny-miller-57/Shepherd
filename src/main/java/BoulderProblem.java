
import java.util.Objects;

/**
 * This class represents a mutable individual boulder problem with a
 * name, a grade, number of stars, and a description.
 */
public class BoulderProblem {
    private final String destination;
    private final String area;
    private final String subarea;
    private final String boulder;
    private final String name;
    private final String grade;
    private final int stars;
    private final String description;

    /**
     * Creates a new mutable parser.BoulderProblem with the provided attributes.
     *
     * @param destination   Destination the boulder problem is at
     * @param area          Area within the destination the problem is at
     * @param subarea       Subarea within the area the problem is at
     * @param boulder       The boulder that the problem is on
     * @param name          The name of the problem
     * @param grade         The v-grade of the problem
     * @param stars         The star quality of the problem
     * @param description   The description of the problem
     */
    public BoulderProblem(String destination, String area, String subarea, String boulder,
                          String name, String grade, int stars, String description) {
        this.destination = destination;
        this.area = area;
        this.subarea = subarea;
        this.boulder = boulder;
        this.name = name;
        this.grade = grade;
        this.stars = stars;
        this.description = description;
    }

    /**
     * @return the destination associated with the boulder problem
     */
    public String getDestination() {
        return destination;
    }

    /**
     * @return the area associated with the boulder problem
     */
    public String getArea() {
        return area;
    }

    /**
     * @return the subarea associated with the boulder problem
     */
    public String getSubarea() {
        return subarea;
    }

    /**
     * @return the boulder associated with the boulder problem
     */
    public String getBoulder() {
        return boulder;
    }

    /**
     * @return the name of the boulder problem
     */
    public String getName() {
        return name;
    }

    /**
     * @return the grade of the boulder problem
     */
    public String getGrade() {
        return grade;
    }

    /**
     * @return the number of stars given for the boulder problem
     */
    public int getStars() {
        return stars;
    }

    /**
     * @return the description for the boulder problem
     */
    public String getDescription() {
        return description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BoulderProblem that = (BoulderProblem) o;
        return stars == that.stars && destination.equals(that.destination) && area.equals(that.area)
                && subarea.equals(that.subarea) && name.equals(that.name) && grade.equals(that.grade)
                && boulder.equals(that.boulder);
    }

    @Override
    public int hashCode() {
        return Objects.hash(destination, area, subarea, name, grade, stars);
    }
}
