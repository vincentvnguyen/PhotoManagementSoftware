package Tests;

import Models.Picture;
import Models.Tag;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * In all test cases, if an unexpected Exception occurs <tt>Exception</tt> will be thrown.
 */
public class PictureTest {

    private Tag tag1 = new Tag("George");
    private Tag tag2 = new Tag("Samantha");
    private Picture ig1 = new Picture("/fakeLocation/photos", "kitty.fakepng");

    /**
     * Tests Picture.getFileType
     * result: file type of ig1 is "fakepng"
     */
    @Test
    public void getFileType() throws Exception {
        assertEquals("fakepng", ig1.getFileType());
    }

    /**
     * Tests Picture.hasTagString
     * result: ig1 has tag2.getName
     */
    @Test
    public void hasTagString() throws Exception {
        ig1.addTag(tag2);
        assertTrue(ig1.hasTagString(tag2.getName()));
    }

    /**
     * Tests Picture.addTag
     * result: ig1.tags contains tag1
     */
    @Test
    public void addTag() throws Exception {
        ig1.addTag(tag1);
        assertTrue(ig1.getTags().contains(tag1));
    }

    /**
     * Tests Picture.removeTagByName
     * result: ig1.tags no longer contains ig1
     */
    @Test
    public void removeTagByName() throws Exception {
        ig1.addTag(tag1);
        ig1.addTag(tag2);
        ig1.removeTagByName("George");
        assertFalse(ig1.getTags().contains(tag1));
    }

    /**
     * Tests Picture.getDirectory
     */
    @Test
    public void getDirectory() throws Exception {
        assertEquals("/fakeLocation/photos", ig1.getDirectory());
    }

    /**
     * Tests Picture.setDirectory
     * result: ig1.getDirectory will no longer be "/fakeLocation/photos"
     */
    @Test
    public void setDirectory() throws Exception {
        String newDirectory = "/fakeLocation2/photos";
        ig1.setDirectory(newDirectory);
        assertEquals("/fakeLocation2/photos", ig1.getDirectory());
    }

    /**
     * Tests Picture.getName
     */
    @Test
    public void getName() throws Exception {
        assertEquals("kitty", ig1.getName());
    }

    /**
     * Tests Picture.setName
     * result: ig1.getName will no longer be "kitty"
     */
    @Test
    public void setName() throws Exception {
        String newName = "new name";
        ig1.setName(newName);
        assertEquals("new name", ig1.getName());
    }

    /**
     * Tests Picture.rename
     */
    @Test
    public void rename() throws Exception {
        ig1.addTag(tag1);
        ig1.addTag(tag2);
        ig1.rename(ig1.getName());
        assertEquals("kitty@George @Samantha", ig1.getName());
    }

    /**
     * Tests Picture.hasTag which should result in true
     */
    @Test
    public void hasTag() throws Exception {
        ig1.addTag(tag1);
        assertTrue(ig1.hasTagString(tag1.getName()));
    }

    /**
     * Tests Picture.getTags
     */
    @Test
    public void getTags() throws Exception {
        ig1.addTag(tag1);
        ig1.addTag(tag2);
        ArrayList<Tag> lst = new ArrayList<>();
        lst.add(tag1);
        lst.add(tag2);
        assertEquals(lst, ig1.getTags());
    }

    /**
     * Tests Picture.hasTag
     * result: false. ig1.tags has tag2 not tag1
     */
    @Test
    public void notInTags() throws Exception {
        ig1.addTag(tag2);
        assertFalse(ig1.hasTagString(tag1.getName()));
    }

}