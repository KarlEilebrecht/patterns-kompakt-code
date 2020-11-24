//@formatter:off
/*
 * Team View - the VIEW in this MVC-example.
 * Code-Beispiel zum Buch Patterns Kompakt, Verlag Springer Vieweg
 * Copyright 2014 Karl Eilebrecht
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
//@formatter:on
package de.calamanari.pk.modelviewcontroller;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Team View - the VIEW in this MVC-example. This is a so-called PASSIVE VIEW, it does not hold a reference to the model. The controller observes the model as
 * well as the view and exclusively updates the view after model changes.<br>
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 */
@SuppressWarnings("squid:MaximumInheritanceDepth")
public class TeamView extends JFrame {

    private static final Logger LOGGER = LoggerFactory.getLogger(TeamView.class);

    /**
     * for serialization
     */
    private static final long serialVersionUID = -6807864012253667394L;

    /**
     * constant name for close button
     */
    public static final String BTN_CLOSE = "btnClose";

    /**
     * constant name for add button
     */
    public static final String BTN_ADD = "btnAdd";

    /**
     * constant name for remove button
     */
    public static final String BTN_REMOVE = "btnRemove";

    /**
     * member display label 1
     */
    public final JLabel lblMember1 = new JLabel("");

    /**
     * member display label 2
     */
    public final JLabel lblMember2 = new JLabel("");

    /**
     * member display label 3
     */
    public final JLabel lblMember3 = new JLabel("");

    /**
     * member display label 4
     */
    public final JLabel lblMember4 = new JLabel("");

    /**
     * member display label 5
     */
    public final JLabel lblMember5 = new JLabel("");

    /**
     * member display label 6
     */
    public final JLabel lblMember6 = new JLabel("");

    /**
     * selection 1
     */
    public final JRadioButton rad1 = new JRadioButton();

    /**
     * selection 2
     */
    public final JRadioButton rad2 = new JRadioButton();

    /**
     * selection 3
     */
    public final JRadioButton rad3 = new JRadioButton();

    /**
     * selection 4
     */
    public final JRadioButton rad4 = new JRadioButton();

    /**
     * selection 5
     */
    public final JRadioButton rad5 = new JRadioButton();

    /**
     * selection 6
     */
    public final JRadioButton rad6 = new JRadioButton();

    /**
     * groups the selection buttons
     */
    public final ButtonGroup radioGroup = new ButtonGroup();

    /**
     * center panel
     */
    public final JPanel pnlBase = new JPanel();

    /**
     * panel to show signal
     */
    public final JPanel pnlSignal = new JPanel();

    /**
     * add-button
     */
    public final JButton btnAdd = new JButton("Add");

    /**
     * remove-button
     */
    public final JButton btnRemove = new JButton("Remove");

    /**
     * close-button
     */
    public final JButton btnClose = new JButton("Close");

    /**
     * input field
     */
    public final JTextField txtInput = new JTextField("");

    private volatile boolean disposed = false;

    /**
     * Creates the view
     */
    public TeamView() {
        super("Team Manager");

        LOGGER.debug("{} created, building the components", this.getClass().getSimpleName());
        setupWindow();

    }

    /**
     * Creates the window with all the components and settings.
     */
    private void setupWindow() {
        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setSize(400, 280);
        setResizable(false);
        setLocationRelativeTo(null);
        setupUiComponents();
    }

    /**
     * Creates the visible components.
     */
    private void setupUiComponents() {
        JPanel pnlCenter = createCenterPanel();
        JPanel pnlButtons = new JPanel();

        btnAdd.setName(BTN_ADD);
        btnAdd.setMnemonic('A');
        btnRemove.setName(BTN_REMOVE);
        btnRemove.setMnemonic('R');

        btnClose.setName(BTN_CLOSE);
        btnClose.setMnemonic('C');

        btnAdd.setPreferredSize(btnRemove.getPreferredSize());
        btnClose.setPreferredSize(btnRemove.getPreferredSize());

        pnlButtons.add(btnAdd);
        pnlButtons.add(btnRemove);
        pnlButtons.add(btnClose);
        pnlButtons.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        pnlBase.setLayout(new BoxLayout(pnlBase, BoxLayout.Y_AXIS));
        pnlBase.add(pnlCenter);
        pnlBase.add(Box.createVerticalGlue());
        pnlBase.add(pnlButtons);

        this.getContentPane().add(pnlBase);
    }

    /**
     * Creates the center panel with the main inputs and outputs.
     * 
     * @return created panel
     */
    private JPanel createCenterPanel() {

        JPanel pnlOutput = createOutputPanel();

        JPanel pnlRadio = createRadioPanel();

        JPanel pnlCenter = new JPanel();
        pnlCenter.setLayout(new BorderLayout());
        pnlCenter.add(txtInput, BorderLayout.NORTH);

        pnlSignal.setMinimumSize(new Dimension(20, 10));
        pnlSignal.setMinimumSize(new Dimension(20, 10));

        pnlSignal.setBackground(Color.RED);

        pnlCenter.add(pnlSignal, BorderLayout.WEST);

        pnlCenter.add(pnlOutput, BorderLayout.CENTER);

        pnlCenter.add(pnlRadio, BorderLayout.EAST);
        return pnlCenter;
    }

    /**
     * Creates the group of radio buttons which lets the user select a member.
     * 
     * @return created panel
     */
    private JPanel createRadioPanel() {
        JPanel pnlRadio = new JPanel();
        pnlRadio.setLayout(new GridLayout(6, 1));

        pnlRadio.add(rad1);
        radioGroup.add(rad1);
        pnlRadio.add(rad2);
        radioGroup.add(rad2);
        pnlRadio.add(rad3);
        radioGroup.add(rad3);
        pnlRadio.add(rad4);
        radioGroup.add(rad4);
        pnlRadio.add(rad5);
        radioGroup.add(rad5);
        pnlRadio.add(rad6);
        radioGroup.add(rad6);
        return pnlRadio;
    }

    /**
     * Creates the output panel which lists the members.
     * 
     * @return created panel
     */
    private JPanel createOutputPanel() {
        JPanel pnlOutput = new JPanel();

        lblMember1.setHorizontalAlignment(SwingConstants.CENTER);
        lblMember2.setHorizontalAlignment(SwingConstants.CENTER);
        lblMember3.setHorizontalAlignment(SwingConstants.CENTER);
        lblMember4.setHorizontalAlignment(SwingConstants.CENTER);
        lblMember5.setHorizontalAlignment(SwingConstants.CENTER);
        lblMember6.setHorizontalAlignment(SwingConstants.CENTER);

        pnlOutput.setLayout(new GridLayout(6, 1));
        pnlOutput.add(lblMember1);
        pnlOutput.add(lblMember2);
        pnlOutput.add(lblMember3);
        pnlOutput.add(lblMember4);
        pnlOutput.add(lblMember5);
        pnlOutput.add(lblMember6);
        return pnlOutput;
    }

    @Override
    public void setVisible(boolean b) {
        super.setVisible(b);
        LOGGER.debug("{} is now {}visible", this.getClass().getSimpleName(), (b ? "" : "in"));
    }

    @Override
    public void dispose() {
        super.dispose();
        this.disposed = true;
        LOGGER.debug("{} has been disposed", this.getClass().getSimpleName());
    }

    public boolean isDisposed() {
        return disposed;
    }

}
