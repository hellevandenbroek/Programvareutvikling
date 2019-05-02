package tdt4140.gr1811.app.testfx_utils;

import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.stage.Screen;
import java.awt.*;

/*
 * See: https://github.com/TestFX/TestFX/issues/245
 * 
 * - Thomas
 */

public class ScaledBounds extends BoundingBox {

    private static final double scale;
    static {
        scale = Toolkit.getDefaultToolkit().getScreenSize().getWidth() / Screen.getPrimary().getBounds().getWidth();
    }

    public static ScaledBounds wrap(Bounds bounds){
        return new ScaledBounds(bounds);
    }
    private ScaledBounds(Bounds wrapped){
        super(
                wrapped.getMinX() * scale,
                wrapped.getMinY() * scale,
                wrapped.getMinZ() * scale,
                wrapped.getWidth() * scale,
                wrapped.getHeight() * scale,
                wrapped.getDepth());
    }
}