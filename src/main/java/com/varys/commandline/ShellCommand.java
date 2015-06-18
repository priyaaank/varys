package com.varys.commandline;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ShellCommand {

  public static final String SINGLE_SPACE = " ";
  private String commandName;
  private CommandArgument[] arguments;

  public ShellCommand(String commandName, CommandArgument...arguments) {
    this.commandName = commandName;
    this.arguments = arguments;
  }

  public void addArguments(CommandArgument...additionalArguments) {
    if(additionalArguments == null || additionalArguments.length ==0) return;
    if(arguments == null) arguments = new CommandArgument[0];
    arguments = contactArrays(arguments, additionalArguments);
  }

  @Override
  public String toString() {
    if(arguments == null || arguments.length == 0) return commandName;

    StringBuilder concatenatedArguments = new StringBuilder(commandName);
    for(CommandArgument argument : arguments) {
      concatenatedArguments.append(SINGLE_SPACE);
      concatenatedArguments.append(argument.toString());
    }

    return concatenatedArguments.toString();
  }

  private CommandArgument[] contactArrays(CommandArgument[]...arrays) {
    List<CommandArgument> concatenatedArrays = new ArrayList<CommandArgument>();
    for (CommandArgument[] array : arrays) {
      concatenatedArrays.addAll(Arrays.asList(array));
    }
    return concatenatedArrays.toArray(new CommandArgument[concatenatedArrays.size()]);
  }

  public static class CommandArgument {

    private String key;
    private String separator;
    private String value;

    public CommandArgument(String key, String value) {
      this(key, SINGLE_SPACE, value);
    }

    public CommandArgument(String key, String separator, String value) {
      this.separator = separator;
      this.key = key;
      this.value = value;
    }

    public CommandArgument(String value) {
      this(null, "", value);
    }

    @Override
    public String toString() {
      if(key == null) return value;
      if(separator == null) return key + SINGLE_SPACE + value;
      return key + separator + value;
    }
  }

}
