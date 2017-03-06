/*
 * Copyright (c) 2014, 2017, Marcus Hirt, Miroslav Wengner
 *
 * Robo4J is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Robo4J is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Robo4J. If not, see <http://www.gnu.org/licenses/>.
 */

package com.robo4j.lego.tank.example;

import com.robo4j.core.RoboSystem;
import com.robo4j.core.client.util.RoboHttpUtils;
import com.robo4j.core.configuration.Configuration;
import com.robo4j.core.configuration.ConfigurationFactory;
import com.robo4j.core.httpunit.HttpServerUnit;
import com.robo4j.core.util.SystemUtil;
import com.robo4j.hw.lego.util.BrickUtils;
import com.robo4j.hw.lego.util.EscapeButtonUtil;
import com.robo4j.lego.tank.example.controller.TankExampleController;
import com.robo4j.lego.tank.example.controller.TankSonicController;
import com.robo4j.units.lego.BasicSonicUnit;
import com.robo4j.units.lego.BrickButtonsUnit;
import com.robo4j.units.lego.LcdUnit;
import com.robo4j.units.lego.SimpleTankUnit;

/**
 * @author Marcus Hirt (@hirt)
 * @author Miro Wengner (@miragemiko)
 */
public class TankExampleMain {

	private static final String CONTROLLER_PLATFORM = "controller";
	private static final String CONTROLLER_SONIC = "sonicController";
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
		config.setString("target", CONTROLLER_PLATFORM.concat(",").concat(CONTROLLER_SONIC));
		config.setInteger("port", PORT);
		/* specific configuration */
		Configuration targetUnits = config.createChildConfiguration(RoboHttpUtils.HTTP_TARGET_UNITS);
		targetUnits.setString(CONTROLLER_PLATFORM, "GET");
		targetUnits.setString(CONTROLLER_SONIC, "GET");
		http.initialize(config);

		BrickButtonsUnit brickButtonsUnit = new BrickButtonsUnit(result, "buttons");
		config = ConfigurationFactory.createEmptyConfiguration();
		config.setString("target", CONTROLLER_PLATFORM);
		config.setString(BrickUtils.PREFIX_BUTTON.concat("right"), "left");
		config.setString(BrickUtils.PREFIX_BUTTON.concat("left"), "right");
		config.setString(BrickUtils.PREFIX_BUTTON.concat("up"), "down");
		config.setString(BrickUtils.PREFIX_BUTTON.concat("down"), "up");
		config.setString(BrickUtils.PREFIX_BUTTON.concat("enter"), "enter");
		brickButtonsUnit.initialize(config);

		TankExampleController platformCtrl = new TankExampleController(result, CONTROLLER_PLATFORM);
		config = ConfigurationFactory.createEmptyConfiguration();
		config.setString("target", "platform");
		platformCtrl.initialize(config);

		TankSonicController sonicCtrl = new TankSonicController(result, CONTROLLER_SONIC);
		config = ConfigurationFactory.createEmptyConfiguration();
		config.setString("target", "sonic");
		sonicCtrl.initialize(config);

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
		config.setString("target", "sonicBrain");
		sonic.initialize(config);


		result.addUnits(http, platformCtrl, sonicCtrl, platform, brickButtonsUnit, lcd, sonic);

		result.start();
		lcd.onMessage(SystemUtil.generateSocketPoint(http, platformCtrl));
		lcd.onMessage(SystemUtil.generateSocketPoint(http, sonicCtrl));
		System.out.println(SystemUtil.generateSocketPoint(http, platformCtrl));
		System.out.println(SystemUtil.generateSocketPoint(http, sonicCtrl));

		return result;

	}

	public void shutdown(RoboSystem system){
		EscapeButtonUtil.waitForPressAndRelease();
		system.shutdown();
	}

	// Private Methdos

}
