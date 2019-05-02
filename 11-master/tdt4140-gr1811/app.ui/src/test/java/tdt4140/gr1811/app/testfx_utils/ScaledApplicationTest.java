package tdt4140.gr1811.app.testfx_utils;

import javafx.geometry.Bounds;
import javafx.scene.Node;

import java.util.Objects;

import org.testfx.api.FxRobotContext;
import org.testfx.framework.junit.ApplicationTest;
import org.testfx.service.locator.impl.BoundsLocatorImpl;
import org.testfx.service.locator.impl.PointLocatorImpl;

/*
 * See: https://github.com/TestFX/TestFX/issues/245
 * 
 * - Thomas
 */

public abstract class ScaledApplicationTest extends ApplicationTest {
    public ScaledApplicationTest(){
    	if (!Objects.equals(System.getProperty("testfx.headless"), "true")) {
    		FxRobotContext context = robotContext();
            context.setBoundsLocator(new BoundsLocatorImpl(){
                @Override
                public Bounds boundsOnScreenFor(Node node) {
                    Bounds bounds = super.boundsOnScreenFor(node);
                    return ScaledBounds.wrap(bounds);
                }
            });
            robotContext().setPointLocator(new PointLocatorImpl(context.getBoundsLocator()));
    	}
    }
}
