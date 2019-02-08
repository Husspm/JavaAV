#version 450
#ifdef GL_ES
precision mediump float;
precision mediump int;
#endif

#define PROCESSING_TEXTURE_SHADER
#define res vec2(1024, 720)

varying vec4 vertTexCoord;
uniform sampler2D texture;
uniform sampler2D altTex;
uniform float cMod;
uniform float midVol;
uniform float time;
uniform vec3 offset;
uniform vec3 offset2;
uniform vec3 [] off;

float map (float value, float minV, float maxV, float newMin, float newMax){
	float perc = (value - minV) / (maxV - minV);
	float val = perc * (newMax - newMin) + newMin;
	return val;
}

void main() {
//	gl_FragDepth = max(distance(vertTexCoord.xy, offset.xy), distance(vertTexCoord.xy, offset2.xy));
	float lightDist =  normalize(distance(vertTexCoord.xy, offset.xy));
	float lightDist2 =  normalize(distance(vertTexCoord.xy, offset2.xy));
	vec3 oS = normalize(offset2);
	vec2 repos = vec2(vertTexCoord.x, vertTexCoord.y);
	vec3 texColor = texture2D(texture, repos).rgb;
	vec3 texColor2 = texture2D(altTex, repos).rgb;
	gl_FragColor = vec4(mix(texColor, texColor2, 0.5), 0.095 );
//	gl_FragColor = vec4(texColor2, 0.95);
}
