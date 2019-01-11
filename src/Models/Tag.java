package Models;

public class Tag {
    /**
     * The name of this Tag
     */
    private String name;

    /**
     * Initializing a Tag object
     *
     * @param name the name of this Tag
     */
    public Tag(String name) {
        this.name = name;
    }

    /**
     * Returns name of this tag
     *
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns name with "@" symbol in front
     *
     * @return name with "@" symbol in front
     */
    public String nameWithAtSign() {
        return "@" + name;
    }

    /**
     * Returns true iff this tag equals obj
     *
     * @param obj other tag
     * @return true iff this tag equals obj
     */
    @Override
    public boolean equals(Object obj) {
        return (obj instanceof Tag && ((Tag) obj).getName().equals(this.name));
    }

    /**
     * Returns string representation of tag
     * @return the tag's name
     */
    @Override
    public String toString() {
        return this.getName();
    }
}