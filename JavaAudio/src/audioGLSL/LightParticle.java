package audioGLSL;

import processing.core.PApplet;
import processing.core.PVector;

public class LightParticle {
	PVector pos, acc;
	float off;
	float intensity;
	private PApplet p;

	LightParticle(PApplet p) {
		this.p = p;
		pos = new PVector(p.width / 2, p.height / 2);
		acc = new PVector(p.random(1.2f, 2.3f), p.random(1.2f, 2.3f));
	}

	void move(float vol) {
		off += 0.0001;
		PVector newAcc = acc.copy();
		newAcc.mult(vol);
		newAcc.rotate(p.noise(off) / 50 * vol);
		newAcc.x *= p.random(0.8f, 1.3f);
		newAcc.y *= p.random(0.28f, 2.3f);
		intensity = p.noise(off) * 24;
		pos.add(newAcc);
		if (pos.x > p.width || pos.x < 0) {
			acc.x *= -1;
			pos.x = p.width / 2;
		}
		if (pos.y > p.height || pos.y < 0) {
			acc.y *= -1;
			pos.y = p.height / 2;
		}
	}
}
