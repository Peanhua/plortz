#version 330

in vec3 v2f_position;
in vec3 v2f_normal;
in vec4 v2f_color;

out vec4 out_color;

uniform mat4 model_matrix;
uniform mat4 view_matrix;

uniform vec3 sun_position;
const vec4 light_color = vec4(1.0f, 1.1f, 1.1f, 1.0f);
const vec4 ambient = vec4(0.1f, 0.1f, 0.1f, 1.0f);
const float material_shininess = 16f;

// Blinn-Phong: https://en.wikipedia.org/wiki/Blinn%E2%80%93Phong_reflection_model
void main()
{
  vec3 normal = normalize(v2f_normal);
  vec3 sunpos = vec3(view_matrix * model_matrix * vec4(sun_position, 1));
  vec3 light_direction = normalize(sunpos - v2f_position);

  float lambertian = max(dot(light_direction, normal), 0.0f);
  float specular = 0.0f;

  if(lambertian > 0.0f)
    {
      vec3 view_direction = normalize(-v2f_position);
      vec3 half_direction = normalize(light_direction + view_direction);
      float specular_angle = max(dot(half_direction, normal), 0.0f);
      specular = pow(specular_angle, material_shininess);
    }

  vec4 color = ambient;
  color += v2f_color * lambertian * light_color;
  color += v2f_color * specular * light_color;
    
  if(color.a < 0.01)
    discard;

  out_color = color;
}
