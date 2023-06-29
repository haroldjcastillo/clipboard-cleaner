/*
 * Copyright [2023] [Harold J Castillo]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.haroldjcastillo.cc;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.IntFunction;
import java.util.function.IntUnaryOperator;

/**
 * @author haroldjcastillo
 */
@Slf4j
public class Tray implements ActionListener {

    private static final IntUnaryOperator calculateDelay = minutes -> minutes * 60000;
    private static final int DEFAULT_DELAY = 30;
    private final AtomicBoolean enabled = new AtomicBoolean(true);
    private final Timer timer;

    public Tray() {
        if (!SystemTray.isSupported()) {
            throw new IllegalStateException("SystemTray is not supported.");
        }
        final var tray = SystemTray.getSystemTray();
        final var trayIcon = new TrayIcon(getImage(), "Clipboard Cleaner");
        final var popup = new PopupMenu();
        popup.add(getEnabledItem());
        popup.add(getTimerSettingItem());
        popup.addSeparator();
        popup.add(getExitItem(tray, trayIcon));
        trayIcon.setPopupMenu(popup);
        try {
            tray.add(trayIcon);
        } catch (AWTException e) {
            throw new IllegalStateException("TrayIcon could not be added.");
        }
        timer = new Timer(calculateDelay.applyAsInt(DEFAULT_DELAY), this);
        timer.setInitialDelay(0);
        timer.start();
        Runtime.getRuntime().addShutdownHook(new Thread(timer::stop));
    }

    private MenuItem getEnabledItem() {
        final var enabledItem = new CheckboxMenuItem("Enabled", enabled.get());
        enabledItem.addItemListener(e -> {
            final var newState = !enabled.get();
            log.info("%s the clipboard-cleaner".formatted(newState ? "Enabling" : "Disabling"));
            enabled.set(!enabled.get());
        });

        return enabledItem;
    }

    private MenuItem getTimerSettingItem() {
        final IntFunction<String> formatLabel = "Set delay (%s min)"::formatted;
        final var timerSettingItem = new MenuItem(formatLabel.apply(DEFAULT_DELAY));
        timerSettingItem.addActionListener(e -> {

            final Object[] possibilities = {1, 5, 10, 30, 60, 120};
            final var input = JOptionPane
                    .showInputDialog(null, "Select the delay in minutes", "Timer settings",
                            JOptionPane.PLAIN_MESSAGE, null, possibilities, DEFAULT_DELAY);
            if (input != null) {
                final var delay = (int) input;
                log.info("Selecting {} minutes", delay);
                timer.setDelay(calculateDelay.applyAsInt(delay));
                timer.restart();
                timerSettingItem.setLabel(formatLabel.apply(delay));
            }
        });

        return timerSettingItem;
    }

    private MenuItem getExitItem(final SystemTray tray, final TrayIcon trayIcon) {
        final var exitItem = new MenuItem("Exit");
        exitItem.addActionListener(e -> {
            tray.remove(trayIcon);
            System.exit(0);
        });
        return exitItem;
    }

    @NonNull
    private Image getImage() {
        final var imageURL = Tray.class.getResource("/img/icon-16.png");
        if (imageURL != null) {
            return new ImageIcon(imageURL, "Tray icon").getImage();
        }
        throw new IllegalStateException("Try icon not found");
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
        if (enabled.get()) {
            log.info("Cleaning clipboard");
            Toolkit.getDefaultToolkit().getSystemClipboard()
                    .setContents(new StringSelection(""), null);
        }
    }
}
