#version 330
#extension GL_ARB_separate_shader_objects : require

layout(location = 0) in vec2 local;
layout(location = 1) in vec4 tint;
layout(location = 2) flat in ivec2 params;

layout(location = 0) out vec4 fragColor;

const float PI = 3.14159265;
const vec4 OUTLINE = vec4(0.0, 0.0, 0.0, 0.55);

void main() {
    int type = params.x & 3;
    float center = radians(float(params.x >> 2));
    float halfAngle = max(radians(float(params.y >> 8)), 0.02);
    float radius = float(params.y & 255);

    vec2 p = floor(local) + 0.5;
    float r = length(p);
    float dr = abs(r - radius);
    vec4 color = vec4(0.0);

    if (type == 0) {
        if (dr <= 0.5) {
            color = vec4(tint.rgb, 1.0);
        }
    } else {
        float angle = atan(p.x, -p.y);
        float t = abs(mod(angle - center + PI, 2.0 * PI) - PI) / halfAngle;
        float slack = 1.4 / (radius * halfAngle);
        float thickness = t < 0.45 ? 1.5 : (t < 0.8 ? 1.0 : 0.5);
        if (t <= 1.0 && dr <= thickness) {
            float shade = t < 0.45 ? 1.0 : (t < 0.8 ? 0.8 : 0.55);
            color = vec4(tint.rgb, shade);
        } else if (t <= 1.0 + slack && dr <= thickness + 1.0) {
            color = OUTLINE;
        }
    }

    float alpha = color.a * tint.a;
    if (alpha <= 0.002) {
        discard;
    }
    fragColor = vec4(color.rgb, alpha);
}
