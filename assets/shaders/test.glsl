#type vertex
#version 330 core

layout(location = 0) in vec3 aPos;  // Позиция вершины
layout(location = 1) in vec2 aTexCoord;  // Текстурные координаты

out vec2 TexCoord;

uniform mat4 model;
uniform mat4 view;
uniform mat4 projection;

void main() {
    gl_Position = projection * view * model * vec4(aPos, 1.0);
    TexCoord = aTexCoord;
}

#type fragment
#version 330 core

in vec2 TexCoord;
out vec4 FragColor;

// Rendering parameters
#define MAZE_DENSITY 0.5
#define MAZE_ZOOM 5.0
#define TEXTURE_ZOOM 0.25
#define RAY_LENGTH 5.0
#define FADE_POWER 1.5
#define AMBIENT 0.0

// Animation parameters
#define LIGHT_SPEED_X 1.7
#define LIGHT_MOVE_X 3.0
#define LIGHT_MOVE_Y 4.3
#define LIGHT_FREQUENCY_X 0.2
#define LIGHT_FREQUENCY_Y1 0.3
#define LIGHT_FREQUENCY_Y2 0.5
#define FADE_VARIATION 0.2
#define FADE_FREQUENCY 3.0

// Math constants
#define SQRT2 1.41421356237
#define DELTA 0.001

uniform float iTime;
uniform vec2 iResolution;
uniform vec2 iMouse;
uniform sampler2D iChannel0;
uniform sampler2D iChannel1;

float rand(in vec2 seed) {
    return fract(11.0 * sin(3.0 * seed.x + 5.0 * seed.y));
}

float wallCheck(in vec2 p) {
    vec2 tile = floor(p + 0.5);
    float wall = step(1.0 - MAZE_DENSITY, rand(tile));
    wall *= step(max(1.0, abs(LIGHT_DY(tile.x))), abs(LIGHT_Y(tile.x) - tile.y));
    return wall;
}

float wallDistance(in vec2 rayOrigin, in vec2 rayDirection) {
    vec2 raySign = sign(rayDirection);
    vec2 rayInv = 1.0 / rayDirection;
    float rayLength = 0.0;
    vec2 rayLengthNext = (0.5 * raySign - fract(rayOrigin + 0.5) + 0.5) * rayInv;

    for (float rayStep = 0.0; rayStep < RAY_LENGTH * SQRT2; ++rayStep) {
        rayLength = min(rayLengthNext.x, rayLengthNext.y);
        vec2 hitNormal = step(rayLengthNext.xy, rayLengthNext.yx) * raySign;

        if (wallCheck(rayOrigin + rayLength * rayDirection + hitNormal * 0.5) > 0.5) {
            break;
        }

        rayLengthNext += hitNormal * rayInv;
    }

    return rayLength;
}

void main() {
    vec2 fragCoord = gl_FragCoord.xy;

    float cameraX = LIGHT_SPEED_X * iTime;
    vec2 rayOrigin = MAZE_ZOOM * (2.0 * fragCoord - iResolution.xy) / iResolution.y;
    rayOrigin.x += cameraX;

    vec2 lightPosition;
    if (iMouse.z < 0.5) {
        lightPosition.x = cameraX + LIGHT_MOVE_X * cos(LIGHT_FREQUENCY_X * iTime);
        lightPosition.y = LIGHT_Y(lightPosition.x);
    } else {
        lightPosition = MAZE_ZOOM * (2.0 * iMouse.xy - iResolution.xy) / iResolution.y;
        lightPosition.x += cameraX;
    }

    vec2 lightDirection = lightPosition - rayOrigin;
    float lightDistance = length(lightDirection);
    float lightIntensity = pow(max(0.0, 1.0 - lightDistance / RAY_LENGTH), FADE_POWER + FADE_VARIATION * cos(FADE_FREQUENCY * iTime));

    vec4 fragColor;
    if (wallCheck(rayOrigin) < 0.5) {
        if (lightDistance < RAY_LENGTH) {
            lightIntensity *= step(lightDistance + DELTA, wallDistance(rayOrigin, lightDirection / lightDistance));
        }
        fragColor = texture(iChannel0, rayOrigin * TEXTURE_ZOOM);
    } else {
        fragColor = texture(iChannel1, rayOrigin * TEXTURE_ZOOM);
    }

    fragColor *= mix(lightIntensity, 1.0, AMBIENT);
    fragColor += smoothstep(0.2, 0.0, lightDistance) * step(0.2, rand(vec2(iTime)));

    FragColor = fragColor;
}

