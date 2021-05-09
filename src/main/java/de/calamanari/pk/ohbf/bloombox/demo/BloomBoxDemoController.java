//@formatter:off
/*
 * BloomBoxDemoController
 * Code-Beispiel zum Buch Patterns Kompakt, Verlag Springer Vieweg
 * Copyright 2014 Karl Eilebrecht
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"):
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

package de.calamanari.pk.ohbf.bloombox.demo;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;

import de.calamanari.pk.ohbf.bloombox.BbxMessage;
import de.calamanari.pk.ohbf.bloombox.BloomBox;
import de.calamanari.pk.ohbf.bloombox.BloomBoxQueryRunner;
import de.calamanari.pk.ohbf.bloombox.QueryBundle;
import de.calamanari.pk.ohbf.bloombox.QueryBundleResult;

/**
 * CONTROLLER of the demo UI application, model is not explicit here (not much to hold except from the box)
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 *
 */
public class BloomBoxDemoController {

    /**
     * VIEW reference
     */
    private final BloomBoxDemoView view;

    /**
     * The runner for executing queries
     */
    private BloomBoxQueryRunner runner;

    /**
     * underlying box
     */
    private BloomBox bloomBox;

    /**
     * display text
     */
    private static final String ABOUT_TEXT = """
            BloomBox Demo UI

            Code-Beispiel zum Buch Patterns Kompakt, Verlag Springer Vieweg
            Copyright 2014 Karl Eilebrecht

            Licensed under the Apache License, Version 2.0 (the "License"):
            you may not use this file except in compliance with the License.
            You may obtain a copy of the License at

            http://www.apache.org/licenses/LICENSE-2.0

            Unless required by applicable law or agreed to in writing, software
            distributed under the License is distributed on an "AS IS" BASIS,
            WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
            See the License for the specific language governing permissions and
            limitations under the License.

            Visit Patterns Kompakt on github:
            https://github.com/KarlEilebrecht/patterns-kompakt-code

            The BloomBox query language BBQ is powered by ANTLR:
            https://www.antlr.org/
            """;

    /**
     * display text
     */
    private static final String HELP_TEXT = """

            # The upper split pane of the BloomBox Demo application's main window displays some information
            # about the currently loaded box data.
            # The large area below allows running queries against the box.
            #
            # On the left side you can enter queries (click Execute-button).
            # Results and error messages will be displayed on the right.

            # Queries can be either entered as valid JSON (QueryBundle) OR using EasyScript syntax.

            # EasyScript is a line-based less verbose format, 1 line is one query or sub-query.
            # Comment lines starts with a '#' (this help text is a valid EasyScript).
            # Any line that does not start with whitespace is a taken as a main query.
            # A main query that contains any '${' is lazily interpreted as post query.
            # Any line that starts with whitespace will be added as a sub query to the previous main query.
            # Each query line must start with a name (query identifier), followed by a colon, followed by a
            # valid BBQ expression.

            # Example:

            Query1: vendor=Audi and model=A3
                red: color=red
                blue: color=blue
                black: color=black
                other: color NOT IN (red, blue, black)

            Query2: vendor=Ford and model=Mustang

            Details on Mustang: ${Query2}
                sedan: type=sedan
                not sedan: type!=sedan

            Sedan by color: ${'Details on Mustang.sedan'}
                red: color=red
                blue: color=blue
                black: color=black
                other: color NOT IN (red, blue, black)

            """;

    /**
     * task to update the view at startup
     */
    private final Runnable updateViewTask = this::updateView;

    /**
     * @param view VIEW
     * @param bloomBox underlying box
     * @param runner for executing queries
     */
    public BloomBoxDemoController(BloomBoxDemoView view, BloomBox bloomBox, BloomBoxQueryRunner runner) {
        this.view = view;
        this.bloomBox = bloomBox;
        this.runner = runner;
        this.view.btnExecute.addActionListener(e -> this.executeQuery());
        this.view.btnHelp.addActionListener(e -> this.showHelpPopup());

        this.view.lblBloomBoxIcon.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                JOptionPane.showMessageDialog(null, ABOUT_TEXT, "About BloomBox Demo UI", JOptionPane.INFORMATION_MESSAGE);
            }

        });

        // initialize view status
        SwingUtilities.invokeLater(updateViewTask);

    }

    /**
     * Shows a popup with help information
     */
    private void showHelpPopup() {
        JOptionPane.showMessageDialog(null, new JScrollPane(this.view.createHelpTextArea(HELP_TEXT)), "Help", JOptionPane.QUESTION_MESSAGE);
    }

    /**
     * Updates the view according to the current state
     */
    private void updateView() {

        StringBuilder sb = new StringBuilder();
        sb.append("BloomBox VERSION: ");
        sb.append(BloomBox.VERSION);
        sb.append("\nCreated: ");
        if (bloomBox.getDateCreated() == null) {
            sb.append("<N/A>");
        }
        else {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'");
            df.setTimeZone(TimeZone.getTimeZone("UTC"));
            sb.append(df.format(bloomBox.getDateCreated()));
        }
        sb.append("\n");
        sb.append(bloomBox.getConfig());
        sb.append("\n");
        sb.append(bloomBox.getDataStore().toString());
        view.tarBloomBoxInfo.setText(sb.toString());
        view.tarBloomBoxInfo.setCaretPosition(0);
        view.tarBloomBoxDescription.setText(bloomBox.getDescription());
        view.tarBloomBoxDescription.setCaretPosition(0);

        view.tarBloomBoxOutput.setText("Welcome to BloomBox Demo!\n\nEnter your query on the left.");
        view.tarBloomBoxOutput.setCaretPosition(0);

        view.tarBloomBoxInput.setText("");
        view.tarBloomBoxInput.setCaretPosition(0);

    }

    /**
     * Runs the current query and updates the output
     */
    private void executeQuery() {
        QueryBundle bundle = null;
        try {
            String input = (view.tarBloomBoxInput.getText().trim());
            if (input.startsWith("{")) {
                bundle = parseBundle(input);
            }
            else if (!input.isBlank()) {
                bundle = QueryBundle.fromEasyScript(input);
            }
            else {
                view.tarBloomBoxOutput.setText("");
            }
        }
        catch (JsonProcessingException | RuntimeException ex) {
            if (ex.getMessage() == null || !ex.getMessage().startsWith(BbxMessage.ERROR_CODE_PREFIX)) {
                StringWriter sw = new StringWriter();
                PrintWriter pw = new PrintWriter(sw);
                ex.printStackTrace(pw);
                view.tarBloomBoxOutput.setText(sw.toString());
            }
            else {
                view.tarBloomBoxOutput.setText(ex.getMessage());
            }
        }
        if (bundle != null) {
            long time = System.currentTimeMillis();
            QueryBundleResult res = runner.execute(bundle);
            view.tarBloomBoxOutput.setText(res.toDebugString() + "\n\nDuration: " + (System.currentTimeMillis() - time) + " ms");
        }
    }

    /**
     * If the user entered a JSON, this method converts it
     * 
     * @param json user input
     * @return query bundle
     * @throws JsonProcessingException
     */
    private QueryBundle parseBundle(String json) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);
        objectMapper.setPropertyNamingStrategy(new PropertyNamingStrategies.SnakeCaseStrategy());
        return objectMapper.readValue(json, QueryBundle.class);
    }

    /**
     * Close view and shutdown
     */
    public void shutDown() {
        this.view.setVisible(false);
        this.view.dispose();
    }

}
