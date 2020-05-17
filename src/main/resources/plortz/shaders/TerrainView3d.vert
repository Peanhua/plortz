#version 330

uniform mat4 normal_matrix;
uniform mat4 model_matrix;
uniform mat4 pillu;
uniform mat4 view_matrix;
uniform mat4 projection_matrix;

layout(location=0) in vec3 in_position;
layout(location=1) in vec4 in_color;
layout(location=2) in vec3 in_normal;

out vec3 v2f_position;
out vec3 v2f_normal;
out vec4 v2f_color;

void main()
{
  v2f_position = (view_matrix * model_matrix * vec4(in_position, 1)).xyz;
  v2f_color = in_color;
  v2f_normal = normalize(normal_matrix * vec4(in_normal, 1)).xyz;
  gl_Position = projection_matrix * view_matrix * model_matrix * vec4(in_position, 1);
}
