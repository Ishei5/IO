package com.pankov.roadtoseniour.io.manager;

import java.io.*;

public class FileManager {

    // public static int countFiles(String path) - принимает путь к папке,
    // возвращает количество файлов в папке и всех подпапках по пути
    public static int countFiles(String path) throws FileNotFoundException {
        int count = 0;
        File root = new File(path);
        if (!root.exists()) {
            throw new FileNotFoundException("The current path is not valid");
        }
        File[] listFiles = root.listFiles();

        if (listFiles != null) {
            for (File file : listFiles) {
                if (file.isDirectory()) {
                    count += countFiles(file.getPath());
                } else if (file.isFile()) {
                    count++;
                }
            }
        }

        return count;
    }

    // public static int countDirs(String path) - принимает путь к папке,
    // возвращает количество папок в папке и всех подпапках по пути
    public static int countDirs(String path) throws IOException {
        int count = 0;
        File root = new File(path);
        if (!root.exists()) {
            throw new FileNotFoundException("The current path is not valid");
        }
        File[] listFiles = new File(path).listFiles();
        if (listFiles != null) {
            for (File file : listFiles) {
                if (file.isDirectory()) {
                    count += countDirs(file.getPath());
                    count++;
                }
            }
        }

        return count;
    }

    //метод по копированию папок и файлов.
    //Параметр from - путь к файлу или папке, параметр to - путь к папке куда будет производиться копирование.
    public static void copy(String from, String to) throws IOException {
        File source = new File(from);
        File destination = new File(to);

        if (!source.exists()) {
            throw new FileNotFoundException("The current path is not valid");
        }

        if (!destination.exists()) {
            destination.mkdir();
        }

        for (String file : source.list()) {
            copyItem(new File(from, file), new File(to, file));
        }
    }

    private static void copyItem(File source, File destination) throws IOException {
        if (source.isDirectory()) {
            copy(source.getPath(), destination.getPath());
        } else {
            copyFile(source, destination);
        }
    }

    private static void copyFile(File source, File destination) throws IOException {
        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(source));
             BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(destination))) {
            byte[] buffer = new byte[8192];
            int length;
            while ((length = bis.read(buffer)) > 0) {
                bos.write(buffer, 0, length);
            }
        }


    }

    //метод по перемещению папок и файлов.
    //Параметр from - путь к файлу или папке, параметр to - путь к папке куда будет производиться копирование.
    public static void move(String from, String to) throws IOException {
        if (!new File(from).renameTo(new File(to))) {
            copy(from, to);
            remove(from);
        }
    }

    static void remove(String from) throws IOException {
        File rootFile = new File(from);

        if (rootFile.isDirectory()) {
            File[] filesList = rootFile.listFiles();
            if (filesList != null) {
                for (File file : filesList) {
                    remove(file.getPath());
                }
            }
        }

        if (!rootFile.delete()) {
            throw new IOException("Failed to delete - " + rootFile);
        }
    }
}
