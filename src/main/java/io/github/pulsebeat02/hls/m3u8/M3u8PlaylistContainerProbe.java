package io.github.pulsebeat02.hls.m3u8;

import com.sedmelluq.discord.lavaplayer.container.playlists.M3uPlaylistContainerProbe;
import com.sedmelluq.discord.lavaplayer.tools.io.HttpClientTools;
import com.sedmelluq.discord.lavaplayer.tools.io.HttpClientTools.NoRedirectsStrategy;
import com.sedmelluq.discord.lavaplayer.tools.io.HttpInterfaceManager;
import com.sedmelluq.discord.lavaplayer.tools.io.SeekableInputStream;
import com.sedmelluq.discord.lavaplayer.tools.io.ThreadLocalHttpInterfaceManager;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import io.github.pulsebeat02.hls.HlsStreamTrack;
import org.apache.http.impl.client.HttpClientBuilder;

public class M3u8PlaylistContainerProbe extends M3uPlaylistContainerProbe {

  private static final String TYPE_HLS_OUTER = "hls-outer";
  private static final String TYPE_HLS_INNER = "hls-inner";

  private final HttpInterfaceManager http;

  public M3u8PlaylistContainerProbe() {

    final NoRedirectsStrategy strategy = new NoRedirectsStrategy();
    final HttpClientBuilder builder = HttpClientTools.createSharedCookiesHttpBuilder();
    builder.setRedirectStrategy(strategy);

    this.http =
        new ThreadLocalHttpInterfaceManager(builder, HttpClientTools.DEFAULT_REQUEST_CONFIG);
  }

  @Override
  public AudioTrack createTrack(
      final String parameters,
      final AudioTrackInfo trackInfo,
      final SeekableInputStream inputStream) {
    return switch (parameters) {
      case TYPE_HLS_INNER -> new HlsStreamTrack(trackInfo, trackInfo.identifier, this.http, true);
      case TYPE_HLS_OUTER -> new HlsStreamTrack(trackInfo, trackInfo.identifier, this.http, false);
      default -> null;
    };
  }
}
