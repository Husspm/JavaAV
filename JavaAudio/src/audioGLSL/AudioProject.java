package audioGLSL;

import java.io.File;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import com.jogamp.opengl.GL;

import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PImage;
import processing.core.PMatrix3D;
import processing.opengl.PGL;
import processing.opengl.PJOGL;
import processing.opengl.PShader;
import processing.video.Movie;

public class AudioProject extends PApplet {
	Audio IO;
	private PShader shade2, shade4, shade5, shade6, bcsShade, bcsShade2, bcsShade3, mirror, mirror2;
	PShader[] layerOneShader;
	private PShader[] lightShaders;
	Movie film;
	Particle p, p2;
	LightParticle lp, lp2;
	PGraphics target, subTarget, subTarget2, finalTarget, fxLayer, mirrorTarget;
	PImage img, overlay;
	Timer updateAudio;
	TimerTask audioData;
	public float rotOff = 0;
	public boolean open = true;
	public int pulses = 1;
	public int selector = 1;
	public int shadeSwitch = 1;
	public int layerOneSwitch = 6;
	public int layerOneRenderSetting = 0;
	public int layerTwoSwitch = 4;
	public int finalRenderSwitch = 4;
	public float sum, bass, mid, hi, hiCTRL;
	public int lsIndex = 0;
	public boolean lightWarp = false;
	private char[] keys = { 'a', 's', 'z', 'x', 'o' };
	Thread updateAudioData;
	static Controls UI;
	public int bright = 0;
	HashMap<String, Number> vals = new HashMap<String, Number>();
	PJOGL gl;
	PGL GL;

	public void settings() {
//		delay(5000);
		size(1280, 720, P3D);
//		fullScreen(P3D);
		PJOGL.profile = 4;
		gl = new PJOGL(null);
//		smooth(4);
	}

	public void setup() {
		vals.put("LayerOneSwitch", 0);
		vals.put("LayerTwoSwitch", layerTwoSwitch);
		System.out.println(vals);
		hiCTRL = 1.0f;
//		frameRate(60);
//		film = new Movie(this, "film.mp4");
		target = createGraphics(width, height, P3D);
		subTarget = createGraphics(width, height, P3D);
		subTarget2 = createGraphics(width, height, P3D);
		finalTarget = createGraphics(width, height, P3D);
		mirrorTarget = createGraphics(width, height, P3D);
		img = loadImage("image2.png");
		img.resize(width, height);
		overlay = loadImage("overlay.png");
		layerOneShader = new PShader[9];
		lightShaders = new PShader[3];
		shade2 = loadShader("targetShader4.glsl", "targetVert.vert");
		shade4 = loadShader("targetShader2.glsl", "targetVert.vert");
		shade5 = loadShader("targetShader3.glsl", "targetVert.vert");
		shade6 = loadShader("targetShader5.glsl", "targetVert.vert");
		bcsShade = loadShader("brcosa2.glsl");
		bcsShade2 = loadShader("brcosa7.glsl");
		bcsShade3 = loadShader("brcosa8.glsl");
		mirror = loadShader("mirrorImage.glsl");
		mirror2 = loadShader("mirrorImageVertical.glsl");
		layerOneShader[0] = loadShader("partShader2.glsl");
		layerOneShader[1] = loadShader("partShader.glsl");
		layerOneShader[2] = loadShader("partShader3.glsl");
		layerOneShader[3] = loadShader("partShader4.glsl");
		layerOneShader[4] = loadShader("partShader5.glsl");
		layerOneShader[5] = loadShader("partShader6.glsl");
		layerOneShader[6] = loadShader("partShader7.glsl");
		layerOneShader[7] = loadShader("partShader8.glsl");
		layerOneShader[8] = loadShader("partShader9.glsl");
		lightShaders[0] = loadShader("targetShader6.glsl", "targetVert.vert");
		lightShaders[1] = loadShader("targetShader7.glsl", "targetVert.vert");
		lightShaders[2] = loadShader("targetShader8.glsl", "targetVert.vert");
		IO = new Audio(this);
//		film.loop();
//		film.volume(0);
		p = new Particle(this);
		p2 = new Particle(this);
		lp = new LightParticle(this);
		lp2 = new LightParticle(this);
		updateAudioData = new Thread(new AudioDataCollector(IO));
		selectInput("Please Select a film to use as a texture", "aquireFilmData");
//		updateAudio.scheduleAtFixedRate(audioData, 0, 20);
//		frameRate(1000);
//		noCursor();
		UI = new Controls(this);
	    javax.swing.SwingUtilities.invokeLater(new Runnable() {
	        public void run() {
//	            UI.createAndShowGUI();
	        	}	
	        });
	}

