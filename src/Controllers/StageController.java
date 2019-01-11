package Controllers;

import Models.Picture;
import Models.Tag;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.awt.*;
import java.io.*;
import java.net.URL;
import java.util.*;
import java.util.List;

public class StageController implements Initializable {
    private static Stage stage;

    /**
     * The buttons on the stage
     */
    public Button addTagButton;
    public Button deleteTagButton;
    public Button saveTagButton;
    public Button searchDirectoryButton;
    public Button searchSubdirectoryButton;
    public Button searchPictureButton;
    public Button addTagToNameButton;
    public Button logButton;
    public Button openContainingFolderButton;

    /**
     * The list views on the stage
     */
    public ListView<Tag> tagList;
    public ListView<String> previousNamesList;
    public ListView<Picture> pictureList;

    /**
     * The observable lists
     */
    private ObservableList<Tag> tagListItems;
    private ObservableList<Picture> pictureListItems;

    /**
     * The text fields on the stage
     */
    public TextField tagField;
    public TextField nameTextField;
    public TextField absolutePathField;

    /**
     * Where the image is viewed
     */
    public StackPane imageBackground;
    public ImageView pictureView;
    /**
     * The tags list for image
     */
    public FlowPane tagsListForPictureField;
    /**
     * The tagController
     */
    private TagController tagController;
    /**
     * The history log
     */
    private List<String> globalLogData;
    /**
     * The current picture selected
     */
    private Picture currentPicture;

    /**
     * Setting the stage
     *
     * @param stage the stage being set
     */
    public static void setStage(Stage stage) {
        StageController.stage = stage;
    }

    /**
     * The action for the Add Tag button
     */
    public void handleAddTagPress() {
        Tag newTag = new Tag(tagField.getText().trim());
        addTag(newTag);

        tagField.clear();
    }

    /**
     * Adding tag to tagController and tag list
     *
     * @param newTag the tag to add
     */
    private void addTag(Tag newTag) {
        // checking existing tags
        if (!newTag.getName().isEmpty() && !tagListItems.contains(newTag)) {
            if (!tagListItems.contains(newTag))
                tagListItems.add(newTag);

            tagController.addTag(newTag);
        }
    }

    /**
     * The action for the Delete Tag button
     */
    public void handleDeleteTagPress() {
        Tag selectedTag = tagList.getSelectionModel().getSelectedItem();
        tagListItems.remove(selectedTag);
        tagController.deleteTag(selectedTag);
    }

