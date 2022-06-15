package io.github.pulsebeat02.reflect;

import com.sedmelluq.discord.lavaplayer.container.MediaContainerProbe;
import com.sedmelluq.discord.lavaplayer.container.MediaContainerRegistry;
import com.sedmelluq.discord.lavaplayer.container.adts.AdtsContainerProbe;
import com.sedmelluq.discord.lavaplayer.container.flac.FlacContainerProbe;
import com.sedmelluq.discord.lavaplayer.container.matroska.MatroskaContainerProbe;
import com.sedmelluq.discord.lavaplayer.container.mp3.Mp3ContainerProbe;
import com.sedmelluq.discord.lavaplayer.container.mpeg.MpegContainerProbe;
import com.sedmelluq.discord.lavaplayer.container.mpegts.MpegAdtsContainerProbe;
import com.sedmelluq.discord.lavaplayer.container.ogg.OggContainerProbe;
import com.sedmelluq.discord.lavaplayer.container.playlists.PlainPlaylistContainerProbe;
import com.sedmelluq.discord.lavaplayer.container.playlists.PlsPlaylistContainerProbe;
import com.sedmelluq.discord.lavaplayer.container.wav.WavContainerProbe;
import io.github.pulsebeat02.hls.m3u8.M3u8PlaylistContainerProbe;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Set;

public final class MediaContainerRegistryEditor {

  private static final Field DEFAULT_REGISTRY_FIELD;

  static {
    try {
      DEFAULT_REGISTRY_FIELD = MediaContainerRegistry.class.getField("DEFAULT_REGISTRY");
      DEFAULT_REGISTRY_FIELD.setAccessible(true);
    } catch (final NoSuchFieldException e) {
      throw new AssertionError(e);
    }
  }

  public MediaContainerRegistryEditor() {
  }

  public void modifyField() {
    try {

      final MediaContainerRegistry registry =
          (MediaContainerRegistry) DEFAULT_REGISTRY_FIELD.get(null);

      final Set<MediaContainerProbe> modified =
          Set.of(
              new WavContainerProbe(),
              new MatroskaContainerProbe(),
              new MpegContainerProbe(),
              new FlacContainerProbe(),
              new OggContainerProbe(),
              new M3u8PlaylistContainerProbe(),
              new PlsPlaylistContainerProbe(),
              new PlainPlaylistContainerProbe(),
              new Mp3ContainerProbe(),
              new AdtsContainerProbe(),
              new MpegAdtsContainerProbe());

      final List<MediaContainerProbe> probes = registry.getAll();
      probes.clear();
      probes.addAll(modified);

    } catch (final IllegalAccessException e) {
      e.printStackTrace();
    }
  }
}
