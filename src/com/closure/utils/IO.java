package com.closure.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * IO Helper routines.
 * @author Mohamed Mansour
 */
public class IO {

  /**
   * Recursively get all the JavaScript files within some directory.
   * 
   * Sanity check to ommit all directories starting with dot to handle
   * not recursively going deeper for svn and git directories.
   * 
   * @param path The directory to start searching.
   * @param all A collection of JavaScript file paths that were found.
   */
  public static void getJSFiles(File path, Collection<File> all) {
    final File[] children = path.listFiles();
    if (children != null) {
      for (File child : children) {
        String fileName = child.getName();
        if (child.isFile() && fileName.endsWith(".js"))
          all.add(child);
        else if (child.isDirectory() && fileName.charAt(0) != '.')
          getJSFiles(child, all);
      }
    }
  }
  
  /**
   * Get file contents of a given file in some List.
   * 
   * @todo There are many ways to optimize this, but we will worry 
   *       about that later.
   * @param path The file to parse.
   * @return A list containing each line in the parsed file.
   */
  public static List<String> getFileContents(File path) {
    List<String> builder = new ArrayList<String>();
    try {
      BufferedReader reader = new BufferedReader(new FileReader(path));
      String line;
      while ((line = reader.readLine()) != null) {
        builder.add(line);
      }
      reader.close();
    } catch (FileNotFoundException e) {
      Logger.error("Cannot find file", e);
    } catch (IOException e) {
      Logger.error("Cannot read file", e);
    }
    return builder;
  }

  /**
   * Write file contents to disk
   * @param output The file to write to.
   * @param contents The contents to dump.
   */
  public static void writeFile(File output, StringBuilder contents) {
    try {
      BufferedWriter writer = new BufferedWriter(new FileWriter(output));
      writer.append(contents);
      writer.close();
    } catch (IOException e) {
      Logger.error("Cannot write deps file.", e);
    }
  }

  /**
   * Get the relative path based 
   * @param targetFile The directory we need to be relative.
   * @param baseFile The directory to be relative to.
   * @return A relative path from |baseFile| to |targetFile|
   */
  public static String getRelativePath(File targetFile, File baseFile) {
    String targetPath = targetFile.getPath();
    String basePath = baseFile.getPath();
    
    // Find the last common index between the target and base so that
    // the common path could be discoverable.
    int commonIndex = 0;
    for (int i = 0; i < targetPath.length() && i < basePath.length(); i++) {
      if (targetPath.charAt(i) == basePath.charAt(i)) {
        commonIndex++;
      } else {
        break;
      }
    }
    String commonPath = targetPath.substring(0, commonIndex);
    
    char systemSeparator = File.separatorChar;
    StringBuilder relativePath = new StringBuilder();
    
    // Within the same base directory.
    if (commonIndex == basePath.length()) { // Inside.
      relativePath.append(targetPath.substring(commonIndex + 1));
    } else { // Outside.
      // Remove the common path from target and base.
      String targetCondensed = targetPath.substring(commonPath.length());
      String baseCondensed = basePath.substring(commonPath.length());
      
      // Figure out how many paths there are to reach base.
      int depth = baseCondensed.split("\\" + systemSeparator).length;
     
      // Convert each depth into "../".
      for (int i = 0; i < depth; i++) {
        relativePath.append(".." + systemSeparator);
      }
      relativePath.append(targetCondensed);
    }
    
    // Replace all separators to Closure friendly separators.
    for (int i = 0; i < relativePath.length(); i++) {
      if (relativePath.charAt(i) == systemSeparator) {
        relativePath.setCharAt(i, '/');
      }
    }
    return relativePath.toString();
  }
}
