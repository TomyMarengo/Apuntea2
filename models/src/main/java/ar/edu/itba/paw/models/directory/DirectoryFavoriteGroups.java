package ar.edu.itba.paw.models.directory;

import java.util.List;

public class DirectoryFavoriteGroups {
    private final List<Directory> rootDirectoryList;
    private final List<Directory> directoryList;

    public DirectoryFavoriteGroups(List<Directory> rootDirectoryList, List<Directory> directoryList) {
        this.rootDirectoryList = rootDirectoryList;
        this.directoryList = directoryList;
    }

    public List<Directory> getRootDirectoryList() {
        return rootDirectoryList;
    }

    public List<Directory> getDirectoryList() {
        return directoryList;
    }
}
