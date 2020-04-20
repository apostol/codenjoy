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

import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.settings.Parameter;
import com.codenjoy.dojo.bomberman.interfaces.IField;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class DestroyWallLayer implements Iterable<DestroyWall> {

    private final LinkedList<DestroyWall> destroyWalls = new LinkedList<>();
    private final IField field;
    private final Parameter<Integer> count;
    private final LinkedList<DestroyWall> toDestroyList = new LinkedList<>();

    public DestroyWallLayer(IField field) {
      this.field = field;
      this.count = field.getGameSettings().getDestroyWallCountParameter(); //TODO: должно быть в отдельном классе с настройками
    }

    public void update() {
      toDestroyList.forEach(dw->destroyWalls.remove(dw)); //убираем разрушенные стены
      toDestroyList.clear();
      if (count.getValue() < 0) {
        count.update(0);
      }
      int c = destroyWalls.size();
      List<Point> free = field.getMapLayer().getFreeCells(); //получаем все свободные ячейки
      while (c < count.getValue()) {
        Point position = free.remove(field.getGameSettings().getDice().next(free.size()));
        destroyWalls.add(new DestroyWall(position.getX(), position.getY()));
        c++;
      }
    }

    public void regenerate() {
        //все генерируется в методе update - который вызывается регулярно
    }

    @Override
    public Iterator<DestroyWall> iterator() {
        return destroyWalls.iterator();
    }

    public void clear() {
          destroyWalls.clear();
    }

    public boolean itsMe(int x, int y) {
          return destroyWalls.stream().anyMatch(r->r.itsMe(x, y));
    }

    public List<DestroyWall> getList() {
      return destroyWalls;
    }

    public void addToDestroy(DestroyWall destroyWall) {
      toDestroyList.add(destroyWall);
    }

}
