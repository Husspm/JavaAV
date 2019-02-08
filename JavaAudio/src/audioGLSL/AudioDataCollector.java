package audioGLSL;

import java.util.Timer;
import java.util.TimerTask;

public class AudioDataCollector implements Runnable{
	public Audio IO;
	private Timer timer;
	private TimerTask update;
	AudioDataCollector(Audio IO){
		this.IO = IO;
		timer = new Timer();
		update = new TimerTask() {

			@Override
			public void run() {
				IO.updateFFT();
				IO.updateVol();
				IO.updateBass(20);
				IO.updateMid(55);
				IO.updateHi(40);
			}
		};
	}
	@Override
	public void run() {
		timer.scheduleAtFixedRate(update, 0, 3);
	}	
}
