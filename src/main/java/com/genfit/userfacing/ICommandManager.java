package com.genfit.userfacing;

/**
 * Interface for a Command Manager.
 */
public interface ICommandManager {
  /**
   * Registers an application's commands with the command manager, allows for
   * association of commands with an application rather than just a String.
   *
   * @param app array of applications to register with this command manager
   */
  void registerApplications(I32Application... app);

  /**
   * Registers commands with the Command Manager. Ideally this would be
   * private, not allowed in Java 8.
   *
   * @param pattern String representing command pattern
   * @param c       ICommand representing a command that can be executed
   * @param app     application to register command with, allows for
   *                verification that the proper application is being used to
   *                run the command
   */
  void registerCommand(String pattern, ICommand c, I32Application app);

  /**
   * Method to check if a command is supported by the IREPL.
   *
   * @param cmd command to check
   * @return boolean indicating whether command is supported or not
   */
  boolean isCommandSupported(String cmd);

  /**
   * Returns output to a given input to the proper application.
   *
   * @param input            command string to process
   * @param formatForGUI     boolean indicating whether or not response
   *                         needs to be formatted for the web
   * @param appToExecuteWith app to run command with, must be the same
   *                         instance of the I32 application that was
   *                         previously registered with this command manager
   * @return String representing command response
   */
  String processInput(String input, boolean formatForGUI,
                      I32Application appToExecuteWith);
}