    /**
     * Save global tag list to tag file
     */
    private void saveTags() {
        try {
            tagController.saveFile(new File(System.getProperty("user.dir") + "/" + "tags.txt"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * The action for the Save Tag button
     */
    public void handleSaveTagPress() {
        saveTags();
    }

    /**
     * The action for the Add Tag to Name button
     */
    public void handleAddTagToNamePress() throws IOException {
        ObservableList<Tag> selectedTags = tagList.getSelectionModel().getSelectedItems();
        if (currentPicture != null && selectedTags.size() > 0) {
            for (Tag tag : selectedTags) {
                // if picture doesn't have a tag, then we add one
                if (!currentPicture.hasTagString(tag.getName())) {
                    currentPicture.addTag(tag);
                    // Create button around tag and add it
                    createATagButton(tag.getName());
                }
            }
            savePicture();
        }
    }

    /**
     * Fills in previousNamesList with the previously used and stored names of the current file
     */
    private void loadPreviousNames() {
        previousNamesList.getItems().clear();
        ObservableList<String> history = FXCollections.observableArrayList(currentPicture.getNameHistory());
        previousNamesList.setItems(history);
    }

    /**
     * Loads the image list with all image files from the specified directory
     *
     * @param includeSubdirectories Whether or not to search subdirectories for files
     */
    private void getPicturesFromDir(Boolean includeSubdirectories) {
        try {
            List<Picture> newPictures = PictureController.getImagesFromDir(StageController.stage, includeSubdirectories);
            loadPicturesToView(newPictures);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets an image list with all image files selected from a FileChooser
     */
    private void getSpecificPictures() {
        try {
            List<Picture> newPictures = PictureController.getSpecificImages(StageController.stage);
            loadPicturesToView(newPictures);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Loads the given list of pictures onto the imageList on the GUI, to be later selected.
     *
     * @param newPictures The list of pictures to be loaded onto the GUI
     */
    private void loadPicturesToView(List<Picture> newPictures) {
        pictureListItems.remove(0, pictureListItems.size());
        pictureListItems.addAll(newPictures);

        for (Picture picture : pictureListItems) {
            for (Tag tag : picture.getTags()) {
                addTag(tag);
            }
        }
        saveTags();
    }

    /**
     *
     */
    public void handleSearchDirPress() {
        getPicturesFromDir(false);
    }

    /**
     *
     */
    public void handleSearchSubPress() {
        getPicturesFromDir(true);
    }

    /**
     * @throws IOException
     */
    public void handleSearchPicturePress() throws IOException {
        getSpecificPictures();
    }

    /**
     * The action for the Log button
     */
    public void handleLogButtonPress() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getClassLoader().getResource("Views/LogWindowView.fxml"));
        loader.load();
        LogWindowController logWindowController = loader.getController();
        Parent root = loader.getRoot();
        Scene scene = new Scene(root);
        Stage window = new Stage();
        window.setTitle("Log");
        window.setScene(scene);
        window.show();

        logWindowController.passData(globalLogData);
    }

    /**
     * The action for the Save Picture button
     */
    public void handleSavePictureNamePress() {
        if (currentPicture != null) {
            try {
                savePicture();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Saves name of image, updating image data in file as well as GUI
     *
     * @throws IOException If fileNotFound
     */
    private void savePicture() throws IOException {
        currentPicture.setName(nameTextField.getText());
        StringBuilder newName = new StringBuilder(currentPicture.getName());
        for (Tag tag : currentPicture.getTags()) {
            newName.append("@").append(tag.getName());
        }
        currentPicture.setName(newName.toString());

        File newFileName = new File(currentPicture.getDirectory() + "/" + newName + "." + currentPicture.getFileType());
        File oldFileName = currentPicture.getFile();
        currentPicture.setFile(newFileName);
        absolutePathField.setText(newFileName.getAbsolutePath());

        // Add save to log
        globalLogData.add("Old name: " + oldFileName.getAbsolutePath());
        globalLogData.add("New name: " + newFileName.getAbsolutePath());
        globalLogData.add("Date: " + new Date().toString());
        globalLogData.add("");
        try {
            saveGlobalLogList();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Add name to the previous names list and reload
        currentPicture.getNameHistory().add(newFileName.getAbsolutePath());
        currentPicture.saveHistory();
        loadPreviousNames();

        oldFileName.renameTo(newFileName);
    }

    /**
     * Records the latest version of the master log, writing a new log file and storing it
     * under user directory
     *
     * @throws IOException if FileNotFound
     */
    private void saveGlobalLogList() throws IOException {
        File globalLogSave = new File(System.getProperty("user.dir") + "/" + "globalLog.txt");
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(globalLogSave));
        for (String line : this.globalLogData) {
            bufferedWriter.write(line);
            bufferedWriter.newLine();
        }
        bufferedWriter.close();
    }

    /**
     * The action for the Delete Picture button
     */
    public void handleDeletePicturePress() {
        if (currentPicture != null) {
            // selecting image and deletes it from history
            File fileToDelete = currentPicture.getFile();
            File historyFileToDelete = currentPicture.getHistory();
            historyFileToDelete.delete();
            pictureList.getItems().remove(currentPicture);
            // since picture is gone, need to set everything to ""
            absolutePathField.setText("");
            nameTextField.setText("");
            tagsListForPictureField.getChildren().clear();
            previousNamesList.getItems().clear();

            File file = new File("src/Images/image.png");
            Image image = new Image(file.toURI().toString());
            pictureView.setImage(image);

            fileToDelete.delete();
            currentPicture = null;
        }
    }

    /**
     * The action for the Move Image button
     */
    public void handleMovePicturePress() {
        if (currentPicture != null) {
            File oldFileName = currentPicture.getFile();
            // use directory chooser to get directory
            DirectoryChooser chooser = new DirectoryChooser();
            chooser.setTitle("Select a New Directory");
            File selectedDirectory = chooser.showDialog(stage);
            if (selectedDirectory != null) {
                StringBuilder newName = new StringBuilder(currentPicture.getName());
                for (Tag tag : currentPicture.getTags()) {
                    newName.append("@").append(tag.getName());
                }
                File newFileName = new File(selectedDirectory + "/" + newName.toString() + "." + currentPicture.getFileType());
                System.out.println(currentPicture.getFile());
                System.out.println(newFileName);
                // Have to change directory too
                currentPicture.setDirectory(selectedDirectory.getAbsolutePath());
                currentPicture.setFile(newFileName);

                // Add save to log
                globalLogData.add("Old name: " + oldFileName.getAbsolutePath());
                globalLogData.add("New name: " + newFileName.getAbsolutePath());
                globalLogData.add("Date: " + new Date().toString());

                globalLogData.add("");
                try {
                    saveGlobalLogList();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                // Add name to the previous names list and reload
                currentPicture.getNameHistory().add(newFileName.getAbsolutePath());
                try {
                    currentPicture.saveHistory();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                loadPreviousNames();

                oldFileName.renameTo(newFileName);
                absolutePathField.setText(newFileName.getAbsolutePath());

                List<Picture> movedPictureList = new ArrayList<>();
                movedPictureList.add(currentPicture);

                loadPicturesToView(movedPictureList);
            }
        }
    }

    /**
     * The action for the Create a Tag button
     */
    private void createATagButton(String tagName) {
        Button newTagButton = new Button(tagName);
        newTagButton.setStyle("-fx-background-color: #8D8E9E; -fx-text-fill: WHITE;");
        EventHandler<ActionEvent> buttonHandler = event -> {
            tagsListForPictureField.getChildren().remove(newTagButton);
            currentPicture.removeTagByName(tagName);
            try {
                savePicture();
            } catch (IOException e) {
                e.printStackTrace();
            }
            event.consume();
        };
        newTagButton.setOnAction(buttonHandler);
        // Add to pane to the right of picture
        tagsListForPictureField.getChildren().add(newTagButton);
    }

    /**
     * When an picture is selected to be loaded
     */
    public void onPictureListClick() {
        // First clear old tags
        tagsListForPictureField.getChildren().clear();
        currentPicture = pictureList.getSelectionModel().getSelectedItem();
        if (currentPicture != null) {
            loadPictureDataOnGUI(currentPicture);
        }
    }

    /**
     * Gets a previous name of the file and reverts it, if possible
     *
     * @throws IOException if FileNotFound
     */
    public void onNameHistoryClick() throws IOException {
        if (currentPicture != null) {
            String newName = previousNamesList.getSelectionModel().getSelectedItem();
            if (newName != null) {
                File oldFileName = currentPicture.getFile();
                File newFileName = new File(newName);
                oldFileName.renameTo(newFileName);
                absolutePathField.setText(newName);

                // Add save to log
                globalLogData.add(oldFileName.getAbsolutePath());
                globalLogData.add(newFileName.getAbsolutePath());
                globalLogData.add(new Date().toString());
                globalLogData.add("");
                try {
                    saveGlobalLogList();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                currentPicture.clearTags();
                currentPicture.separateName(newFileName.getName());
                currentPicture.setFile(newFileName);
                loadPictureDataOnGUI(currentPicture);

                // Add name to the file that holds this picture's names
                currentPicture.getNameHistory().add(newFileName.getAbsolutePath());
                currentPicture.saveHistory();
                loadPreviousNames();
            }
        }
    }

    /**
     * Open the current selected file's directory via the file explorer of the
     * user's operating system
     *
     * @throws IOException
     */
    public void openContainingFolder() throws IOException {
        File currentImagePath = currentPicture.getFile().getParentFile();

        if (currentPicture != null) {
            if (Desktop.isDesktopSupported()) {
                new Thread(() -> {
                    try {
                        Desktop.getDesktop().open(currentImagePath);
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }).start();
            }
        }
    }

    /**
     * Loads the image data onto the GUI, including name, tags, tags buttons, name history, etc.
     *
     * @param image The image to extract data from and load
     */
    private void loadPictureDataOnGUI(Picture image) {
        if (currentPicture != null)
        {
            tagsListForPictureField.getChildren().clear();
            pictureView.setImage(currentPicture.getImage());
            // Tags and name in a list
            List<String> imageData = splitTagsFromImageName(image.getName());
            // Set name field
            nameTextField.setText(imageData.get(0));
            String imageFilePath = currentPicture.getFile().getAbsolutePath();
            absolutePathField.setText(imageFilePath);

            if (imageData.size() >= 1) {
                for (Tag tag : currentPicture.getTags()) {
                    createATagButton(tag.getName());
                }
            }
            loadPreviousNames();
        }
    }

    /**
     * Returns a list of the data from a given file name, separated by tags and names
     *
     * @param fileName The file to parse and separate
     * @return The list of separated data
     */
    private List<String> splitTagsFromImageName(String fileName) {
        return Arrays.asList(fileName.split("@"));
    }

    /**
     * Initialize controller, stage and stage elements to create GUI after root element has
     * been processed
     *
     * @param location  The location used to resolve relative paths for the root object,
     *                  or null if the location is not known.
     * @param resources The resources used to localize the root object, or null if
     *                  the root object was not localized.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        try {
            tagController = new TagController();
            File tagsSet = new File(System.getProperty("user.dir") + "/" + "tags.txt");
            File globalLog = new File(System.getProperty("user.dir") + "/" + "globalLog.txt");
            // Create files if they do not exist
            tagsSet.createNewFile();
            globalLog.createNewFile();
            globalLogData = TagController.readTextFileByLines(System.getProperty("user.dir") + "/" + "globalLog.txt");

            tagController.readTagsFromFile(new File(System.getProperty("user.dir") + "/" + "tags.txt"));
            tagListItems = TagController.getTagsList();
            tagList.setItems(tagListItems);
            tagList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

            pictureView.fitWidthProperty().bind(imageBackground.widthProperty());
            pictureView.fitHeightProperty().bind(imageBackground.heightProperty());

        } catch (IOException e) {
            e.printStackTrace();
        }

        pictureListItems = FXCollections.observableArrayList();
        pictureList.setItems(pictureListItems);
    }

}
