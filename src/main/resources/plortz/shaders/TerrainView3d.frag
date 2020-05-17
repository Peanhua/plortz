#version 330

//uniform sampler2DShadow ShadowMap;
//uniform sampler2D tex;

in vec3 v2f_position;
in vec3 v2f_normal;
in vec4 v2f_color;

//in vec4 ShadowCoord;

out vec4 out_color;


uniform bool  shadows = true;
uniform float sun = 1.0f;
uniform vec3  sun_light_pos = vec3(0, 0, 100);
uniform vec3  sun_light_half_vector;
uniform float sun_light_specular = 0.5f;

//uniform float highlight;

const float night_start = 0.09f;
const vec4  ambient     = vec4(0.2f, 0.2f, 0.2f, 1.0f);

// gl_FrontMaterial:
const float shininess = 0.0f;
const float specular  = 0.0f;


vec3 nnormal;
vec4 basecolour;

vec4 positionalLight(vec3 light_position, vec3 half_vector, float constant_attenuation, float linear_attenuation, float quadratic_attenuation);

void main()
{
  vec4 colour;

  nnormal = normalize(v2f_normal);
                                   
  vec4 basecolour;

  basecolour = v2f_color; // * texture2D(tex, gl_TexCoord[0].st);

  if(shadows)
    {
      /* Sun light */
      if(sun > 0.0f)
        {
          float distanceFromLight;
          
          /* Ambient light */
          colour = max(sun, 0.0f) * ambient * basecolour;

          /*
          distanceFromLight = shadow2DProj(ShadowMap, ShadowCoord).z;
          if(ShadowCoord.w <= 0.0f || distanceFromLight > ShadowCoord.z / ShadowCoord.w + 0.0005f)
          */
            {
              float intensity;
      
              intensity = max(dot(nnormal, sun_light_pos), 0.0f);
              colour += sun * intensity * basecolour;
        
              /* Specular */
              if(intensity > 0.0f)
                if(/*gl_FrontMaterial.*/shininess > 0.0f)
                  {
                    float s;
              
                    s = max(dot(nnormal, sun_light_half_vector), 0.0f);
                    colour += sun * specular * sun_light_specular * pow(s, shininess);
                  }
            }
        }
      else
        colour = vec4(0.0f, 0.0f, 0.0f, basecolour.a);

      //if(sun <= night_start)
      { /* Light from nightsky (moon, stars). */
        /*
        float amount;
        float intensity;

        amount = night_start; // - clamp(sun, 0.0f, night_start);
        intensity = dot(nnormal, gl_LightSource[1].position.xyz);
        colour += amount * intensity * basecolour;
        */
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
    }
  else
    {
      colour = basecolour;
      if(colour.a < 0.01)
        discard;
    }

  //  colour.rgb += vec3(highlight, highlight, highlight);

  out_color = colour;
}

vec4 positionalLight(vec3 light_position, vec3 half_vector, float constant_attenuation, float linear_attenuation, float quadratic_attenuation)
{
  vec4  colour          = vec4(0.0f, 0.0f, 0.0f, basecolour.a);
  vec3  light_direction = light_position - v2f_position;
  float light_distance  = length(light_direction);
  float light_NdotL     = max(dot(nnormal, normalize(light_direction)), 0.0f);
  if(light_NdotL > 0.0f)
    {
      float attenuation = 1.0f / (constant_attenuation +
                                  linear_attenuation    * light_distance +
                                  quadratic_attenuation * light_distance * light_distance);
      colour += attenuation * (basecolour * light_NdotL); // diffuse

      /* Specular */
      if(shininess > 0.0f)
        {
          float s;

          s = max(dot(nnormal, half_vector), 0.0f);
          colour += attenuation * specular * pow(s, shininess);
        }
    }

  return colour;
}
