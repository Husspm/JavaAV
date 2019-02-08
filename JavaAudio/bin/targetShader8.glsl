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
uniform vec2 mousePos;
uniform vec2 mousePos2;
uniform vec2 mousePos3;
uniform vec2 mousePos4;
uniform float intensity;
uniform float intensity2;
out vec4 finalColor2;
float divideMod = 21.4;
float map (float value, float minV, float maxV, float newMin, float newMax){
	float perc = (value - minV) / (maxV - minV);
	float val = perc * (newMax - newMin) + newMin;
	return val;
}

void main() {
//	vec4 color = vec4(texture2D(texture, vertTexCoord.xy));
	float colorMod = distance(vertTexCoord.xy, mousePos.xy);
	float colorMod2 = distance(vertTexCoord.xy, mousePos2.xy);
	float colorMod3 = distance(vertTexCoord.xy, mousePos3.xy);
	float colorMod4 = distance(vertTexCoord.xy, mousePos4.xy);
	vec2 pos = vec2(vertTexCoord.x * colorMod + intensity, vertTexCoord.y * colorMod2 + intensity2);
	vec2 pos2 = vec2(vertTexCoord.x * colorMod3 + intensity, vertTexCoord.y * colorMod4 + intensity2);
//	vec2 pos = vec2(vertTexCoord.x * colorMod, vertTexCoord.y * colorMod2);
//	vec2 pos = vec2(((atan(vertTexCoord.y, vertTexCoord.x) / tan(intensity / divideMod * colorMod2)) * colorMod), ((atan(vertTexCoord.x, vertTexCoord.y) / tan(intensity2 / divideMod * colorMod)) * colorMod2));
	float extraMod = distance(mousePos2, mousePos);
	vec4 color = vec4(texture2D(texture, pos));
	vec4 color1 = color / (colorMod * 20) * intensity;
	vec4 color2 = color / (colorMod2 * 20) * intensity2;
//	gl_FragDepth = intensity * 100;
	vec3 endColor = vec3(mix(color1, color2, 0.5));
	color = vec4(texture2D(texture, pos2));
	color1 = color / (colorMod3 * 15) * intensity;
	color2 = color / (colorMod4 * 15) * intensity2;
	vec3 endColor2 = vec3(mix(color1, color2, 0.5));
	finalColor2 = vec4(mix(endColor, endColor2, 0.5), colorMod);
//	vec4 colorX = vec4(texture2D(texture, pos1));
//	vec4 color3 = colorX / (colorMod * 20) * intensity;
//	finalColor = vec4(0, 0, 1, 0.81);
}

