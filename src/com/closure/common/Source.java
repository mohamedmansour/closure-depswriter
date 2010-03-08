package com.closure.common;

import java.io.File;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.closure.utils.IO;

/**
 * Scans a source JS file for its provided and required namespaces.
 * Simple immutable class to scan a JavaScript file and express its
 * dependencies.
 * 
 * @author Mohamed Mansour
 */
public class Source {
  private static final String BASE_REGEX_STRING  = "^\\s*goog\\.%s\\(\\s*['\"](.*)['\"]\\s*\\);\\s*";
  private static final Pattern PROVIDE_REGEX = Pattern.compile(String.format(BASE_REGEX_STRING, "provide"));
  private static final Pattern REQUIRES_REGEX = Pattern.compile(String.format(BASE_REGEX_STRING, "require"));

  // Used Set to ensure only one item exists.
  private final Set<String> provides;
  private final Set<String> requires;

  private final String path;

  /**
   * Scans a JavaScript source for its provided and required namespaces.
   * @param file The JavaScript file to scan.
   */
  public Source(File file) {
    path = file.getPath();
    provides = new HashSet<String>();
    requires = new HashSet<String>();

    List<String> source = IO.getFileContents(file);

    // Scan the source.
    for (String line : source) {
      // Find Provides.
      Matcher m = PROVIDE_REGEX.matcher(line);
      if (m.matches())
        provides.add(m.group(1));
      // Find Requires.
      m = REQUIRES_REGEX.matcher(line);
      if (m.matches())
        requires.add(m.group(1));
    }
  }

  @Override
  public String toString() {
    return path;
  }

  public String getProvides() {
    return toString(provides);
  }

  public String getRequires() {
    return toString(requires);
  }

  /**
   * Convert each Set to to a closure defined set.
   * Coded used from AbstractCollection.toString().
   * 
   * @param set The set to process.
   * @return String representation in form of ['1', '2']
   */
  private String toString(Set<String> set) {
    Iterator<String> i = set.iterator();
    if (! i.hasNext())
      return "[]";

    StringBuilder sb = new StringBuilder();
    sb.append('[');
    for (;;) {
      String e = i.next();
      sb.append("'" + e + "'");
      if (! i.hasNext())
        return sb.append(']').toString();
      sb.append(", ");
    }
  }
}
