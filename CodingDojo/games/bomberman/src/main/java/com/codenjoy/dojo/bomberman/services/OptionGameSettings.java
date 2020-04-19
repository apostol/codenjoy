package com.codenjoy.dojo.bomberman.services;

import com.codenjoy.dojo.bomberman.interfaces.IGameSettings;

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

import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.multiplayer.MultiplayerType;
import com.codenjoy.dojo.services.settings.Parameter;
import com.codenjoy.dojo.services.settings.Settings;

import java.util.Arrays;
import java.util.List;

public abstract class OptionGameSettings extends DefaultGameSettings {

    protected final Parameter<Integer> bombPower;
    protected final Parameter<Integer> bombsCount;
    protected final Parameter<Integer> boardSize;
    protected final Parameter<Integer> playersPerField;
    protected final Parameter<String>  gameType;
    protected final Parameter<Integer> meatChoppersCount;
    protected final Parameter<Integer> destroyWallCount;
    protected final Parameter<String>  currentMap;
    protected Settings settings;
 
    public OptionGameSettings(Settings settings, Dice dice) {
        super(dice);
        this.settings = settings;
        this.bombsCount = settings.addEditBox(DefaultGameSettings.BOMBS_COUNT_PARAM).type(Integer.class).def(DefaultGameSettings.BOMBS_COUNT);
        this.bombPower = settings.addEditBox(DefaultGameSettings.BOMB_POWER_PARAM).type(Integer.class).def(DefaultGameSettings.BOMB_POWER);
        this.boardSize = settings.addEditBox(DefaultGameSettings.BOARD_SIZE_PARAM).type(Integer.class).def(DefaultGameSettings.BOARD_SIZE);
        this.destroyWallCount = settings.addEditBox(DefaultGameSettings.DESTROY_WALL_COUNT_PARAM).type(Integer.class).def(boardSize.getValue()*boardSize.getValue()/10);
        this.meatChoppersCount = settings.addEditBox(DefaultGameSettings.MEAT_CHOPPERS_COUNT_PARAM).type(Integer.class).def(DefaultGameSettings.MEAT_CHOPPERS_COUNT);
        this.playersPerField = settings.addEditBox(DefaultGameSettings.PLAYERS_PER_FIELD_PARAM).type(Integer.class).def(DefaultGameSettings.PLAYERS_PER_FIELD);
        this.gameType = settings.addSelect(DefaultGameSettings.GAME_TYPE_PARAM, Arrays.asList("MULTIPLAYER", "TEAM")).type(String.class).def(DefaultGameSettings.GAME_TYPE);
        this.currentMap = settings.addEditBox(DefaultGameSettings.CURRENT_FIELD_MAP_PARAM).type(String.class).def(DefaultGameSettings.CURRENT_FIELD_MAP);
    }

    public MultiplayerType getGameType(){
        switch (gameType.getValue()){
            case "MULTIPLAYER":
                return MultiplayerType.MULTIPLE;
            case "TEAM": {
                return MultiplayerType.TEAM.apply(this.playersPerField.getValue(), !MultiplayerType.DISPOSABLE);
            }
            default:
                return MultiplayerType.MULTIPLE; //все на одной карте
        }
    }

    @Override
    public Parameter<String> getGameTypeParameter() {
        return gameType;
    }

    @Override
    public Parameter<String> getCurrentMapParameter(){  return this.currentMap; }

    public void setBoardSizeParameter(int size){
        boardSize.update(size);
    }

    @Override
    public Parameter<Integer> getBoardSizeParameter() {
        return boardSize;
    }

    @Override
    public Parameter<Integer> getBombsPowerParameter() {
        return bombPower;
    }

    @Override
    public Parameter<Integer> getBombsCountParameter() {
        return bombsCount;
    }



    // @Override
    // public ILevel getLevel() {
    //     return new Level() {
    //         @Override
    //         public int bombsCount() {
    //             return bombsCount.getValue();
    //         }

    //         @Override
    //         public int bombsPower() {
    //             return bombPower.getValue();
    //         }
    //     };
    // }

    // @Override
    // public Walls getWalls(Bomberman board) {
    //     OriginalWalls originalWalls = new OriginalWalls(boardSize);
    //     MeatChoppers meatChoppers = new MeatChoppers(originalWalls, board, meatChoppersCount, dice);
    //     EatSpaceWalls eatWalls = new EatSpaceWalls(meatChoppers, board, destroyWallCount, dice);
    //     return eatWalls;
    // }

    // @Override
    // public Hero getBomberman(Level level) {
    //     return new Hero(level, dice);
    // }
}
