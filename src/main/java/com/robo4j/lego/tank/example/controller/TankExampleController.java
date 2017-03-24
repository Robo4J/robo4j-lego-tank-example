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

package com.robo4j.lego.tank.example.controller;

import java.util.Collection;
import java.util.Collections;

import com.robo4j.core.AttributeDescriptor;
import com.robo4j.core.ConfigurationException;
import com.robo4j.core.DefaultAttributeDescriptor;
import com.robo4j.core.RoboContext;
import com.robo4j.core.RoboUnit;
import com.robo4j.core.configuration.Configuration;
import com.robo4j.units.lego.enums.LegoPlatformMessageTypeEnum;
import com.robo4j.units.lego.platform.LegoPlatformMessage;

/**
 * Simple Lego Tank Example
 *
 * @author Marcus Hirt (@hirt)
 * @author Miro Wengner (@miragemiko)
 */
public class TankExampleController extends RoboUnit<LegoPlatformMessageTypeEnum> {

	private String target;

	public TankExampleController(RoboContext context, String id) {
		super(LegoPlatformMessageTypeEnum.class, context, id);
	}

	/**
	 * @param message
	 *            accepted message
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void onMessage(LegoPlatformMessageTypeEnum message) {
		processMessage(message);
	}

	/**
	 * @param configuration
	 *            desired configuration
	 * @throws ConfigurationException
	 */
	@Override
	protected void onInitialization(Configuration configuration) throws ConfigurationException {
		target = configuration.getString("target", null);
		if (target == null) {
			throw ConfigurationException.createMissingConfigNameException("target");
		}
	}

	// Private Methods
	private void sendMessage(RoboContext ctx, LegoPlatformMessage message) {
		ctx.getReference(target).sendMessage(message);
	}

	private void processMessage(LegoPlatformMessageTypeEnum myMessage) {
		sendMessage(getContext(), new LegoPlatformMessage(myMessage));
	}
}
