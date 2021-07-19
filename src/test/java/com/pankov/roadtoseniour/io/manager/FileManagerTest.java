package com.pankov.roadtoseniour.io.manager;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

public class FileManagerTest {

    //    FileManager fileManager = new FileManager();
    final static String PATH_TO_TEST_RESOURCES = "src\\test\\resources\\";
    final String TEST_DIR = "testfilemanager\\";

    @Test
    public void testCountFilesInRootDir() throws FileNotFoundException {
        assertEquals(1, FileManager.countFiles(PATH_TO_TEST_RESOURCES + TEST_DIR));
    }

    @Test
    public void testCountFilesInEmptyDir() throws FileNotFoundException {
        assertEquals(0, FileManager.countFiles(PATH_TO_TEST_RESOURCES + TEST_DIR + "test1"));
    }

    @Test
    public void testCountFilesWithExeption() {
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
    public void testCountDirsWithExсeption() {
        assertThrows(FileNotFoundException.class, () ->
        FileManager.countDirs(PATH_TO_TEST_RESOURCES + TEST_DIR + "\\test3"));
    }

    @Test
    public void testCopy() throws IOException {
        FileManager.copy(PATH_TO_TEST_RESOURCES + TEST_DIR, PATH_TO_TEST_RESOURCES + "testfilemanagercopy\\");
        File duplicatedItem = new File(PATH_TO_TEST_RESOURCES, "testfilemanagercopy");
        assertTrue(duplicatedItem.exists());

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
    public static void after() throws Exception {
        Class clazz = FileManager.class;
        File testDirMoveIn = new File(PATH_TO_TEST_RESOURCES, "testDirForMove");
        Method removeMethod =  clazz.getDeclaredMethod("remove", String.class);
        removeMethod.setAccessible(true);
        removeMethod.invoke(null, testDirMoveIn.getPath());
    }
}
