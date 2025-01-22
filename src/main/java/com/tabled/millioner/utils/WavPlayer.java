
package com.tabled.millioner.utils;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Utility class for playing WAV audio files.
 * The `WavPlayer` class provides functionality to play WAV audio files using the `javax.sound.sampled` package.
 * It handles file validation, audio stream processing, and playback lifecycle management.
 */
public class WavPlayer {

    private static final Logger logger = LogManager.getLogger(WavPlayer.class);

    /**
     * Plays the specified WAV audio file.

     * This method validates the file, initializes the audio stream, and plays the audio file.
     * It also ensures that the audio playback lifecycle is properly managed, including closing the
     * audio resources after playback is complete.
     *
     * @param path The path to the WAV file to be played.
     */
    public void play(String path) {
        try {
            File soundFile = new File(path);
            if (!soundFile.exists() || !soundFile.canRead()) {
                logger.error("File not found or not readable: {}", path);
                return;
            }

            AudioInputStream audioStream = AudioSystem.getAudioInputStream(soundFile);

            AudioFormat format = audioStream.getFormat();
            DataLine.Info info = new DataLine.Info(Clip.class, format);

            if (!AudioSystem.isLineSupported(info)) {
                logger.error("Audio line not supported.");
                return;
            }

            Clip audioClip = (Clip) AudioSystem.getLine(info);
            audioClip.open(audioStream);
            audioClip.start();

            logger.info("Playing audio file: {}", path);

            audioClip.addLineListener(event -> {
                if (event.getType() == LineEvent.Type.STOP) {
                    audioClip.close();
                }
                logger.info("Playback completed for file: {}", path);
            });

            Thread.sleep(audioClip.getMicrosecondLength() / 1000);
        } catch (UnsupportedAudioFileException e) {
            logger.error("Unsupported audio format for file: {}", path, e);
        } catch (IOException e) {
            logger.error("Error reading the audio file: {}", path, e);
        } catch (LineUnavailableException e) {
            logger.error("Audio line unavailable for file: {}", path, e);
        } catch (InterruptedException e) {
            logger.error("Playback interrupted for file: {}", path, e);
            Thread.currentThread().interrupt();
        }
    }
}