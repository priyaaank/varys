package com.varys.commandline;

public class ShellCommand {

  public static final String SINGLE_SPACE = " ";
  private String commandName;
  private CommandArgument[] arguments;

  public ShellCommand(String commandName, CommandArgument...arguments) {
    this.commandName = commandName;
    this.arguments = arguments;
  }

  @Override
  public String toString() {
    if(arguments == null || arguments.length == 0) return commandName;

    StringBuffer concatenatedArguments = new StringBuffer(commandName);
    for(CommandArgument argument : arguments) {
      concatenatedArguments.append(SINGLE_SPACE);
      concatenatedArguments.append(argument.toString());
    }

    return concatenatedArguments.toString();
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
