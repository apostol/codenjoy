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
import com.codenjoy.dojo.services.printer.BoardReader;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.bomberman.interfaces.IField;
import com.codenjoy.dojo.bomberman.interfaces.ILevel;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static com.codenjoy.dojo.services.PointImpl.pt;
import static java.util.stream.Collectors.toList;

public class Bomberman implements IField {

    private List<Player> players = new LinkedList<>();
    private MeatChoppers meatChoppers;
    private EatSpaceWalls eatSpacesWalls;
    private List<Bomb> bombs;
    private List<Blast> blasts; // список взрывов - длительность 1 тик.
    private List<Wall> destroyedWalls; // список стен, которые уничтожены - длительность 1 тик
    private List<Bomb> destroyedBombs; // список уничтоженных бомб - длительность 1 тик
    private ILevel level;
    private final LinkedList<Point> map = new LinkedList<>();

    public Bomberman(ILevel level) {
        this.level = level;
        this.regenerate();
    }

    @Override
    public int size() {
        return level.getSize();
    }

    private void regenerate() {
        meatChoppers = level.getMeatChoppers();
        meatChoppers.clear();

        eatSpacesWalls = level.getEatSpacesWalls();
        eatSpacesWalls.clear();

        bombs = new LinkedList<>();
        blasts = new LinkedList<>();
        destroyedWalls = new LinkedList<>();
        destroyedBombs = new LinkedList<>();
    }

    private void generateMap(){
        map.clear();
        map.addAll(getBombermans());
        map.addAll(getWalls());
        map.addAll(getMeatChoppers());
        map.addAll(getEatSpacesWalls());
        map.addAll(getBombs());
        map.addAll(getBlasts());
    }

    @Override
    public void tick() {
        if (level.isChanged()){
            regenerate();
            level.changesReacted();
        }
        blasts.clear(); //уберем бласт

        destroyedWalls.forEach(dw->eatSpacesWalls.getList().remove(dw)); //убираем разрушенные стены
        destroyedWalls.clear(); // уберем разрушенные стены

        tactAllBombermans();//Бомберы переместились

        meatChopperEatBombermans();//Мясник сожрал бомбера?

        meatChoppers.tick(this); //Мясник переместился

        eatSpacesWalls.tick(this); //Стенки обновились

        meatChopperEatBombermans(); //Мясник сожрал бомбера?

        tactAllBombs(); //Сработали бомбы

        generateMap(); //показали что получилось.
    }

    private void tactAllBombermans() {
        for (Player player : players) {
            player.getHero().apply();
        }
    }

    private void wallDestroyed(Blast blast) {
        for (Player player : players) {
            if (blast.itsMine(player.getHero())) {
                if (meatChoppers.itsMe(blast.getX(), blast.getY())) {
                    player.event(Events.KILL_MEAT_CHOPPER);
                }
                if (eatSpacesWalls.itsMe(blast.getX(), blast.getY())) {
                    player.event(Events.KILL_DESTROY_WALL);
                }
            }
        }
    }

    private void meatChopperEatBombermans() {
        meatChoppers.forEach(chopper -> {
            players.forEach(player -> {
                Hero bomberman = player.getHero();
                if (bomberman.isAlive() && chopper.itsMe(bomberman)) {
                    player.event(Events.KILL_BOMBERMAN);
                }
            });
        });
    }

    private void tactAllBombs() {
        bombs.forEach(b -> b.tick());
        destroyedBombs.forEach(bomb -> {
            bombs.remove(bomb);
            List<Blast> blast = makeBlast(bomb);
            killAllNear(blast, bomb);
            blasts.addAll(blast);
        });
        destroyedBombs.clear();
    }

    @Override
    public List<Bomb> getBombs() {
        return bombs;
    }

    @Override
    public List<Bomb> getBombs(Hero bomberman) {
        List<Bomb> result = new LinkedList<>();
        for (Bomb bomb : bombs) {
            if (bomb.itsMine(bomberman)) {
                result.add(bomb);
            }
        }
        return result;
    }

    @Override
    public List<Blast> getBlasts() {
        return blasts;
    }

