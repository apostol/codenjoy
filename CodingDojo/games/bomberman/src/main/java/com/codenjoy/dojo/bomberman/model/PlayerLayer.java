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

public class PlayerLayer implements Iterable<Player> {

    private final LinkedList<Player> players = new LinkedList<>();
    private final LinkedList<Hero> heroes = new LinkedList<>();
    private final IField field;

    public PlayerLayer(IField field) {
        this.field = field;
    }

    @Override
    public Iterator<Player> iterator() {
        return players.iterator();
    }

    public void update() {
        for (Hero hero : heroes) {
            hero.apply();
        }
    }

    public void remove(Player player) {
        heroes.remove(player.getHero());
        players.remove(player);
    }

    public void add(Player player) {
        if (!players.contains(player)) {
            players.add(player);
        }
        heroes.remove(player.getHero());
        player.newHero(field);
        addHero(player.getHero());
    }

    private void addHero(Hero hero){
        if (!heroes.contains(hero)) {
            heroes.add(hero);
        }
    }

    public boolean itsMe(int x, int y) {
        return getHeroes().parallelStream().anyMatch(r->r.itsMe(x, y));
    }

    public void regenerate() {
         //ичего не генерим, так как нечего :)
    }

    public List<Hero> getHeroes() {
        return heroes;
    }

    public void reset() {
        heroes.forEach(h->h.init(field));
    }
}
