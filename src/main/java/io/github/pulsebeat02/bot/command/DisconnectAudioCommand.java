package io.github.pulsebeat02.bot.command;

import io.github.pulsebeat02.bot.MediaBot;
import io.github.pulsebeat02.bot.audio.MusicManager;
import io.github.pulsebeat02.bot.locale.DiscordLocale;
import java.util.Set;
import net.dv8tion.jda.api.entities.Message;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DisconnectAudioCommand extends DiscordBaseCommand {

  public DisconnectAudioCommand(@NotNull final MediaBot bot) {
    super(bot, "disconnect", Set.of());
  }

  @Override
  public boolean execute(@NotNull final Message executor, final String @Nullable [] arguments) {
    this.leaveVoiceChannel();
    executor.getChannel().sendMessageEmbeds(DiscordLocale.DC_VC_EMBED.build()).queue();
    return true;
  }

  private void leaveVoiceChannel() {
    final MusicManager manager = this.getBot().getMusicManager();
    manager.leaveVoiceChannel();
  }
}