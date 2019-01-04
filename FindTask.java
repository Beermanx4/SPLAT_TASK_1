package sample;

import javafx.concurrent.Task;


public class FindTask extends Task<ObjectsInFolder> {
    private String textToFind;
    private String folderName;
    private String extension;

    @Override
    protected ObjectsInFolder call() {
        ObjectsInFolder objectsInFolder = new ObjectsInFolder();
        objectsInFolder.setFolderName(folderName);
        ObjectsInFolder.setExtension(extension);
        objectsInFolder.getObjectsInFolder(folderName);


        return objectsInFolder.findTextInFolder(textToFind);
    }

    void setTaskValues(String folderName, String textToFind, String extension) {
        this.folderName = folderName;
        this.textToFind = textToFind;
        this.extension = extension;
    }
}
