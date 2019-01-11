package Controllers;

import Models.Picture;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * If an input or output stream is failed then a <tt>IOException</tt> will be thrown.
 */
class PictureController {

    /**
     * Returns all images from directory and returns images in subdirectories if descendIntoSubDirectories is true
     *
     * @param directory                 the directory to get images from
     * @param descendIntoSubDirectories whether to get images from subdirectories
     * @return all images in directory and returns images in subdirectories if descendIntoSubDirectories is true
     */
    private static ArrayList<Picture> getAllImages(File directory, boolean descendIntoSubDirectories) throws IOException {
        ArrayList<Picture> resultList = new ArrayList<>(256);

        if (directory != null && directory.listFiles() != null) {
            // Only get files that are images
            File[] f = directory.listFiles();
            ArrayList<String> fileTypes = new ArrayList<String>() {
            };
            fileTypes.add(".jpeg");
            fileTypes.add(".jpg");
            fileTypes.add(".png");
            fileTypes.add(".gif");
            fileTypes.add(".bmp");
            assert f != null;
            for (File file : f) {
                if (file != null && stringContainsItemFromList(file.getName().toLowerCase(), fileTypes)) {
                    resultList.add(new Picture(file.getParent(), file.getName()));
                }
                // If descendIntoSubDirectories is true, get all images in subdirectories
                assert file != null;
                if (descendIntoSubDirectories && file.isDirectory()) {
                    ArrayList<Picture> tmp = getAllImages(file, true);
                    if (tmp != null) {
                        resultList.addAll(tmp);
                    }
                }
            }
        }
        return resultList;
    }

    /**
     * Returns true iff inputStr is in items
     *
     * @param inputStr the string being searched for
     * @param items    the list being searched
     * @return true iff inputStr is in items
     */
    private static boolean stringContainsItemFromList(String inputStr, ArrayList<String> items) {
        for (String item : items) {
            if (inputStr.endsWith(item)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns all images in selected directory
     *
     * @param stage                 the stage to show all images
     * @param includeSubdirectories true if to include images from subdirectories
     * @return images from selected directory
     */
    static List<Picture> getImagesFromDir(Stage stage, boolean includeSubdirectories) throws IOException {
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Select a Directory for Images");
        File selectedDirectory = chooser.showDialog(stage);

        return PictureController.getAllImages(selectedDirectory, includeSubdirectories);

    }

    /**
     * Returns a list of selected pictures by user
     *
     * @param stage the stage to show the selected pictures
     * @return a list of selected pictures by user
     */
    static List<Picture> getSpecificImages(Stage stage) throws IOException {

        FileChooser chooser = new FileChooser();
        chooser.setTitle("Select pictures");

        // Show only images
        chooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All Images", "*.jpg", "*.jpeg", "*.png", "*.gif", "*.bmp")
        );

        List<File> selectedFiles = chooser.showOpenMultipleDialog(stage);

        // For each image file selected, create a Picture
        ArrayList<Picture> selectedPictureList = new ArrayList<>();
        if (selectedFiles != null)
        {
            for (File file : selectedFiles) {
                Picture picture = new Picture(file.getParent(), file.getName());
                selectedPictureList.add(picture);
            }
        }

        return selectedPictureList;
    }
}
