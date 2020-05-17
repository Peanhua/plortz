#version 120

varying vec3 normal;
varying vec3 position;
varying vec4 ShadowCoord;

void main()
{
  ShadowCoord = gl_TextureMatrix[7] * gl_Vertex;

  normal = normalize(gl_NormalMatrix * gl_Normal);
  position = (gl_ModelViewMatrix * gl_Vertex).xyz;
  gl_FrontColor = gl_Color;
  gl_Position = ftransform();
  gl_TexCoord[0] = /* gl_TextureMatrix[0] * */ gl_MultiTexCoord0;
}
