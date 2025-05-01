package utils;

import types.BackgroundTaskStatic;
import types.ThreadedBufferedReader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

// separating this from Application.java in case the app may receive input in other forms in the future
public class ThreadedScanner extends ThreadedBufferedReader {
    public ThreadedScanner() {
        super(System.in);
    }
}
