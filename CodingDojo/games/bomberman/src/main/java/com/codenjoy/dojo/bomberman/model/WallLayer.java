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
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class WallLayer implements Iterable<Wall> {

    private final LinkedList<Wall> walls = new LinkedList<>();
    private final IField field;

    public WallLayer(IField board) {
        this.field = board;
    }

    @Override
    public Iterator<Wall> iterator() {
        return walls.iterator();
    }

    public void clear() {
        walls.clear();
    }

    public boolean itsMe(int x, int y) {
        return walls.parallelStream().anyMatch(r -> r.itsMe(x, y));
    }

    public List<Wall> getList() {
        return walls;
    }

    public void add(List<Wall> collect) {
        walls.addAll(collect);
    }

    public void regenerate() {
        //ничего не генерим, так как соллекция уже создана.
    }
}
