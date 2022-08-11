#version 330

out vec4 outputColor;

in vec3 pos;
uniform sampler2D tex;



void main()
{
	outputColor = texture(tex, vec2((pos.x + 1) / 2, (pos.y + 1) / 2));
}