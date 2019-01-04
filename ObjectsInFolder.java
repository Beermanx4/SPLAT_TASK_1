package sample;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class ObjectsInFolder {
    private List<File> files = new ArrayList<>();
    private List<ObjectsInFolder> folders = new ArrayList<>();
    private String folder_name;
    private static String extension;
    private boolean empty = false;
    private boolean text_found = false;


    List<ObjectsInFolder> getFolders() {
        return folders;
    }

    List<File> getFiles() {
        return files;
    }

    String getFolderName() {
        return folder_name;
    }

    void setFolderName(String name) {
        folder_name = name;
    }

    void getObjectsInFolder(String directoryName) {
        File directory = new File(directoryName);

        // Get all files from a directory.
        File[] fList = directory.listFiles();
        if (fList != null) {
            for (File file : fList) {
                if (file.isFile() && file.getName().endsWith(extension)) {
                    files.add(file);
                } else if (file.isDirectory()) {

                    ObjectsInFolder folder = new ObjectsInFolder();
                    folder.setFolderName(file.getAbsolutePath());
                    folder.getObjectsInFolder(file.getAbsolutePath());
                    folders.add(folder);
                }
            }
        } else
            empty = true;
    }

    static void setExtension(String desiredExtension) {
        extension = desiredExtension;
    }

    ObjectsInFolder findTextInFolder(String text) {
        ObjectsInFolder found_objects = new ObjectsInFolder();
        ObjectsInFolder sub_objects;

        for (ObjectsInFolder object : folders) {
            if (!object.empty) {
                sub_objects = object.findTextInFolder(text);
                if (sub_objects.text_found) {
                    found_objects.text_found = true;
                    found_objects.folders.add(sub_objects);
                }
            }
        }
        boolean found = false;
        for (File file : files) {
            if (file.canRead()) {
                try {
                    Scanner sc = new Scanner(file, "cp1251");

                    while (sc.hasNextLine()) {
                        String line = sc.nextLine();
                        if (line.contains(text)) {
                            found = true;
                            found_objects.text_found = true;
                        }
                    }

                } catch (FileNotFoundException exception) {
                    found = false;
                }
                if (found)
                    found_objects.files.add(file);
            }
            found = false;

        }

        found_objects.folder_name = this.folder_name;
        return found_objects;
    }


}

