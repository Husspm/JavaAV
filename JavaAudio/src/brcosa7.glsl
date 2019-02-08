#version 450
#ifdef GL_ES
precision mediump float;
precision mediump int;
#endif

#define PROCESSING_TEXTURE_SHADER

in vec4 vertTexCoord;
uniform sampler2D texture;
uniform sampler2D texture2;
uniform vec3 offset;
float brightness = 3.0;
uniform float contrast;
uniform float saturation = 1.5;
vec2 reposition2;

float map (float value, float minV, float maxV, float newMin, float newMax){
	float perc = (value - minV) / (maxV - minV);
	float val = perc * (newMax - newMin) + newMin;
	return val;
}

void main() {	
	float angleAdjust = 0.51;
	float newContrast = clamp(contrast, 0, 2);
	float colorMod = distance(offset.xy, vertTexCoord.xy) / 10000;
	float zoomAdjust = abs(noise1(contrast));
	vec2 reposition = vec2((vertTexCoord.x + zoomAdjust) * 0.75, (vertTexCoord.y + zoomAdjust) * 0.75); 
	reposition2 = vec2(vertTexCoord.x,
	map(vertTexCoord.y, 0, 1, 1, 0)); 
	vec3 texColor = texture2D(texture, reposition).rgb;
	vec3 texColor2 = texture2D(texture, reposition2).rgb * (colorMod * 10);
 	vec3 LumCoeff = vec3(0.9125, 0.37, 0.721);
 	vec3 AvgLumin = vec3(0.5, 0.5, 0.5);
 	vec3 intensity = vec3(dot(AvgLumin, LumCoeff));
	vec3 texColor4 = mix(texColor, texColor2, 0.5);
	vec3 satColor = mix(texColor, texColor2, saturation);
 	vec3 conColor = mix(AvgLumin, satColor, newContrast) * intensity;
	vec3 newColor = mix(conColor, texColor4, 0.5);
//	gl_FragDepth = sin(vertMod * newColor.g);
 	gl_FragColor = vec4(newColor, 0.95);
}