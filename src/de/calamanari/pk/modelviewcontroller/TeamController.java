/*
 * Team Controller - the CONTROLLER in this MVC-example.
 * Code-Beispiel zum Buch Patterns Kompakt, Verlag Springer Vieweg
 * Copyright 2013 Karl Eilebrecht
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
package de.calamanari.pk.modelviewcontroller;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Enumeration;
import java.util.logging.Logger;

import javax.swing.AbstractButton;
import javax.swing.JRadioButton;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * Team Controller - the CONTROLLER in this MVC-example.
 * @author <a href="mailto:Karl.Eilebrecht(a/t)web.de">Karl Eilebrecht</a>
 */
public class TeamController {

    /**
     * logger
     */
    private static final Logger LOGGER = Logger.getLogger(TeamController.class.getName());

    /**
     * reference to the model
     */
    private final TeamModel model;

    /**
     * reference to the view
     */
    private final TeamView view;

    /**
     * task to update the view
     */
    private final Runnable updateViewTask = new Runnable() {
        @Override
        public void run() {
            updateView();
        }
    };

    /**
     * Creates new controller responsible for the given model and the given view
     * @param teamModel the <b>M</b>odel to be attached to the <b>C</b>ontroller
     * @param teamView the <b>V</b>iew to be attached to the <b>C</b>ontroller
     */
    public TeamController(TeamModel teamModel, TeamView teamView) {

        LOGGER.fine(this.getClass().getSimpleName() + " created, holding refences to model and view");

        this.model = teamModel;
        this.view = teamView;

        registerCloseButtonActionListener();
        registerAddButtonActionListener();
        registerRemoveButtonActionListener();
        registerRadioButtonChangeListener();

        LOGGER.fine("Controls from view are now under surveillance of controller");

        registerTeamModelObserver();

        LOGGER.fine("Model is now under surveillance of controller");

        // initialize view status
        SwingUtilities.invokeLater(updateViewTask);

    }

    /**
     * Creates a new model observer and registers it at the internal team model.
     */
    private void registerTeamModelObserver() {
        // observe the model
        model.setModelObserver(new TeamModel.TeamModelObserver() {
            @Override
            public void handleModelChanged() {
                LOGGER.fine(TeamController.class.getSimpleName() + " - model changed, updating view.");
                SwingUtilities.invokeLater(updateViewTask);
            }
        });
    }

