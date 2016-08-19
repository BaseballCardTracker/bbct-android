/*
 * This file is part of BBCT.
 *
 * Copyright 2012-14 codeguru <codeguru@users.sourceforge.net>
 *
 * BBCT is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * BBCT is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package bbct.swing.gui;

import bbct.swing.BBCTStringResources;
import bbct.swing.FontResources;
import bbct.swing.gui.event.SetDefaultButtonAncestorListener;
import bbct.swing.gui.event.ShowCardActionListener;
import bbct.swing.gui.event.UpdateInstructionsAncestorListener;
import bbct.swing.gui.event.UpdateTitleAncestorListener;
import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

/**
 * A panel that displays the name of this application and copyright information.
 */
@SuppressWarnings("serial")
public class AboutPanel extends JPanel {

    /**
     * Creates a new {@link AboutPanel}.
     */
    public AboutPanel() {
        this.initComponents();
    }

    private void initComponents() {
        this.setLayout(new BorderLayout());

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();

        JLabel titleLabel = new JLabel(BBCTStringResources.AboutResources.ABOUT_TITLE);
        titleLabel.setFont(FontResources.TITLE_FONT);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        infoPanel.add(titleLabel, gbc);

        JLabel versionLabel = new JLabel(BBCTStringResources.AboutResources.ABOUT_VERSION);
        versionLabel.setFont(FontResources.VERSION_FONT);
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        infoPanel.add(versionLabel, gbc);

        JLabel copyrightLabel = new JLabel(BBCTStringResources.AboutResources.ABOUT_COPYRIGHT);
        copyrightLabel.setFont(FontResources.DEFAULT_FONT);
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.ipady = 10;
        gbc.anchor = GridBagConstraints.WEST;
        infoPanel.add(copyrightLabel, gbc);

        JLabel websiteLabel = new JLabel(BBCTStringResources.AboutResources.ABOUT_WEBSITE);
        websiteLabel.setFont(FontResources.DEFAULT_FONT);
        gbc.gridx = 0;
        gbc.gridy = 4;
        infoPanel.add(websiteLabel, gbc);

        JLabel gplLabel = new JLabel(BBCTStringResources.AboutResources.ABOUT_LICENSE);
        gplLabel.setFont(FontResources.DEFAULT_FONT);
        gbc.gridx = 0;
        gbc.gridy = 5;
        infoPanel.add(gplLabel, gbc);

        this.add(infoPanel, BorderLayout.PAGE_START);

        JPanel buttonPanel = new JPanel();
        final JButton okButton = new JButton();
        okButton.setFont(FontResources.BUTTON_FONT);
        okButton.setText(BBCTStringResources.ButtonResources.OK_BUTTON);
        okButton.addActionListener(new ShowCardActionListener(this,
                BBCTFrame.MENU_CARD_NAME));
        buttonPanel.add(okButton);

        this.add(buttonPanel, BorderLayout.SOUTH);

        this.addAncestorListener(new UpdateTitleAncestorListener(BBCTStringResources.TitleResources.ABOUT_PANEL_TITLE));
        this.addAncestorListener(new UpdateInstructionsAncestorListener(BBCTStringResources.InstructionResources.ABOUT_INSTRUCTIONS));
        this.addAncestorListener(new SetDefaultButtonAncestorListener(okButton));
    }

    /**
     * Tests for {@link AboutPanel}. Simply creates a {@link javax.swing.JFrame}
     * in which to display it.
     *
     * @param args
     *            Command-line arguments. (ignored)
     */
    public static void main(String[] args) {
        JFrame f = new JFrame("AboutPanel Test");
        f.setDefaultCloseOperation(EXIT_ON_CLOSE);
        f.add(new AboutPanel());
        f.setSize(400, 400);
        f.setVisible(true);
    }
}
