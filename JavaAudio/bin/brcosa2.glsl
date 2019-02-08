#version 450
#ifdef GL_ES
precision mediump float;
precision mediump int;
#endif

#define PROCESSING_TEXTURE_SHADER

varying vec4 vertTexCoord;
uniform sampler2D texture;
uniform float brightness;
uniform float contrast;
float saturation = 1.1;
uniform float depth;
vec3 conColor;

float map (float value, float minV, float maxV, float newMin, float newMax){
	float perc = (value - minV) / (maxV - minV);
	float val = perc * (newMax - newMin) + newMin;
	return val;
}
float findMax(vec4 c){
	return max(max(c.r, c.g), c.b);
}
bool isWhite(vec4 c){
	if(c.r > 0.9 && c.g > 0.9 && c.b > 0.9){
		return true;
	}else{
		return false;
	}
}

const vec3 LumCoeff = vec3(0.2125, 0.7154, 0.0721);
const vec3 AvgLumin = vec3(0.5, 0.5, 0.5);

void main() {
	vec3 texColor = texture2D(texture, vertTexCoord.xy).rgb;
 	vec3 intensity = vec3(dot(texColor, LumCoeff));
	vec3 satColor = mix(intensity, texColor, saturation);
	conColor = mix(AvgLumin, satColor, contrast);
	vec4 finalMix = vec4(brightness * conColor, 0.25);
//	float blackCheck = findMax(finalMix);
//	if (blackCheck < 0.24){
//		float col = abs(map(blackCheck, 0, 0.399, -1, 1));
//		float trans = map(col, 0, 1, 1, 0.25);
//		gl_FragColor = vec4(col, col, col, trans);
//		}
	if(isWhite(finalMix)){
		float col = abs(map(finalMix.r, 0.9, 1.9, -1, 1));
		gl_FragColor = vec4(col, col, col, finalMix.a * 2);
	}else{
  		gl_FragColor = finalMix;
	}
}
