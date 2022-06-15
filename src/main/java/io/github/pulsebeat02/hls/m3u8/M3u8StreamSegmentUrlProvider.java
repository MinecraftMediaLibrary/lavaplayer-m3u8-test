package io.github.pulsebeat02.hls.m3u8;

import static com.sedmelluq.discord.lavaplayer.tools.FriendlyException.Severity.SUSPICIOUS;
import static com.sedmelluq.discord.lavaplayer.tools.io.HttpClientTools.fetchResponseLines;

import com.sedmelluq.discord.lavaplayer.container.playlists.ExtendedM3uParser;
import com.sedmelluq.discord.lavaplayer.container.playlists.ExtendedM3uParser.Line;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.tools.io.HttpClientTools;
import com.sedmelluq.discord.lavaplayer.tools.io.HttpInterface;
import io.github.pulsebeat02.ChannelStreamInfo;
import io.github.pulsebeat02.SegmentInfo;
import io.github.pulsebeat02.utils.M3u8Utils;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;

public abstract class M3u8StreamSegmentUrlProvider {

  private static final long SEGMENT_WAIT_STEP_MS = 200;

  public M3u8StreamSegmentUrlProvider() {}

  private SegmentInfo lastSegment;

  private String getNextSegmentUrl(final HttpInterface httpInterface) {

    try {

      final String playlist = this.fetchSegmentPlaylistUrl(httpInterface);
      if (playlist == null) {
        return null;
      }

      final long start = Instant.now().toEpochMilli();
      final SegmentInfo next = this.getNextSegment(httpInterface, playlist, start);
      if (next == null) {
        return null;
      }

      this.lastSegment = next;

      return this.createSegmentUrl(playlist, this.lastSegment.url());

    } catch (final IOException e) {
      throw new FriendlyException("Failed to get next part of the stream.", SUSPICIOUS, e);
    }
  }

  private SegmentInfo getNextSegment(
      final HttpInterface httpInterface, final String playlist, final long startTime)
      throws IOException {
    while (true) {

      final List<SegmentInfo> segments = this.loadStreamSegmentsList(httpInterface, playlist);
      final SegmentInfo next = this.chooseNextSegment(segments, this.lastSegment);
      if (!this.shouldWaitForSegment(startTime, segments)) {
        return next;
      }

      if (next != null) {
        return next;
      }

      this.sleepDelay();
    }
  }

  private void sleepDelay() {
    try {
      Thread.sleep(SEGMENT_WAIT_STEP_MS);
    } catch (final InterruptedException e) {
      throw new AssertionError(e);
    }
  }

  public InputStream getNextSegmentStream(final HttpInterface httpInterface) {

    final String url = this.getNextSegmentUrl(httpInterface);
    if (url == null) {
      return null;
    }

    try (final CloseableHttpResponse response =
        httpInterface.execute(this.createSegmentGetRequest(url))) {
      HttpClientTools.assertSuccessWithContent(response, "segment data URL");
      return response.getEntity().getContent();
    } catch (final IOException e) {
      throw new RuntimeException(e);
    }
  }

  protected List<ChannelStreamInfo> loadChannelStreamsList(final String[] lines) {

    final List<ChannelStreamInfo> streams = new ArrayList<>();
    for (final String lineText : lines) {

      final ExtendedM3uParser.Line line = ExtendedM3uParser.parseLine(lineText);

      if (!line.isData()) {
        continue;
      }

      final ExtendedM3uParser.Line info = M3u8Utils.isExtStreamInfo(line) ? line : null;
      if (info == null) {
        continue;
      }

      final String quality = this.getQualityFromM3uDirective(info);
      if (quality == null) {
        continue;
      }

      final ChannelStreamInfo stream = this.getStreamInfo(line, quality);
      streams.add(stream);
    }

    return streams;
  }

  private ChannelStreamInfo getStreamInfo(final Line line, final String quality) {
    return new ChannelStreamInfo(quality, line.lineData);
  }

  private List<SegmentInfo> loadStreamSegmentsList(
      final HttpInterface httpInterface, final String streamSegmentPlaylistUrl) throws IOException {

    final List<SegmentInfo> segments = new ArrayList<>();
    final HttpGet get = new HttpGet(streamSegmentPlaylistUrl);
    final String[] lines = fetchResponseLines(httpInterface, get, "stream segments list");

    for (final String lineText : lines) {

      final ExtendedM3uParser.Line line = ExtendedM3uParser.parseLine(lineText);
      final ExtendedM3uParser.Line segmentInfo =
          line.isDirective() && "EXTINF".equals(line.directiveName) ? line : null;

      if (!line.isData()) {
        continue;
      }

      final SegmentInfo info = M3u8Utils.getSegmentInfo(line, segmentInfo);
      segments.add(info);
    }

    return segments;
  }

  private SegmentInfo chooseNextSegment(
      final List<SegmentInfo> segments, final SegmentInfo lastSegment) {

    SegmentInfo selected = null;
    for (int i = segments.size() - 1; i >= 0; i--) {
      final SegmentInfo current = segments.get(i);
      if (lastSegment != null && current.url().equals(lastSegment.url())) {
        break;
      }
      selected = current;
    }

    return selected;
  }

  private boolean shouldWaitForSegment(final long startTime, final List<SegmentInfo> segments) {

    if (segments.isEmpty()) {
      return false;
    }

    final SegmentInfo sampleSegment = segments.get(0);
    final Long duration = sampleSegment.duration();
    if (duration != null) {
      return System.currentTimeMillis() - startTime < duration;
    }

    return false;
  }

  private String createSegmentUrl(final String playlistUrl, final String segmentName) {
    return URI.create(playlistUrl).resolve(segmentName).toString();
  }

  protected abstract String getQualityFromM3uDirective(final ExtendedM3uParser.Line directiveLine);

  protected abstract String fetchSegmentPlaylistUrl(final HttpInterface httpInterface)
      throws IOException;

  protected abstract HttpUriRequest createSegmentGetRequest(final String url);
}
