/*
Copyright 2020 Daniel A. Hawton <daniel@hawton.org>

Permission is hereby granted, free of charge, to any person obtaining a copy of
this software and associated documentation files (the "Software"), to deal in the
Software without restriction, including without limitation the rights to use, copy,
modify, merge, publish, distribute, sublicense, and/or sell copies of the Software,
and to permit persons to whom the Software is furnished to do so, subject to the
following conditions:

The above copyright notice and this permission notice shall be included in all copies
or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE
FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
DEALINGS IN THE SOFTWARE.
 */

package io.hawton.utils.imgresize;

import org.apache.commons.cli.*;
import org.imgscalr.Scalr;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Main {
    private static Options options;
    private static CommandLineParser commandLineParser;
    private static HelpFormatter helpFormatter;
    private static CommandLine commandLine;
    private static int maxSide = -1;
    private static int width = -1;
    private static int height = -1;
    private static String inFile = null;
    private static String outFile = null;

    public static void main(String[] args) {
        BuildCommandLineParser();
        ParseCommandLine(args);

        try {
            BufferedImage originalImage = ImageIO.read(new File(inFile));
            Dimension maxSize = new Dimension(width, height);
            BufferedImage resizedImage = Scalr.resize(originalImage, Scalr.Method.QUALITY, maxSize.width, maxSize.height);
            ImageIO.write(resizedImage, "jpg", new File(outFile));
        } catch(IOException e) {
            System.out.println("Caught IOException: " + e.getMessage());
            System.exit(1);
        }
    }

    private static void BuildCommandLineParser() {
        commandLineParser = new DefaultParser();
        options = new Options();

        Option helpOption = new Option("h", "help", false, "Show help");
        options.addOption(helpOption);
        Option versionOption = new Option("v", "version", false, "Show jircd version information");
        options.addOption(versionOption);
        Option widthOption = new Option("w", "width", true, "Set width in pixels");
        //widthOption.setRequired(true);
        options.addOption(widthOption);
        Option heightOption = new Option("h", "height", true, "Set height in pixels");
        //heightOption.setRequired(true);
        options.addOption(heightOption);

        helpFormatter = new HelpFormatter();
    }

    private static void ParseCommandLine(String[] args) {
        try {
            commandLine = commandLineParser.parse(options, args);
        } catch(ParseException e) {
            // This will catch if required parameters are missing
            ShowHelp(true, 1);
        }

        if (commandLine.hasOption("help")) {
            ShowHelp(true, 0);
        }

        if (commandLine.hasOption("w") || commandLine.hasOption("width")) {
            int m = Integer.parseInt(commandLine.getOptionValue("w"));
            if (m <= 0) {
                System.out.println("Invalid width value specified.");
                ShowHelp(true, 1);
            }
            width = m;
        }

        if (commandLine.hasOption("h") || commandLine.hasOption("height")) {
            int m = Integer.parseInt(commandLine.getOptionValue("h"));
            if (m <= 0) {
                System.out.println("Invalid height value specified.");
                ShowHelp(true, 1);
            }
            height = m;
        }

        if (height == -1) height = 300;
        if (width == -1) width = 300;

        String[] otherArgs = commandLine.getArgs();
        if (otherArgs.length < 2) {
            ShowHelp(true, 1);
        }

        inFile = otherArgs[0];
        outFile = otherArgs[1];
    }

    private static void ShowHelp() {
        ShowHelp(false, 0);
    }

    private static void ShowHelp(Boolean exit, int ExitCode) {
        String jar = new java.io.File(Main.class.getProtectionDomain()
                .getCodeSource()
                .getLocation()
                .getPath())
                .getName();
        String jarPath = String.format("java -jar %s", jar);
        helpFormatter.printHelp(String.format("%s [options] (input file) (output file)", jarPath), options);
        if (exit)
            System.exit(ExitCode);
    }
}
