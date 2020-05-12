package com.codenjoy.dojo.snakebattle.services;

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


import static com.codenjoy.dojo.services.settings.SimpleParameter.v;
import static com.codenjoy.dojo.snakebattle.model.level.custom.CutsomMaps.SMALL;

import com.codenjoy.dojo.client.ClientBoard;
import com.codenjoy.dojo.client.Solver;
import com.codenjoy.dojo.services.AbstractGameType;
import com.codenjoy.dojo.services.EventListener;
import com.codenjoy.dojo.services.GameType;
import com.codenjoy.dojo.services.PlayerScores;
import com.codenjoy.dojo.services.multiplayer.GameField;
import com.codenjoy.dojo.services.multiplayer.GamePlayer;
import com.codenjoy.dojo.services.multiplayer.MultiplayerType;
import com.codenjoy.dojo.services.settings.Parameter;
import com.codenjoy.dojo.snakebattle.client.Board;
import com.codenjoy.dojo.snakebattle.client.ai.AISolver;
import com.codenjoy.dojo.snakebattle.model.Elements;
import com.codenjoy.dojo.snakebattle.model.Player;
import com.codenjoy.dojo.snakebattle.model.board.SnakeBoard;
import com.codenjoy.dojo.snakebattle.model.board.Timer;
import com.codenjoy.dojo.snakebattle.model.level.Level;
import com.codenjoy.dojo.snakebattle.model.level.LevelImpl;
import com.codenjoy.dojo.snakebattle.model.level.custom.CutsomMaps;

public class GameRunner extends AbstractGameType implements GameType {

    private Level level;
    private final Parameter<Integer> timeBeforeStart;
    private final Parameter<Integer> roundsPerMatch;
    private final Parameter<Integer> playersPerRoom;
    private final Parameter<Integer> flyingCount;
    private final Parameter<Integer> flyingPillsPerTick;
    private final Parameter<Integer> furyCount;
    private final Parameter<Integer> furyPillsPerTick;
    private final Parameter<Integer> stoneReducedValue;
    private final Parameter<Integer> minTicksForWin;
    private final Parameter<Integer> timePerRound;
    private final Parameter<Integer> timeForWinner;
    private final Parameter<Boolean> virtualRooms;
    private final Parameter<String> mapPath;
    private final Parameter<String> levelSize;

    public GameRunner() {
        new Scores(0, settings);
        timePerRound = settings.addEditBox("Time per Round").type(Integer.class).def(300);
        timeForWinner = settings.addEditBox("Time for Winner").type(Integer.class).def(1);
        timeBeforeStart = settings.addEditBox("Time before start Round").type(Integer.class).def(5);
        roundsPerMatch = settings.addEditBox("Rounds per Match").type(Integer.class).def(1);
        playersPerRoom = settings.addEditBox("Players per Room").type(Integer.class).def(5);
        flyingCount = settings.addEditBox("Flying count").type(Integer.class).def(10);
        flyingPillsPerTick = settings.addEditBox("Flying pills count per tick").type(Integer.class).def(0);
        furyCount = settings.addEditBox("Fury count").type(Integer.class).def(10);
        furyPillsPerTick = settings.addEditBox("Fury pills count per tick").type(Integer.class).def(0);
        stoneReducedValue = settings.addEditBox("Stone reduced value").type(Integer.class).def(3);
        minTicksForWin = settings.addEditBox("Min length for win").type(Integer.class).def(40);
        virtualRooms = settings.addCheckBox("Virtual rooms for training").type(Boolean.class).def(true);
        mapPath = settings.addEditBox("Map file path").type(String.class).def("");
        levelSize = settings.addSelect("Map size",  CutsomMaps.maps()).type(String.class).def(SMALL.name());
        level = new LevelImpl(CutsomMaps.byName(levelSize.getValue()).getMap());
    }

    public GameField createGame(int levelNumber) {
        level = new LevelImpl(getMap());

        return new SnakeBoard(level, getDice(),
                new Timer(timeBeforeStart),
                new Timer(timePerRound),
                new Timer(timeForWinner),
                roundsPerMatch,
                flyingCount,
                furyCount,
                stoneReducedValue,
                minTicksForWin,
                furyPillsPerTick,
                flyingPillsPerTick
            );
    }

    private String getMap() {
        String map = getMapFromFile(mapPath.getValue());
        if (map != null) {
            return map;
        }
            return CutsomMaps.byName(levelSize.getValue()).getMap();
    }

    private String getMapFromFile(String path) {
        return MapLoader.loadMapFromFile(path);
    }

    @Override
    public PlayerScores getPlayerScores(Object score) {
        return new Scores((Integer) score, settings);
    }

    @Override
    public Parameter<Integer> getBoardSize() {
        return v(level.getSize());
    }

    @Override
    public String name() {
        return "snakebattle";
    }

    @Override
    public Enum[] getPlots() {
        return Elements.values();
    }

    @Override
    public Class<? extends Solver> getAI() {
        return AISolver.class;
    }

    @Override
    public Class<? extends ClientBoard> getBoard() {
        return Board.class;
    }

    @Override
    public MultiplayerType getMultiplayerType() {
        if (virtualRooms.getValue()) {
            return MultiplayerType.TEAM.apply(playersPerRoom.getValue(), MultiplayerType.DISPOSABLE);
        }

        return MultiplayerType.MULTIPLE;
    }

    @Override
    public GamePlayer createPlayer(EventListener listener, String playerName) {
        return new Player(listener);
    }
}
