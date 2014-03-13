import org.puredata.core.*;
import org.puredata.core.utils.*;
import java.io.*;

public class NephosAudio {

	private static final String TAG = "NephosWeatherAudio";
	private PdDispatcher dispatcher;

	public void onCreate(){
		try{ 
			// initPd();
			loadPatch();
		} catch (IOException e){
			System.out.println(TAG + e.toString());
		}
		catch (InterruptedException e){
			System.out.println(TAG + e.toString());
		}
	}


	/*private void initPd() throws IOException{
		//int sampleRate = 44100;
		//PdAudio.initAudio(sampleRate, 0, 2, 8, true);

		dispatcher = new PdDispatcher();
		PdBase.setReceiver(dispatcher);
	}*/


	/*public class PdAudio{
		static void initAudio(int sampleRate, int inChannels, int outChannels, int ticksPerBuffer, boolean restart);
		static void startAudio();
		static void stopAudio();
		static boolean isRunning();
		
	}*/

	private void loadPatch() throws InterruptedException, IOException{
		JavaSoundThread audioThread = new JavaSoundThread(44100, 2, 16);
		int patch = PdBase.openPatch("resources/audio/test.pd");
		//PdBase.openAudio(0, 2, 44100);
		//audioThread.start();
		//PdBase.startAudio();
		audioThread.start();
		Thread.sleep(5000);  // Sleep for five seconds; this is where the main application code would go in a real program.
		audioThread.interrupt();
		audioThread.join();
		PdBase.closePatch(patch);
	}

	

	private void triggerThunder(){
		PdBase.sendBang("trigger.thunder");
	}


}
