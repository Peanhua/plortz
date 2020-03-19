/*
 * Copyright (C) 2020 Joni Yrjana <joniyrjana@gmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package plortz.ui;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.Scanner;

/**
 * User interface reading commands operating with stdin and stdout.
 *
 * @author Joni Yrjana <joniyrjana@gmail.com>
 */
public class ConsoleUI extends UserInterface {
    private final Scanner     input;
    private final PrintStream output;

    public ConsoleUI(InputStream input, PrintStream output) {
        super();
        this.input   = new Scanner(input);
        this.output  = output;
    }
    
    public ConsoleUI() {
        this(System.in, System.out);
    }
    
    @Override
    public String getNextCommand() {
        if (!this.input.hasNextLine()) {
            this.stop();
            return null;
        }
        return this.input.nextLine();
    }

    @Override
    public void showError(String error_message) {
        this.output.println("Error: " + error_message);
    }
}
