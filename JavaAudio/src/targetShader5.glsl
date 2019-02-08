#version 450
#ifdef GL_ES
precision lowp float;
precision lowp int;
#endif

#define PROCESSING_TEXTURE_SHADER
#define PI 3.1415926535897932384626433832795
varying vec4 vertTexCoord;
varying vec4 offsetCoord;
varying vec4 vertColor;
uniform sampler2D texture;
uniform sampler2D texture2;
uniform float depthMod;
uniform vec3 transMod;
uniform float time;
vec4 clr[] = vec4[](
	vec4(0.171, 0.0, 0.084, 0.418),
	vec4(0.271, 0.0, 0.074, 0.418)
	);
const vec2 offset = vec2(1 / 1280, 1 / 720);

float map (float value, float minV, float maxV, float newMin, float newMax){
	float perc = (value - minV) / (maxV - minV);
	float val = perc * (newMax - newMin) + newMin;
	return val;
}



void main() {
	vec2 pos = offsetCoord.xy;
	vec2 pos2 = vertTexCoord.xy;
	vec4 tC = sin(texture2D(texture, pos) * depthMod);
	vec4 tC2 = texture2D(texture, atan(pos2, pos));
	vec4 coEff = vec4(transMod, smoothstep(0, 1, atan(pos.y - pos2.y, pos.x - pos2.x)));
	gl_FragColor = mix(tC, tC2, coEff);
}

