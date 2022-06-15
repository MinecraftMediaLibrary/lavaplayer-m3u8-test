package io.github.pulsebeat02.bot.locale;

import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import java.text.SimpleDateFormat;
import java.util.Date;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.jetbrains.annotations.NotNull;

public interface DiscordLocale {

  SimpleDateFormat HOURS_MINUTES_SECONDS = new SimpleDateFormat("mm:ss:SSS");

  NullComponent ERR_PERMS =
      () ->
          builder()
              .setTitle("Not Enough Permissions")
              .setDescription(
                  "You must have administrator permissions or the DJ role to execute this command!")
              .build();

  NullComponent CONNECT_VC_EMBED =
      () ->
          builder()
              .setTitle("Audio Voice Channel Connection")
              .setDescription("Connected to voice channel!")
              .build();

  NullComponent DC_VC_EMBED =
      () ->
          builder()
              .setTitle("Audio Voice Channel Connection")
              .setDescription("Left voice channel!")
              .build();

  NullComponent ERR_INVALID_MRL =
      () ->
          builder()
              .setTitle("User Error")
              .setDescription("Invalid arguments! Specify a media source argument to play.")
              .build();

  NullComponent ERR_LAVAPLAYER =
      () ->
          builder()
              .setTitle("Severe Player Error Occurred!")
              .setDescription(
                  "An error occurred! Check console for possible exceptions or warnings.")
              .build();

  NullComponent PAUSE_AUDIO =
      () -> builder().setTitle("Audio Stop").setDescription("Stopped Audio!").build();

  UniComponent<AudioTrackInfo> LOADED_TRACK =
      (info) ->
          builder()
              .setTitle(info.title, info.uri)
              .addField("Author", info.author, false)
              .addField(
                  "Playtime Length", HOURS_MINUTES_SECONDS.format(new Date(info.length)), false)
              .addField("Stream", info.isStream ? "Yes" : "No", false)
              .build();

  UniComponent<String> ERR_INVALID_TRACK =
      (url) ->
          builder()
              .setTitle("Media Error")
              .setDescription("Could not find song %s!".formatted(url))
              .build();

  TriComponent<Long, AudioPlaylist, String> LOADED_PLAYLIST =
      (ms, audioPlaylist, url) ->
          builder()
              .setTitle(audioPlaylist.getName(), url)
              .addField("Playtime Length", HOURS_MINUTES_SECONDS.format(new Date(ms)), false)
              .build();

  static @NotNull EmbedBuilder builder() {
    return new EmbedBuilder();
  }

  @FunctionalInterface
  interface NullComponent {

    MessageEmbed build();
  }

  @FunctionalInterface
  interface UniComponent<A0> {

    MessageEmbed build(A0 arg0);
  }

  @FunctionalInterface
  interface BiComponent<A0, A1> {

    MessageEmbed build(A0 arg0, A1 arg1);
  }

  @FunctionalInterface
  interface TriComponent<A0, A1, A2> {

    MessageEmbed build(A0 arg0, A1 arg1, A2 arg2);
  }

  @FunctionalInterface
  interface QuadComponent<A0, A1, A2, A3> {

    MessageEmbed build(A0 arg0, A1 arg1, A2 arg2, A3 arg3);
  }

  @FunctionalInterface
  interface PentaComponent<A0, A1, A2, A3, A4> {

    MessageEmbed build(A0 arg0, A1 arg1, A2 arg2, A3 arg3, A4 arg4);
  }

  @FunctionalInterface
  interface HexaComponent<A0, A1, A2, A3, A4, A5> {

    MessageEmbed build(A0 arg0, A1 arg1, A2 arg2, A3 arg3, A4 arg4, A5 arg5);
  }
}