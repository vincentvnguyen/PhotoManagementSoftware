package Models;

import javafx.scene.image.Image;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Picture {
    /**
     * Name of this picture
     */
    private String name;
    /**
     * List of tags that this picture has
     */
    private ArrayList<Tag> tags;
    /**
     * The directory of this picture
     */
    private String directory;
    /**
     * The file type of this picture
     */
    private String fileType;
    /**
     *
     */
    private File file;
    /**
     * Image object created for this picture
     */
    private Image image;
    /**
     * Represents the history file of picture (Name changes)
     */
    private File history;
    private List<String> nameHistory;

    /**
     * Creating an Picture object
     *
     * @param directory where Picture is located
     * @param name      name of picture
     */
    public Picture(String directory, String name) {
        tags = new ArrayList<>();
        this.directory = directory;

        this.file = new File(directory + "/" + name);

        separateName(name);
        // Add this because even though all images chosen by file chooser will include these, test cases won't
        String fileTypeLowerCase = fileType.toLowerCase();
        if (fileTypeLowerCase.equals("png")
                || fileTypeLowerCase.equals("jpg")
                || fileTypeLowerCase.equals("jpeg")
                || fileTypeLowerCase.equals("gif")
                || fileTypeLowerCase.equals("bmp")) {
            image = new Image("file:///" + file);

            File newHistoryFile = new File( file + "-history.txt");
            try {
                boolean historyFileCreated = newHistoryFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            history = newHistoryFile;
            try {
                 nameHistory = Files.readAllLines(Paths.get(history.getAbsolutePath()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Returns nameHistory
     * @return nameHistory
     */
    public List<String> getNameHistory() {
        return nameHistory;
    }

    /**
     * Returns history file location
     * @return history file location
     */
    public File getHistory() {
        return history;
    }

    /**
     * Returns string representation of this picture
     *
     * @return string representation of this picture
     */
    @Override
    public String toString() {
        return this.name;
    }

    /**
     * Separates name and tags from name. Changes this.name and adds tags to this.tags from name
     * @param name the name given with name and tags
     */
    public void separateName(String name) {
        int fileExtensionPosition = name.lastIndexOf(".") + 1;
        this.fileType = name.substring(fileExtensionPosition);

        // Separate name and set name to the first name obtained and the remainder are tags. Add them to this.tags
        String[] nameAndTags = name.substring(0, fileExtensionPosition - 1).split("@");

        this.name = nameAndTags[0];

        if (nameAndTags.length > 1) {
            for (int i = 1; i < nameAndTags.length; ++i) {
                tags.add(new Tag(nameAndTags[i]));
            }
        }
    }

    /**
     * Returns file
     * @return file
     */
    public File getFile() {
        return this.file;
    }

    /**
     * Sets file
     * @param file the file to be set to
     */
    public void setFile(File file) {
        this.file = file;
    }

    /**
     * Returns fileType
     * @return fileType
     */
    public String getFileType() {
        return fileType;
    }

    /**
     * Return true iff this Picture contains the name of tag
     *
     * @param tagString the tag being searched for
     * @return true iff this Picture contains tag
     */
    public boolean hasTagString(String tagString) {
        for (Tag tagInImage : tags) {
            if (tagInImage.getName().equals(tagString)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Renames this image with tags
     */
    public void rename(String name) {
        // Avoid string concatenation
        StringBuilder sb = new StringBuilder(name);
        for (Tag tag : this.tags) {
            sb.append(tag.nameWithAtSign()).append(" ");
        }
        sb = sb.deleteCharAt(sb.length() - 1);
        this.setName(sb.toString());
    }

    /**
     * Clears the list of tags
     */
    public void clearTags() {
        this.tags = new ArrayList<>();
    }


    /**
     * Returns tags
     *
     * @return tags
     */
    public ArrayList<Tag> getTags() {
        return tags;
    }

    /**
     * Adds tag to tags iff tag does not already exist
     *
     * @param tag the tag being added to tags
     */
    public void addTag(Tag tag) throws IOException {
        if (!this.tags.contains(tag)) {
            this.tags.add(tag);
        }
    }

    /**
     * Removes tag from tags if name equals string
     * @param string the string being searched for
     */
    public void removeTagByName(String string) {
        tags.removeIf(tagInImage -> tagInImage.getName().equals(string));
    }

    /**
     * Returns directory
     *
     * @return directory
     */
    public String getDirectory() {
        return directory;
    }

    /**
     * Sets directory
     *
     * @param directory the new directory
     */
    public void setDirectory(String directory) {
        this.directory = directory;
    }

    /**
     * Returns name of this image
     *
     * @return name of this image
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name and fileType
     *
     * @param name the new name to change to
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns image
     * @return image
     */
    public Image getImage() {
        return this.image;
    }

    /**
     * Saves nameHistory to history file
     * @throws IOException if input or output stream fails
     */
    public void saveHistory() throws IOException {
        // delete old history file and create a new one
        File oldHistoryToDelete = history;
        boolean oldHistoryDeleted = oldHistoryToDelete.delete();
        File newHistoryFile = new File(file + "-history.txt");
        history = newHistoryFile;

        // Write nameHistory to new file
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(newHistoryFile));
        for (String line : this.nameHistory) {
            bufferedWriter.write(line);
            bufferedWriter.newLine();
        }
        bufferedWriter.close();
    }
}