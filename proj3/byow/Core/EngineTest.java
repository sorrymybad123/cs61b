package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import org.junit.Assert;
import org.junit.Test;
import org.junit.Assert.*;

public class EngineTest {

    @Test
    public void interactWithInputStringTest() {
        Engine engine = new Engine();
        TETile[][] s = engine.interactWithInputString("n1253549394908837991s");
        TETile[][] s1 = engine.interactWithInputString("n1253549394908837991s");
        TETile[][] s2 = engine.interactWithInputString("n5197880843569031643s");
        Assert.assertArrayEquals(s, s1);

    }

}