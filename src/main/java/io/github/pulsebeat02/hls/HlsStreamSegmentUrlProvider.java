package io.github.pulsebeat02.hls;

import static com.sedmelluq.discord.lavaplayer.tools.io.HttpClientTools.fetchResponseLines;

import com.sedmelluq.discord.lavaplayer.container.playlists.ExtendedM3uParser.Line;
import com.sedmelluq.discord.lavaplayer.tools.io.HttpInterface;
import io.github.pulsebeat02.ChannelStreamInfo;
import io.github.pulsebeat02.hls.m3u8.M3u8StreamSegmentUrlProvider;
import java.io.IOException;
import java.util.List;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;

public class HlsStreamSegmentUrlProvider extends M3u8StreamSegmentUrlProvider {

  private final String streamListUrl;
  private volatile String segmentPlaylistUrl;

  public HlsStreamSegmentUrlProvider(final String streamListUrl, final String segmentPlaylistUrl) {
    this.streamListUrl = streamListUrl;
    this.segmentPlaylistUrl = segmentPlaylistUrl;
  }

  @Override
  protected String getQualityFromM3uDirective(final Line directiveLine) {
    return "default";
  }

  @Override
  protected String fetchSegmentPlaylistUrl(final HttpInterface httpInterface) throws IOException {

    if (this.segmentPlaylistUrl != null) {
      return this.segmentPlaylistUrl;
    }

    final HttpUriRequest request = new HttpGet(this.streamListUrl);
    final List<ChannelStreamInfo> streams =
        this.loadChannelStreamsList(fetchResponseLines(httpInterface, request, "HLS stream list"));

    if (streams.isEmpty()) {
      throw new IllegalStateException("No streams listed in HLS stream list.");
    }

    final ChannelStreamInfo stream = streams.get(0);

    this.segmentPlaylistUrl = stream.url();

    return this.segmentPlaylistUrl;
  }

  @Override
  protected HttpUriRequest createSegmentGetRequest(final String url) {
    return new HttpGet(url);
  }
}
