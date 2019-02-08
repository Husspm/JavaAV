
#version 450
#ifdef GL_ES
precision mediump float;
precision mediump int;
#endif

#define PROCESSING_TEXTURE_SHADER
#define PI 3.1415926535897932384626433832795


varying vec4 vertTexCoord;
uniform sampler2D texture;
uniform sampler2D altTex;
uniform float cMod;
uniform float midVol;
uniform float time;
uniform vec3 offset;
uniform vec3 offset2;
uniform vec3 [] off;
float finalAdjust;

float radius = 0.5;

float map (float value, float minV, float maxV, float newMin, float newMax){
	float perc = (value - minV) / (maxV - minV);
	float val = perc * (newMax - newMin) + newMin;
	return val;
}

void main() {
	gl_FragDepth = max(distance(vertTexCoord.xy, offset.xy), distance(vertTexCoord.xy, offset2.xy));
//	finalAdjust = noise1(time) / 10.0;
	float lightDist =  normalize(distance(vertTexCoord.xy, offset.xy));
	float lightDist2 =  normalize(distance(vertTexCoord.xy, offset2.xy));
//	vec2 repos = vec2(vertTexCoord.x * abs(noise1(vertTexCoord.xy * 0.5) - 0.2), vertTexCoord.y);
//	vec3 texColor = texture2D(texture, repos + (offset.xy / 10000)).rgb * cMod;
//	vec3 texColor2 = texture2D(texture, vertTexCoord.xy + (offset2.xy / 10000)).rgb * cMod * 10;
	float ang = map(vertTexCoord.y, 0, 1, -PI * 2, PI * 2);
	vec2 wavelines = vec2(vertTexCoord.x - (sin(ang + time / vertTexCoord.x) / (cMod * max(1.5, midVol / 1.5))), vertTexCoord.y * cos(ang + time / 10)/ distance(vertTexCoord.xy, offset2.xy));
	vec3 mixColors = texture2D(texture, wavelines / lightDist).rgb;
	vec3 mixColors2 = texture2D(texture, vec2(tan(map(wavelines.x, 0, 1, 1, 0)), atan(wavelines.y - vertTexCoord.y, wavelines.x - vertTexCoord.x))).rgb * lightDist2;
	gl_FragColor = vec4(mix(mixColors, mixColors2, 0.45), 0.35);
//	gl_FragColor = vec4(mixColors2, 0.35);
}