package com.varys.commandline;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ShellCommandTest {

  @Test
  public void shouldCreateAShellCommandWithProvidedArguments() throws Exception {
    //when
    ShellCommand.CommandArgument arguments = new ShellCommand.CommandArgument("-la");
    ShellCommand listCommand = new ShellCommand("ls", arguments);

    //then
    assertEquals("ls -la", listCommand.toString());
  }

  @Test
  public void shouldReturnAShellCommandWithoutArgumentsWhenNoneProvided() throws Exception {
    //when
    ShellCommand listCommand = new ShellCommand("ls");

    //then
    assertEquals("ls", listCommand.toString());
  }

  @Test
  public void shouldReturnAShellCommandWhenArgumentListIsEmpty() throws Exception {
    //when
    ShellCommand listCommand = new ShellCommand("ls", new ShellCommand.CommandArgument[0]);

    //then
    assertEquals("ls", listCommand.toString());
  }

  @Test
  public void shouldAppendArgumentsLater() {
    //given
    ShellCommand.CommandArgument arguments = new ShellCommand.CommandArgument("logcat");
    ShellCommand listCommand = new ShellCommand("adb", arguments);

    //when
    ShellCommand.CommandArgument additionalArgument = new ShellCommand.CommandArgument("ActivityManager",":","I");
    listCommand.addArguments(additionalArgument);

    //then
    assertEquals("adb logcat ActivityManager:I", listCommand.toString());
  }

  @Test
  public void shouldReturnSameCommandWhenAdditionalArgumentsAreEmpty() throws Exception {
  //given
    ShellCommand.CommandArgument arguments = new ShellCommand.CommandArgument("logcat");
    ShellCommand listCommand = new ShellCommand("adb", arguments);

    //when
    listCommand.addArguments(new ShellCommand.CommandArgument[0]);

    //then
    assertEquals("adb logcat", listCommand.toString());
  }

  @Test
  public void shouldReturnSameCommandWhenAdditionalArgumentsAreNull() throws Exception {
    //given
    ShellCommand.CommandArgument arguments = new ShellCommand.CommandArgument("logcat");
    ShellCommand listCommand = new ShellCommand("adb", arguments);

    //when
    listCommand.addArguments();

    //then
    assertEquals("adb logcat", listCommand.toString());
  }
}