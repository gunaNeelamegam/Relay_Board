/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author user
 */
import org.pjsip.pjsua2.LogEntry;
import org.pjsip.pjsua2.LogWriter;

public class MyLogWriter extends LogWriter {
  public void write(LogEntry paramLogEntry) {
    System.out.println(paramLogEntry.getMsg());
  }
}
