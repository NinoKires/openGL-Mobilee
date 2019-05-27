#version 150
uniform mat4 ModelViewProjectionMatrix;
uniform mat4 ModelViewMatrix;
uniform mat4 ModelMatrix;
uniform mat4 NormalMatrix;

uniform vec4 lightPos;

in vec4 vert_in;
in vec4 norm_in;
in vec3 col_in;
in vec2 tex0_in;

out vec3 col_out;
out vec2 uv_out;

void main()
{
	//JUST DIFFUSE ANTEIL
	vec4 pos =  ModelViewMatrix * vert_in;
	uv_out = tex0_in;

	gl_Position = ModelViewProjectionMatrix * vert_in;

	vec3 norm = normalize((NormalMatrix*norm_in).xyz);
	vec3 l = normalize(-pos.xyz + lightPos.xyz);
	float light = max(0,0.8*dot(norm,l));
	col_out.rgb = vec3(0.2) + vec3(light); //with ambient
}
