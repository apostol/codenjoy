package com.codenjoy.dojo.bomberman.services;

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

import com.codenjoy.dojo.bomberman.interfaces.*;
import com.codenjoy.dojo.services.Dice;

public abstract class DefaultGameSettings implements IGameSettings {

    public static int MEAT_CHOPPERS_COUNT = 1;
    public static int BOMB_POWER = 3;
    public static int BOMBS_COUNT = 5;
    public static int BOARD_SIZE = 10;
    public static int DESTROY_WALL_COUNT = BOARD_SIZE * BOARD_SIZE / 10;
    public static int PLAYERS_PER_FIELD = 2;
    public static String CURRENT_FIELD_MAP = "";
    public static String GAME_TYPE = "TEAM";
    protected final Dice dice;

    public static String CURRENT_FIELD_MAP_PARAM = "Current field map";
    public static String MEAT_CHOPPERS_COUNT_PARAM = "Meat choppers count";
    public static String BOMB_POWER_PARAM = "Bomb power";
    public static String BOMBS_COUNT_PARAM = "Bombs count";
    public static String BOARD_SIZE_PARAM = "Board size";
    public static String DESTROY_WALL_COUNT_PARAM = "Destroy wall count";
    public static String PLAYERS_PER_FIELD_PARAM = "Players per field";
    public static String GAME_TYPE_PARAM = "Game type";

    public DefaultGameSettings(Dice dice) {
        this.dice = dice;
    }

    public Dice getDice(){
        return dice;
    }
}
