package Tests;

import Models.Tag;
import org.junit.Test;

import static org.junit.Assert.*;
/**
 * In all test cases, if an unexpected Exception occurs <tt>Exception</tt> will be thrown.
 */
public class TagTest {

    @Test
    public void equals() throws Exception {
    }

    private Tag tag1 = new Tag("Samantha");
    private Tag tag2 = new Tag("George");
    private Tag tag3 = new Tag("Samantha");

    /**
     * Tests Tag.getName
     */
    @Test
    public void getName() throws Exception {
        assertEquals("Samantha", tag1.getName());
    }

    /**
     * Tests Tag.nameWithAtSign
     */
    @Test
    public void nameWithAtSign() throws Exception {
        assertEquals("@Samantha", tag1.nameWithAtSign());
    }

    /**
     * Tests Tag.equals
     * result: true. The tags have the same name
     */
    @Test
    public void tagEqualsTrue() throws Exception {
        assertTrue(tag1.equals(tag3));
    }

    /**
     * Tests Tag.equals
     * result: false. the tags have different names
     */
    @Test
    public void tagEqualsFalse()throws Exception {
        assertFalse(tag2.equals(tag1));
    }

    /**
     * Tests Tag.toString()
     */
    @Test
    public void tagToString() throws Exception {
        assertEquals("Samantha", tag1.toString());
    }

}