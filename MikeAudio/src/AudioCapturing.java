

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;
public class AudioCapturing {

	private final int AUDIO_SAMPLE_RATE = 32000;
	 private final int AUDIO_SAMPLE_SIZE = 2;
	 private final int AUDIO_SAMPLE_SIZE_IN_BITS = AUDIO_SAMPLE_SIZE * Byte.SIZE;
	 private final int AUDIO_CHANNELS = 1;
	 private final boolean AUDIO_SIGNED = true;
	 private final boolean AUDIO_BIG_ENDIAN = false;
	 //
	 private final int AUDIO_FRAME_SAMPLES_COUNT = 256;
	 private final int AUDIO_FRAME_RATE = AUDIO_SAMPLE_RATE / AUDIO_FRAME_SAMPLES_COUNT;
	 private final int AUDIO_FRAME_SIZE = AUDIO_SAMPLE_SIZE * AUDIO_FRAME_SAMPLES_COUNT;
	 private final int AUDIO_BUFFER_SIZE = AUDIO_FRAME_SIZE * AUDIO_FRAME_RATE;
	 //
	 private final AudioFormat AUDIO_FORMAT = new AudioFormat
	 ( AUDIO_SAMPLE_RATE
	 , AUDIO_SAMPLE_SIZE_IN_BITS
	 , AUDIO_CHANNELS
	 , AUDIO_SIGNED
	 , AUDIO_BIG_ENDIAN
	 );
	 
	 private void record(byte[] aSound) throws LineUnavailableException {
	  int bytesRead = 0;
	  int frameBytesRead = 0;
	  //
	  TargetDataLine microphone = AudioSystem.getTargetDataLine(AUDIO_FORMAT);
	  microphone.open(AUDIO_FORMAT);
	  System.out.println("record...");
	  microphone.start();
	  for (;;) {
	   frameBytesRead = 0;
	   for (;;) {
	    frameBytesRead += microphone.read
	    ( aSound
	    , bytesRead
	    , AUDIO_FRAME_SIZE - frameBytesRead
	    );
	    if (frameBytesRead >= AUDIO_FRAME_SIZE) break;
	   }
	   // process frame if need
	   bytesRead += AUDIO_FRAME_SIZE;
	   if (bytesRead >= AUDIO_BUFFER_SIZE) break;
	  }
	  microphone.stop();
	  microphone.flush();
	  microphone.close();
	 }
	 
	 private void play(byte[] aSound) throws LineUnavailableException {
	  SourceDataLine speaker = AudioSystem.getSourceDataLine(AUDIO_FORMAT);
	  speaker.open();
	  System.out.println("play...");
	  speaker.start();
	  speaker.write(aSound, 0, AUDIO_BUFFER_SIZE);
	  speaker.drain();
	  speaker.stop();
	  speaker.close();
	 }
	 
	 public void run() throws Exception {
	  byte[] sound = new byte[AUDIO_BUFFER_SIZE];
	  record(sound);
	  play(sound);
	 }
	 
	 public static void main(String[] args) throws Exception {
	  new AudioCapturing().run();
	  System.out.println("done.");
	 }

	}
	

