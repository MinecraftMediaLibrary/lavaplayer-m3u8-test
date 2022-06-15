package io.github.pulsebeat02.bot;

import static net.dv8tion.jda.api.OnlineStatus.ONLINE;
import static net.dv8tion.jda.api.requests.GatewayIntent.GUILD_MEMBERS;
import static net.dv8tion.jda.api.requests.GatewayIntent.GUILD_MESSAGES;
import static net.dv8tion.jda.api.requests.GatewayIntent.GUILD_MESSAGE_REACTIONS;

import io.github.pulsebeat02.bot.audio.MusicManager;
import io.github.pulsebeat02.reflect.MediaContainerRegistryEditor;
import javax.security.auth.login.LoginException;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.hooks.AnnotatedEventManager;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import org.jetbrains.annotations.NotNull;

public final class MediaBot {

  public static final String PREFIX;

  static {
    PREFIX = ";;";
  }

  private final JDA jda;
  private final MusicManager manager;
  private final Guild guild;
  private final VoiceChannel channel;

  public MediaBot(
      @NotNull final String token, @NotNull final String guildID, @NotNull final String vc)
      throws LoginException, InterruptedException {

    final JDABuilder builder =
        JDABuilder.createDefault(token)
            .setStatus(ONLINE)
            .setMemberCachePolicy(MemberCachePolicy.ALL)
            .enableIntents(GUILD_MEMBERS, GUILD_MESSAGES, GUILD_MESSAGE_REACTIONS)
            .setEventManager(new AnnotatedEventManager())
            .addEventListeners(new MediaCommandListener(this));

    this.jda = builder.build().awaitReady();
    this.guild = this.jda.getGuildById(guildID);
    this.channel = this.jda.getVoiceChannelById(vc);

    //this.injectM3u8Fix();
    this.manager = new MusicManager(this);
  }

  private void injectM3u8Fix() {
    new MediaContainerRegistryEditor().modifyField();
  }

  public @NotNull MusicManager getMusicManager() {
    return this.manager;
  }

  public @NotNull Guild getGuild() {
    return this.guild;
  }

  public @NotNull VoiceChannel getChannel() {
    return this.channel;
  }

  public static void main(final String[] args) throws LoginException, InterruptedException {
    new MediaBot(
        "",
        "817501569108017223",
        "817501569108017227");
  }
}
