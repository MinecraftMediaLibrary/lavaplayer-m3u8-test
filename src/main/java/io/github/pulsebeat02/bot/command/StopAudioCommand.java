package io.github.pulsebeat02.bot.command;

import io.github.pulsebeat02.bot.MediaBot;
import io.github.pulsebeat02.bot.audio.MusicManager;
import io.github.pulsebeat02.bot.locale.DiscordLocale;
import java.util.Set;
import net.dv8tion.jda.api.entities.Message;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class StopAudioCommand extends DiscordBaseCommand {

  public StopAudioCommand(@NotNull final MediaBot bot) {
    super(bot, "stop", Set.of());
  }

  @Override
  public boolean execute(@NotNull final Message executor, final String @Nullable [] arguments) {
    this.stopAudio();
    executor.getChannel().sendMessageEmbeds(DiscordLocale.PAUSE_AUDIO.build()).queue();
    return true;
  }

  private void stopAudio() {
    final MusicManager manager = this.getBot().getMusicManager();
    manager.getPlayerManager().shutdown();
  }
}
