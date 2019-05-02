package tdt4140.gr1811.app.ui;

public class Headless {

	public static void headless() {
		if (Boolean.valueOf(System.getProperty("gitlab-ci", "false"))) {
			System.out.println("Is runnning FxAppTest, gitlab-ci is true");
			System.setProperty("prism.verbose", "true"); // optional
			System.setProperty("java.awt.headless", "true");
			System.setProperty("testfx.robot", "glass");
			System.setProperty("testfx.headless", "true");
			System.setProperty("glass.platform", "Monocle");
			System.setProperty("monocle.platform", "Headless");
			System.setProperty("prism.order", "sw");
			System.setProperty("prism.text", "t2k");
			System.setProperty("testfx.setup.timeout", "60000");
		}
	}

}
