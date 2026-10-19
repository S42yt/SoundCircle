#version 330
#extension GL_ARB_separate_shader_objects : require

layout(std140) uniform DynamicTransforms {
    mat4 ModelViewMat;
};
layout(std140) uniform Projection {
    mat4 ProjMat;
};

layout(location = 0) in vec3 Position;
layout(location = 1) in vec4 Color;
layout(location = 2) in vec2 UV0;
layout(location = 3) in ivec2 UV2;

layout(location = 0) out vec2 local;
layout(location = 1) out vec4 tint;
layout(location = 2) flat out ivec2 params;

void main() {
    gl_Position = ProjMat * ModelViewMat * vec4(Position, 1.0);
    local = UV0;
    tint = Color;
    params = UV2;
}
