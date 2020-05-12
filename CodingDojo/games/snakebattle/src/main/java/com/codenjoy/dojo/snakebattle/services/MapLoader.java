package com.codenjoy.dojo.snakebattle.services;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 - 2020 Codenjoy
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

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MapLoader {
    protected static Logger logger = Logger.getLogger(MapLoader.class.getName());

    public static String loadMapFromFile(String mapFilePath) {
        if (StringUtils.isNotEmpty(mapFilePath)) {
            try {
                StringBuilder map = new StringBuilder();
                Scanner scanner = new Scanner(new File(mapFilePath), "UTF-8");
                while (scanner.hasNextLine()) {
                    map.append(scanner.nextLine());
                }
                return map.toString();
            } catch (FileNotFoundException ex) {
                logger.log(Level.WARNING, "Couldn't read map file", ex);
            }
        }
        return null;
    }
}
