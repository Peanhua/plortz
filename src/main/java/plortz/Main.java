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
package plortz;

import plortz.benchmark.Benchmark;
import plortz.ui.ConsoleUI;
import plortz.ui.GraphicalUI;
import plortz.ui.UserInterface;

/**
 * The starting point of the application.
 * 
 * @author Joni Yrjana {@literal <joniyrjana@gmail.com>}
 */
public class Main {
    private static boolean use_gui        = true;
    private static boolean output_timing  = false;
    private static boolean run_benchmarks = false;
    
    public static void main(String args[]) {
        if (!parseArgs(args)) {
            return;
        }
        if (run_benchmarks) {
            runBenchmarks();
            return;
        }
        UserInterface ui = null;
        if (use_gui) {
            ui = new GraphicalUI();
        } else {
            ui = new ConsoleUI(System.in, System.out);
        }
        ui.setOutputTiming(output_timing);
        ui.run();
    }
    
    private static boolean parseArgs(String[] args) {
        for (String arg : args) {
            if (arg.equals("--no-gui")) {
                use_gui = false;
            } else if (arg.equals("--timing")) {
                output_timing = true;
            } else if (arg.equals("--benchmark")) {
                run_benchmarks = true;
            } else {
                System.out.println("Unknown argument '" + arg + "'");
                return false;
            }
        }
        return true;
    }
    
    private static void runBenchmarks() {
        var tests = Benchmark.getAllTests();
        for (var test : tests) {
            System.out.print(test.getName() + ": ");
            System.out.flush();
            var t = test.run(20, 20);
            System.out.println(t);
        }
    }
}
