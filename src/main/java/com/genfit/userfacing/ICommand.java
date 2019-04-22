package com.genfit.userfacing;

/**
 * Interface for a command to be executed.
 */
public interface ICommand {

  /**
   * Executes current command.
   *
   * @param args         String containing space separated command line
   *                     arguments
   * @param formatForGUI boolean indicating whether or not response
   *                     needs to be formatted for the web
   * @return String representing output of command
   */
  String execute(String args, boolean formatForGUI);
}
