/*
 * Model View Controller Test - demonstrates MODEL VIEW CONTROLLER pattern.
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
package de.calamanari.pk.modelviewcontroller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.AbstractButton;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import org.junit.BeforeClass;
import org.junit.Test;

import de.calamanari.pk.modelviewcontroller.TeamController;
import de.calamanari.pk.modelviewcontroller.TeamModel;
import de.calamanari.pk.modelviewcontroller.TeamView;
import de.calamanari.pk.util.LogUtils;
import de.calamanari.pk.util.MiscUtils;

/**
 * Model View Controller Test - demonstrates MODEL VIEW CONTROLLER pattern.
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 */
public class ModelViewControllerTest {

    /**
     * logger
     */
    private static final Logger LOGGER = Logger.getLogger(ModelViewControllerTest.class.getName());

    /**
     * Log-level for this test
     */
    private static final Level LOG_LEVEL = Level.INFO;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        LogUtils.setConsoleHandlerLogLevel(LOG_LEVEL);
        LogUtils.setLogLevel(LOG_LEVEL, ModelViewControllerTest.class, TeamModel.class, TeamView.class,
                TeamController.class);
    }

    @Test
    public void testModelViewController() throws Exception {

        // In this example we have an application for team management (some sports).
        // A team can have at most 6 members.
        // At least two members form a valid team, otherwise a read signal will be shown.
        // Up to 4 members are a weak team (signal will be yellow), more than four
        // members a are a good team (no warning signal).
        // Members can be added to a team or be removed.

        // The view only displays data, it neither knows the model nor the controller.
        // This is called a PASSIVE VIEW.
        // The model only maintains data, using the observer pattern it
        // informs a listener (the controller) when the model has changed.
        // The controller observes the view as well as the model and manages
        // consistency between them.

        // If you're not familiar with Java SWING, don't wonder about the strange Runnables
        // and this awaitDispatch()-Thingy, it's only technical stuff to cope with
        // threading. Be aware that we have two threads here, one actually running
        // the test, the other one for updating the UI.
        // SWING provides a Singleton-Dispatch-Thread, the only thread who is allowed
        // to modify the UI. Thus at certain points calls have to be wrapped into
        // Runnables not to break the law :-)

        // HINTS:
        // * Adjust the log-level above to FINE to see the MODEL VIEW CONTROLLER working

        LOGGER.info("Test Model View Controller ...");
        long startTimeNanos = System.nanoTime();

        final TeamView view = new TeamView();
        TeamModel model = new TeamModel();

        view.setVisible(true);

        TeamController controller = new TeamController(model, view);

        LOGGER.fine("Controller " + controller.getClass().getSimpleName() + " connected, application ready to use.");

        assertEquals(0, model.size());

        enterText(view.txtInput, "Jeremy");
        doClick(view.btnAdd);

        assertEquals(1, model.size());

        assertEquals("Jeremy", model.getMember(0));

        waitSomeTime();

        doClick(view.rad1);
        doClick(view.btnRemove);

        assertEquals(0, model.size());

        enterText(view.txtInput, "Jenny");
        doClick(view.btnAdd);

        enterText(view.txtInput, "Marta");
        doClick(view.btnAdd);

        enterText(view.txtInput, "Hugo");
        doClick(view.btnAdd);

        enterText(view.txtInput, "Jack");
        doClick(view.btnAdd);

        // now do something on the backside,
        // the controller shall observe that and immediately
        // update the UI
        model.add("Larry");

        waitSomeTime();

        assertEquals(5, model.size());

        model.add("Bernie");

        waitSomeTime();

        assertEquals(6, model.size());

        doClick(view.rad5);
        doClick(view.btnRemove);

        assertEquals("Bernie", model.getMember(4));

        enterText(view.txtInput, "Dug");
        doClick(view.btnAdd);

        assertFalse(view.btnAdd.isEnabled());

        assertEquals(6, model.size());

        for (int i = 0; i < 5; i++) {
            doClick(view.rad1);
            doClick(view.btnRemove);
            waitSomeTime();
        }

        assertEquals(1, model.size());

        doClick(view.btnClose);

        view.shutDownLatch.await();

        LOGGER.info("Test Model View Controller successful! Elapsed time: "
                + MiscUtils.formatNanosAsSeconds(System.nanoTime() - startTimeNanos) + " s");

    }

    /**
     * short wait time (for better UI-experience)
     */
    private void waitSomeTime() {
        MiscUtils.sleepIgnoreException(1000);
    }

    /**
     * Clicks the given button
     * @param button
     */
    private void doClick(final AbstractButton button) {
        try {
            SwingUtilities.invokeAndWait(new Runnable() {
                @Override
                public void run() {
                    button.doClick(100);
                }
            });
            Thread.sleep(120);
        }
        catch (Exception ex) {
            throw new RuntimeException("Unexpected Problem during dispatch!", ex);
        }
    }

    /**
     * Simulates the user while entering data
     * @param field
     * @param text
     */
    private void enterText(final JTextField field, final String text) {
        int len = text.length();
        for (int i = len - 1; i > -1; i--) {
            final int curLen = len - i;
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    field.setText(text.substring(0, curLen));
                }
            });
            MiscUtils.sleepIgnoreException(100);
        }
        MiscUtils.sleepIgnoreException(500);
    }

}
