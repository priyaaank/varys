package com.varys.commandline;

import java.io.InputStream;

public interface CommandOutputHandler {

  void handleCommandOutput(Process commandProcess, InputStream commandInputStream);

}
