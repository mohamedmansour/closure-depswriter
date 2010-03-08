package com.closure.utils;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Test;

public class RelativePathTest {

  @Test
  public void sameBaseTest() {
    String[][] testcase = new String[][] {
      {IO.getRelativePath(new File("/foo/bar/test.js"), new File("/foo/")), "bar/test.js" },
      {IO.getRelativePath(new File("/foo/bar/test/test.js"), new File("/foo/")), "bar/test/test.js" },
      {IO.getRelativePath(new File("/foo/test.js"), new File("/foo/")), "test.js" },
      {IO.getRelativePath(new File("C:\\path\\to\\my\\foo\\bar\\test.js"), new File("C:\\path\\to\\my\\foo\\")), "bar/test.js" },
    };

    for (String[] test : testcase) {
      assertEquals(test[0], test[1]);
    }
  }
  
  @Test
  public void outsideBaseTest() {
    String[][] testcase = new String[][] {
      {IO.getRelativePath(new File("/foo/bar/test.js"), new File("/bar/foo/")), "../../foo/bar/test.js" },
      {IO.getRelativePath(new File("/foo/bar/test.js"), new File("/foo/foo/")), "../bar/test.js" },
      {IO.getRelativePath(new File("/foo/bar/test.js"), new File("/foo/foo/bar/")), "../../bar/test.js" },

    };

    for (String[] test : testcase) {
      System.out.println("actual: " + test[0] + " expected: " + test[1]);
      assertEquals(test[0], test[1]);
    }
  }
}
