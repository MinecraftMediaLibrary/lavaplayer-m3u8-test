package io.github.pulsebeat02.bot;

import io.github.pulsebeat02.bot.command.ConnectAudioCommand;
import io.github.pulsebeat02.bot.command.DisconnectAudioCommand;
import io.github.pulsebeat02.bot.command.DiscordBaseCommand;
import io.github.pulsebeat02.bot.command.PlayAudioCommand;
import io.github.pulsebeat02.bot.command.StopAudioCommand;
import io.github.pulsebeat02.bot.locale.DiscordLocale;
import java.util.Arrays;
import java.util.Map;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.SubscribeEvent;
import org.jetbrains.annotations.NotNull;

public class MediaCommandListener {

  private final MediaBot bot;
  private final Map<String, DiscordBaseCommand> commands;

  public MediaCommandListener(@NotNull final MediaBot bot) {
    this.bot = bot;
    this.commands =
        Map.of(
            "connect", new ConnectAudioCommand(bot),
            "disconnect", new DisconnectAudioCommand(bot),
            "play", new PlayAudioCommand(bot),
            "stop", new StopAudioCommand(bot));
  }

  @SubscribeEvent
  public void onMessageReceivedEvent(@NotNull final MessageReceivedEvent event) {
    final String message = event.getMessage().getContentRaw();
    final String prefix = MediaBot.PREFIX;
    if (message.startsWith(prefix)) {
      final Message msg = event.getMessage();
      if (!this.canExecuteCommand(event.getAuthor())) {
        msg.getChannel().sendMessageEmbeds(DiscordLocale.ERR_PERMS.build()).queue();
        return;
      }
      this.executeCommand(message, prefix, msg);
    }
  }

  private void executeCommand(
      @NotNull final String message, @NotNull final String prefix, @NotNull final Message msg) {
    final String[] content = message.substring(prefix.length()).split(" ");
    final DiscordBaseCommand command = this.commands.get(content[0]);
    if (command != null) {
      command.execute(msg, Arrays.copyOfRange(content, 1, content.length));
    }
  }

  private boolean canExecuteCommand(@NotNull final User user) {
    final Member member = this.bot.getGuild().getMember(user);
    if (member != null) {
      return member.isOwner()
          || member.hasPermission(Permission.ADMINISTRATOR)
          || member.getRoles().stream().anyMatch(role -> role.getName().equalsIgnoreCase("DJ"));
    }
    return false;
  }

  public @NotNull MediaBot getBot() {
    return this.bot;
  }
}
