#version 450
#ifdef GL_ES
precision lowp float;
precision lowp int;
#endif

#define PROCESSING_TEXTURE_SHADER
#define PI 3.1415926535897932384626433832795
in vec4 vertTexCoord;
in vec4 offsetCoord;
in vec4 vertColor;
uniform sampler2D texture;
uniform sampler2D texture2;
uniform vec2 mousePos;
uniform vec2 mousePos2;
uniform vec2 mousePos3;
uniform vec2 mousePos4;
uniform float intensity;
uniform float intensity2;
uniform float volMod;
uniform float time;
out vec4 finalColor2;
float divideMod = 21.4;
float map (float value, float minV, float maxV, float newMin, float newMax){
	float perc = (value - minV) / (maxV - minV);
	float val = perc * (newMax - newMin) + newMin;
	return val;
}

void main() {
//	vec4 color = vec4(texture2D(texture, vertTexCoord.xy));
	divideMod *= smoothstep(0.0, 2.0, vertTexCoord.x);
	float colorMod = distance(vertTexCoord.xy, mousePos.xy);
	float colorMod2 = distance(vertTexCoord.xy, mousePos2.xy);
	float colorMod3 = distance(vertTexCoord.xy, mousePos3.xy);
	float colorMod4 = distance(vertTexCoord.xy, mousePos4.xy);
	vec2 pos1 = vec2(vertTexCoord.x - atan(colorMod3, sin(colorMod) * time) + intensity, vertTexCoord.y * colorMod4 + intensity2);
	vec2 pos = vec2(vertTexCoord.x * colorMod, vertTexCoord.y * colorMod2);
//	vec2 pos = vec2(((atan(vertTexCoord.y, vertTexCoord.x) / tan(intensity / divideMod * colorMod2)) * colorMod), ((atan(vertTexCoord.x, vertTexCoord.y) / tan(intensity2 / divideMod * colorMod)) * colorMod2));
	float extraMod = distance(mousePos2, mousePos);
	vec4 color = vec4(texture2D(texture, pos / pos1));
	vec4 color1 = tan(sin(color / (colorMod * 22.0) * (intensity - volMod)) * time);
	vec4 color2 = cos(color / (colorMod2 * 22.0) * (intensity2 - volMod)) * time;
//	gl_FragDepth = intensity * 100;
	vec3 endColor = vec3(mix(color1, color2, 0.5));
	color1 = vec4(texture2D(texture, sin(vec2(mousePos3 - vertTexCoord.xy)) + 0.5));
	color2 = vec4(texture2D(texture, cos(vec2(mousePos4 - vertTexCoord.xy)) - 0.5));
	vec3 newEndColor = vec3(mix(color1, color2, 0.5));
//	vec2 p = gl_FragCoord.xy / vec2(1024, 720);
	vec3 blend = mix(endColor, newEndColor, 0.5);
	vec3 color3;
	color3 = texture2D(texture, sin(vec2(atan(colorMod, colorMod3), atan(colorMod2, colorMod4)))).rgb;
	color3 += atan(colorMod3 - vertTexCoord.y, colorMod4 - vertTexCoord.x) * (cos(intensity * volMod) * time);
	finalColor2 = vec4(mix(newEndColor, color3, 0.5), 0.9851);
//	finalColor2 = vec4(newEndColor, 1.0);
//	finalColor = vec4(0, 0, 1, 0.81);
}

