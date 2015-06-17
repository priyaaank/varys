package com.varys.commandline;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class CommandExecutor {

  private ShellCommand command;
  private CommandOutputHandler outputHandler = new DefaultCommandOutputHandler();

  public CommandExecutor(ShellCommand command) {
    this.command = command;
  }

  public CommandExecutor(ShellCommand command, CommandOutputHandler outputHandler) {
    this.command = command;
    this.outputHandler = outputHandler;
  }

  public void execute() throws IOException {
    System.out.println("Executing command: [ " + this.command.toString() +" ]");
    final Process newProcess = Runtime.getRuntime().exec(this.command.toString());

    Thread commandThread = new Thread(new Runnable() {
      @Override
      public void run() {
        CommandExecutor.this.outputHandler.handleCommandOutput(newProcess, newProcess.getInputStream());
      }
    });

    commandThread.start();
  }

  private class DefaultCommandOutputHandler implements CommandOutputHandler {

    @Override
    public void handleCommandOutput(Process commandProcess, InputStream commandInputStream) {
      BufferedReader reader = null;
      try {
        reader = new BufferedReader(new InputStreamReader(commandInputStream));
        logAllCommandOutput(reader);
      } catch (IOException ioException) {
        ioException.printStackTrace();
      } finally {
        closeReader(reader);
      }
    }

    private BufferedReader logAllCommandOutput(BufferedReader reader) throws IOException {
      String line = null;
      while ((line = reader.readLine()) != null) {
        System.out.println(line);
      }
      return reader;
    }

    private void closeReader(BufferedReader reader) {
      if(reader != null) {
        try {
          reader.close();
        } catch (IOException ioEx) {
          ioEx.printStackTrace();
        }
      }
    }
  }
}