    @Override
    public void drop(Bomb bomb) {
        if (!existAtPlace(bomb.getX(), bomb.getY())) {
            bombs.add(bomb);
        }
    }

    @Override
    public void removeBomb(Bomb bomb) {
        destroyedBombs.add(bomb);
    }

    private List<Blast> makeBlast(Bomb bomb) {
        LinkedList<Point> barriers = new LinkedList<Point>() {{
            addAll(getWalls());
            addAll(getBombermans());
            addAll(getMeatChoppers());
            addAll(getEatSpacesWalls());
            addAll(getBombs());
        }}; //ограничители для взрува бомбы.
        return new BoomEngineOriginal(bomb.getOwner()).boom(barriers, size(), bomb, bomb.getPower()); // TODO move bomb
                                                                                                      // inside
                                                                                                      // BoomEngine
    }

    private void killAllNear(List<Blast> blasts, Bomb bomb) {
        for (Blast blast : blasts) {
            Optional<DestroyWall> dw = eatSpacesWalls.getList().parallelStream().filter(w->w.itsMe(blast.getX(), blast.getY())).findFirst();
            if (dw.isPresent()) {
                destroyedWalls.add(dw.get());
                wallDestroyed(blast);
            }
            Optional<Bomb> _b = bombs.parallelStream().filter(b->b.itsMe(blast)).findFirst();
            if (_b.isPresent()) {
                bombs.remove(_b.get());
            }
        }
        for (Blast blast : blasts) {
            for (Player dead : players) {
                if (dead.getHero().itsMe(blast)) {
                    dead.event(Events.KILL_BOMBERMAN);
                    for (Player bombOwner : players) {
                        if (dead != bombOwner && blast.itsMine(bombOwner.getHero())) {
                            bombOwner.event(Events.KILL_OTHER_BOMBERMAN);
                        }
                    }
                }
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

    @Override
    public List<Wall> getWalls() {
        return level.getWalls();
    }

    @Override
    public List<DestroyWall> getDestroyWall() {
        return eatSpacesWalls.getList();
    }

    @Override
    public boolean isBarrier(int x, int y, boolean isWithMeatChopper) {
        if (!getWalls().stream().anyMatch(b -> b.itsMe(x, y))) {
            if (!getBombermans().stream().anyMatch(b -> b.itsMe(x, y))) {
                if (!bombs.stream().anyMatch(b -> b.itsMe(x, y))) {
                    if (!meatChoppers.itsMe(x, y)) {
                        if (!eatSpacesWalls.itsMe(x, y)) {
                            return x < 0 || y < 0 || x > size() - 1 || y > size() - 1;
                        }
                    }
                }
            }
        }
        return true;
    }

    @Override
    public List<Hero> getBombermans() {
/*
        List<Hero> result = new LinkedList<Hero>();
        for (Player player : players) {
            result.add(player.getHero());
        }
*/
        return players.parallelStream().map(p->p.getHero()).collect(toList());
    }

    public void remove(Player player) {
        players.remove(player);
    }

    @Override
    public void newGame(Player player) {
        if (!players.contains(player)) {
            players.add(player);
        }
        player.newHero(this);
    }

    public BoardReader reader() {
        return new BoardReader() {
            @Override
            public int size() {
                return Bomberman.this.size();
            }

            @Override
            public Iterable<? extends Point> elements() {
                return Bomberman.this.getFilledCells();
            }

        };
    }

    protected LinkedList<Point> getFilledCells() {
        return map;
    }

    protected List<DestroyWall> getEatSpacesWalls() {
        return eatSpacesWalls.getList();
    }

    protected List<MeatChopper> getMeatChoppers() {
        return meatChoppers.getList();
    }

    @Override
    public LinkedList<Point> getFreeCells() {
        LinkedList<Point> free = new LinkedList<Point>();
        for (int x = 0; x < size(); x++) {
            for (int y = 0; y < size(); y++) {
                final int x1 = x;
                final int y1 = y;
                if (map.parallelStream().noneMatch(s -> s.itsMe(x1, y1)))
                {
                    free.add(pt(x, y));
                }
            }
        }
        return free;
    }

    @Override
    public ILevel getLevel() {
        return level;
    }
}
