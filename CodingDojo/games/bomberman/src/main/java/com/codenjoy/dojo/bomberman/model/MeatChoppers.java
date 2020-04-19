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

import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.settings.Parameter;
import com.codenjoy.dojo.bomberman.interfaces.IField;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class MeatChoppers implements Iterable<MeatChopper> {

    private static final boolean WITH_MEATCHOPPERS = true;
    private Parameter<Integer> count;
    private int count_cache = 0;
    private Dice dice;
    private final LinkedList<MeatChopper> list = new LinkedList<>(); ;

    public MeatChoppers (Parameter<Integer> count, Dice dice) {
        this.dice = dice;
        this.count = count;
        this.count_cache = count.getValue();
    }

    public void clear(){
        list.clear();
    }

    public void regenerate(IField board) {
        if (count.getValue() < 0) {
            count.update(0);
        }

        if (count.getValue() != count_cache && count.getValue() > 0) {
            count_cache = count.getValue();
        };

        if (list.size() < count_cache) {
            int c = list.size();
            List<Point> free = board.getFreeCells();
            while (c < count_cache) {
                Point position = free.remove(dice.next(free.size()));
                list.add(new MeatChopper(position.getX(), position.getY()));
                c++;
            }
        }
    }

    public void tick(IField board) {
        regenerate(board);
        list.forEach(meatChopper -> {
            Direction direction = meatChopper.getDirection();
            if (direction != null && dice.next(5) > 0) {
                int x = direction.changeX(meatChopper.getX());
                int y = direction.changeY(meatChopper.getY());
                if (!itsMe(x, y) && !isBarrier(board, x, y)) {
                    meatChopper.move(x, y);
                }
            }
            meatChopper.setDirection(tryToMove(board, meatChopper));
        });
    }

    private boolean isBarrier(IField board, int x, int y){
        if (x <= board.size() && y <= board.size() && x > 0 && y > 0) {
            if (!(itsMe(x, y) && WITH_MEATCHOPPERS)) {
                if (!board.getWalls().parallelStream().anyMatch(b -> b.itsMe(x, y))) {
                    if (!board.getBombs().parallelStream().anyMatch(b -> b.itsMe(x, y))) {
                        return board.getDestroyWall().parallelStream().anyMatch(b -> b.itsMe(x, y));
                    }
                }
            }
        }
        return true;
    }

    private Direction tryToMove(IField board, Point pt) {
        int x = pt.getX();
        int y = pt.getY();
        List<Direction> dirs = new ArrayList<>(Direction.getValues());
        Direction direction;
        do {
            direction = dirs.get(dice.next(dirs.size()));
            x = direction.changeX(pt.getX());
            y = direction.changeY(pt.getY());
            dirs.remove(direction);
        } while (dirs.size()>0 && isOutOfBorder(board, x, y));

        if (dirs.size() > 0) {
            return direction;
        }
        return null;
    }

    public boolean itsMe(int x, int y) {
        return list.stream().anyMatch(r->r.itsMe(x, y));
    }

    private boolean isOutOfBorder(IField board, int x, int y) {
        return x >= board.size() || y >= board.size() || x < 0 || y < 0;
    }

    @Override
    public Iterator<MeatChopper> iterator() {
        return list.iterator();
    }

	public List<MeatChopper> getList() {
		return list;
	}

}
