package com.codenjoy.dojo.snakebattle.model.level.custom;

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

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum CutsomMaps {
    SMALL("☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼"+
            "☼☼        ☼○☼              ☼$☼"+
            "☼#       ●●      ☼☼☼☼☼       ☼"+
            "☼☼  ○   ☼#          ●●       ☼"+
            "☼☼  ●                   ○    ☼"+
            "☼☼ ○         ●              ☼☼"+
            "☼#         ●●     ☼#        $☼"+
            "☼☼      ●●☼☼   ☼   ☼  ☼●    ☼☼"+
            "☼☼ ● ●  ☼     ☼○☼  ●  ●○ ●   ☼"+
            "☼☼ ● ●  ☼○         ☼  ☼●     ☼"+
            "☼☼      ●●☼               ●  ☼"+
            "☼☼              ☼#        ☼ ☼☼"+
            "☼☼○            ☼            ○☼"+
            "☼☼    ●      ●☼○☼●   ●●●☼ ☼ ☼☼"+
            "☼#          ●●○$○●●          ☼"+
            "☼☼   ●       ●☼○☼●   ●●●     ☼"+
            "☼☼   ○         ☼   ☼#        ☼"+
            "☼☼   ●   ●☼ ☼                ☼"+
            "☼☼☼         ●     ●     ○    ☼"+
            "☼☼○      ☼☼●●               ☼☼"+
            "☼☼☼         ☼               ○☼"+
            "☼☼○☼●     ●☼#               ☼☼"+
            "☼☼  ●        ○               ☼"+
            "☼☼                  ☼●☼    ● ☼"+
            "☼☼   ●                 ●   ●○☼"+
            "☼☼   ●  ○        ☼●☼#      ● ☼"+
            "☼#        ●●                 ☼"+
            "☼☼               ○           ☼"+
            "☼☼○☼       ●●      ☼○☼     ☼$☼"+
            "☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼"),

    MEDIUM("☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼"+
            "☼☼         ○                 ☼"+
            "☼#                           ☼"+
            "☼☼  ○   ☼#         ○         ☼"+
            "☼☼                      ○    ☼"+
            "☼# ○         ●               ☼"+
            "☼☼                ☼#         ☼"+
            "☼☼      ☼☼☼        ☼  ☼      ☼"+
            "☼#      ☼      ○   ☼  ☼      ☼"+
            "☼☼      ☼○         ☼  ☼      ☼"+
            "☼☼      ☼☼☼               ●  ☼"+
            "☼#              ☼#           ☼"+
            "☼☼○                         $☼"+
            "☼☼    ●              ☼       ☼"+
            "☼#             ○             ☼"+
            "☼☼                           ☼"+
            "☼☼   ○             ☼#        ☼"+
            "☼#       ☼☼ ☼                ☼"+
            "☼☼          ☼     ●     ○    ☼"+
            "☼☼       ☼☼ ☼                ☼"+
            "☼#          ☼                ☼"+
            "☼☼         ☼#                ☼"+
            "☼☼           ○               ☼"+
            "☼#                  ☼☼☼      ☼"+
            "☼☼                           ☼"+
            "☼☼      ○        ☼☼☼#    ○   ☼"+
            "☼#                           ☼"+
            "☼☼               ○           ☼"+
            "☼☼                           ☼"+
            "☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼"),

    BIG("☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼"+
            "☼☼     ☼#                      ☼  ○☼      ☼#        ☼"+
            "☼#                 ☼☼#       ●●☼   ☼#              ☼☼"+
            "☼☼                                                  ☼"+
            "☼☼      ○      ●                                 ○  ☼"+
            "☼#                   ☼#        ●   ☼#              ☼☼"+
            "☼☼         $                   ●                    ☼"+
            "☼#                ☼☼#        $ ●               ○    ☼"+
            "☼☼     ○           ☼                                ☼"+
            "☼#                 ●                ☼☼☼#          ●●☼"+
            "☼☼    ☼☼☼#         ●               ☼○   ☼           ☼"+
            "☼#                                ○  ●              ☼"+
            "☼☼    ○        ☼☼☼☼☼☼☼☼☼☼#           ●●             ☼"+
            "☼☼                                   ☼☼#            ☼"+
            "☼#                                   ☼              ☼"+
            "☼☼        ☼         ●                ○      ●●●●    ☼"+
            "☼#        ☼         ☼                 ☼             ☼"+
            "☼☼●     ○ ☼         ☼☼#                  ☼          ☼"+
            "☼#                  ☼       ☼☼☼#                    ☼"+
            "☼☼                                     ☼☼#          ☼"+
            "☼#                 ●☼☼ ☼#                        ●● ☼"+
            "☼☼      ☼#                                          ☼"+
            "☼#                       ☼☼#                        ☼"+
            "☼☼             $        ○                  ●        ☼"+
            "☼☼      ☼                                  ☼        ☼"+
            "☼#     ☼         ●☼●○●☼ ●#          ●☼☼ ☼○☼○☼☼#     ☼"+
            "☼☼        ○                              ☼          ☼"+
            "☼☼                     $☼☼#              ●          ☼"+
            "☼#                        ●●                ●●      ☼"+
            "☼☼           ○                                      ☼"+
            "☼☼      ☼                                           ☼"+
            "☼#      ☼●☼       ☼☼#             ☼☼#               ☼"+
            "☼☼        ☼                         ☼               ☼"+
            "☼☼         ○                        ○ ☼      ☼○     ☼"+
            "☼#                        ●●                    ☼   ☼"+
            "☼☼                        ☼☼#                   ☼   ☼"+
            "☼☼      ☼☼☼●☼#                                      ☼"+
            "☼#         ☼                       ☼#               ☼"+
            "☼☼          ○    ☼☼#            ●             ○     ☼"+
            "☼#                         $        ●●              ☼"+
            "☼☼     ☼                                            ☼"+
            "☼☼     ☼     ○                       ●        ●     ☼"+
            "☼#           ☼☼☼●●☼☼☼#                        ●   ●●☼"+
            "☼☼                                                  ☼"+
            "☼☼            ○              ☼☼#            ☼☼ ☼☼☼  ☼"+
            "☼#                                       ●  ●   ○☼● ☼"+
            "☼☼ ○    ☼         ☼        ☼☼      ○●    ●  ☼  ☼    ☼"+
            "☼☼      ☼                   ☼$              ☼ ☼○ ☼  ☼"+
            "☼#              ☼☼☼#                       ●☼ ○☼ ☼  ☼"+
            "☼☼  ☼☼☼ ○                   ☼               ☼  ☼ ☼  ☼"+
            "☼☼      ☼           ☼       ☼#          ●      ☼ ☼  ☼"+
            "☼#                  ☼             ●    ●    ●○ ☼ ○ ●☼"+
            "☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼");

    private String map;

    CutsomMaps(String map) {
        this.map = map;
    }

    public String getMap() {
        return map;
    }

    public static CutsomMaps byName(String name) {
        return Arrays.stream(values())
            .filter(v -> v.name().equalsIgnoreCase(name))
            .findFirst()
            .orElse(SMALL);
    }

    public static List<Object> maps() {
        return Arrays.stream(CutsomMaps.values()).map(Enum::name).collect(Collectors.toList());
    }
}
