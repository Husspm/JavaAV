#version 450
#ifdef GL_ES
precision mediump float;
precision mediump int;
#endif


uniform mat4 transformMatrix;
uniform mat4 texMatrix;
uniform float uTime;

attribute vec4 position;
attribute vec4 color;
attribute vec2 texCoord;

varying vec4 vertColor;
out vec4 vertTexCoord;
out vec4 offsetCoord;

void main() {
//  vec2 pos = vec2(position.x, position.y / sin(position.x));
  gl_Position = transformMatrix  * position;
  vertColor = color;
  vertTexCoord = texMatrix * vec4(texCoord, 1.0, 1.0);
  offsetCoord = texMatrix * vec4(texCoord - 0.001, 1.0 , 1.0);
}