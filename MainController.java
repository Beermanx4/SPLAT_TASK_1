package sample;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Scanner;

public class MainController {
    @FXML
    private Button btnCancel;
    @FXML
    private Button btnFind;
    @FXML
    private TabPane tabPane;

    @FXML
    private TextField textToFindTextField;
    @FXML
    private TextField folderTextField;
    @FXML
    private TextField extensionTextField;
    @FXML
    private VBox mainVBox;
    @FXML
    private TreeView<String> foldersTreeView;
    @FXML
    private Label loadingLable;

    @FXML
    private CheckBox extensionCheckBox;


    @FXML
    private ProgressIndicator loadingProgressIndicator;

    private String textToFind;
    private String path;
    private FindTask find_task;

    @FXML
    private void clickedBtnCancel() throws Exception {
        find_task.cancel();
    }


    @FXML
    private void clickedBtnSetFolder() {
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("JavaFX Projects");
        File defaultDirectory = new File("C:\\Program Files");
        chooser.setInitialDirectory(defaultDirectory);
        File selectedDirectory = chooser.showDialog(mainVBox.getScene().getWindow());
        if (selectedDirectory == null) {
            path = defaultDirectory.getAbsolutePath();
            folderTextField.setText(path);
            return;
        }
        path = selectedDirectory.getAbsolutePath();
        folderTextField.setText(path);
    }

    public void initialize() {
        extensionCheckBox.selectedProperty().addListener(event ->
                extensionTextField.disableProperty().setValue(!extensionTextField.disableProperty().getValue())
        );
    }

    public void clickedBtnFind() {
        btnFind.disableProperty().setValue(true);
        btnCancel.disableProperty().setValue(false);
        textToFind = textToFindTextField.getText();
        if (textToFind.isEmpty()) {
            showErrorMessage("Cannot find empty string!");
            return;
        }
        if (path == null) {
            path = folderTextField.getText();
        }
        find_task = new FindTask();
        //here!!
        String extension = extensionTextField.getText();
        if (extension.isEmpty() || !extensionCheckBox.isSelected())
            extension = "log";
        find_task.setTaskValues(path, textToFind, extension);
        loadingProgressIndicator.opacityProperty().setValue(1);
        loadingLable.opacityProperty().setValue(1);
        loadingProgressIndicator.progressProperty().unbind();
        loadingProgressIndicator.progressProperty().bind(find_task.progressProperty());
        find_task.setOnCancelled(event -> {
            loadingProgressIndicator.progressProperty().unbind();
            loadingProgressIndicator.opacityProperty().setValue(0);
            loadingLable.opacityProperty().setValue(0);
            btnFind.disableProperty().setValue(false);
            btnCancel.disableProperty().setValue(true);
        });
        find_task.setOnSucceeded(event -> {
            ObjectsInFolder objects_in_folder = find_task.getValue();
            loadingProgressIndicator.progressProperty().unbind();
            loadingProgressIndicator.opacityProperty().setValue(0);
            loadingLable.opacityProperty().setValue(0);
            TreeItem<String> root = new TreeItem<>(path);
            draw(root, objects_in_folder);
            foldersTreeView.setRoot(root);
            btnFind.disableProperty().setValue(false);
            btnCancel.disableProperty().setValue(true);
        });
        new Thread(find_task).start();
    }

    private void draw(TreeItem item, ObjectsInFolder folder) {
        for (ObjectsInFolder objects : folder.getFolders()) {
            TreeItem<String> sub_item = new TreeItem<>(objects.getFolderName());
            draw(sub_item, objects);
            item.getChildren().add(sub_item);
        }
        for (File file : folder.getFiles()) {
            TreeItem<String> sub_item = new TreeItem<>(file.getName());
            item.getChildren().add(sub_item);
        }
        foldersTreeView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (mouseEvent.getClickCount() == 2) {
                    try {
                        loadText();
                    } catch (Exception exeption) {
                        showExeptionErrorMessage(exeption);
                    }

                }
            }
        });
    }

    private void loadText() throws Exception {
        TreeItem<String> item = foldersTreeView.getSelectionModel().getSelectedItem();

        if (!item.getValue().contains("\\")) {
            Tab tab = new Tab(item.getValue());
            ListView<String> lst = new ListView<>();
            ObservableList<String> new_str = FXCollections.observableArrayList();

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("tab.fxml"));

            Node node = loader.load();

            File file = new File(item.getParent().getValue() + "\\" + item.getValue());
            TabController tabController = loader.getController();
            tabController.setTextToColor(textToFind);
            //tabController.name = file.getName();
            //Node node = FXMLLoader.load(this.);
            Scanner sc = new Scanner(file, "cp1251");
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                new_str.add(line);
                tabController.addStringToTab(line, line.contains(textToFind));
                lst.getItems().add(line);
            }
            tabController.setStartPosition();
            tab.setContent(node);
            tabPane.getTabs().add(tab);


        }

    }

    private void showExeptionErrorMessage(Exception e) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error alert");
        alert.setHeaderText(e.getMessage());

        VBox dialogPaneContent = new VBox();

        Label label = new Label("Stack Trace:");


        String stackTrace = this.getStackTrace(e);
        TextArea textArea = new TextArea();
        textArea.setText(stackTrace);

        dialogPaneContent.getChildren().addAll(label, textArea);

        // Set content for Dialog Pane
        alert.getDialogPane().setContent(dialogPaneContent);

        alert.showAndWait();
    }

    private String getStackTrace(Exception e) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        return sw.toString();
    }

    private void showErrorMessage(String s) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("ERROR!");
        alert.setHeaderText(null);
        alert.setContentText(s);
        alert.showAndWait();
    }
}