    /**
     * Creates a new ChangeListener and registers it at all the radio button controls.
     */
    private void registerRadioButtonChangeListener() {
        ChangeListener selectionListener = new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                LOGGER.fine(TeamController.class.getSimpleName() + " - selection changed, updating view.");
                updateView();
            }

        };

        view.rad1.addChangeListener(selectionListener);
        view.rad2.addChangeListener(selectionListener);
        view.rad3.addChangeListener(selectionListener);
        view.rad4.addChangeListener(selectionListener);
        view.rad5.addChangeListener(selectionListener);
        view.rad6.addChangeListener(selectionListener);
    }

    /**
     * Creates an action listener and adds it to the remove-button.<br>
     * The remove action removes the selected element (if any) and clears the selection.
     */
    private void registerRemoveButtonActionListener() {
        view.btnRemove.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                LOGGER.fine(TeamController.class.getSimpleName() + " - [Remove] clicked.");
                int radioButtonIndex = 0;
                int selectedIdx = -1;
                for (Enumeration<AbstractButton> en = view.radioGroup.getElements(); en.hasMoreElements();) {
                    JRadioButton rad = (JRadioButton) en.nextElement();
                    if (rad.isSelected()) {
                        selectedIdx = radioButtonIndex;
                    }
                    radioButtonIndex++;
                }
                if (selectedIdx >= 0) {
                    LOGGER.fine(TeamController.class.getSimpleName() + " - entry " + (selectedIdx + 1)
                            + " was selected, removing.");
                    model.remove(selectedIdx);
                }
                else {
                    LOGGER.fine(TeamController.class.getSimpleName() + " - no entry selected, request ignored.");
                }
                view.radioGroup.clearSelection();
                view.txtInput.setText("");
            }
        });
    }

    /**
     * Creates an action listener and adds it to the add-button.<br>
     * The add-action adds a new element to the model.
     */
    private void registerAddButtonActionListener() {
        view.btnAdd.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                LOGGER.fine(TeamController.class.getSimpleName() + " - [Add] clicked.");
                String newMember = view.txtInput.getText().trim();
                view.txtInput.setText("");
                if (newMember.length() > 0) {
                    LOGGER.fine("Adding member to model");
                    model.add(newMember);
                }
                else {
                    LOGGER.fine("Invalid input ignored");
                }
            }
        });
    }

    /**
     * Adds a new action listener to the close-button.
     */
    private void registerCloseButtonActionListener() {
        view.btnClose.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                LOGGER.fine(TeamController.class.getSimpleName() + " - [Close] clicked.");
                shutDown();
            }
        });
    }

    /**
     * Updates the view according to the current state
     */
    private void updateView() {

        LOGGER.fine(this.getClass().getSimpleName() + ".updateView called ...");

        updateLabels();

        LOGGER.fine("Controller has updated data on view");

        updateMemberControlStatus();

        updateButtonStatus();

        updateSignalBar();

        LOGGER.fine("Controller has updated the status (enabled/disabled/signal color) of controls on view");

    }

    /**
     * Updates the member labels from the model.
     */
    private void updateLabels() {
        String member1 = model.getMember(0);
        String member2 = model.getMember(1);
        String member3 = model.getMember(2);
        String member4 = model.getMember(3);
        String member5 = model.getMember(4);
        String member6 = model.getMember(5);

        view.lblMember1.setText(member1 == null ? "" : member1);
        view.lblMember2.setText(member2 == null ? "" : member2);
        view.lblMember3.setText(member3 == null ? "" : member3);
        view.lblMember4.setText(member4 == null ? "" : member4);
        view.lblMember5.setText(member5 == null ? "" : member5);
        view.lblMember6.setText(member6 == null ? "" : member6);
    }

    /**
     * Updates the status of the radio-buttons
     */
    private void updateMemberControlStatus() {
        updateMemberControlStatus(model.getMember(0), view.rad1);
        updateMemberControlStatus(model.getMember(1), view.rad2);
        updateMemberControlStatus(model.getMember(2), view.rad3);
        updateMemberControlStatus(model.getMember(3), view.rad4);
        updateMemberControlStatus(model.getMember(4), view.rad5);
        updateMemberControlStatus(model.getMember(5), view.rad6);
    }

    /**
     * The color of the signal bar depends on the number of members in the list.
     */
    private void updateSignalBar() {

        // update color of signal bar
        if (model.getMember(1) == null) {
            // need more members
            view.pnlSignal.setBackground(Color.RED);
        }
        else if (model.getMember(2) == null || model.getMember(3) == null) {
            // more members would be better
            view.pnlSignal.setBackground(Color.YELLOW);
        }
        else {
            // enough members
            view.pnlSignal.setBackground(Color.LIGHT_GRAY);
        }
    }

    /**
     * Updates the button status according to the number of members in the list.
     */
    private void updateButtonStatus() {
        String member6 = model.getMember(5);
        view.btnAdd.setEnabled(member6 == null);
        view.txtInput.setEnabled(member6 == null);
        view.btnRemove.setEnabled(view.radioGroup.getSelection() != null);
    }

    /**
     * updates the control depending on the current status
     * @param member input name of the member
     * @param radioButton control associated to the member
     */
    private void updateMemberControlStatus(String member, JRadioButton radioButton) {
        if (member == null) {
            if (radioButton.isSelected()) {
                view.radioGroup.clearSelection();
            }
            radioButton.setEnabled(false);
        }
        else {
            radioButton.setEnabled(true);
        }

    }

    /**
     * Close view and shutdown
     */
    public void shutDown() {
        LOGGER.fine(this.getClass().getSimpleName() + ".shutDown() called.");
        this.view.setVisible(false);
        this.view.dispose();
    }

}
