package com.pankov.roadtoseniour.io.manager;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.*;

import static org.junit.jupiter.api.Assertions.*;

public class FileManagerTest {


    final static String PATH_TO_TEST_RESOURCES = "src/test/resources/";
    final static String TEST_DIR = "testfilemanager/";
    final static String TEST_STRING = "Test copy of file in FileManager";

    @BeforeAll
    public static void before() throws IOException {
        File testFileManagerDir = new File(PATH_TO_TEST_RESOURCES +TEST_DIR);
        testFileManagerDir.mkdir();

        new File(testFileManagerDir, "test1").mkdir();

        File test2Dir = new File(testFileManagerDir, "test2");
        test2Dir.mkdir();

        File testFile = new File(test2Dir, "test.txt");
        testFile.createNewFile();
        try (FileOutputStream fileOutputStream = new FileOutputStream(testFile)) {
            fileOutputStream.write(TEST_STRING.getBytes());
        }
    }

    @Test
    public void testCountFilesInRootDir() throws FileNotFoundException {
        assertEquals(1, FileManager.countFiles(PATH_TO_TEST_RESOURCES + TEST_DIR));
    }

    @Test
    public void testCountFilesInEmptyDir() throws FileNotFoundException {
        assertEquals(0, FileManager.countFiles(PATH_TO_TEST_RESOURCES + TEST_DIR + "test1"));
    }

    @Test
    public void testCountFilesWithException() {
        assertThrows(FileNotFoundException.class, () ->
                FileManager.countFiles(PATH_TO_TEST_RESOURCES + TEST_DIR + "test3"));
    }

    @Test
    public void testCountDirsInRootDir() throws IOException {
        assertEquals(2, FileManager.countDirs(PATH_TO_TEST_RESOURCES + TEST_DIR));
    }

    @Test
    public void testCountDirsInEmptyDir() throws IOException {
        assertEquals(0, FileManager.countDirs(PATH_TO_TEST_RESOURCES + TEST_DIR + "test1"));
    }

    @Test
    public void testCountDirsWithException() {
        assertThrows(FileNotFoundException.class, () ->
        FileManager.countDirs(new File(PATH_TO_TEST_RESOURCES + TEST_DIR, "test3").getPath()));
    }

    @Test
    @DisplayName("testCopy Should be the same count of dirs and files and the same file content")
    public void testCopyShouldBeTheSameCountOfDirsAndFilesAndSameContentOfFile() throws IOException {
        FileManager.copy(PATH_TO_TEST_RESOURCES + TEST_DIR, PATH_TO_TEST_RESOURCES + "testfilemanagercopy");
        File duplicatedItem = new File(PATH_TO_TEST_RESOURCES, "testfilemanagercopy");

        assertTrue(duplicatedItem.exists());
        assertEquals(2, FileManager.countDirs(duplicatedItem.getPath()));
        assertEquals(1, FileManager.countFiles(duplicatedItem.getPath()));

        StringBuilder stringBuilder = new StringBuilder();

        try(BufferedInputStream fileInputStream = new BufferedInputStream(
                new FileInputStream(new File(duplicatedItem, "test2/test.txt")))) {

            int value;
            while((value = fileInputStream.read()) != -1) {
                stringBuilder.append((char) value);
            }
        }

        assertEquals(TEST_STRING, stringBuilder.toString());
    }

    @Test
    public void testMoveDir() throws IOException {
        File itemToMove = new File(PATH_TO_TEST_RESOURCES + "testfilemanagercopy");

        int expectedFilesCount = FileManager.countFiles(itemToMove.getPath());
        int expectedDirsCount = FileManager.countDirs(itemToMove.getPath());

        File testDirMoveIn = new File(PATH_TO_TEST_RESOURCES, "testDirForMove");
        testDirMoveIn.mkdir();
        FileManager.move(itemToMove.getPath(), testDirMoveIn.getPath());

        assertFalse(itemToMove.exists());
        assertEquals(expectedFilesCount, FileManager.countFiles(testDirMoveIn.getPath()));
        assertEquals(expectedDirsCount, FileManager.countDirs(testDirMoveIn.getPath()));
    }

    @AfterAll
    public static void after() throws IOException {
        FileManager.remove(new File(PATH_TO_TEST_RESOURCES, "testDirForMove").getAbsolutePath());
        FileManager.remove(new File(PATH_TO_TEST_RESOURCES +TEST_DIR).getAbsolutePath());
    }
}
