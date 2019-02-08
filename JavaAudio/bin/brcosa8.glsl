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
uniform float brightness;
uniform float contrast;
uniform float saturation;
vec2 reposition2;

float map (float value, float minV, float maxV, float newMin, float newMax){
	float perc = (value - minV) / (maxV - minV);
	float val = perc * (newMax - newMin) + newMin;
	return val;
}

void main() {	
	float angleAdjust = saturation / 10000;
//	float birghtAdjust = clamp(brightness, -2.0, 2.5);
	float colorMod = distance(offset.xy, vertTexCoord.xy) / 10000;
	float zoomAdjust = abs(noise1(contrast));
	vec2 reposition = vec2(vertTexCoord.x, vertTexCoord.y); 
	reposition2 = vec2(vertTexCoord.x,
	map(vertTexCoord.y, 0, 1, 1, 0)); 
	vec3 texColor = sin(texture2D(texture2, reposition).rgb);
	vec3 texColor2 = texture2D(texture, reposition2).rgb * (colorMod * 10);
 	vec3 LumCoeff = vec3(0.9125, 0.37, 0.721);
 	vec3 AvgLumin = vec3(0.5, 0.5, 0.5);
	vec3 texColor4 = mix(texColor, texColor2, 0.5);
	vec3 intensity = vec3(dot(AvgLumin, LumCoeff));
	vec3 satColor = mix(texColor, texColor2, saturation) * intensity;
 	vec3 conColor = mix(texColor4, satColor, contrast);
	vec3 newColor = mix(conColor, texColor4, 0.5) * max((brightness / 5), 0.2);
//	gl_FragDepth = sin(offset.x + offset.y) * con;
 	gl_FragColor = vec4(newColor, 0.975);
}