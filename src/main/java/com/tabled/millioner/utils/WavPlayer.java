
package com.tabled.millioner.utils;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

/**
 * Utility class for playing WAV audio files.
 *
 * The `WavPlayer` class provides functionality to play WAV audio files using the `javax.sound.sampled` package.
 * It handles file validation, audio stream processing, and playback lifecycle management.
 */
public class WavPlayer {

    public void play(String path) {
        /**
         * Plays the specified WAV audio file.
         *
         * This method validates the file, initializes the audio stream, and plays the audio file.
         * It also ensures that the audio playback lifecycle is properly managed, including closing the
         * audio resources after playback is complete.
         *
         * @param path The path to the WAV file to be played.
         */
        try {
            File soundFile = new File(path);
            if (!soundFile.exists() || !soundFile.canRead()) {
                System.out.println("File not found or not readable: " + path);
                return;
            }

            AudioInputStream audioStream = AudioSystem.getAudioInputStream(soundFile);

            AudioFormat format = audioStream.getFormat();
            DataLine.Info info = new DataLine.Info(Clip.class, format);

            if (!AudioSystem.isLineSupported(info)) {
                System.out.println("Audio line not supported.");
                return;
            }

            Clip audioClip = (Clip) AudioSystem.getLine(info);
            audioClip.open(audioStream);
            audioClip.start();

            audioClip.addLineListener(event -> {
                if (event.getType() == LineEvent.Type.STOP) {
                    audioClip.close();
                }
            });

            Thread.sleep(audioClip.getMicrosecondLength() / 1000);
        } catch (UnsupportedAudioFileException e) {
            System.out.println("Unsupported audio format.");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Error reading the audio file.");
            e.printStackTrace();
        } catch (LineUnavailableException e) {
            System.out.println("Audio line unavailable.");
            e.printStackTrace();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}