	static public int attempts = 0;

	public void aquireFilmData(File f) {
		if (f == null) {
			println("Wrong file type");
			attempts++;
			if (attempts > 1) {
				exit();
			}
			selectInput("Please Select a film to process", "aquireFilmData");
		} else {
			String filmName = f.getAbsoluteFile().toString();
			String[] test2 = split(filmName, ".");
			String[] compare = { "mp4", "mov" };
			if (test2[1].compareTo(compare[0]) > 0 && test2[1].compareTo(compare[1]) > 0) {
				selectInput("File must be of type mp4 or mov", "aquireFilmData");
			} else {
				film = new Movie(this, filmName);
				film.loop();
				film.volume(0);
//				film.speed(9.4f);
				selector = 0;
				updateAudioData.start();
			}
		}
	}

	public void movieEvent(Movie m) {
		m.read();
	}

	public int DIR(int t) {
		return floor(random(t)) + 1 == t ? -1 : 1;
	}

	public void pulseCounter() {
		try {
			pulses++;
			if (pulses % 32 == 0) {
				pulses++;
				hiCTRL = 1;
//				changeRoutine(keys[floor(random(keys.length))]);
			}
			Thread.sleep(75);
			open = true;
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void routineOne() {
//		IO.updateFFT();
		sum = IO.getVol() * 10;
		vals.put("sum", sum);
		rotOff += 0.00001;
		bass = IO.getBass();
		mid = IO.getMid();
		hi = IO.getHi();
		if (hi > hiCTRL){
			hiCTRL = hi;
		}
		if (bass > 1  && open|| hi > 1 && open) {
			open = false;
			thread("pulseCounter");
		}
		surface.setTitle("FPS: " + floor(frameRate) + " res: " + width + "x" + height + " bassSum: " + bass
				+ " midSum: " + mid + " hiSum " + hi + " volSum: " + sum + " l1 Setting " + layerOneSwitch);
		p.move(bass / 30);
		p2.move(mid / 30);
		lp.move(mid / 15);
		lp2.move(hi / 15);
		this.layerOne(layerOneSwitch, layerOneRenderSetting);
		this.layerTwo(layerTwoSwitch);
		this.finalRender(finalRenderSwitch);
//		resetShader();
//		image(overlay, 0, -200);
	}

	public void routineTwo() {
		background(0);
		textSize(30);
		text("PLEASE WAIT", random(width), random(height));
//		if (film.height > 0 && img.isLoaded() && IO.init) {
//			selector = 0;
//		}
	}

	public void layerOne(int L1, int RS) {
//		gl.blendEquation(1);
		target.beginDraw();
//		switch (L1) {
//		case 0:
		layerOneShader[L1].set("cMod", sum);
		layerOneShader[L1].set("altTex", subTarget);
		layerOneShader[L1].set("midVol", mid);
		layerOneShader[L1].set("offset", p.pos);
		layerOneShader[L1].set("offset2", p2.pos);
		layerOneShader[L1].set("time", (millis() / 1000));
//		layerOneShader[L1].set("finalAdjust", map(mouseX, 0, width, -1, 1));
		target.shader(layerOneShader[L1]);
		float wMod = noise(rotOff) * (sum > 1 ? sum : 1) + 1;
//			target.image(target, 0, 0, width, height);
		// target.filter(bcsShade);
		switch (RS) {
		case 0:
			target.image(film, 0, 0, width, height);
			target.image(subTarget2, 0, 0, width, height);
			break;
		case 1:
			target.image(img, 0, 0, width, height);
			target.image(film, 0, 0, width * wMod, height);
			break;
		case 2:
			target.image(overlay, 0, 0, width, height);
			target.image(film, 0, 0, width, height);
			target.image(target, 0, 0, width * wMod, height);
			break;
		}
		target.endDraw();
	}
	public void layerOne(int L1, PGraphics tex, PGraphics store) {
		store.beginDraw();
		layerOneShader[L1].set("cMod", sum);
		layerOneShader[L1].set("altTex", mirrorTarget);
		layerOneShader[L1].set("midVol", mid);
		layerOneShader[L1].set("offset", p.pos);
		layerOneShader[L1].set("offset2", p2.pos);
		layerOneShader[L1].set("time", (millis() / 1000));
		layerOneShader[L1].set("mixCoOff", map(mouseX, 0, width, 0, 1));
		store.shader(layerOneShader[L1]);
		store.image(tex, 0, 0);
		store.endDraw();
	}
	public void layerOne(int L1, PGraphics tex) {
		target.beginDraw();
//		switch (L1) {
//		case 0:
		layerOneShader[L1].set("cMod", sum);
		layerOneShader[L1].set("altTex", mirrorTarget);
		layerOneShader[L1].set("midVol", mid);
		layerOneShader[L1].set("offset", p.pos);
		layerOneShader[L1].set("offset2", p2.pos);
		layerOneShader[L1].set("time", (millis() / 1000));
		layerOneShader[L1].set("mixCoOff", map(mouseX, 0, width, 0, 1));
		target.shader(layerOneShader[L1]);
		target.image(tex, 0, 0);
		target.endDraw();
	}
	public void layerTwo(int L2) {
		switch (L2) {
		case 0:
			subTarget.shader(shade4);
			shade4.set("transformMatrix", (PMatrix3D) target.getMatrix());
			shade4.set("texMatrix", (PMatrix3D) subTarget.getMatrix());
			shade4.set("depthMod", sum / 100);
			shade4.set("transMod", bass, mid / 10, hi);
			subTarget.beginDraw();
			subTarget.image(target, 0, 0, width, height);
			subTarget.endDraw();
			finalTarget.beginDraw();
			bcsShade.set("contrast", (mid / 3));
			bcsShade.set("brightness", (bass * 10));
			finalTarget.shader(bcsShade);
			finalTarget.image(subTarget, 0, 0);
			finalTarget.endDraw();
			break;
		case 1:
			subTarget2.shader(shade2);
			shade2.set("transformMatrix", (PMatrix3D) target.getMatrix());
			shade2.set("texMatrix", (PMatrix3D) target.getMatrix());
			shade2.set("depthMod", sum / 100);
			shade2.set("transMod", bass, mid / 10, hi);
			shade2.set("texture2", subTarget);
			subTarget2.beginDraw();
			subTarget2.image(target, 0, 0, width, height);
			subTarget2.endDraw();
			finalTarget.beginDraw();
			bcsShade2.set("contrast", hi);
			bcsShade2.set("offset", p.pos);
			finalTarget.shader(bcsShade2);
			finalTarget.image(subTarget2, 0, 0);
			finalTarget.endDraw();
			break;
		case 2:
			subTarget2.shader(shade5);
			shade5.set("transformMatrix", (PMatrix3D) target.getMatrix());
			shade5.set("texMatrix", (PMatrix3D) target.getMatrix());
			shade5.set("depthMod", sum / 100);
			shade5.set("transMod", bass, mid / 10, hi);
			shade5.set("texture2", subTarget);
			subTarget2.beginDraw();
			subTarget2.image(target, 0, 0, width, height);
			subTarget2.endDraw();
			finalTarget.beginDraw();
			bcsShade3.set("contrast", bass);
			bcsShade3.set("offset", p2.pos);
			bcsShade3.set("brightness", mid - sum);
			bcsShade3.set("texture2", target);
			finalTarget.shader(bcsShade3);
			finalTarget.image(subTarget2, 0, 0);
			finalTarget.endDraw();
			break;
		case 3:
			subTarget.shader(shade6);
			shade6.set("transformMatrix", (PMatrix3D) target.getMatrix());
			shade6.set("texMatrix", (PMatrix3D) target.getMatrix());
			shade6.set("depthMod", sum / 100);
			shade6.set("transMod", bass, mid / 10, hi);
			shade6.set("texture2", subTarget2);
			shade6.set("time", millis() / 1000);
			subTarget.beginDraw();
			subTarget.image(target, 0, 0, width, height);
			subTarget.endDraw();
			finalTarget.beginDraw();
			layerOne(layerOneSwitch, subTarget);
			bcsShade3.set("contrast", bass);
			bcsShade3.set("offset", p2.pos);
			bcsShade3.set("brightness", mid - sum);
			bcsShade3.set("texture2", target);
			bcsShade3.set("saturation", map(hi, 0, hiCTRL, -5, 5));
			finalTarget.shader(bcsShade3);
			finalTarget.image(subTarget, 0, 0);
			finalTarget.endDraw();
			break;
		case 4:
			PShader curShader = lightShaders[lsIndex];
			subTarget.shader(curShader);
			curShader.set("transformMatrix", (PMatrix3D) target.getMatrix());
			curShader.set("texMatrix", (PMatrix3D) target.getMatrix());
			curShader.set("mousePos", norm(lp.pos.x, 0, width), norm(lp.pos.y, 0, height));
			curShader.set("mousePos2", norm(lp2.pos.x, 0, width), norm(lp2.pos.y, 0, height));
			curShader.set("mousePos3", norm(p.pos.x, 0, width), norm(p.pos.y, 0, height));
			curShader.set("mousePos4", norm(p2.pos.x, 0, width), norm(p2.pos.y, 0, height));
			curShader.set("intensity", lp.intensity);
			curShader.set("intensity2", lp2.intensity);
			curShader.set("volMod", sum);
			curShader.set("time", millis() / 1000);
			subTarget.beginDraw();
			subTarget.image(target, 0, 0);
			subTarget.endDraw();
			finalTarget.beginDraw();
//			bcsShade3.set("contrast", bass);
			bcsShade3.set("offset", p2.pos);
			bcsShade3.set("brightness", (sum));
			bcsShade3.set("texture2", target);
//			bcsShade3.set("saturation", hi * 3);
			bcsShade3.set("saturation", map(mouseX, 0, width, -5, 5));
			bcsShade3.set("contrast", map(mouseY, 0, height, -5, 5));
			shade4.set("transformMatrix", (PMatrix3D) target.getMatrix());
			shade4.set("texMatrix", (PMatrix3D) subTarget2.getMatrix());
			shade4.set("uTime", millis() / 1000);
			shade4.set("depthMod", sum / 100);
			shade4.set("transMod", bass, mid / 10, hi);
			shade4.set("texture2", subTarget2);
			subTarget2.beginDraw();
			subTarget2.shader(shade4);
			subTarget2.image(subTarget, 0 ,0);
			subTarget2.endDraw();
			finalTarget.shader(bcsShade3);
			finalTarget.filter(shade4);
			finalTarget.image(subTarget, 0, 0);
			finalTarget.endDraw();
//			layerOne(6, finalTarget);
			break;
		}
	}

	public void finalRender(int FR) {
		mirrorTarget.beginDraw();
		switch (FR) {
		case 0: // mirror horizontal
			mirrorTarget.resetShader();
			mirrorTarget.image(finalTarget, 0, 0, width / 2, height);
			mirrorTarget.shader(mirror);
			mirrorTarget.image(finalTarget, width / 2, 0, width / 2, height);
			break;
		case 1: // right side upside down
			mirrorTarget.image(finalTarget, 0, 0, width / 2, height);
			mirrorTarget.pushMatrix();
			mirrorTarget.translate(width, height);
			mirrorTarget.rotate(-PI);
			mirrorTarget.image(finalTarget, 0, 0, width / 2, height);
			mirrorTarget.popMatrix();
			break;
		case 2: // fullScreen
			mirrorTarget.filter(mirror);
			mirrorTarget.image(finalTarget, 0, 0, width, height);
			break;
		case 3: // mirror vertical
			mirrorTarget.resetShader();
			mirrorTarget.image(finalTarget, 0, 0, width, height / 2);
			mirrorTarget.shader(mirror2);
			mirrorTarget.image(finalTarget, 0, height / 2, width, height / 2);
			break;
		case 4:
			mirrorTarget.resetShader();
			mirrorTarget.image(finalTarget, 0, 0, width / 2, height / 2);
			mirrorTarget.resetShader();
			mirrorTarget.shader(mirror);
			mirrorTarget.image(finalTarget, width / 2, 0, width / 2, height / 2);
			mirrorTarget.resetShader();
			mirrorTarget.pushMatrix();
			mirrorTarget.translate(width / 2, height);
			mirrorTarget.rotate(-PI);
			mirrorTarget.shader(mirror);
			mirrorTarget.image(finalTarget, 0, 0, width / 2, height / 2);
			mirrorTarget.popMatrix();
			mirrorTarget.resetShader();
			mirrorTarget.pushMatrix();
			mirrorTarget.translate(width, height);
			mirrorTarget.rotate(-PI);
//			shader(mirror2);
			mirrorTarget.image(finalTarget, 0, 0, width / 2, height / 2);
			mirrorTarget.popMatrix();
			break;
		}
		mirrorTarget.endDraw();
		if (lightWarp) {
			int curr_ls_index = lsIndex;
			if (curr_ls_index == 2) {
				curr_ls_index = 1;
			}
			lightShaders[curr_ls_index].set("transformMatrix", (PMatrix3D) mirrorTarget.getMatrix());
			lightShaders[curr_ls_index].set("texMatrix", (PMatrix3D) finalTarget.getMatrix());
			lightShaders[curr_ls_index].set("mousePos", norm(lp.pos.x, 0, width), norm(lp.pos.y, 0, height));
			lightShaders[curr_ls_index].set("mousePos2", norm(lp2.pos.x, 0, width), norm(lp2.pos.y, 0, height));
			lightShaders[curr_ls_index].set("mousePos3", norm(p.pos.x, 0, width), norm(p.pos.y, 0, height));
			lightShaders[curr_ls_index].set("mousePos4", norm(p2.pos.x, 0, width), norm(p2.pos.y, 0, height));
			lightShaders[curr_ls_index].set("intensity", lp.intensity);
			lightShaders[curr_ls_index].set("intensity2", lp2.intensity);
			lightShaders[curr_ls_index].set("volMod", sum);
			lightShaders[curr_ls_index].set("time", millis() / 1000);
			shader(lightShaders[curr_ls_index]);
		} else {
			resetShader();
		}
		image(mirrorTarget, 0, 0);
		layerOne(7, mirrorTarget, subTarget2);
//		overlayExists(useOverlay);
	}

	public void overlayExists(boolean yes) {
		if (yes) {
			resetShader();
			image(overlay, -20, -20, width, overlay.height);
		} else {
			return;
		}
	}

	public void draw() {
		switch (selector) {
		case 0:
			this.routineOne();
			break;
		case 1:
			this.routineTwo();
			break;
		}
	}

	public void changeRoutine(char key) {
		switch (key) {
		case 'a':
//			layerOneSwitch++;
			layerOneSwitch = floor(random(layerOneShader.length));
			if (layerOneSwitch >= layerOneShader.length) {
				layerOneSwitch = 0;
			}
			break;
		case 'z':
			layerTwoSwitch++;
			lsIndex++;
			if (layerTwoSwitch > 4) {
				layerTwoSwitch = 0;
			}
			if (lsIndex >= lightShaders.length) {
				lsIndex = 0;
			}
			break;
		case 's':
			finalRenderSwitch++;
			if (finalRenderSwitch > 4) {
				finalRenderSwitch = 0;
			}
			break;
		case 'o':
//			lightWarp = !lightWarp;
			break;
		case 'x':
			layerOneRenderSetting++;
			if (layerOneRenderSetting > 2) {
				layerOneRenderSetting = 0;
			}
		}
	}
	public void keyPressed() {
		switch (key) {
		case 'a':
			lsIndex++;
			if (lsIndex >= lightShaders.length) {
				lsIndex = 0;
			}
			break;
		case 's':
			layerOneSwitch++;
			if (layerOneSwitch >= layerOneShader.length) {
				layerOneSwitch = 0;
			}
			break;
		case 'z':
			layerTwoSwitch++;
			if (layerTwoSwitch > 4) {
				layerTwoSwitch = 0;
			}
			break;
		case 'd':
			System.out.println(vals);
			vals.forEach( (k, v ) -> System.out.println(k + v));
			break;
		case 'q':
			save("Image@frame" + frameCount);
		}
	}
	public void setParam(int val, String pName) {
		System.out.println("Called!! " + val);
		vals.replace(pName, val);
		System.out.println(vals.get(pName));
		
	}
	public void mousePressed() {
		lp.pos.x = mouseX;
		lp.pos.y = mouseY;
		lp2.pos.x = mouseX;
		lp2.pos.y = mouseY;
	}

	public static void main(final String[] args) {
		PApplet.main(new String[] { AudioProject.class.getName() });
	}
}

