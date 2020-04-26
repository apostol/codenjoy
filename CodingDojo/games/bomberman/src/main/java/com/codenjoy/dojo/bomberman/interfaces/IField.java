package com.codenjoy.dojo.bomberman.interfaces;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 Codenjoy
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import com.codenjoy.dojo.bomberman.model.*;
import com.codenjoy.dojo.services.multiplayer.GameField;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.Point;

import java.util.Collection;
import java.util.List;

public interface IField extends GameField<Player> {  // TODO применить тут ISP (все ли методы должны быть паблик?)
    IGameSettings getGameSettings(); //насройки игры
    PlayerLayer getPlayerLayer(); //работа с игроками
    MeatChopperLayer getMeatChoppersLayer(); //работа с мясниками
    DestroyWallLayer getDestroyWallsLayer(); //работа с уничтожаемыми стенами
    BombsLayer getBombsLayer(); //работа с бомбами
    WallLayer getWallsLayer(); //работа со стенами
    MapLayer getMapLayer(); //работа с картой
}