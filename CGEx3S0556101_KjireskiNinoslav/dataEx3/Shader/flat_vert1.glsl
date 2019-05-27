#version 150
uniform mat4 ModelViewProjectionMatrix;
uniform mat4 ModelViewMatrix;
uniform mat4 ModelMatrix;
uniform mat4 NormalMatrix;

uniform vec4 lightPos;
uniform vec4 drawCol;

in vec4 vert_in;
in vec4 col_in;
in vec4 norm_in;
in vec2 tex0_in;

out vec4 col_out;

void main()
{
	gl_Position = ModelViewProjectionMatrix * vec4(vert_in);
    col_out = drawCol;	
}
