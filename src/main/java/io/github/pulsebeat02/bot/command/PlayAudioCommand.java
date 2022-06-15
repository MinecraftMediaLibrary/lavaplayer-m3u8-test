package io.github.pulsebeat02.bot.command;

import io.github.pulsebeat02.bot.MediaBot;
import io.github.pulsebeat02.bot.audio.MusicManager;
import io.github.pulsebeat02.bot.locale.DiscordLocale;
import java.util.Set;
import net.dv8tion.jda.api.entities.Message;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PlayAudioCommand extends DiscordBaseCommand {

  public PlayAudioCommand(@NotNull final MediaBot bot) {
    super(bot, "play", Set.of());
  }

  @Override
  public boolean execute(@NotNull final Message executor, final String @Nullable [] arguments) {
    if (this.invalidArguments(arguments)) {
      executor.getChannel().sendMessageEmbeds(DiscordLocale.ERR_INVALID_MRL.build()).queue();
      return false;
    }
    this.joinVoiceChannel(executor, arguments);
    executor.getChannel().sendMessageEmbeds(DiscordLocale.CONNECT_VC_EMBED.build()).queue();
    return true;
  }

  private void joinVoiceChannel(
      @NotNull final Message executor, final String @NotNull [] arguments) {
    final MusicManager manager = this.getBot().getMusicManager();
    manager.joinVoiceChannel();
    manager.addTrack(executor.getChannel(), arguments[0]);
  }

  private boolean invalidArguments(final String @Nullable [] arguments) {
    return arguments == null || arguments.length < 1;
  }
}