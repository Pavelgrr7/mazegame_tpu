#type vertex
#version 330 core
layout (location = 0) in vec3 aPos;

void main()
{
    gl_Position = vec4(aPos, 1.0);
}

#type fragment
#version 330 core

out vec4 color;

uniform vec2 iResolution;
uniform float iTime;
uniform vec4 iMouse;

#define STEPS 80

#define sr(a) (a * 2.0 - 1.0)
#define rs(a) (a * 0.5 + 0.5)
#define sq(a) (a * vec2(1, iResolution.y / iResolution.x))

float random(vec2 co) {
    return fract(sin(dot(co.xy,vec2(12.9898,78.233))) * 43758.5453);
}

float smin(float a, float b, float k) {
    float h = clamp(0.5 + 0.5 * (b - a) / k, 0.0, 1.0);
    return mix(b, a, h) - k * h * (1.0 - h);
}

float map(vec2 p, vec2 ro) {
    vec2 P = p;

    float w = 0.15;
    float r = 0.03125;
    float a = iTime;
    float d = 0.0;

    p.y += iTime * 0.15;

    vec2 idx = floor(p / w * 0.5 + 0.5);

    r += sin((idx.x + idx.y) * 2. + iTime * 5.) * 0.009;
    a += sin((idx.x + idx.y));

    p = mod(p + w, w * 2.) - w;
    d = length(p - 0.095 * vec2(sin(a), cos(a))) - r;
    d = smin(d, length(P) - 0.25, 0.05);
    d = -smin(-d, length(P - ro) - 0.05, 0.05);

    return d;
}

float shadow(vec2 uv, vec2 ro, vec2 rd) {
    float lim = 0.0005;
    float res = -1.0;
    float inc = lim * 2.0;
    float t = inc;
    float maxt = length(ro - uv);

    if (map(uv, ro) < 0.0) return 0.0;

    for (int i = 0; i < STEPS; i++) {
        if (t >= maxt) return -1.0;
        float d = map(uv - rd * t, ro);
        if (d <= 0.0) return 0.0;

        t = min(t + d * 0.2, maxt);
        res = t;
    }

    return res;
}

void main()
{
    vec2 fragCoord = gl_FragCoord.xy;
    vec2 uv = sq(sr(fragCoord.xy / iResolution.xy));
    vec2 ro = (iMouse.z > 0.0
    ? sq(sr(iMouse.xy / iResolution.xy))
    : 0.5 * vec2(
    sin(iTime),
    cos(iTime)
    ));

    vec2 rd = normalize(uv - ro);
    float s = shadow(uv, ro, rd) > -0.5 ? 0.35 : 1.0;
    float l = s * pow(max(0.0, 1.0 - length(ro - uv) * 0.8), 2.5);
    float d = map(uv, ro);

    bool inside = d < 0.0;
    bool stroke = d > -0.005 && inside;
    vec3 m = inside ? vec3(stroke ? 0 : 5) : vec3(1, 0.9, 0.7);

    color = vec4(m * l, 1);
}
