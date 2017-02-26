package com.robo4j.lego.tank.example;

import java.io.IOException;

import com.robo4j.core.RoboBuilder;
import com.robo4j.core.RoboBuilderException;
import com.robo4j.core.RoboContext;
import com.robo4j.core.RoboReference;
import com.robo4j.core.client.util.RoboClassLoader;
import com.robo4j.core.util.SystemUtil;
import com.robo4j.hw.lego.util.EscapeButtonUtil;

/**
 * @author Marcus Hirt (@hirt)
 * @author Miro Wengner (@miragemiko)
 */
public class TankExampleDeclarativeMain {

	public static void main(String[] args) throws RoboBuilderException, IOException {
		RoboBuilder builder = new RoboBuilder().add(RoboClassLoader.getInstance().getResource("robo4j.xml"));
		RoboContext ctx = builder.build();

		System.out.println("State before start:");
		System.out.println(SystemUtil.generateStateReport(ctx));
		ctx.start();

		System.out.println("State after start:");
		System.out.println(SystemUtil.generateStateReport(ctx));

		RoboReference<Object> httpRef = ctx.getReference("http");
		System.out.println(
				"RoboSystem http server\n\tPort:" + httpRef.getConfiguration().getInteger("port", null) + "\n");
		System.out.println("Usage:\n\tRequest GET: http://<IP_ADDRESS>:"
				+ httpRef.getConfiguration().getInteger("port", null) + "?type=lcd&command=down");
		System.out.println("\tRequest command types: up, down, select, left, right\n");

		System.out.println("Press Escape to quit!");
		EscapeButtonUtil.waitForPressAndRelease();
		System.out.println("Press Going Down!");
		ctx.shutdown();
	}
}
