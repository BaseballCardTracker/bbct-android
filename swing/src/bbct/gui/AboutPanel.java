/*
 * This file is part of BBCT.
 *
 * Copyright 2012 codeguru <codeguru@users.sourceforge.net>
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
package bbct.gui;

import bbct.gui.event.SetDefaultButtonAncestorListener;
import bbct.gui.event.ShowCardActionListener;
import bbct.gui.event.UpdateInstructionsAncestorListener;
import bbct.gui.event.UpdateTitleAncestorListener;
import java.awt.BorderLayout;
import java.awt.Font;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.LayoutStyle;

/**
 * A panel that displays the name of this application and copyright information.
 *
 * @author codeguru <codeguru@users.sourceforge.net>
 */
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

        JLabel titleLabel = new JLabel("Baseball Card Tracker");
        titleLabel.setFont(new Font("Tahoma", 0, 36)); // NOI18N

        JLabel copyrightLabel = new JLabel("<html>&copy 2012 codeguru</html>");
        copyrightLabel.setFont(new Font("Tahoma", 0, 14)); // NOI18N

        JLabel versionLabel = new JLabel("Version 0.5.2 (beta)");
        versionLabel.setFont(new Font("Tahoma", 0, 18)); // NOI18N

        JLabel websiteLabel = new JLabel("Project website: http://www.sourceforge.net/p/bbct");
        websiteLabel.setFont(new Font("Tahoma", 0, 14)); // NOI18N

        JLabel gplLabel = new JLabel("<html>This software is licensed under the GPL. Visit<br> http://www.gnu.org/licenses/ for a copy of the license.</html>");
        gplLabel.setFont(new Font("Tahoma", 0, 14)); // NOI18N

        GroupLayout infoPanelLayout = new GroupLayout(infoPanel);
        infoPanel.setLayout(infoPanelLayout);
        infoPanelLayout.setHorizontalGroup(
                infoPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGap(0, 366, Short.MAX_VALUE)
                .addGroup(infoPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(infoPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(infoPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addComponent(copyrightLabel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addComponent(versionLabel)
                .addComponent(websiteLabel)
                .addComponent(gplLabel)
                .addComponent(titleLabel))
                .addContainerGap())));
        infoPanelLayout.setVerticalGroup(
                infoPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGap(0, 521, Short.MAX_VALUE)
                .addGroup(infoPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(infoPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(titleLabel)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(versionLabel)
                .addGap(18, 18, 18)
                .addComponent(copyrightLabel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(websiteLabel)
                .addGap(330, 330, 330)
                .addComponent(gplLabel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))));

        this.add(infoPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        final JButton okButton = new JButton();
        okButton.setFont(new Font("Tahoma", 0, 16)); // NOI18N
        okButton.setText("OK");
        okButton.addActionListener(new ShowCardActionListener(this, BBCTFrame.MENU_CARD_NAME));
        buttonPanel.add(okButton);

        this.add(buttonPanel, BorderLayout.SOUTH);

        this.addAncestorListener(new UpdateTitleAncestorListener(GUIResources.ABOUT_PANEL_TITLE));
        this.addAncestorListener(new UpdateInstructionsAncestorListener("Click OK when ready."));
        this.addAncestorListener(new SetDefaultButtonAncestorListener(okButton));
    }

    /**
     * Tests for {@link AboutPanel}. Simply creates a {@link javax.swing.JFrame}
     * in which to display it.
     *
     * @param args Command-line arguments. (ignored)
     */
    public static void main(String[] args) {
        JFrame f = new JFrame("AboutPanel Test");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.add(new AboutPanel());
        f.pack();
        f.setVisible(true);
    }
}
