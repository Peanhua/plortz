#version 120

uniform sampler2DShadow ShadowMap;
uniform sampler2D tex;

in vec4 ShadowCoord;
in vec3 normal;
in vec3 position;

uniform bool  drawing_font;
uniform bool  shadows = true;
uniform float sun;
uniform float highlight;

const float night_start = 0.09f;
const vec4  ambient     = vec4(0.2f, 0.2f, 0.2f, 1.0f);

vec3 nnormal;
vec4 basecolour;

vec4 positionalLight(vec3 light_position, vec3 half_vector, float constant_attenuation, float linear_attenuation, float quadratic_attenuation);
vec4 fontColour(vec4 colour);

void main()
{
  vec4 colour;

  nnormal = normalize(normal);

  vec4 basecolour;

  if(drawing_font)
    basecolour = fontColour(gl_Color);
  else
    basecolour = gl_Color * texture2D(tex, gl_TexCoord[0].st);


  if(shadows)
    {
      /* Sun light */
      if(sun > 0.0f)
        {
          float distanceFromLight;
          
          /* Ambient light */
          colour = max(sun, 0.0f) * ambient * basecolour;
          
          distanceFromLight = shadow2DProj(ShadowMap, ShadowCoord).z;
          if(ShadowCoord.w <= 0.0f || distanceFromLight > ShadowCoord.z / ShadowCoord.w + 0.0005f)
            {
              float intensity;
      
              intensity = max(dot(nnormal, gl_LightSource[0].position.xyz), 0.0f);
              colour += sun * intensity * basecolour;
        
              /* Specular */
              if(intensity > 0.0f)
                if(gl_FrontMaterial.shininess > 0.0f)
                  {
                    float s;
              
                    s = max(dot(nnormal, gl_LightSource[0].halfVector.xyz), 0.0f);
                    colour += sun * gl_FrontMaterial.specular * gl_LightSource[0].specular * pow(s, gl_FrontMaterial.shininess);
                  }
            }
        }
      else
        colour = vec4(0.0f, 0.0f, 0.0f, basecolour.a);

      //if(sun <= night_start)
      { /* Light from nightsky (moon, stars). */
        float amount;
        float intensity;

        amount = night_start; // - clamp(sun, 0.0f, night_start);
        intensity = dot(nnormal, gl_LightSource[1].position.xyz);
        colour += amount * intensity * basecolour;
      }


      /* Positional light #0 (glLight[2]) */
      if(false)
        colour += positionalLight(gl_LightSource[2].position.xyz,
                                  gl_LightSource[2].halfVector.xyz,
                                  gl_LightSource[2].constantAttenuation,
                                  gl_LightSource[2].linearAttenuation,
                                  gl_LightSource[2].quadraticAttenuation);
    }
  else
    {
      colour = basecolour;
      if(colour.a < 0.01)
        discard;
    }

  colour.rgb += vec3(highlight, highlight, highlight);

  gl_FragColor = colour;
}

vec4 positionalLight(vec3 light_position, vec3 half_vector, float constant_attenuation, float linear_attenuation, float quadratic_attenuation)
{
  vec4  colour          = vec4(0.0f, 0.0f, 0.0f, basecolour.a);
  vec3  light_direction = light_position - position;
  float light_distance  = length(light_direction);
  float light_NdotL     = max(dot(nnormal, normalize(light_direction)), 0.0f);
  if(light_NdotL > 0.0f)
    {
      float attenuation = 1.0f / (constant_attenuation +
                                  linear_attenuation    * light_distance +
                                  quadratic_attenuation * light_distance * light_distance);
      colour += attenuation * (basecolour * light_NdotL); // diffuse

      /* Specular */
      if(gl_FrontMaterial.shininess > 0.0f)
        {
          float s;

          s = max(dot(nnormal, half_vector), 0.0f);
          colour += attenuation * gl_FrontMaterial.specular * pow(s, gl_FrontMaterial.shininess);
        }
    }

  return colour;
}
