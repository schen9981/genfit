package com.genfit.userfacing;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Class for 32 project REPL.
 */
public class MainREPL {
  private CommandManager commandManager;

  /**
   * Default constructor for MainREPL.
   */
  public MainREPL() {
    this.commandManager = new CommandManager();
  }

  /**
   * Constructor for MainREPL with various applications to register commands.
   *
   * @param apps Array of applications to register with this REPL
   */
  public MainREPL(I32Application... apps) {
    this.commandManager = new CommandManager();
    for (int i = 0; i < apps.length; i++) {
      this.commandManager.registerApplications(apps[i]);
    }
  }

  /**
   * Getter method for commandManager.
   *
   * @return edu.brown.cs.lhuang21.userfacing.CommandManager value of
   * commandManager
   **/
  CommandManager getCommandManager() {
    return this.commandManager;
  }

  /**
   * Method to start the REPL processing on the command line.
   */
  public void runREPL() {
    BufferedReader cmdReader =
            new BufferedReader(new InputStreamReader(System.in));
    String input;

    System.out.println("INFO: 32 REPL Started");

    while (true) {
      try {
        System.out.println("INFO: << Enter command below >>");
        input = cmdReader.readLine();

        // check for EOF
        if (input == null) {
          // exit REPL
          System.out.println("INFO: Goodbye :)");
          break;
        } else {
          // don't print full info on the commandline
          String res = null;
          try {
            String mainCmd = CommandManager.getMainCmd(input);

            I32Application registeredApp =
                    this.commandManager.getCommandToAppMap().get(mainCmd);

            if (registeredApp != null) {
              res = this.commandManager.processInput(input,
                      false, registeredApp);
              // only print if not empty string
              if (res.trim().length() > 0) {
                System.out.println(res);
              }
            } else {
              System.out.println("ERROR: no application found to run this "
                      + "command");
            }
          } catch (IllegalArgumentException e) {
            System.out.println("ERROR:" + e.getMessage());
          }
        }
      } catch (IOException e) {
        System.out.println("ERROR: There was a problem with IO: "
                + e.getMessage());
      }
    }
  }
}
