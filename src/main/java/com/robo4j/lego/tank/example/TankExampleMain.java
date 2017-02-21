package com.robo4j.lego.tank.example;

import com.robo4j.core.RoboSystem;
import com.robo4j.core.client.util.RoboHttpUtils;
import com.robo4j.core.configuration.Configuration;
import com.robo4j.core.configuration.ConfigurationFactory;
import com.robo4j.core.httpunit.HttpServerUnit;
import com.robo4j.hw.lego.util.BrickUtils;
import com.robo4j.hw.lego.util.EscapeButtonUtil;
import com.robo4j.lego.tank.example.controller.TankExampleController;
import com.robo4j.units.lego.BasicSonicUnit;
import com.robo4j.units.lego.BrickButtonsUnit;
import com.robo4j.units.lego.LcdUnit;
import com.robo4j.units.lego.SimpleTankUnit;

/**
 * @author Marcus Hirt (@hirt)
 * @author Miro Wengner (@miragemiko)
 * @since 30.01.2017
 */
public class TankExampleMain {

	protected static final int PORT = 8025;

	public static void main(String[] args) throws Exception {
		TankExampleMain tank = new TankExampleMain();
		RoboSystem system = tank.initSystem();
		tank.shutdown(system);
	}

	public TankExampleMain(){

	}

	public RoboSystem initSystem() throws Exception {
		RoboSystem result = new RoboSystem();
		Configuration config = ConfigurationFactory.createEmptyConfiguration();

		HttpServerUnit http = new HttpServerUnit(result, "http");
		config.setString("target", "controller");
		config.setInteger("port", PORT);
		/* specific configuration */
		Configuration targetUnits = config.createChildConfiguration(RoboHttpUtils.HTTP_TARGET_UNITS);
		targetUnits.setString("controller", "GET");
		http.initialize(config);

		BrickButtonsUnit brickButtonsUnit = new BrickButtonsUnit(result, "buttons");
		config = ConfigurationFactory.createEmptyConfiguration();
		config.setString("target", "controller");
		config.setString(BrickUtils.PREFIX_BUTTON.concat("right"), "left");
		config.setString(BrickUtils.PREFIX_BUTTON.concat("left"), "right");
		config.setString(BrickUtils.PREFIX_BUTTON.concat("up"), "down");
		config.setString(BrickUtils.PREFIX_BUTTON.concat("down"), "up");
		config.setString(BrickUtils.PREFIX_BUTTON.concat("enter"), "enter");
		brickButtonsUnit.initialize(config);

		TankExampleController ctrl = new TankExampleController(result, "controller");
		config = ConfigurationFactory.createEmptyConfiguration();
		config.setString("target", "platform");
		ctrl.initialize(config);

		/* platform is listening to the bus */
		SimpleTankUnit platform = new SimpleTankUnit(result, "platform");
		config = ConfigurationFactory.createEmptyConfiguration();
		config.setString("leftMotorPort", "B");
		config.setCharacter("leftMotorType", 'N');
		config.setString("rightMotorPort", "C");
		config.setCharacter("rightMotorType", 'N');
		platform.initialize(config);

		/* lcd is listening to the bus */
		LcdUnit lcd = new LcdUnit(result, "lcd");
		config = ConfigurationFactory.createEmptyConfiguration();
		lcd.initialize(config);

		BasicSonicUnit sonic = new BasicSonicUnit(result, "sonic");
		config = ConfigurationFactory.createEmptyConfiguration();
		config.setString("target", "controller");
		sonic.initialize(config);


		result.addUnits(http, ctrl, platform, brickButtonsUnit, lcd, sonic);

		result.start();
		lcd.onMessage("http server Port:" + PORT);
		lcd.onMessage("Usage: Request GET:");
		lcd.onMessage("http://<IP>:" + PORT + "?type");
		lcd.onMessage("=tank&command=stop");
		lcd.onMessage("commands: stop, move, back, left, right");

		return result;

	}

	public void shutdown(RoboSystem system){
		EscapeButtonUtil.waitForPressAndRelease();
		system.shutdown();
	}

}
