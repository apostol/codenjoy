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

import com.codenjoy.dojo.bomberman.interfaces.IField;
import com.codenjoy.dojo.bomberman.services.MapLoader;
import com.codenjoy.dojo.services.LengthToXY;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.settings.Parameter;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import static com.codenjoy.dojo.bomberman.model.Elements.WALL;
import static com.codenjoy.dojo.services.PointImpl.pt;
import static java.util.stream.Collectors.toList;

public class MapLayer implements Iterable<Point> {

    private final LinkedList<Point> cells = new LinkedList<>(); //в одной ячейки может быть несколько классов.
    private final IField field;
    private final Parameter<String> currentMap;
    private final Parameter<Integer> boardSize;
    private LengthToXY.Map map;
    private String sourceMap = "---";
    private LinkedList<Point> free = new LinkedList<Point>();

    public MapLayer(IField board) {
        this.field = board;
        this.currentMap = field.getGameSettings().getCurrentMapParameter();
        this.boardSize = field.getGameSettings().getBoardSizeParameter();
    }

    public void load(String mapLink) {
        String stringMap = MapLoader.loadMapFromFile(mapLink);
        map = new LengthToXY.Map(stringMap);
        boardSize.update(map.getSize());
    }

    public void update() {
        free.clear(); //чистим все предыдущие точки
        if (currentMap.getValue() != sourceMap) {
            sourceMap = currentMap.getValue();
            load(sourceMap);
            field.getWallsLayer().clear();
            field.getWallsLayer().add(pointsOf(WALL).parallelStream().map(Wall::new).collect(toList()));
            field.getBombsLayer().clear();
            field.getDestroyWallsLayer().clear();
            field.getMeatChoppersLayer().clear();
            field.getPlayerLayer().reset();
        }
        generateMap();
    }

    @Override
    public Iterator<Point> iterator() {
        return cells.iterator();
    }

    public void clear() {
        cells.clear();
    }

//    public boolean itsMe(int x, int y, Elements element) {
//      return getAt(x, y) == element;
//    }

    public List<Point> generateMap() {
        cells.clear();
        cells.addAll(field.getPlayerLayer().getHeroes());
        cells.addAll(field.getWallsLayer().getList());
        cells.addAll(field.getMeatChoppersLayer().getList());
        cells.addAll(field.getDestroyWallsLayer().getList());
        cells.addAll(field.getBombsLayer().getBombs());
        cells.addAll(field.getBombsLayer().getBlasts());
        return cells;
    }

    public boolean isBarrier(int x, int y, boolean isWithMeatChopper) {
        int size = getSize();
        if (x > 0 && y > 0 && x < size - 1 && y < size - 1) { //isWithMeatChopper - проверить на чопера, так как я должен иметь возможносьт сходить на эту позицию. 
            if (cells.parallelStream().noneMatch(s -> s.itsMe(x, y))) {
                return false;
            }
        }
        return true;
        /*
        if (!wallsLayer.itsMe(x, y)) {
            if (!playerLayer.itsMe(x,y)) {
                if (!bombsLayer.itsMe(x,y)) {
                    if (!meatChoppersLayer.itsMe(x, y)) {
                        if (!destroyWallsLayer.itsMe(x, y)) {
                            return x < 0 || y < 0 || x > size - 1 || y > size - 1;
                        }
                    }
                }
            }
        }
        return true;
         */
    }

    public List<Point> getList() {
        return cells;
    }

    public void add(Point element) {
        cells.add(element);
    }

    public List<Point> get(int x, int y) {
        return cells.parallelStream().filter(p -> p.getX() == x && p.getY() == y).collect(toList());
    }

    public LinkedList<Point> getFreeCells() {
        if (free.size() == 0) {
            //собираем тольео один раз в секунду
            int size = boardSize.getValue();
            for (int x = 0; x < size; x++) {
                for (int y = 0; y < size; y++) {
                    final int x1 = x;
                    final int y1 = y;
                    if (cells.parallelStream().noneMatch(s -> s.itsMe(x1, y1))) {
                        free.add(pt(x, y));
                    }
                }
            }
        }
        return free;
    }


//    public setAt(int x, int y, Elements element){
//      map.setAt(x, y, element.ch());
//    }

//    public Elements getAt(int x, int y){
//      return Elements.valueOf(map.getAt(x, y));
//    }

//    public String getMap() {
//      List<Point> result = new LinkedList<>();
//      String strmap = map.getMap();
//      for (int index = 0; index < strmap.length(); index++) {
//
//        if (strmap.charAt(index) == el.ch()) {
//          result.add();
//        }
//      }
//      return result;
//    }

    private Point getXY(int length) {
        int size = boardSize.getValue();
        if (length == -1) {
            return null;
        }
        return pt(length % size, size - 1 - length / size);
    }

    private List<Point> pointsOf(Elements el) {
        List<Point> result = new LinkedList<>();
        String strmap = map.getMap();
        for (int index = 0; index < strmap.length(); index++) {
            if (strmap.charAt(index) == el.ch()) {
                result.add(getXY(index));
            }
        }
        return result;
    }

    public int getSize() {
        return this.boardSize.getValue();
    }
}
