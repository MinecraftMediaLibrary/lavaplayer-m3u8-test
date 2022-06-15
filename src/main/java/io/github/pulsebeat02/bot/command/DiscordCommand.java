package io.github.pulsebeat02.bot.command;

import io.github.pulsebeat02.bot.MediaBot;
import java.util.Collection;
import net.dv8tion.jda.api.entities.Message;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface DiscordCommand {

  boolean execute(@NotNull final Message executor, final String @Nullable [] arguments);

  @NotNull
  String getCommand();

  @NotNull
  Collection<DiscordBaseCommand> getArguments();

  @NotNull
  MediaBot getBot();
}