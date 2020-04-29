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

import static java.util.stream.Collectors.toSet;

import java.util.*;

public class BombsLayer implements Iterable<Bomb> {

    private final LinkedList<Bomb> bombs = new LinkedList<>();
    private final IField field;
    private final LinkedList<Bomb> toDestroy = new LinkedList<>(); // список уничтоженных бомб - длительность 1 тик

    private final Map<Hero, Set<Blast>> blasts = new IdentityHashMap<>(); // список взрывов - длительность 1 тик.

    public BombsLayer(IField field) {
        this.field = field;
    }

    @Override
    public Iterator<Bomb> iterator() {
        return bombs.iterator();
    }

    public void update() {
        blasts.clear();
        bombs.forEach(b -> b.tick());

        toDestroy.forEach(bomb -> {
            List<Blast> list = makeBlast(bomb);
            Set<Blast> blastsSet = blasts.computeIfAbsent(bomb.getOwner(), (k) -> new TreeSet<>());
            blastsSet.addAll(list);
        });
        toDestroy.forEach(bomb -> {
            remove(bomb);
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

    public Collection<Point> getBlasts() {
        return blasts.entrySet().parallelStream()
                .flatMap((e) -> e.getValue().stream())
                .collect(toSet());
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

    public void regenerate() {
        //тут тоже нечего генерировать
    }

    private List<Blast> makeBlast(Bomb bomb) {
        LinkedList<Point> barriers = new LinkedList<>();// ограничители для взрыва бомбы.
        barriers.addAll(field.getWallsLayer().getList());
        barriers.addAll(bombs);
        barriers.addAll(field.getPlayerLayer().getHeroes());
        barriers.addAll(field.getMeatChoppersLayer().getList());
        barriers.addAll(field.getDestroyWallsLayer().getList());
        return new BoomEngineOriginal(bomb.getOwner()).boom(barriers, field.getMapLayer().getSize(), bomb, bomb.getPower());
    }

    private void killAllNear(Map<Hero, Set<Blast>> blastMap) {
        blastMap.forEach((hero, blastSet) -> {
            Player owner = hero.getPlayer();
            boolean ownerAlive = (owner.getHero()==hero);
            blastSet.forEach(blast -> {
                field.getMeatChoppersLayer().getAt(blast.getX(), blast.getY()).ifPresent(meat -> {
                    if (ownerAlive) {
                        owner.event(Events.KILL_MEAT_CHOPPER);
                    }
                    field.getMeatChoppersLayer().addToDestroy(meat);
                });
                field.getDestroyWallsLayer().getAt(blast.getX(), blast.getY()).ifPresent(dw -> {
                    if (ownerAlive) {
                        owner.event(Events.KILL_DESTROY_WALL);
                    }
                    field.getDestroyWallsLayer().addToDestroy(dw);
                });
                field.getPlayerLayer().getAt(blast.getX(), blast.getY()).ifPresent(dead -> {
                    dead.event(Events.KILL_BOMBERMAN);
                    if (dead != owner && ownerAlive) {
                        owner.event(Events.KILL_OTHER_BOMBERMAN);
                    }
                });
                bombs.parallelStream().filter(b -> b.itsMe(blast)).findFirst().ifPresent(bomb -> {
                    bombs.remove(bomb);
                });
            });
        });
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
}
