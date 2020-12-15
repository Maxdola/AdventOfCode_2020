package io.hardtke.adventofcode.utils;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

/**
 * @author Max Hardtke
 */
public class ResourceLoader {

    /**
     * Loads a resource from the resources folder as an InputStream
     * @param filePath the path within the resources Folder
     * @return the resources as an {@link InputStream}
     */
    public InputStream loadResource(String filePath) {
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        return classloader.getResourceAsStream(filePath);
    }

    /**
     * Loads a resource from the resources folder as an InputStreamReader
     * with {@param encoding} as Charset
     * @param filePath the path within the resources Folder
     * @param encoding the {@link Charset} of the file
     * @return the resources as an {@link InputStreamReader}
     */
    public InputStreamReader loadResource(String filePath, Charset encoding) {
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        InputStream inputStream = classloader.getResourceAsStream(filePath);
        if (inputStream == null) {
            return null;
        }
        return new InputStreamReader(inputStream, encoding);
    }

}
