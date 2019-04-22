package com.genfit.userfacing;

import java.util.Map;

/**
 * Interface for 32 applications.
 */
public interface I32Application {
  /**
   * Returns the mapping of commands for this particular application.
   *
   * @return Map of String to Commands that this application uses
   */
  Map<String, ICommand> getAppCommands();

  /**
   * Checks if a command is supported by the current application.
   *
   * @param cmd checks if command is supported
   * @return boolean indicating whether the above is true
   */
  boolean isCommandSupported(String cmd);
}
