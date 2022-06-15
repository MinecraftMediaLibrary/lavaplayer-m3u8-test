package io.github.pulsebeat02.bot.command;

import io.github.pulsebeat02.bot.MediaBot;
import java.util.Collection;
import org.jetbrains.annotations.NotNull;

public abstract class DiscordBaseCommand implements DiscordCommand {

  private final MediaBot bot;
  private final String command;
  private final Collection<DiscordBaseCommand> subcommands;

  public DiscordBaseCommand(
      @NotNull final MediaBot bot,
      @NotNull final String command,
      @NotNull final Collection<DiscordBaseCommand> subcommands) {
    this.bot = bot;
    this.command = command;
    this.subcommands = subcommands;
  }

  @Override
  public @NotNull String getCommand() {
    return this.command;
  }

  @Override
  public @NotNull Collection<DiscordBaseCommand> getArguments() {
    return this.subcommands;
  }

  @Override
  public @NotNull MediaBot getBot() {
    return this.bot;
  }
}
