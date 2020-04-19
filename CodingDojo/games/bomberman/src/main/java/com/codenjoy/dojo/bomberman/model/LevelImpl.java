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

import com.codenjoy.dojo.bomberman.interfaces.ILevel;
import com.codenjoy.dojo.bomberman.services.DefaultGameSettings;
import com.codenjoy.dojo.bomberman.services.MapLoader;
import com.codenjoy.dojo.bomberman.services.OptionGameSettings;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.LengthToXY;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.settings.Settings;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import static com.codenjoy.dojo.bomberman.model.Elements.*;
import static java.util.stream.Collectors.toList;

//Только настройки уровня
public class LevelImpl extends OptionGameSettings implements ILevel {
    private LengthToXY xy;
    private String map;
    private int size;
    private List<Wall> listOfWalls;
    private boolean isChanged = false;

    public LevelImpl(Settings settings, Dice dice) {
        super(settings, dice);
        this.map = MapLoader.loadMapFromFile();
        this.init();
    }

    @Override
    public int getSize() {
        return size;
    }

    @Override
    public List<Wall> getWalls() {
        return listOfWalls.parallelStream().map(w->new Wall(w)).collect(toList());
    }

    private void init(){
        size = (int) Math.sqrt(map.length());
        xy = new LengthToXY(getSize());
        listOfWalls = pointsOf(WALL).parallelStream().map(pt -> new Wall(pt)).collect(toList());
        setBoardSizeParameter(size);
        isChanged = true;
    }

    private List<Point> pointsOf(Elements el) {
        List<Point> result = new LinkedList<>();
        for (int index = 0; index < map.length(); index++) {
            if (map.charAt(index) == el.ch()) {
                result.add(xy.getXY(index));
            }
        }
        return result;
    }

    @Override
    public void tick() {
        if (settings.<String>getParameter(DefaultGameSettings.CURRENT_FIELD_MAP_PARAM).changed()){
            map = MapLoader.loadMapFromFile(settings.<String>getParameter(DefaultGameSettings.CURRENT_FIELD_MAP_PARAM).getValue());
            init();
            settings.<String>getParameter(DefaultGameSettings.CURRENT_FIELD_MAP_PARAM).changesReacted();
        }
    }

    @Override
    public MeatChoppers getMeatChoppers() {
        return new MeatChoppers(this.meatChoppersCount, getDice());
    }

    @Override
    public EatSpaceWalls getEatSpacesWalls() {
        return new EatSpaceWalls(this.destroyWallCount, getDice());
    }

    @Override
    public boolean isChanged() {
        return isChanged;
    }

    public void changesReacted() {
        isChanged = false;
    }
}
