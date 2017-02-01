package com.robo4j.lego.tank.example;

import com.robo4j.core.RoboSystem;
import com.robo4j.core.configuration.Configuration;
import com.robo4j.core.configuration.ConfigurationFactory;
import com.robo4j.core.unit.HttpUnit;
import com.robo4j.core.util.SystemUtil;
import com.robo4j.lego.tank.example.controller.TankExampleController;
import com.robo4j.units.lego.BrickButtonsUnit;
import com.robo4j.units.lego.LcdUnit;
import com.robo4j.units.lego.SimpleTankUnit;

import com.robo4j.units.lego.util.BrickUtils;
import lejos.hardware.Button;

/**
 * @author Marcus Hirt (@hirt)
 * @author Miro Wengner (@miragemiko)
 * @since 30.01.2017
 */
public class TankExampleMain {

    protected static final int PORT = 8025;

    public static void main(String[] args) throws Exception{
        RoboSystem system = new RoboSystem();
        Configuration config = ConfigurationFactory.createEmptyConfiguration();

        HttpUnit http = new HttpUnit(system, "http");
        config.setString("target", "controller");
        config.setInteger("port", PORT);
        http.initialize(config);

        BrickButtonsUnit brickButtonsUnit = new BrickButtonsUnit(system, "buttons");
        config = ConfigurationFactory.createEmptyConfiguration();
        config.setString("target", "controller");
        config.setString(BrickUtils.PREFIX_BUTTON.concat("right"), "left");
        config.setString(BrickUtils.PREFIX_BUTTON.concat("left"), "right");
        config.setString(BrickUtils.PREFIX_BUTTON.concat("up"), "down");
        config.setString(BrickUtils.PREFIX_BUTTON.concat("down"), "up");
        config.setString(BrickUtils.PREFIX_BUTTON.concat("enter"), "enter");
        brickButtonsUnit.initialize(config);


        TankExampleController ctrl = new TankExampleController(system, "controller");
        config = ConfigurationFactory.createEmptyConfiguration();
        config.setString("target", "platform");
        ctrl.initialize(config);

        SimpleTankUnit platform = new SimpleTankUnit(system, "platform");
        config = ConfigurationFactory.createEmptyConfiguration();
        config.setString("leftMotorPort", "B");
        config.setCharacter("leftMotorType", 'N');
        config.setString("rightMotorPort", "C");
        config.setCharacter("rightMotorType", 'N');
        platform.initialize(config);

        LcdUnit lcd = new LcdUnit(system, "lcd");
        config = ConfigurationFactory.createEmptyConfiguration();
        lcd.initialize(config);

        system.addUnits(http, ctrl, platform, brickButtonsUnit, lcd);
        system.start();
        lcd.onMessage("http server Port:" + PORT );
        lcd.onMessage("Usage: Request GET:");
        lcd.onMessage("http://<IP>:" + PORT + "?type");
        lcd.onMessage("=tank&command=stop");
        lcd.onMessage("commands: stop, move, back, left, right");

        Button.ESCAPE.waitForPressAndRelease();
        system.shutdown();

    }

}
