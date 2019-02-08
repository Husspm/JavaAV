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

float map (float value, float minV, float maxV, float newMin, float newMax){
	float perc = (value - minV) / (maxV - minV);
	float val = perc * (newMax - newMin) + newMin;
	return val;
}
float depth; 
void main() {
	gl_FragDepth = depth = max(distance(vertTexCoord.xy, offset.xy), distance(vertTexCoord.xy, offset2.xy)) * 1000;
	float lightDist =  normalize(distance(vertTexCoord.xy, offset.xy));
	float lightDist2 =  normalize(distance(vertTexCoord.xy, offset2.xy));
	vec3 normal = cross(vertTexCoord.xyz, offset) * lightDist / lightDist2;
	vec2 repos = vec2(vertTexCoord.x * abs(sin(vertTexCoord.y * offset.z) + 0.079), vertTexCoord.y * 0.62 + (cMod / 100));
	vec3 texColor = texture2D(texture, repos + (offset.xy / 10000) * (sin(depth) / lightDist/ 10)).rgb / lightDist2;
	vec3 texColor2 = texture2D(texture, tan(repos) * (normal.xy / 10000)).rgb / lightDist;
	vec3 texColor3 = texture2D(texture, vec2(normal.x, normal.y + (vertTexCoord.x / 100))).rgb;
	gl_FragColor = vec4(tan(mix(texColor2,texColor, 0.5)), 0.7425 );
//	gl_FragColor = vec4(texColor2, 0.5);
}


