package components;
import org.lwjgl.openal.*;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;

import static org.lwjgl.openal.ALC10.*;
import static org.lwjgl.openal.AL10.*;
import static org.lwjgl.system.MemoryUtil.NULL;

// ------------------------------------------

//     ** Not really related to the game **

// ------------------------------------------



public class AudioTest {
    private long device;
    private long context;
    private int buffer;
    private int source;

    public void init() {
        device = alcOpenDevice((ByteBuffer) null);
        if (device == NULL) {
            throw new IllegalStateException("Failed to open the default OpenAL device.");
        }

        ALCCapabilities alcCapabilities = ALC.createCapabilities(device);

        context = alcCreateContext(device, (IntBuffer) null);
        if (context == NULL) {
            throw new IllegalStateException("Failed to create OpenAL context.");
        }

        alcMakeContextCurrent(context);
        AL.createCapabilities(alcCapabilities);

        try {
            buffer = loadWAV("assets/sounds/main_theme.wav");
        } catch (IOException | UnsupportedAudioFileException e) {
            e.printStackTrace();
            return;
        }

        source = alGenSources();
        alSourcei(source, AL_BUFFER, buffer);
    }

    private int loadWAV(String filePath) throws IOException, UnsupportedAudioFileException {
        // Загрузка WAV файла
        File soundFile = new File(filePath);
        AudioInputStream audioStream = AudioSystem.getAudioInputStream(soundFile);
        AudioFormat format = audioStream.getFormat();

        byte[] audioData = audioStream.readAllBytes();
        ByteBuffer bufferData = ByteBuffer.allocateDirect(audioData.length).order(ByteOrder.nativeOrder());
        bufferData.put(audioData).flip();

        // Определение формата OpenAL
        int openALFormat = -1;
        if (format.getChannels() == 1) {
            openALFormat = format.getSampleSizeInBits() == 8 ? AL_FORMAT_MONO8 : AL_FORMAT_MONO16;
        } else if (format.getChannels() == 2) {
            openALFormat = format.getSampleSizeInBits() == 8 ? AL_FORMAT_STEREO8 : AL_FORMAT_STEREO16;
        }

        // Создание буфера
        int buffer = alGenBuffers();
        alBufferData(buffer, openALFormat, bufferData, (int) format.getSampleRate());

        return buffer;
    }

    public void play() {
        alSourcePlay(source);
    }

    public void cleanup() {
        alDeleteSources(source);
        alDeleteBuffers(buffer);
        alcDestroyContext(context);
        alcCloseDevice(device);
    }

    public static void main(String[] args) {
        AudioTest test = new AudioTest();
        test.init();
        test.play();

        // Пауза для проигрывания звука
        try {
            Thread.sleep(60000000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        test.cleanup();
    }
}
