package com.codenjoy.dojo.quadro.client;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2016 Codenjoy
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


import com.codenjoy.dojo.client.AbstractBoard;
import com.codenjoy.dojo.quadro.model.Elements;
import com.codenjoy.dojo.services.Point;

/**
 * Класс, обрабатывающий строковое представление доски.
 * Содержит ряд унаследованных методов {@see AbstractBoard},
 * но ты можешь добавить сюда любые свои методы на их основе.
 */
public class Board extends AbstractBoard<Elements> {

    @Override
    public Elements valueOf(char ch) {
        return Elements.valueOf(ch);
    }

<<<<<<< HEAD
    public boolean isYellow(int x, int y) {
        return isAt(x, y, Elements.YELLOW);
    }

    public boolean isRed(int x, int y) {
        return isAt(x, y, Elements.RED);
    }

    public boolean isGameOver() {
=======
    public boolean isYellowChipAt(int x, int y) {
        return isAt(x, y, Elements.YELLOW_CHIP);
    }

    public boolean isRedChipAt(int x, int y) {
        return isAt(x, y, Elements.RED_CHIP);
    }

    public boolean isGameOver() {
//        return !get(Elements.NONE).isEmpty();
>>>>>>> e88ddd11bf2be859b6168599581a05035b8248e8
        return false;
    }
}