package io.github.pulsebeat02.hls;

import static com.sedmelluq.discord.lavaplayer.tools.io.HttpClientTools.fetchResponseLines;

import com.sedmelluq.discord.lavaplayer.container.playlists.ExtendedM3uParser;
import com.sedmelluq.discord.lavaplayer.tools.io.HttpInterface;
import io.github.pulsebeat02.utils.M3u8Utils;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.client.methods.HttpGet;

public class HlsStreamSegmentParser {

  public List<HlsStreamSegment> parseFromUrl(final HttpInterface httpInterface, final String url)
      throws IOException {
    final HttpGet get = new HttpGet(url);
    final String[] segments = fetchResponseLines(httpInterface, get, "stream segments list");
    return this.parseFromLines(url, segments);
  }

  public List<HlsStreamSegment> parseFromLines(final String url, final String[] lines) {

    final String baseUrl = M3u8Utils.retrieveBaseUrl(url);
    final List<HlsStreamSegment> segments = new ArrayList<>();

    for (final String lineText : lines) {

      final ExtendedM3uParser.Line line = ExtendedM3uParser.parseLine(lineText);
      final ExtendedM3uParser.Line segmentInfo =
          line.isDirective() && "EXTINF".equals(line.directiveName) ? line : null;

      if (!line.isData()) {
        continue;
      }

      final String data = M3u8Utils.getFullUrl(baseUrl, lineText, line);
      final HlsStreamSegment segment = M3u8Utils.getSegment(segmentInfo, data);
      segments.add(segment);
    }

    return segments;
  }
}
