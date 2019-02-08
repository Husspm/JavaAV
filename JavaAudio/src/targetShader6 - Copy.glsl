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
float divideMod;
float map (float value, float minV, float maxV, float newMin, float newMax){
	float perc = (value - minV) / (maxV - minV);
	float val = perc * (newMax - newMin) + newMin;
	return val;
}

void main() {
//	vec4 color = vec4(texture2D(texture, vertTexCoord.xy));
	divideMod = abs(noise1(vertColor)) * 10;
	divideMod = map(divideMod, 0, 10, 10, 60);
	float colorMod = distance(vertTexCoord.xy, mousePos.xy);
	float colorMod2 = distance(vertTexCoord.xy, mousePos2.xy);
	float colorMod3 = distance(vertTexCoord.xy, mousePos3.xy);
	float colorMod4 = distance(vertTexCoord.xy, mousePos4.xy);
	vec2 pos1 = vec2(vertTexCoord.x * colorMod + intensity, vertTexCoord.y * colorMod2 + intensity2);
//	vec2 pos = vec2(vertTexCoord.x * colorMod, vertTexCoord.y * colorMod2);
	vec2 pos = vec2(((atan(vertTexCoord.y, vertTexCoord.x) / tan(intensity / divideMod * colorMod2)) * colorMod), ((atan(vertTexCoord.x, vertTexCoord.y) / tan(intensity2 / divideMod * colorMod)) * colorMod2));
	vec2 pos2 = vec2(((atan(vertTexCoord.y, vertTexCoord.x) / tan(intensity / divideMod * colorMod3)) * colorMod4), ((atan(vertTexCoord.x, vertTexCoord.y) / tan(intensity2 / divideMod * colorMod4)) * colorMod3));
	float extraMod = distance(mousePos2, mousePos);
	vec4 color = vec4(texture2D(texture, pos));
	vec4 secondColor = vec4(texture2D(texture, pos2));
	vec4 color1 = color / (colorMod * 20) * intensity;
	vec4 color2 = color / (colorMod2 * 20) * intensity2;
	vec4 color3 = secondColor / (colorMod3 * 20) * intensity;
	vec4 color4 = secondColor / (colorMod4 * 20) * intensity2;
//	gl_FragDepth = intensity * 100;
	vec3 endColor = vec3(mix(color1, color2, 0.5));
	vec3 endColor2 = vec3(mix(color3, color4, 0.5));
	finalColor2 = vec4(mix(endColor, endColor2, 0.5), 0.51);
}

