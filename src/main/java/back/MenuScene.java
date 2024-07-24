package back;

import graphics.Shader;
import org.lwjgl.BufferUtils;
import org.lwjgl.system.CallbackI;
import util.Time;

import java.awt.event.KeyEvent;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class MenuScene extends Scene{

    public MenuScene() {

        System.out.println("MenuScene");
    }

    private float[] vertexArray = {
            0.5f, -0.5f, 0.0f,     1.0f, 0.0f, 0.0f, 1.0f,

            -0.5f, 0.5f, 0.0f,     0.0f, 1.0f, 0.0f, 1.0f,

            0.5f, 0.5f, 0.0f,      0.0f, 0.0f, 1.0f, 1.0f,

            -0.5f, -0.5f, 0.0f,    1.0f, 1.0f, 0.0f, 1.0f
    };
    private int[] elementArray = {
            2, 1, 0,  // First Triangle
            0, 1, 3   // Second Triangle
    };
//    private int height = 480;
//    private int width = 640;
//    private float time = Time.getTime();

    private int vaoID, vboID, eboID;
    private int vertexID, fragmentID, shaderProgram;

    private Shader defaultShader;

    @Override
    public void init() {
        defaultShader = new Shader("assets/shaders/default.glsl");
        defaultShader.complie();
//        int iResolutionLocation = glGetUniformLocation(shaderProgram, "iResolution");
//        int iTimeLocation = glGetUniformLocation(shaderProgram, "iTime");

//        glUniform2f(iResolutionLocation, width, height); // ширина и высота вашего окна
//        glUniform1f(iTimeLocation, time);


        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);
        FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(vertexArray.length);
        vertexBuffer.put(vertexArray).flip();


        vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);

        IntBuffer elementBuffer = BufferUtils.createIntBuffer(elementArray.length);
        elementBuffer.put(elementArray).flip();

        eboID = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, elementBuffer, GL_STATIC_DRAW);

        int positionSize = 3;
        int colorSize = 4;
        int floatSizeBytes = 4;
        int vertexSizeBytes = (positionSize + colorSize) * floatSizeBytes;
        glVertexAttribPointer(0, positionSize, GL_FLOAT, false, vertexSizeBytes, 0);
        glEnableVertexAttribArray(0);
        glVertexAttribPointer(1, colorSize, GL_FLOAT, false, vertexSizeBytes, positionSize * floatSizeBytes);
        glEnableVertexAttribArray(1);

    }
//    private boolean isSceneChanging = false;
//    private float sceneChangeTime = 2.0f;



    @Override
    public void update(float dt) {
        defaultShader.use();

        glUseProgram(shaderProgram);
        glBindVertexArray(vaoID);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        glDrawElements(GL_TRIANGLES, elementArray.length, GL_UNSIGNED_INT, 0);
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glBindVertexArray(0);

        defaultShader.detach();
    }
}
