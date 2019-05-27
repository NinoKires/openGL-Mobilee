#version 150
uniform sampler2D testtex;

in vec3 col_out;
in vec2 uv_out;

out vec4 final_col;

void main()
{
	vec4 coltex = texture(testtex, uv_out);
	coltex.a = 1; //Alpha channel is for glossiness which is not implemented yet
	final_col = coltex* vec4(col_out,1);
}
