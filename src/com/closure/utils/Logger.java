package com.closure.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Simple logging utility.
 * @author mmansour
 */
public class Logger {
  
  public enum Level {
    INFO,
    ERROR,
    DEBUG,
  }

  public static void info(String log) {
    log(log, Level.INFO);
  }

  public static void debug(String log) {
    log(log, Level.DEBUG);
  }
  
  public static void error(String log) {
    log(log, Level.ERROR);
  }
  
  public static void error(String log, Throwable throwable) {
    log(log, Level.ERROR);
    throwable.printStackTrace();
  }

  public static void log(String log, Level level) {
    if (level == Level.INFO) {
      System.out.println(log);
    } else {
      System.out.println(new SimpleDateFormat("HH:mm").format(Calendar.getInstance().getTime()) + " - " + level + " - " + log);
    }
  }
}
