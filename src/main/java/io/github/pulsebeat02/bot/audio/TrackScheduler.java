package io.github.pulsebeat02.bot.audio;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import org.jetbrains.annotations.NotNull;

/** Stolen from itxfrosty Music bot. */
public class TrackScheduler extends AudioEventAdapter {

  private final AudioPlayer audioPlayer;
  private final MusicManager musicManager;

  public TrackScheduler(
      @NotNull final MusicManager musicManager,
      @NotNull final AudioPlayer player) {
    this.musicManager = musicManager;
    this.audioPlayer = player;
  }

  /**
   * Queues Song.
   *
   * @param track Track to Queue.
   */
  public void queueSong(@NotNull final AudioTrack track) {
    if (this.audioPlayer.getPlayingTrack() == null) {
      this.audioPlayer.playTrack(track);
    }
  }

  /** Clear's Audio Queue. */
  public void clearQueue() {
    this.audioPlayer.stopTrack();
  }

  /** Skips Song. */
  public void skip() {
    this.audioPlayer
        .getPlayingTrack()
        .setPosition(this.audioPlayer.getPlayingTrack().getDuration());
  }

  /**
   * Check's if Audio is paused.
   *
   * @return If Paused.
   */
  public boolean isPaused() {
    return this.audioPlayer.isPaused();
  }

  /**
   * Set's the Audio to paused parameter.
   *
   * @param paused Pause or not.
   */
  public void setPaused(final boolean paused) {
    this.audioPlayer.setPaused(paused);
  }

  /**
   * Set's Volume of Audio Player.
   *
   * @param volume Volume to set Audio player.
   */
  public void setVolume(final int volume) {
    this.audioPlayer.setVolume(volume);
  }

  public @NotNull AudioPlayer getAudioPlayer() {
    return this.audioPlayer;
  }

  public @NotNull MusicManager getMusicManager() {
    return this.musicManager;
  }
}