package com.genfit.userfacing;

import java.util.HashMap;
import java.util.Map;

/**
 * Class that manages the routing of commands given input.
 */
public class CommandManager implements ICommandManager {
  public static final String WRONG_APP_MESSAGE_START = "ERROR: this "
          + "application "
          + "was not"
          + " the one registered with the"
          + " command manager to run ";
  private Map<String, I32Application> commandToAppMap;
  private Map<String, ICommand> commandsMap;

  /**
   * Default constructor for CommandManager.
   */
  public CommandManager() {
    this.commandsMap = new HashMap<>();
    this.commandToAppMap = new HashMap<>();
  }

  /**
   * @param args            String containing space separated args
   * @param numArgsExpected number of arguments that are expected from the
   *                        String
   * @return array of Strings containing number of arguments expected
   * @throws IllegalArgumentException does not contain that many arguments
   */
  public static String[] getArgsFromString(String args, int numArgsExpected) {
    String[] tokens = args.split("[\\s]+", numArgsExpected);
    if (tokens.length != numArgsExpected) {
      throw new IllegalStateException("ERROR: " + numArgsExpected + " "
              + "arguments not found, instead found " + tokens.length + " "
              + "arguments");
    } else {
      return tokens;
    }
  }

  /**
   * Extracts the main command from a line of user input (the first token in
   * a comma separated phrase. Throws exception if entered String is not
   * eligible.
   *
   * @param completeInput complete input
   * @return the main command from the input
   * @throws IllegalArgumentException if the input does not contain a main
   *                                  command
   */
  public static String getMainCmd(String completeInput)
          throws IllegalArgumentException {
    if (completeInput == null || completeInput.trim().equals("")) {
      throw new IllegalArgumentException("No main command found");
    } else {
      // split on first space(s) to get the main command and arguments
      String[] tokens = completeInput.trim().split("[\\s]+", 2);
      String mainCmd = tokens[0].trim();
      return mainCmd;
    }
  }

  /**
   * Getter method for commandToAppMap.
   *
   * @return returns commandToAppMap
   **/
  Map<String, I32Application> getCommandToAppMap() {
    return this.commandToAppMap;
  }

  @Override
  public void registerApplications(I32Application... apps) {
    if (apps != null) {
      for (int i = 0; i < apps.length; i++) {
        I32Application app = apps[i];
        if (app != null) {
          Map<String, ICommand> appCommands = app.getAppCommands();
          for (String cmdName : appCommands.keySet()) {
            this.registerCommand(cmdName, appCommands.get(cmdName), app);
          }
        }
      }
    }
  }

  @Override
  public void registerCommand(String pattern, ICommand c, I32Application app) {
    if (this.commandsMap.containsKey(pattern)) {
      throw new IllegalArgumentException("ERROR: a registered application"
              + " already contains a command for " + pattern);
    } else {
      this.commandsMap.put(pattern, c);
      this.commandToAppMap.put(pattern, app);
    }
  }

  @Override
  public boolean isCommandSupported(String cmd) {
    return this.commandsMap.containsKey(cmd);
  }

  @Override
  public String processInput(String input, boolean formatForGUI,
                             I32Application appToExecuteWith) {
    try {
      String mainCmd = this.getMainCmd(input);

      if (!this.isCommandSupported(mainCmd)) {
        return "ERROR: Unsupported command";
      } else {
        // rest of the command arguments, will pass an empty string if there
        // were no arguments
        // NB: includes leading and trailing whitespaces for the arguments
        String cmdArgs =
                input.replaceFirst("^[\\s]+", "").substring(mainCmd.length());

        I32Application appRegisteredForCommand =
                this.commandToAppMap.get(mainCmd);

        if (!appRegisteredForCommand.equals(appToExecuteWith)) {
          return WRONG_APP_MESSAGE_START + mainCmd;
        } else {
          ICommand cmdToExecute = this.commandsMap.get(mainCmd);
          if (cmdToExecute != null) {
            return cmdToExecute.execute(cmdArgs, formatForGUI);
          } else {
            return "ERROR: unsupported command";
          }
        }
      }
    } catch (IllegalArgumentException e) {
      return "ERROR" + e.getMessage();
    }
  }
}
