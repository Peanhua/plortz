/*
 * Copyright (C) 2020 Joni Yrjana {@literal <joniyrjana@gmail.com>}
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
import plortz.ui.command.Command;
import plortz.ui.command.CommandFactory;

/**
 * User interface reading commands operating with stdin and stdout.
 *
 * @author Joni Yrjana {@literal <joniyrjana@gmail.com>}
 */
public class ConsoleUI extends UserInterface {
    private final Scanner     input;
    private final PrintStream output;
    private final CommandFactory command_factory;

    public ConsoleUI(InputStream input, PrintStream output) {
        super();
        this.input           = new Scanner(input);
        this.output          = output;
        this.command_factory = CommandFactory.getInstance();
        this.listenOnMessage(() -> this.output.println(this.getMessage()));
    }
    
    @Override
    public void run() {
        while (this.isRunning()) {
            String command = this.getNextCommand();
            Command cmd = this.command_factory.create(command);
            if (cmd != null) {
                cmd.execute(this);
            } else if (command != null && command.length() > 0) {
                this.showMessage("Unknown command: " + command);
                this.showMessage("Try \"help\".");
            }
        }
    }
    
    private String getNextCommand() {
        if (!this.input.hasNextLine()) {
            this.stop();
            return null;
        }
        return this.input.nextLine();
    }
}
