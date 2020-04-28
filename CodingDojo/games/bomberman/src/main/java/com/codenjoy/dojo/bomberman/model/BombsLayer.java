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
import com.codenjoy.dojo.bomberman.services.Events;
import com.codenjoy.dojo.services.Point;

import java.util.*;

public class BombsLayer implements Iterable<Bomb> {

    private final LinkedList<Bomb> bombs = new LinkedList<>();
    private final IField field;
    private final LinkedList<Bomb> toDestroy = new LinkedList<>(); // список уничтоженных бомб - длительность 1 тик

    private final List<Blast> blasts = new LinkedList<>(); // список взрывов - длительность 1 тик.

    public BombsLayer(IField field) {
        this.field = field;
    }

    @Override
    public Iterator<Bomb> iterator() {
        return bombs.iterator();
    }

    public void update() {
        blasts.clear();
        LinkedList<Point> barriers = new LinkedList<Point>() {{
            addAll(field.getWallsLayer().getList());
            addAll(field.getPlayerLayer().getHeroes());
            addAll(field.getMeatChoppersLayer().getList());
            addAll(field.getDestroyWallsLayer().getList());
            addAll(bombs);
        }}; //ограничители для взрыва бомбы. Собираем один раз, так как в этом потоке мы работаем только с бомбами и ничего более не меняем в ограничителях

        bombs.forEach(b -> b.tick());
        toDestroy.forEach(bomb -> {
            remove(bomb);
            if (!itsBlast(bomb.getX(), bomb.getY())) { //Добавляем только если бомба еще не взорвалась.
                blasts.addAll(makeBlast(bomb, barriers));
            }
        });
        killAllNear(blasts);
        toDestroy.clear();
    }

    public void remove(Bomb bomb) {
        bombs.remove(bomb);
    }

    public void drop(Bomb bomb) {
        if (!existAtPlace(bomb.getX(), bomb.getY())) {
            bombs.add(bomb);
        }
    }

    public List<Bomb> getBombs(Hero bomberman) {
        List<Bomb> result = new LinkedList<>();
        for (Bomb bomb : bombs) {
            if (bomb.itsMine(bomberman)) {
                result.add(bomb);
            }
        }
        return result;
    }

    public List<Blast> getBlasts() {
        return blasts;
    }

    public List<Bomb> getBombs() {
        return bombs;
    }

    public boolean itsMe(int x, int y) {
        return bombs.parallelStream().anyMatch(r->r.itsMe(x, y));
    }

    public void addToDestroy(Bomb bomb) {
        toDestroy.add(bomb);
    }

    private List<Blast> makeBlast(Bomb bomb, LinkedList<Point> barriers) {
        return new BoomEngineOriginal(bomb.getOwner()).boom(barriers, field.getMapLayer().getSize(), bomb, bomb.getPower());
    }

    private void killAllNear(List<Blast> blasts) {
        for (Blast blast : blasts) {
            for (Player dead : field.getPlayerLayer()) {
                if (blast.itsMine(dead.getHero())) {
                    if (field.getMeatChoppersLayer().itsMe(blast.getX(), blast.getY())) {
                        dead.event(Events.KILL_MEAT_CHOPPER);
                        Optional<MeatChopper> meat = field.getMeatChoppersLayer().getList().parallelStream().filter(w->w.itsMe(blast.getX(), blast.getY())).findFirst();
                        if (meat.isPresent()) {
                            field.getMeatChoppersLayer().addToDestroy(meat.get());
                        }
                    }
                    if (field.getDestroyWallsLayer().itsMe(blast.getX(), blast.getY())) {
                        dead.event(Events.KILL_DESTROY_WALL);
                    }
                }
                if (dead.getHero().itsMe(blast)) {
                    dead.event(Events.KILL_BOMBERMAN);
                    for (Player bombOwner : field.getPlayerLayer()) {
                        if (dead != bombOwner && blast.itsMine(bombOwner.getHero())) {
                            bombOwner.event(Events.KILL_OTHER_BOMBERMAN);
                        }
                    }
                }
            }

            Optional<DestroyWall> dw = field.getDestroyWallsLayer().getList().parallelStream().filter(w->w.itsMe(blast.getX(), blast.getY())).findFirst();
            if (dw.isPresent()) {
                field.getDestroyWallsLayer().addToDestroy(dw.get());
            }
            Optional<Bomb> _b = bombs.parallelStream().filter(b->b.itsMe(blast)).findFirst();
            if (_b.isPresent()) {
                bombs.remove(_b.get());
            }
        }
    }

    private boolean existAtPlace(int x, int y) {
        for (Bomb bomb : bombs) {
            if (bomb.getX() == x && bomb.getY() == y) {
                return true;
            }
        }
        return false;
    }

    public void clear() {
        bombs.clear();
        blasts.clear();
        toDestroy.clear();
    }

    private boolean itsBlast(int x, int y) {
        return blasts.stream().anyMatch(r->r.itsMe(x, y));
    }
}
