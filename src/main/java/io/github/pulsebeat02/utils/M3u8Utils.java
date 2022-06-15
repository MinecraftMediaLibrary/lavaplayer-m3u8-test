package io.github.pulsebeat02.utils;

import com.sedmelluq.discord.lavaplayer.container.playlists.ExtendedM3uParser.Line;
import io.github.pulsebeat02.hls.HlsStreamSegment;
import io.github.pulsebeat02.SegmentInfo;

public final class M3u8Utils {

  private M3u8Utils() {}

  public static String retrieveBaseUrl(final String url) {
    final int index = url.lastIndexOf('/');
    if (index > 0) {
      return url.substring(0, index);
    }
    return "/";
  }

  public static String getFullUrl(final String baseUrl, final String lineText, final Line line) {
    String data = line.lineData;
    if (!M3u8Utils.baseIncluded(baseUrl, lineText)) {
      data = baseUrl + "/" + data;
    }
    return data;
  }

  public static HlsStreamSegment getSegment(final Line segmentInfo, final String data) {
    final boolean valid = isValidDuration(segmentInfo);
    if (valid) {
      final String[] fields = segmentInfo.extraData.split(",", 2);
      final Long duration = DurationUtils.parseSecondDuration(fields[0]);
      return new HlsStreamSegment(data, duration, fields[1]);
    } else {
      return new HlsStreamSegment(data, null, null);
    }
  }

  public static SegmentInfo getSegmentInfo(final Line line, final Line segmentInfo) {
    final boolean valid = isValidDuration(segmentInfo);
    if (valid) {
      final String[] fields = segmentInfo.extraData.split(",", 2);
      final Long duration = DurationUtils.parseSecondDuration(fields[0]);
      return new SegmentInfo(line.lineData, duration, fields[1]);
    } else {
      return new SegmentInfo(line.lineData, null, null);
    }
  }

  public static boolean isExtStreamInfo(final Line line) {
    return line.isDirective() && "EXT-X-STREAM-INF".equals(line.directiveName);
  }

  private static boolean baseIncluded(final String base, final String url) {
    return url.startsWith(base);
  }

  private static boolean isValidDuration(final Line segmentInfo) {
    return segmentInfo != null && segmentInfo.extraData.contains(",");
  }
}
