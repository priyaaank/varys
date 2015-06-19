package com.varys.commandline;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class CommandOutputStreamReader implements CommandOutputHandler {

  private OutputListener listener;

  public CommandOutputStreamReader() {}

  public CommandOutputStreamReader(OutputListener outputListener) {
    this.listener = outputListener;
  }

  public void registerAsListener(OutputListener listener) {
    this.listener = listener;
  }

  @Override
  public void handleCommandOutput(Process commandProcess, InputStream commandInputStream) {
    BufferedReader reader = null;
    try {
      reader = new BufferedReader(new InputStreamReader(commandInputStream));
      sendOutputToListener(reader);
    } catch (IOException ioException) {
      ioException.printStackTrace();
    } finally {
      closeReader(reader);
    }
  }

  private BufferedReader sendOutputToListener(BufferedReader reader) throws IOException {
    StringBuilder commandOutput = new StringBuilder();
    String line = null;
    while ((line = reader.readLine()) != null) {
      commandOutput.append(line);
    }
    listener.commandOutputReceived(commandOutput.toString());
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
