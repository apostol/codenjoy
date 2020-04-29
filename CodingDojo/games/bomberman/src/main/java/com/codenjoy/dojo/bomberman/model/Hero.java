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

import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.State;
import com.codenjoy.dojo.services.multiplayer.PlayerHero;
import static com.codenjoy.dojo.bomberman.model.Elements.*;
import com.codenjoy.dojo.bomberman.interfaces.*;

import java.util.List;

public class Hero extends PlayerHero<IField> implements State<Elements, Player> {

    private static final boolean WITHOUT_MEAT_CHOPPER = false;
    private final Player player;
    private boolean alive;
    private boolean bomb;
    private Direction direction;
    private boolean collision;

    public Hero(Player player) {
        super(-1, -1);
        this.player = player;
        alive = true;
        direction = null;
    }

    public void init(IField field) {
        super.init(field);
        List<Point> free = field.getMapLayer().getFreeCells();
        Point position = free.remove(field.getGameSettings().getDice().next(free.size()));
        move(position.getX(), position.getY());
    }

    @Override
    public void right() {
        if (!alive) return;
        direction = Direction.RIGHT;
    }

    @Override
    public void down() {
        if (!alive) return;
        direction = Direction.DOWN;
    }

    @Override
    public void up() {
        if (!alive) return;
        direction = Direction.UP;
    }

    @Override
    public void left() {
        if (!alive) return;
        direction = Direction.LEFT;
    }

    @Override
    public void act(int... p) {
        if (!alive) return;

        if (direction != null) {
            bomb = true;
        } else {
            setBomb(x, y);
        }
    }

    public void apply() {
        if (!alive) return;
        if (direction == null) {
            return;
        }

        int newX = direction.changeX(x);
        int newY = direction.changeY(y);
        if (!field.getMapLayer().isBarrier(newX, newY, WITHOUT_MEAT_CHOPPER) && !collision) {
            move(newX, newY);
        }
        collision = false;
        direction = null;
        if (bomb) {
            setBomb(x, y);
            bomb = false;
        }
    }

    private void setBomb(int bombX, int bombY) {
        if (field.getBombsLayer().getBombs(this).size() < field.getGameSettings().getBombsCountParameter().getValue()) {
            field.getBombsLayer().drop(new Bomb(this, bombX, bombY, field.getGameSettings().getBombsPowerParameter().getValue(), field));
        }
    }

    public boolean isAlive() {
        return alive;
    }

    public void kill() {
        alive = false;
    }

    @Override
    public Elements state(Player player, Object... alsoAtPoint) {
        Bomb bomb = null;

        if (alsoAtPoint[1] != null) {
            if (alsoAtPoint[1] instanceof Bomb) {
                bomb = (Bomb)alsoAtPoint[1];
            }
        }

        if (isAlive()) {
            if (this == player.getHero()) {
                if (bomb != null) {
                    return BOMB_BOMBERMAN;
                } else {
                    return BOMBERMAN;
                }
            } else {
                if (bomb != null) {
                    return OTHER_BOMB_BOMBERMAN;
                } else {
                    return OTHER_BOMBERMAN;
                }
            }
        } else {
            if (this == player.getHero()) {
                return DEAD_BOMBERMAN;
            } else {
                return OTHER_DEAD_BOMBERMAN;
            }
        }
    }

    @Override
    public void tick() {

    }

    @Override
    public int getX() {
        return this.x;
    }

    @Override
    public int getY() {
        return this.y;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setCollision(boolean collision) {
        this.collision = collision;
    }

    @Override
    public void setX(int x) {
        this.x = x;
    }

    @Override
    public void setY(int y) {
        this.y = y;
    }

    public Player getPlayer() {
        return player;
    }
}

