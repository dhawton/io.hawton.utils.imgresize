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

import org.imgscalr.Scalr;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Main {
    private static String _VERSION = "1.1.0";
    private static int width = 300;
    private static int height = 300;
    private static String inFile = null;
    private static String outFile = null;

    public static void main(String[] args) {
        for (int i = 0; i < args.length; i++) {
            switch(args[i]) {
                case "-h":
                case "--height":
                    height = Integer.parseInt(args[++i]);
                    break;
                case "-w":
                case "--width":
                    width = Integer.parseInt(args[++i]);
                    break;
                case "--help":
                    ShowHelp(true, 0);
                    break;
                default:
                    if (inFile == null) inFile = args[i];
                    else if (outFile == null) outFile = args[i];
                    else ShowHelp(true, 1);
            }
        }

        if (inFile == null || outFile == null) {
            ShowHelp(true, 1);
        }

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
        System.out.println(String.format("%s v%s", jar, _VERSION));
        System.out.println(String.format("%s [options] (input file) (output file)", jarPath));
        System.out.println(String.format("  -h, --height (size in px) = Set desired height [default 300px]"));
        System.out.println(String.format("  -w, --width (size in px)  = Set desired width [default 300px]"));
        System.out.println(String.format("  --help                    = Show this help output"));
        if (exit)
            System.exit(ExitCode);
    }
}
