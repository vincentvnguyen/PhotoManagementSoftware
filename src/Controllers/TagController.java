package Controllers;

import Models.Tag;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class TagController {
    /**
     * List of tags
     */
    private ArrayList<Tag> tags;

    /**
     * Initializing a TagController Object
     *
     * @throws IOException if input/output stream fails
     */
    public TagController() throws IOException {
        File file = new File(System.getProperty("user.dir") + "/" + "tags.txt");
        boolean listOfAllTags = file.createNewFile();
        tags = new ArrayList<>();
    }

    /**
     * Add tag to tags
     *
     * @param tag the tag being added
     */
    void addTag(Tag tag) {
        // there can only be one of each tag
        if (!tags.contains(tag)) {
            tags.add(tag);
        }
    }

    /**
     * Delete tag from tags
     *
     * @param tag the tag being deleted
     */
    void deleteTag(Tag tag) {
        tags.remove(tag);
    }

    /**
     * Writing the list of tags into file
     *
     * @param file the file being written to
     * @throws IOException if input/output stream fails
     */
    void saveFile(File file) throws IOException {

        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
        for (Tag tag : this.tags) {
            bufferedWriter.write(tag.getName());
            bufferedWriter.newLine();
        }
        bufferedWriter.close();

    }

    /**
     * Adding new tags to this.tags from file
     *
     * @param file the file with the new tags
     * @throws IOException if input/output stream fails
     */
    void readTagsFromFile(File file) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line;
        while ((line = reader.readLine()) != null) {
            tags.add(new Tag(line.trim()));
        }
        reader.close();
    }

    /**
     * Returns a list of tags from tags file
     *
     * @return the list of tags from tags file
     */
    static ObservableList<Tag> getTagsList() {
        ObservableList<String> tagsStringObservableList = null;
        try {
            List<String> tagsDataList = readTextFileByLines(System.getProperty("user.dir") + "/" + "tags.txt");
            tagsStringObservableList = FXCollections.observableArrayList(tagsDataList);
        } catch (IOException e) {
            e.printStackTrace();
        }

        ArrayList<Tag> tagsObservableList;
        tagsObservableList = new ArrayList<>();
        if (tagsStringObservableList != null) {
            for (String tagString : tagsStringObservableList) {
                tagsObservableList.add(new Tag(tagString));
            }
        }
        return FXCollections.observableArrayList(tagsObservableList);
    }

    /**
     * Returns a list of string of the lines of file with name fileName
     *
     * @param fileName the name of the file
     * @return a list of the lines in file with name fileName
     * @throws IOException if input/output stream fails
     */
    static List<String> readTextFileByLines(String fileName) throws IOException {
        return Files.readAllLines(Paths.get(fileName));
    }
} 