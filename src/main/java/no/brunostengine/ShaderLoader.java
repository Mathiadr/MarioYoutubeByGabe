package no.brunostengine;


class ShaderLoader {
    public static String defaultShader
            = "#type vertex\n" +
            "#version 330 core\n" +
            "layout (location=0) in vec3 aPos;\n" +
            "layout (location=1) in vec4 aColor;\n" +
            "layout (location=2) in vec2 aTexCoords;\n" +
            "layout (location=3) in float aTexId;\n" +
            "\n" +
            "uniform mat4 uProjection;\n" +
            "uniform mat4 uView;\n" +
            "\n" +
            "out vec4 fColor;\n" +
            "out vec2 fTexCoords;\n" +
            "out float fTexId;\n" +
            "\n" +
            "void main()\n" +
            "{\n" +
            "    fColor = aColor;\n" +
            "    fTexCoords = aTexCoords;\n" +
            "    fTexId = aTexId;\n" +
            "\n" +
            "    gl_Position = uProjection * uView * vec4(aPos, 1.0);\n" +
            "}\n" +
            "\n" +
            "#type fragment\n" +
            "#version 330 core\n" +
            "\n" +
            "in vec4 fColor;\n" +
            "in vec2 fTexCoords;\n" +
            "in float fTexId;\n" +
            "\n" +
            "uniform sampler2D uTextures[8];\n" +
            "\n" +
            "out vec4 color;\n" +
            "\n" +
            "void main()\n" +
            "{\n" +
            "    if (fTexId > 0) {\n" +
            "        int id = int(fTexId);\n" +
            "        switch (id) {\n" +
            "            case 0:\n" +
            "                color = fColor * texture(uTextures[0], fTexCoords);\n" +
            "            break;\n" +
            "            case 1:\n" +
            "                color = fColor * texture(uTextures[1], fTexCoords);\n" +
            "            break;\n" +
            "            case 2:\n" +
            "                color = fColor * texture(uTextures[2], fTexCoords);\n" +
            "            break;\n" +
            "            case 3:\n" +
            "                color = fColor * texture(uTextures[3], fTexCoords);\n" +
            "            break;\n" +
            "            case 4:\n" +
            "                color = fColor * texture(uTextures[4], fTexCoords);\n" +
            "            break;\n" +
            "            case 5:\n" +
            "                color = fColor * texture(uTextures[5], fTexCoords);\n" +
            "            break;\n" +
            "            case 6:\n" +
            "                color = fColor * texture(uTextures[6], fTexCoords);\n" +
            "            break;\n" +
            "            case 7:\n" +
            "                color = fColor * texture(uTextures[7], fTexCoords);\n" +
            "            break;\n" +
            "        }\n" +
            "    } else {\n" +
            "        color = fColor;\n" +
            "    }\n" +
            "}";

    public static String debugShader
            = "#type vertex\n" +
            "#version 330 core\n" +
            "layout (location = 0) in vec3 aPos;\n" +
            "layout (location = 1) in vec3 aColor;\n" +
            "\n" +
            "out vec3 fColor;\n" +
            "\n" +
            "uniform mat4 uView;\n" +
            "uniform mat4 uProjection;\n" +
            "\n" +
            "void main()\n" +
            "{\n" +
            "    fColor = aColor;\n" +
            "\n" +
            "    gl_Position = uProjection * uView * vec4(aPos, 1.0);\n" +
            "}\n" +
            "\n" +
            "#type fragment\n" +
            "#version 330 core\n" +
            "in vec3 fColor;\n" +
            "\n" +
            "out vec4 color;\n" +
            "\n" +
            "void main()\n" +
            "{\n" +
            "    color = vec4(fColor, 1);\n" +
            "}";

    public static String pickingShader
            = "#type vertex\n" +
            "#version 330 core\n" +
            "layout (location=0) in vec3 aPos;\n" +
            "layout (location=1) in vec4 aColor;\n" +
            "layout (location=2) in vec2 aTexCoords;\n" +
            "layout (location=3) in float aTexId;\n" +
            "layout (location=4) in float aEntityId;\n" +
            "\n" +
            "uniform mat4 uProjection;\n" +
            "uniform mat4 uView;\n" +
            "\n" +
            "out vec4 fColor;\n" +
            "out vec2 fTexCoords;\n" +
            "out float fTexId;\n" +
            "out float fEntityId;\n" +
            "\n" +
            "void main()\n" +
            "{\n" +
            "    fColor = aColor;\n" +
            "    fTexCoords = aTexCoords;\n" +
            "    fTexId = aTexId;\n" +
            "    fEntityId = aEntityId;\n" +
            "\n" +
            "    gl_Position = uProjection * uView * vec4(aPos, 1.0);\n" +
            "}\n" +
            "\n" +
            "    #type fragment\n" +
            "    #version 330 core\n" +
            "\n" +
            "in vec4 fColor;\n" +
            "in vec2 fTexCoords;\n" +
            "in float fTexId;\n" +
            "in float fEntityId;\n" +
            "\n" +
            "uniform sampler2D uTextures[8];\n" +
            "\n" +
            "out vec3 color;\n" +
            "\n" +
            "void main()\n" +
            "{\n" +
            "    vec4 texColor = vec4(1, 1, 1, 1);\n" +
            "    if (fTexId > 0) {\n" +
            "        int id = int(fTexId);\n" +
            "        texColor = fColor * texture(uTextures[id], fTexCoords);\n" +
            "    }\n" +
            "\n" +
            "    if (texColor.a < 0.5) {\n" +
            "        discard;\n" +
            "    }\n" +
            "    color = vec3(fEntityId, fEntityId, fEntityId);\n" +
            "}";
}
