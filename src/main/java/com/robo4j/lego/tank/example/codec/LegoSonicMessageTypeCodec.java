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

package com.robo4j.lego.tank.example.codec;

import com.robo4j.core.httpunit.HttpDecoder;
import com.robo4j.core.httpunit.HttpEncoder;
import com.robo4j.core.httpunit.HttpProducer;
import com.robo4j.core.httpunit.codec.SimpleCommand;
import com.robo4j.core.httpunit.codec.SimpleCommandCodec;
import com.robo4j.units.lego.enums.LegoSonicMessageTypeEnum;

/**
 * @author Marcus Hirt (@hirt)
 * @author Miro Wengner (@miragemiko)
 */
@HttpProducer
public class LegoSonicMessageTypeCodec implements HttpDecoder<LegoSonicMessageTypeEnum>, HttpEncoder<LegoSonicMessageTypeEnum> {
    private final SimpleCommandCodec codec = new SimpleCommandCodec();

    @Override
    public LegoSonicMessageTypeEnum decode(String json) {
        final SimpleCommand simpleCommand = codec.decode(json);
        return LegoSonicMessageTypeEnum.getInternalByName(simpleCommand.getValue());
    }

    @Override
    public Class<LegoSonicMessageTypeEnum> getDecodedClass() {
        return LegoSonicMessageTypeEnum.class;
    }

    @Override
    public String encode(LegoSonicMessageTypeEnum legoSonicMessageTypeEnum) {
        final SimpleCommand simpleCommand = new SimpleCommand(legoSonicMessageTypeEnum.getName());
        return codec.encode(simpleCommand);
    }

    @Override
    public Class<LegoSonicMessageTypeEnum> getEncodedClass() {
        return LegoSonicMessageTypeEnum.class;
    }
}
