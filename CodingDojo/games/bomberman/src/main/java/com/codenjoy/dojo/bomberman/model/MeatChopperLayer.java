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

import com.codenjoy.dojo.bomberman.services.Events;
import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.settings.Parameter;
import com.codenjoy.dojo.bomberman.interfaces.IField;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class MeatChopperLayer implements Iterable<MeatChopper> {

    private static final boolean WITH_MEATCHOPPERS = true;
    private final IField field;
    private Parameter<Integer> count;
    private int count_cache = 0;
    private final LinkedList<MeatChopper> list = new LinkedList<>();
    private final LinkedList<MeatChopper> toDestroy = new LinkedList<>();

    public MeatChopperLayer(IField field) {
        this.field = field;
        this.count = field.getGameSettings().getMeatChoppersCountParameter(); //TODO: должно быть в отдельном классе с настройками
    }

    public void clear(){
        list.clear();
    }
    public void addToDestroy(MeatChopper meat) { toDestroy.add(meat); };

    public void regenerate() {
        if (count.getValue() < 0) {
            count.update(0);
        }

        if (count.getValue() != count_cache && count.getValue() > 0) {
            count_cache = count.getValue();
        };

        toDestroy.forEach(meat -> {
            list.remove(meat);
        });
        toDestroy.clear();

        if (list.size() < count_cache) {
            int c = list.size();
            List<Point> free = field.getMapLayer().getFreeCells();
            while (c < count_cache) {
                Point position = free.remove(field.getGameSettings().getDice().next(free.size()));
                list.add(new MeatChopper(position.getX(), position.getY()));
                c++;
            }
        }
    }

    public void update() {
        regenerate();
        //TODO: Проверить правильно ли работаеь
        //тут мы проверяем сожрали ли мы игрока до нашего движения
        list.forEach(chopper -> {
            field.getPlayerLayer().forEach(player -> {
                Hero bomberman = player.getHero();
                if (bomberman.isAlive() && chopper.itsMe(bomberman)) {
                    player.event(Events.KILL_BOMBERMAN);
                }
            });
        });

        list.forEach(meatChopper -> {
            Direction direction = meatChopper.getDirection();
            if (direction != null && field.getGameSettings().getDice().next(5) > 0) {
                int x = direction.changeX(meatChopper.getX());
                int y = direction.changeY(meatChopper.getY());
                if (!itsMe(x, y) && !isBarrier(field, x, y)) {
                    meatChopper.move(x, y);
                }
            }
            meatChopper.setDirection(tryToMove(field, meatChopper));
        });

        //тут мы проверяем сожрали ли мы игрока после нашего движения. Я так полагаю, сделано для того, чтобы игрок не убежал на следующем ходе
        list.forEach(chopper -> {
            field.getPlayerLayer().forEach(player -> {
                Hero bomberman = player.getHero();
                if (bomberman.isAlive() && chopper.itsMe(bomberman)) {
                    player.event(Events.KILL_BOMBERMAN);
                }
            });
        });
    }

    private boolean isBarrier(IField board, int x, int y){
        int size = board.getMapLayer().getSize();
        if (x <= size && y <= size && x > 0 && y > 0) {
            if (!(itsMe(x, y) && WITH_MEATCHOPPERS)) {
                if (!board.getWallsLayer().itsMe(x, y)) {
                    if (!board.getBombsLayer().itsMe(x, y)) {
                        return board.getDestroyWallsLayer().itsMe(x, y);
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
            direction = dirs.get(field.getGameSettings().getDice().next(dirs.size()));
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
        int size = field.getMapLayer().getSize();
        return x >= size || y >= size || x < 0 || y < 0;
    }

    @Override
    public Iterator<MeatChopper> iterator() {
        return list.iterator();
    }

    public List<MeatChopper> getList() {
        return list;
    }

    public Optional<MeatChopper> getAt(int x, int y) {
        return list.parallelStream().filter(w -> w.itsMe(x, y)).findFirst();
    }

}
