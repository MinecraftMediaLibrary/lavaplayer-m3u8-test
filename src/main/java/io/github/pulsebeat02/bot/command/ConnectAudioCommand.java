package io.github.pulsebeat02.bot.command;

import io.github.pulsebeat02.bot.MediaBot;
import io.github.pulsebeat02.bot.audio.MusicManager;
import io.github.pulsebeat02.bot.locale.DiscordLocale;
import java.util.Set;
import net.dv8tion.jda.api.entities.Message;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ConnectAudioCommand extends DiscordBaseCommand {

  public ConnectAudioCommand(@NotNull final MediaBot bot) {
    super(bot, "connect", Set.of());
  }

  @Override
  public boolean execute(@NotNull final Message executor, final String @Nullable [] arguments) {
    this.joinVoiceChannel();
    executor.getChannel().sendMessageEmbeds(DiscordLocale.CONNECT_VC_EMBED.build()).queue();
    return true;
  }

  private void joinVoiceChannel() {
    final MusicManager manager = this.getBot().getMusicManager();
    manager.joinVoiceChannel();
  }
}