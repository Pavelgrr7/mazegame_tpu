//package back;
//
//import org.lwjgl.opengl.GL11;
//import org.lwjgl.opengl.GL30;
//import org.lwjgl.stb.STBImage;
//import java.nio.ByteBuffer;
//import java.nio.IntBuffer;
//import org.lwjgl.system.MemoryStack;
//
//public class MenuTexture {
//    private int textureId;
//    private int width;
//    private int height;
//
//    public MenuTexture(String filePath) {
//        // Загружаем текстуру
//        try (MemoryStack stack = MemoryStack.stackPush()) {
//            IntBuffer widthBuffer = stack.mallocInt(1);
//            IntBuffer heightBuffer = stack.mallocInt(1);
//            IntBuffer channelsBuffer = stack.mallocInt(1);
//
//            ByteBuffer imageData = STBImage.stbi_load(filePath, widthBuffer, heightBuffer, channelsBuffer, 4);
//            if (imageData == null) {
//                throw new RuntimeException("Не удалось загрузить текстуру: " + STBImage.stbi_failure_reason());
//            }
//
//            this.width = widthBuffer.get();
//            this.height = heightBuffer.get();
//
//            // Генерация текстуры
//            textureId = GL11.glGenTextures();
//            GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureId);
//
//            // Настройки текстуры
//            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
//            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
//
//            // Загрузка текстуры в OpenGL
//            GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, width, height, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, imageData);
//            GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
//
//            // Освобождаем память
//            STBImage.stbi_image_free(imageData);
//        }
//    }
//
//    public void bind() {
//        GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureId);
//    }
//
//    public int getWidth() {
//        return width;
//    }
//
//    public int getHeight() {
//        return height;
//    }
//}
//
