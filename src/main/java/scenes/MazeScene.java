package scenes;

import back.Window;

public class MazeScene extends Scene {

//    private boolean isSceneChanging = false;
//    private float sceneChangeTime = 1.5f;




    public MazeScene() {
        System.out.println("MazeScene");
        Window.get().r = 0;
        Window.get().g = 0;
        Window.get().b = 0;

    }

    @Override
    public void update(float dt){
//        if (!isSceneChanging && KeyboardListener.isKeyPressed(KeyEvent.VK_SPACE)) {
//            isSceneChanging = true;
//        }
//
//        if (isSceneChanging && sceneChangeTime > 0) {
//            sceneChangeTime -= dt;
//            Window.get().r -= dt  * 5.0f;
//            Window.get().g -= dt  * 5.0f;
//            Window.get().b -= dt  * 5.0f;
//        } else if (isSceneChanging) {
//            Window.changeScene(1);
//        }

    }
    @Override
    public void render() {
        this.renderer.render();
    }
}
