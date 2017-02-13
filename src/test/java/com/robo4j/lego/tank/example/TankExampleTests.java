package com.robo4j.lego.tank.example;

import org.junit.Test;

import com.robo4j.core.RoboSystem;
import com.robo4j.core.configuration.Configuration;
import com.robo4j.core.configuration.ConfigurationFactory;
import com.robo4j.core.unit.HttpServerUnit;
import com.robo4j.lego.tank.example.controller.TankExampleController;
import com.robo4j.units.lego.LcdTestUnit;
import com.robo4j.units.lego.SimpleTankTestUnit;

;

/**
 *
 * Simple Tanks examples tests
 *
 * @author Marcus Hirt (@hirt)
 * @author Miro Wengner (@miragemiko)
 * @since 02.02.2017
 */
public class TankExampleTests {

	@Test
	public void legoTankExampleTest() throws Exception {
		RoboSystem system = new RoboSystem();
		Configuration config = ConfigurationFactory.createEmptyConfiguration();

		HttpServerUnit http = new HttpServerUnit(system, "http");
		config.setString("target", "controller");
		config.setInteger("port", TankExampleMain.PORT);
		/* specific configuration */
		Configuration commands = config.createChildConfiguration("commands");
		commands.setString("path", "tank");
		commands.setString("method", "GET");
		commands.setString("up", "move");
		commands.setString("down", "back");
		commands.setString("left", "right");
		commands.setString("right", "left");
		http.initialize(config);

		TankExampleController ctrl = new TankExampleController(system, "controller");
		config = ConfigurationFactory.createEmptyConfiguration();
		config.setString("target", "platform");
		ctrl.initialize(config);

		/* platform is listening to the bus */
		SimpleTankTestUnit platform = new SimpleTankTestUnit(system, "platform");
		config = ConfigurationFactory.createEmptyConfiguration();
		config.setString("leftMotorPort", "B");
		config.setCharacter("leftMotorType", 'N');
		config.setString("rightMotorPort", "C");
		config.setCharacter("rightMotorType", 'N');
		platform.initialize(config);

		/* lcd is listening to the bus */
		LcdTestUnit lcd = new LcdTestUnit(system, "lcd");
		config = ConfigurationFactory.createEmptyConfiguration();
		lcd.initialize(config);

		// BasicSonicUnit sonic = new BasicSonicUnit(system, "sonic");
		// config = ConfigurationFactory.createEmptyConfiguration();
		// config.setString("target", "controller");
		// sonic.initialize(config);

		// system.addUnits(http, ctrl, platform, lcd, sonic);
		system.addUnits(http, ctrl, platform, lcd);
		system.start();
		lcd.onMessage("http server Port:" + TankExampleMain.PORT);
		lcd.onMessage("Usage: Request GET:");
		lcd.onMessage("http://<IP>:" + TankExampleMain.PORT + "/tank?");
		lcd.onMessage("command=stop");
		lcd.onMessage("commands: stop, move, back, left, right");

		lcd.onMessage("Press Key to end...");
//		System.in.read();

		system.shutdown();

	}
}
