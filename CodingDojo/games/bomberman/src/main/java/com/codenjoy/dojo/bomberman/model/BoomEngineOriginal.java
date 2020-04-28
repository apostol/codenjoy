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

import java.util.LinkedList;
import java.util.List;
import static com.codenjoy.dojo.services.PointImpl.*;
import com.codenjoy.dojo.bomberman.interfaces.IBoomEngine;

public class BoomEngineOriginal implements IBoomEngine {

    private Hero bomberman;
    private final List<Blast> blasts = new LinkedList<>();

    public BoomEngineOriginal(Hero bomberman) {
        this.bomberman = bomberman;
    }

    @Override
    public List<Blast> boom(List<? extends Point> barriers, int boardSize, Point source, int radius) {

        addBlast(source.getX(), source.getY());

        for (int dx = 1; dx <= radius; dx++) {
            int x = source.getX() + dx;
            int y = source.getY() + 0;
            if (!add(barriers, boardSize, blasts, x, y, radius)) {
                break;
            }
        }

        for (int dx = -1; dx >= -radius; dx--) {
            int x = source.getX() + dx;
            int y = source.getY() + 0;
            if (!add(barriers, boardSize, blasts, x, y, radius)) {
                break;
            }
        }

        for (int dy = 1; dy <= radius; dy++) {
            int x = source.getX() + 0;
            int y = source.getY() + dy;

            if (!add(barriers, boardSize, blasts, x, y, radius)) {
                break;
            }
        }

        for (int dy = -1; dy >= -radius; dy--) {
            int x = source.getX() + 0;
            int y = source.getY() + dy;

            if (!add(barriers, boardSize, blasts, x, y, radius)) {
                break;
            }
        }
        return blasts;
    }

    private boolean add(List<? extends Point> barriers, int boardSize, List<Blast> blasts, int x, int y, int radius) {
        Point pt = pt(x, y);

        if (!isOnBoard(pt, boardSize)) {
            return false;
        }
        int index = barriers.indexOf(pt);
        if (index>-1) {
            if (!barriers.get(index).getClass().equals(Wall.class)) {   //Стена
                if (barriers.get(index).getClass().equals(Bomb.class)) {    //Детанация
                    barriers.remove(index);
                    boom(barriers, boardSize, pt, radius);
                    return false;
                }
                addBlast(x, y);
            }
            return false;
        }
        addBlast(x, y);
        return true;
    }

    private boolean isOnBoard(Point pt, int boardSize) {
        return pt.getX() >= 0 && pt.getY() >= 0 && pt.getX() < boardSize && pt.getY() < boardSize;
    }

    private void addBlast(int x, int y) {
        if (!itsBlast(x, y)) {
            blasts.add(new Blast(x, y, bomberman));
        }
    }

    private boolean itsBlast(int x, int y) {
        return blasts.stream().anyMatch(r->r.itsMe(x, y));
    }
}
