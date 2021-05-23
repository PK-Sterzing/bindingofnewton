import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.backends.headless.HeadlessApplicationConfiguration;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.bindingofnewton.game.BindingOfNewtonGame;
import org.junit.jupiter.api.Test;


public class GameTest {

    @Test
    public void testGameStart(){
        HeadlessApplicationConfiguration config = new HeadlessApplicationConfiguration();
        new HeadlessApplication(new BindingOfNewtonGame(), config);
    }

}
