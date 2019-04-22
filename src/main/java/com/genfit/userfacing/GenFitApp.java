package com.genfit.userfacing;

import java.util.HashMap;
import java.util.Map;

public class GenFitApp implements I32Application {
  private Map<String, ICommand> appCommands;

  public GenFitApp() {
    this.appCommands = new HashMap<>();
  }

  @Override
  public Map<String, ICommand> getAppCommands() {
    return appCommands;
  }

  @Override
  public boolean isCommandSupported(String cmd) {
    return appCommands.containsKey(cmd);
  }
}
