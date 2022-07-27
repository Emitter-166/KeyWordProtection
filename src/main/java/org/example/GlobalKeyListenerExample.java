package org.example;

import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;

import java.awt.*;
import java.io.*;
import java.util.Arrays;

public class GlobalKeyListenerExample implements NativeKeyListener {
    StringBuilder space_builder = new StringBuilder();
    StringBuilder enter_builder = new StringBuilder();
    String keyText = null;
    static String[] forbidden_words;
    static String[] forbidden_sentences;
    public void nativeKeyPressed(NativeKeyEvent e) {
        keyText = NativeKeyEvent.getKeyText(e.getKeyCode());

        if(keyText.length() == 1) {
            space_builder.append(keyText);
            enter_builder.append(keyText);
        }

        if(keyText.equalsIgnoreCase("Space")){
            enter_builder.append(" ");
        }

        if(keyText.equalsIgnoreCase("Space") || keyText.equalsIgnoreCase("Enter")){
            if(Arrays.stream(forbidden_words).anyMatch(space_builder.toString()::equalsIgnoreCase)){
                try {
                    Robot bot = new Robot();
                    bot.mouseMove(2000, 0);
                    bot.mousePress(1024);
                    bot.delay(50);
                    bot.mouseRelease(1024);
                } catch (AWTException ex) {
                    throw new RuntimeException(ex);
                }
            }
            space_builder = new StringBuilder();
        }

        if(keyText.equalsIgnoreCase("Enter")){
            System.out.println(enter_builder.toString());
            if(Arrays.stream(forbidden_sentences).anyMatch(enter_builder.toString()::equalsIgnoreCase)){
                try {
                    Robot bot = new Robot();
                    bot.mouseMove(2000, 0);
                    bot.mousePress(1024);
                    bot.delay(50);
                    bot.mouseRelease(1024);
                } catch (AWTException ex) {
                    throw new RuntimeException(ex);
                }
            }
            enter_builder = new StringBuilder();
        }

        if(keyText.equalsIgnoreCase("Backspace")){
            if(space_builder.length() != 0){
                space_builder.deleteCharAt(space_builder.length() - 1);
            }
            if(enter_builder.length() != 0){
                enter_builder.deleteCharAt(enter_builder.length() - 1);
            }
        }
    }

    public static void main(String[] args) throws IOException {

        File file = new File("config.txt");
        BufferedReader reader = new BufferedReader(new FileReader(file));
        forbidden_words = reader.readLine().split(", ");
        forbidden_sentences = reader.readLine().split(", ");

        try {
            GlobalScreen.registerNativeHook();
        }catch (NativeHookException ex) {}
        GlobalScreen.addNativeKeyListener(new GlobalKeyListenerExample());
    }
}