package com.codenjoy.dojo.bomberman.model;

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

import com.codenjoy.dojo.bomberman.interfaces.IGameSettings;
import com.codenjoy.dojo.services.printer.BoardReader;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.bomberman.interfaces.IField;

public class Bomberman implements IField {

    private final IGameSettings gameSettings;
    private final PlayerLayer playerLayer;
    private final MeatChopperLayer meatChoppersLayer;
    private final DestroyWallLayer destroyWallsLayer;
    private final BombsLayer bombsLayer;
    private final WallLayer wallsLayer;
    private final MapLayer mapLayer;

    public Bomberman(IGameSettings settings) {
        this.gameSettings = settings;
        this.mapLayer = new MapLayer(this);
        this.playerLayer = new PlayerLayer(this);
        this.meatChoppersLayer = new MeatChopperLayer(this);
        this.destroyWallsLayer = new DestroyWallLayer(this);
        this.bombsLayer = new BombsLayer(this);
        this.wallsLayer = new WallLayer(this);
    }

    @Override
    public void tick() {
        playerLayer.update(); //Бомберы переместились
        meatChoppersLayer.update(); //Мясник переместила и пожрал игрока
        destroyWallsLayer.update(); //Стенки угтчтожились
        bombsLayer.update(); //обновление для бомб
        mapLayer.update(); //Обновление карты
    }

    @Override
    public BombsLayer getBombsLayer() {
        return bombsLayer;
    }

    @Override
    public WallLayer getWallsLayer() {
        return wallsLayer;
    }

    @Override
    public void newGame(Player player) {
        mapLayer.update();
        playerLayer.add(player);
    }

    @Override
    public void remove(Player player) {
        playerLayer.remove(player);
    }

    @Override
    public IGameSettings getGameSettings() {
        return gameSettings;
    }

    @Override
    public PlayerLayer getPlayerLayer() {
        return playerLayer;
    }

    @Override
    public MeatChopperLayer getMeatChoppersLayer() {
        return meatChoppersLayer;
    }

    @Override
    public DestroyWallLayer getDestroyWallsLayer() {
        return destroyWallsLayer;
    }

    @Override
    public MapLayer getMapLayer() {
        return mapLayer;
    }

    @Override
    public BoardReader reader() {
        return new BoardReader() {
            @Override
            public int size() {
                return Bomberman.this.mapLayer.getSize();
            }

            @Override
            public Iterable<? extends Point> elements() {
                return Bomberman.this.mapLayer.getList();
            }

        };
    }

}
