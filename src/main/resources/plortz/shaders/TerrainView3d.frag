#version 330

in vec3 v2f_position;
in vec3 v2f_normal;
in vec4 v2f_color;

out vec4 out_color;


uniform float sun = 1.0f;
vec3  sun_light_pos;
uniform vec3  sun_light_half_vector;
uniform float sun_light_specular = 0.5f;

const float night_start = 0.09f;
const vec4  ambient     = vec4(0.2f, 0.2f, 0.2f, 1.0f);

// gl_FrontMaterial:
const float material_shininess = 0.5f;
const float material_specular  = 0.2f;


vec3 normal;
vec4 basecolour;

vec4 positionalLight(vec3 light_position, vec3 half_vector, float constant_attenuation, float linear_attenuation, float quadratic_attenuation);

void main()
{
  vec4 colour;

  sun_light_pos = normalize(vec3(1.0f, 1.0f, 1.0f));
  normal = normalize(v2f_normal);
                                   
  vec4 basecolour = v2f_color;

  /* Sun light */
  if(sun > 0.0f)
    {
      /* Ambient light */
      colour = max(sun, 0.0f) * ambient * basecolour;

      float intensity = max(dot(normal, sun_light_pos), 0.0f);
      colour += sun * intensity * basecolour;
        
      /* Specular */
      if(intensity > 0.0f)
        if(material_shininess > 0.0f)
          {
            float s = max(dot(normal, sun_light_half_vector), 0.0f);
            colour += sun * material_specular * sun_light_specular * pow(s, material_shininess);
          }
    }
  else
    {
      colour = basecolour * ambient;
    }
  
  /* Positional light #0 (glLight[2]) */
  /*
    if(false)
    colour += positionalLight(gl_LightSource[2].position.xyz,
    gl_LightSource[2].halfVector.xyz,
    gl_LightSource[2].constantAttenuation,
    gl_LightSource[2].linearAttenuation,
    gl_LightSource[2].quadraticAttenuation);
  */
  /*
  if(colour.a < 0.01)
    discard;
  */
  //  colour.a = 1.0f;
  //  colour = basecolour;
  out_color = colour;
}

vec4 positionalLight(vec3 light_position, vec3 half_vector, float constant_attenuation, float linear_attenuation, float quadratic_attenuation)
{
  vec4  colour          = vec4(0.0f, 0.0f, 0.0f, basecolour.a);
  vec3  light_direction = light_position - v2f_position;
  float light_distance  = length(light_direction);
  float light_NdotL     = max(dot(normal, normalize(light_direction)), 0.0f);
  if(light_NdotL > 0.0f)
    {
      float attenuation = 1.0f / (constant_attenuation +
                                  linear_attenuation    * light_distance +
                                  quadratic_attenuation * light_distance * light_distance);
      colour += attenuation * (basecolour * light_NdotL); // diffuse

      /* Specular */
      if(material_shininess > 0.0f)
        {
          float s;

          s = max(dot(normal, half_vector), 0.0f);
          colour += attenuation * material_specular * pow(s, material_shininess);
        }
    }

  return colour;
}
