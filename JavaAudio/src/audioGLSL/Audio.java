package audioGLSL;

import processing.core.PApplet;
import processing.sound.Amplitude;
import processing.sound.AudioIn;
import processing.sound.FFT;
import processing.sound.Sound;

public class Audio extends Sound {
	public int bands = 512;
	public float fftSmooth = 0.725f;
	public float volSmooth = 0.625f;
	private float[] sums = new float[bands];
	private float newVol = 0;
	private AudioIn[] in = new AudioIn[2];
	private Amplitude vol;
	private FFT fft;
	private PApplet p;
	public boolean init = false;
	private float bass;
	private float mid;
	private float hi;

	public Audio(PApplet p) {
		super(p);
		this.p = p;
		in[0] = new AudioIn(this.p, 0);
		in[1] = new AudioIn(this.p, 1);
		vol = new Amplitude(this.p);
		fft = new FFT(this.p, bands);
		in[0].start();
		in[1].start();
		vol.input(in[0]);
		fft.input(in[1]);
		init = true;
	}

	// update Methods are used in separate thread for faster processing
	public void updateVol() {
		newVol += (vol.analyze() - newVol) * volSmooth;
	}

	public void updateFFT() {
		fft.analyze();
		for (int i = 0; i < sums.length; i++) {
			sums[i] += (fft.spectrum[i] - sums[i]) * fftSmooth;
		}
	}

	// TODO
	// add another parameter for the starting index on all update methods
	// to get more control over frequency response
	public void updateBass(int width) {
		bass = 0;
		for (int i = 2; i < 2 + width; i++) {
			bass += sums[i];
		}
	}

	public void updateMid(int width) {
		mid = 0;
		for (int i = 20; i < 20 + width; i++) {
			mid += sums[i] * 10;
		}
	}

	public void updateHi(int width) {
		hi = 0;
		for (int i = 140; i < 140 + width; i++) {
			hi += sums[i] * 10;
		}
	}

	// get methods are used in the drawing thread for shader files and do no
	// processing
	public float getVol() {
		return newVol;
	}

	public float getBass() {
		return bass;
	}

	public float getMid() {
		return mid;
	}

	public float getHi() {
		return hi;
	}
}