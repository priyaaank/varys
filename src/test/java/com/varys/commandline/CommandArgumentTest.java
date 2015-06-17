package com.varys.commandline;

import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

public class CommandArgumentTest {

  @Test
  public void shouldReturnValueAsCommandArgument() {
    //when
    ShellCommand.CommandArgument newArgument = new ShellCommand.CommandArgument("bar");

    //then
    assertEquals("bar", newArgument.toString());
  }

  @Test
  public void shouldReturnKeyValueAsCommandArgumentPair() {
    //when
    ShellCommand.CommandArgument newArgument = new ShellCommand.CommandArgument("-f", "bar");

    //then
    assertEquals("-f bar", newArgument.toString());
  }

  @Test
  public void shouldReturnKeyValueWithSeparatorAsCommandArgumentPair() {
    //when
    ShellCommand.CommandArgument newArgument = new ShellCommand.CommandArgument("foo", "=", "bar");

    //then
    assertEquals("foo=bar", newArgument.toString());
  }
}