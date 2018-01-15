package App;

import org.lwjgl.openal.AL;
import org.lwjgl.openal.AL10;
import org.lwjgl.openal.ALC;
import java.util.ArrayList;
import java.util.List;
import static org.lwjgl.openal.AL10.alDeleteBuffers;
import static org.lwjgl.openal.ALC10.*;

public class AudioMaster
{
	private static List<Integer> buffers = new ArrayList<Integer>();
	private static long device;
	private static long context;

	public static void init()
	{
		final String defaultDeviceName = alcGetString(0, ALC_DEFAULT_DEVICE_SPECIFIER);
		device = alcOpenDevice(defaultDeviceName);

		int[] attributes = {0};
		context = alcCreateContext(device, attributes);
		alcMakeContextCurrent(context);

		AL.createCapabilities(ALC.createCapabilities(device));
	}

	public static void setListenerData(final float x,final float y,final float z)
	{
		AL10.alListener3f(AL10.AL_POSITION, x, y, z);
		AL10.alListener3f(AL10.AL_VELOCITY, 0, 0, 0);
	}

	public static int loadSound(final String file)
	{
		final int buffer = AL10.alGenBuffers();
		buffers.add(buffer);
		WaveData wavFile = WaveData.create(file);
		AL10.alBufferData(buffer, wavFile.format, wavFile.data, wavFile.samplerate);
		wavFile.dispose();
		return buffer;
	}

	public static void cleanUp()
	{
		for (final int buffer : buffers)
		{
			alDeleteBuffers(buffer);
		}

		//Terminate OpenAL
		alcDestroyContext(context);
		alcCloseDevice(device);
	}
}
