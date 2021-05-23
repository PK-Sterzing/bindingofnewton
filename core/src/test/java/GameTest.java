import com.bindingofnewton.game.BindingOfNewtonGame;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


public class GameTest {

    @Test
    public void testGameStart(){
        final HeadlessApplicationConfiguration config = new HeadlessApplicationConfiguration();
        config.renderInterval = 1f/60; // Likely want 1f/60 for 60 fps
        new HeadlessApplication(new BindingOfNewtonGame(), config);

    }

}
