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
vec4 clr[] = vec4[](
	vec4(0.171, 0.0, 0.084, 0.0818),
	vec4(0.271, 0.0, 0.074, 0.0818)
	);
const vec2 offset = vec2(1 / 1280, 1 / 720);

float map (float value, float minV, float maxV, float newMin, float newMax){
	float perc = (value - minV) / (maxV - minV);
	float val = perc * (newMax - newMin) + newMin;
	return val;
}


void quiet(float col){
float trans = 0;
vec3 mixture;
//	gl_FragDepth = map(col, 0, 1, -10, 10);
	if(col < 0.3){
		col = map(col, 0, 0.299, 0.0125, 1);
		trans = map(transMod.y, 0, 2, 0, 0.75);
		mixture = mix(vec3(col), vec3(texture2D(texture, vertTexCoord.xy * ( depthMod / 100)).rgb), 0.15);
		gl_FragColor = vec4(mixture, 0.1);
	}else if(col <  0.6){
		trans = map(transMod.y, 0, 3, 0, 0.5);
		vec3 col = vec3(map(col, 0.3, 0.599, 0, 1));
		mixture = mix(col, vec3(texture2D(texture2, vertTexCoord.xy * ( depthMod / 80)).rgb), 0.15);
		gl_FragColor = vec4(mixture, 0.1);
	}else{
		gl_FragColor = vec4(texture2D(texture2 - texture, vertTexCoord.xy *( depthMod / 50)).rgb, 0.21);
	}
}
void main() {
	vec3 secondTex = vec3(texture2D(texture2, vertTexCoord.xy)).rgb;
	float colorMod = secondTex.r;
	float colorMod2 = secondTex.b;
	vec4 color = vec4(texture2D(texture, vertTexCoord.xy).rgb, 1);
	vec4 color2 = vec4(texture2D(texture, vertTexCoord.xy * colorMod).rgb, transMod.y);
	vec4 color3 = vec4(texture2D(texture, vertTexCoord.xy * colorMod2).rgb, transMod.z);
	if (transMod.y < 0.7){
		quiet(max(max(color.r, color.b), color.g));
	}
	else{
		gl_FragColor = mix(color3, color2, 0.5);
	}
}

