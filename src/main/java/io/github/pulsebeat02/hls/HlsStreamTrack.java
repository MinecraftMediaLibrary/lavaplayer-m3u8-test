package io.github.pulsebeat02.hls;

import static com.sedmelluq.discord.lavaplayer.container.mpegts.MpegTsElementaryInputStream.ADTS_ELEMENTARY_STREAM;

import com.sedmelluq.discord.lavaplayer.container.adts.AdtsAudioTrack;
import com.sedmelluq.discord.lavaplayer.container.mpegts.MpegTsElementaryInputStream;
import com.sedmelluq.discord.lavaplayer.container.mpegts.PesPacketInputStream;
import com.sedmelluq.discord.lavaplayer.tools.io.ChainedInputStream;
import com.sedmelluq.discord.lavaplayer.tools.io.ChainedInputStream.Provider;
import com.sedmelluq.discord.lavaplayer.tools.io.HttpInterface;
import com.sedmelluq.discord.lavaplayer.tools.io.HttpInterfaceManager;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import com.sedmelluq.discord.lavaplayer.track.DelegatedAudioTrack;
import com.sedmelluq.discord.lavaplayer.track.playback.LocalAudioTrackExecutor;
import java.io.InputStream;

public class HlsStreamTrack extends DelegatedAudioTrack {

  private final HlsStreamSegmentUrlProvider segmentUrlProvider;
  private final HttpInterfaceManager httpInterfaceManager;

  public HlsStreamTrack(
      final AudioTrackInfo trackInfo,
      final String streamUrl,
      final HttpInterfaceManager httpInterfaceManager,
      final boolean isInnerUrl) {
    super(trackInfo);
    this.segmentUrlProvider =
        isInnerUrl
            ? new HlsStreamSegmentUrlProvider(null, streamUrl)
            : new HlsStreamSegmentUrlProvider(streamUrl, null);
    this.httpInterfaceManager = httpInterfaceManager;
  }

  protected HlsStreamSegmentUrlProvider getSegmentUrlProvider() {
    return this.segmentUrlProvider;
  }

  protected HttpInterface getHttpInterface() {
    return this.httpInterfaceManager.getInterface();
  }

  protected void processJoinedStream(
      final LocalAudioTrackExecutor localExecutor, final InputStream stream) throws Exception {
    final MpegTsElementaryInputStream elementaryInputStream =
        new MpegTsElementaryInputStream(stream, ADTS_ELEMENTARY_STREAM);
    final PesPacketInputStream pesPacketInputStream =
        new PesPacketInputStream(elementaryInputStream);
    final AdtsAudioTrack track = new AdtsAudioTrack(this.trackInfo, pesPacketInputStream);
    this.processDelegate(track, localExecutor);
  }

  @Override
  public void process(final LocalAudioTrackExecutor localExecutor) throws Exception {
    try (final HttpInterface httpInterface = this.getHttpInterface()) {
      final Provider provider =
          () -> this.getSegmentUrlProvider().getNextSegmentStream(httpInterface);
      try (final ChainedInputStream chainedInputStream = new ChainedInputStream(provider)) {
        this.processJoinedStream(localExecutor, chainedInputStream);
      }
    }
  }
}
