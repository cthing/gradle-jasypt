/*
 * Copyright 2025 C Thing Software
 * SPDX-License-Identifier: Apache-2.0
 */
package org.cthing.gradle.plugins.jasypt;

import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.Serial;
import java.util.ResourceBundle;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;

import org.jspecify.annotations.Nullable;


/**
 * Graphical dialog for entering a password. The stock Swing dialog could not be used because it
 * places focus on the OK button and cannot be coerced to put it on the password field.
 */
class PasswordDialog extends JDialog {

    @Serial
    private static final long serialVersionUID = 1L;

    private static final ResourceBundle MESSAGES = ResourceBundle.getBundle("messages");
    private static final int INSET = 10;
    private static final int FIELD_COLUMNS = 20;

    private final JPasswordField passwordField;
    private boolean cancelled = true;

    PasswordDialog(final String messageId) {
        super((Frame)null, MESSAGES.getString("dialog.title"), true);

        this.passwordField = new JPasswordField(FIELD_COLUMNS);
        final JButton okButton = new JButton(MESSAGES.getString("dialog.ok"));
        final JButton cancelButton = new JButton(MESSAGES.getString("dialog.cancel"));

        final JPanel panel = new JPanel(new GridBagLayout());
        final GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(INSET, INSET, INSET, INSET);

        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel(MESSAGES.getString(messageId)), gbc);

        gbc.gridy = 1;
        panel.add(this.passwordField, gbc);

        final JPanel buttonPanel = new JPanel();
        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);

        gbc.gridy = 2;
        panel.add(buttonPanel, gbc);

        getContentPane().add(panel);
        pack();
        setLocationRelativeTo(null);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowOpened(final WindowEvent event) {
                PasswordDialog.this.passwordField.requestFocusInWindow();
            }
        });

        okButton.addActionListener(e -> {
            this.cancelled = false;
            dispose();
        });

        this.passwordField.addActionListener(e -> okButton.doClick());

        cancelButton.addActionListener(e -> dispose());
    }

    /**
     * Shows the dialog and returns the password.
     *
     * @return Password or {@code null} if no password was entered.
     */
    char @Nullable [] getPassword() {
        setVisible(true);
        return this.cancelled ? null : this.passwordField.getPassword();
    }
}
