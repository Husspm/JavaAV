#version 450
#ifdef GL_ES
precision mediump float;
precision mediump int;
#endif

#define PROCESSING_TEXTURE_SHADER


varying vec4 vertTexCoord;
uniform sampler2D texture;
uniform float cMod;
uniform float midVol;
uniform vec3 offset;
uniform vec3 offset2;
uniform vec3 [] off;
uniform float time;

float map (float value, float minV, float maxV, float newMin, float newMax){
	float perc = (value - minV) / (maxV - minV);
	float val = perc * (newMax - newMin) + newMin;
	return val;
}
float depth;

void main() {
	gl_FragDepth = depth = max(distance(vertTexCoord.xy, offset.xy), distance(vertTexCoord.xy, offset2.xy));
	float lightDist =  normalize(distance(vertTexCoord.xy, offset.xy));
	float lightDist2 =  normalize(distance(vertTexCoord.xy, offset2.xy));
	vec2 repos = vec2(vertTexCoord.x - sin(offset.x * midVol) * time, vertTexCoord.y - cos(offset.y * cMod) * time);
	vec3 texColor = texture2D(texture, repos * atan(offset.y / 10000, offset.x / 10000) / lightDist).rgb;
	vec3 texColor2 = texture2D(texture, repos * (offset2.xy / 10000) / lightDist2).rgb;
//	texColor += cos(depth) * time;
	gl_FragColor = vec4(mix(texColor2,texColor, cMod *4), 0.095);
}


