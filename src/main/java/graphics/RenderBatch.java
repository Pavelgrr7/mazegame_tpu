package graphics;

import back.Window;
import components.SpriteRenderer;
import org.joml.Vector2f;
import org.joml.Vector4f;
import util.AssetPool;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class RenderBatch {

    //vertex
    // position                    color                    texture cords         tex id
    // float float.    float float float float           float float          float
    private final int POS_SIZE = 2;
    private final int COLOR_SIZE = 4;
    private final int TEX_CORDS_SIZE = 2;
    private final int TEX_ID_SIZE = 1;

    private final int POS_OFFSET = 0;
    private final int COLOR_OFFSET = POS_OFFSET + POS_SIZE * Float.BYTES;
    private final int TEX_CORDS_OFFSET = COLOR_OFFSET + COLOR_SIZE * Float.BYTES;
    private final int TEX_ID_OFFSET = TEX_CORDS_OFFSET + TEX_CORDS_SIZE * Float.BYTES;
    private final int VERTEX_SIZE = 9;
    private final int VERTEX_SIZE_BYTES = VERTEX_SIZE * Float.BYTES;

    private SpriteRenderer[] sprites;
    private int numberSprites;
    private boolean hasRoom;
    private float[] vertices;
    private int[] texSlots = {0, 1, 2, 3 ,4 ,5, 7};

    private List<Texture> textures;
    private int vaoID, vboID;
    private int maxBatchSize;
    private Shader shader;

    public RenderBatch(int maxBatchSize) {
        System.out.println("Creating new render batch");
        shader = AssetPool.getShader("assets/shaders/default.glsl");
        this.sprites = new SpriteRenderer[maxBatchSize];
        this.maxBatchSize = maxBatchSize;

        vertices = new float[maxBatchSize * 4 * VERTEX_SIZE];

        this.numberSprites = 0;
        this.hasRoom = true;
        this.textures = new ArrayList<>();
    }

    public void start() {
        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);

        // allocate space for the vertices
        vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferData(GL_ARRAY_BUFFER, vertices.length *
                Float.BYTES, GL_DYNAMIC_DRAW);
        int eboID = glGenBuffers();
        int[] indices = generateIndices();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);

        // pointers
        glVertexAttribPointer(0, POS_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, POS_OFFSET);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1, COLOR_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, COLOR_OFFSET);
        glEnableVertexAttribArray(1);

        glVertexAttribPointer(2, TEX_CORDS_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, TEX_CORDS_OFFSET);
        glEnableVertexAttribArray(2);

        glVertexAttribPointer(3, TEX_ID_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, TEX_ID_OFFSET);
        glEnableVertexAttribArray(3);
    }

    private int[] generateIndices() {
        // 6 indices for quad
        int[] elements = new int[6 * maxBatchSize];
        for (int i = 0; i < maxBatchSize; i++) {
            loadElementIndices(elements, i);
        }

        return elements;
    }


    private void loadElementIndices(int[] elements, int index) {
        int offsetArrayIndex = 6 * index;
        int offset = 4 * index;

        //Trinagle 1
        elements[offsetArrayIndex] = offset + 3;
        elements[offsetArrayIndex + 1] = offset + 2;
        elements[offsetArrayIndex + 2] = offset + 0;

        //Trinagle 2
        elements[offsetArrayIndex + 3] = offset + 0;
        elements[offsetArrayIndex + 4] = offset + 2;
        elements[offsetArrayIndex + 5] = offset + 1;
    }

    public void render() {
        // rebuffer all data every frame
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferSubData(GL_ARRAY_BUFFER, 0, vertices);

        // use shader

        shader.use();
        shader.uploadMat4f("uProjection",
                Window.getScene().camera().getProjectionMatrix()
        );
        shader.uploadMat4f("uView",
                Window.getScene().camera().getViewMatrix()
        );
        for (int i = 0; i < textures.size(); i++) {
            glActiveTexture(GL_TEXTURE0 + i + 1);
            textures.get(i).bind();
        }
        shader.uploadIntArray("uTextures", texSlots);

        glBindVertexArray(vaoID);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        glDrawElements(GL_TRIANGLES, this.numberSprites * 6, GL_UNSIGNED_INT, 0);
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glBindVertexArray(0);
        for (int i = 0; i < textures.size(); i++) { textures.get(i).unbind(); }
        shader.detach();
    }

    public void addSprite(SpriteRenderer spr) {
        // index + add render object
        int index = this.numberSprites;
        this.sprites[index] = spr;
        this.numberSprites++;

        if (spr.getTexture() != null) {
            if (!textures.contains(spr.getTexture())) {
                textures.add(spr.getTexture());
            }
        }

        loadVertexProperties(index);
        if (numberSprites >= this.maxBatchSize) {
            this.hasRoom = false;
        }

    }

    private void loadVertexProperties(int index){
        SpriteRenderer sprite = this.sprites[index];

        int offset = index * 4 * VERTEX_SIZE;
        Vector4f color = sprite.getColor();
        Vector2f[] texCords = sprite.getTexCords();

        int texID = 0;
        // [0, tex, tex, tex,]
        if (sprite.getTexture() != null) {
            for (int i = 0; i < textures.size(); i++) {
                if (textures.get(i) == sprite.getTexture()) {
                    texID = i + 1;
                    break;
                }
            }
        }

        float xAdd = 1.0f;
        float yAdd = 1.0f;
        for (int i = 0; i < 4; i++) {
            if (i == 1) {
                yAdd = 0.0f;
            } else if (i == 2) {
                xAdd = 0.0f;
            } else if (i == 3) {
                yAdd = 1.0f;
            }

            vertices[offset] = sprite.gameObject.transform.position.x
                    + (xAdd * sprite.gameObject.transform.scale.x);
            vertices[offset + 1] = sprite.gameObject.transform.position.y
                    + (yAdd * sprite.gameObject.transform.scale.y);

            vertices[offset + 2] = color.x;
            vertices[offset + 3] = color.y;
            vertices[offset + 4] = color.z;
            vertices[offset + 5] = color.w;

            // load cords and id
            vertices[offset + 6] = texCords[i].x;
            vertices[offset + 7] = texCords[i].y;
            vertices[offset + 8] = texID;



            offset += VERTEX_SIZE;
        }
    }

    public boolean hasRoom() {
        return this.hasRoom;
    }
    public boolean hasTextureRoom() {
        return this.textures.size() < 9;
    }

    public boolean hasTexture(Texture tex) {
        return this.textures.contains(tex);
    }
}
