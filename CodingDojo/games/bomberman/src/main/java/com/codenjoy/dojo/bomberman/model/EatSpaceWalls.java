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
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.settings.Parameter;
import com.codenjoy.dojo.bomberman.interfaces.IField;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class EatSpaceWalls implements Iterable<DestroyWall> {

    private static final boolean WITH_MEAT_CHOPPERS = true;
    private Parameter<Integer> count;
    private Dice dice;
    private final LinkedList<DestroyWall> destroyWalls = new LinkedList<>();;


    public EatSpaceWalls(Parameter<Integer> count, Dice dice) {
        this.count = count;
        this.dice = dice;
    }

    // private int freeSpaces() {
    //     return (board.size() * board.size() - 1) // TODO -1 это один бомбер, а если их несколько?
    //             - walls.subList(Wall.class).size();
    // }

    public void tick(IField board) {
        regenerate(board);
    }

    private void regenerate(IField board) {
        if (count.getValue() < 0) {
            count.update(0);
        }
        int count = destroyWalls.size();
        List<Point> free = board.getFreeCells();

        while (count < this.count.getValue()) {
            Point position = free.remove(dice.next(free.size()));
            destroyWalls.add(new DestroyWall(position.getX(), position.getY()));
            count++;
        }

        // if (needToCreate > free.size()) { // TODO и это потестить
        //     count.update(count.getValue() - (needToCreate - freeSpaces()) - 50); // 50 это место под бомберов
        // }

        // if (count > this.count.getValue()) { // TODO и удаление лишних
        //     for (int i = 0; i < (count - this.count.getValue()); i++) {
        //         DestroyWall meatChopper = destroyWalls.remove(0);
        //         walls.destroy(meatChopper.getX(), meatChopper.getY());
        //     }
        //     return;
        // }

        // int c = 0;
        // int maxc = 10000;
        // while (count < this.count.getValue() && c < maxc) { // TODO и это
        //     int x = dice.next(board.size());
        //     int y = dice.next(board.size());

        //     if (!board.isBarrier(x, y, WITH_MEAT_CHOPPERS)) {
        //         walls.add(new DestroyWall(x, y));
        //         count++;
        //     }

        //     c++;
        // }

        // if (c == maxc) {
        //     throw new RuntimeException("Dead loop at EatSpaceWalls.generate!");
        // }
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
}
