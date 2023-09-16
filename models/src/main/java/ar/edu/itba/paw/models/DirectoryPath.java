package ar.edu.itba.paw.models;

import java.util.List;

public class DirectoryPath {
    private final List<Directory> directories;

    public DirectoryPath(List<Directory> directories){
        if (directories.isEmpty()) throw new IllegalArgumentException();
        this.directories = directories;
    }

    public RootDirectory getRootDirectory() {
        return (RootDirectory) directories.get(0);
    }

    public Directory getParentDirectory() {
        if (directories.size() == 1) return null;
        return directories.get(directories.size() - 2);
    }

    public Directory getCurrentDirectory() {
        return directories.get(directories.size() - 1);
    }

    public int getLength() { return directories.size(); }


}
