//@formatter:off
/*
 * BloomBoxDemoView
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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.font.FontRenderContext;
import java.awt.font.LineMetrics;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.text.CharacterIterator;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.WindowConstants;
import javax.swing.border.LineBorder;

import de.calamanari.pk.ohbf.bloombox.BloomBoxException;

/**
 * Simplistic VIEW of the demo application for easily entering queries.
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 *
 */
// Sonar Lint rule violation cannot be avoided when using Swing
// as well as non-static initializer warning, as it is for better readability here
@SuppressWarnings({ "squid:MaximumInheritanceDepth", "java:S1171" })
public class BloomBoxDemoView extends JFrame {

    /**
     * constant name for execute button
     */
    public static final String BTN_EXECUTE = "btnExecute";

    /**
     * constant name for help button
     */
    public static final String BTN_HELP = "btnHelp";

    /**
     * monospaced font for better readability
     */
    private static final Font MONOSPACED_BIGGER = new Font("monospaced", Font.PLAIN, 14);

    /**
     * center panel
     */
    public final JPanel pnlBase = new JPanel();

    /**
     * info area
     */
    public final JTextArea tarBloomBoxInfo;
    {
        tarBloomBoxInfo = new JTextArea("");
        tarBloomBoxInfo.setEditable(false);
        tarBloomBoxInfo.setMinimumSize(new Dimension(100, 100));
    }

    /**
     * description area
     */
    public final JTextArea tarBloomBoxDescription;
    {
        tarBloomBoxDescription = new JTextArea("");
        tarBloomBoxDescription.setMinimumSize(new Dimension(100, 100));

    }

    /**
     * bloom box icon on the left
     */
    public final JLabel lblBloomBoxIcon;
    {
        try {
            lblBloomBoxIcon = new JLabel(new ImageIcon(ImageIO.read(BloomBoxDemoView.class.getResource("/bloombox_small.png"))));
            lblBloomBoxIcon.setMinimumSize(new Dimension(100, 100));
            lblBloomBoxIcon.setMaximumSize(new Dimension(100, 100));
            lblBloomBoxIcon.setBorder(new LineBorder(new Color(238, 238, 238), 12));
            lblBloomBoxIcon.setToolTipText("About");
        }
        catch (IOException ex) {
            throw new BloomBoxException("Unable to lookup image", ex);
        }
    }

    /**
     * User input
     */
    public final JTextArea tarBloomBoxInput;
    {
        tarBloomBoxInput = new JTextArea("") {
            private static final long serialVersionUID = -3226071186488915788L;

            @Override
            public FontMetrics getFontMetrics(Font font) {
                return new FontMetricsWrapper(super.getFontMetrics(font)) {
                    private static final long serialVersionUID = -8715661694986586776L;

                    @Override
                    public int getHeight() {
                        return 25;
                    }
                };
            }
        };
        tarBloomBoxInput.setFont(MONOSPACED_BIGGER);

    }

    /**
     * message output
     */
    public final JTextArea tarBloomBoxOutput;
    {
        tarBloomBoxOutput = new JTextArea("");
        tarBloomBoxOutput.setFont(MONOSPACED_BIGGER);
        tarBloomBoxOutput.setEditable(false);
        tarBloomBoxOutput.setLineWrap(true);
    }

    /**
     * execute-button
     */
    public final JButton btnExecute;
    {
        btnExecute = new JButton("Execute");
        btnExecute.setName(BTN_EXECUTE);
        btnExecute.setMnemonic('E');
    }

    /**
     * help-button
     */
    public final JButton btnHelp;
    {
        btnHelp = new JButton("Help");
        btnHelp.setName(BTN_HELP);
        btnHelp.setMnemonic('H');
    }

    /**
     * Indicates this {@link #dispose()} has previously been called on this view
     */
    private volatile boolean disposed = false;

    /**
     * for serialization
     */
    private static final long serialVersionUID = -6666864012253667394L;

    public BloomBoxDemoView() {
        super("BloomBox Demo UI");

        setupWindow();
    }

    /**
     * Creates the window with all the components and settings.
     */
    private void setupWindow() {
        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setSize(800, 600);
        setResizable(true);
        setLocationRelativeTo(null);
        setupUiComponents();
    }

    /**
     * Creates the visible components.
     */
    private void setupUiComponents() {

        pnlBase.setLayout(new BorderLayout(10, 10));

        JPanel pnlHeader = createHeaderPanel();

        pnlBase.add(pnlHeader, BorderLayout.NORTH);

        JScrollPane scrBloomBoxInput = new JScrollPane(tarBloomBoxInput);
        scrBloomBoxInput.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrBloomBoxInput.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        JScrollPane scrBloomBoxOutput = new JScrollPane(tarBloomBoxOutput);
        scrBloomBoxOutput.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrBloomBoxOutput.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, scrBloomBoxInput, scrBloomBoxOutput);
        splitPane.setOneTouchExpandable(true);
        splitPane.setDividerLocation(300);

        pnlBase.add(splitPane, BorderLayout.CENTER);

        this.getContentPane().add(pnlBase);

    }

    /**
     * creates the header with box information and the bloom box icon as well as the buttons
     * 
     * @return header panel
     */
    private JPanel createHeaderPanel() {

        JPanel pnlHeader = new JPanel();
        pnlHeader.setLayout(new BorderLayout(5, 5));
        pnlHeader.add(lblBloomBoxIcon, BorderLayout.EAST);

        JScrollPane scrBloomBoxInfo = new JScrollPane(tarBloomBoxInfo);
        scrBloomBoxInfo.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrBloomBoxInfo.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrBloomBoxInfo.setMinimumSize(new Dimension(100, 100));

        JScrollPane scrBloomBoxDescription = new JScrollPane(tarBloomBoxDescription);
        scrBloomBoxDescription.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrBloomBoxDescription.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrBloomBoxDescription.setMinimumSize(new Dimension(100, 100));

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, scrBloomBoxInfo, scrBloomBoxDescription);

        splitPane.setOneTouchExpandable(true);
        splitPane.setDividerLocation(400);

        pnlHeader.add(splitPane, BorderLayout.CENTER);

        JPanel pnlButtons = new JPanel();

        pnlButtons.setLayout(new BorderLayout());

        pnlButtons.add(btnExecute, BorderLayout.WEST);
        pnlButtons.add(btnHelp, BorderLayout.EAST);
        pnlHeader.add(pnlButtons, BorderLayout.SOUTH);

        return pnlHeader;
    }

    /**
     * @param helpText text to display
     * @return a new text area to display some read-only help info
     */
    public JTextArea createHelpTextArea(String helpText) {
        JTextArea res = new JTextArea(helpText);
        res.setFont(new Font("monospaced", Font.PLAIN, 14));
        res.setColumns(120);
        res.setLineWrap(true);
        res.setWrapStyleWord(true);
        res.setSize(res.getPreferredSize().width, 1);
        res.setEditable(false);
        return res;
    }

    @Override
    public void dispose() {
        super.dispose();
        this.disposed = true;
    }

    /**
     * @return true if {@link #dispose()} has previously been called on this view, otherwise false
     */
    public boolean isDisposed() {
        return disposed;
    }

    /**
     * Workaround, allows to easily increase the line spacing in the user input text area for better readability
     *
     */
    // eclipse-generated delegator code (wrapper)
    @SuppressWarnings("all")
    public class FontMetricsWrapper extends FontMetrics {

        private static final long serialVersionUID = -3914352589119355241L;
        private final FontMetrics delegate;

        @Override
        public int hashCode() {
            return delegate.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            return delegate.equals(obj);
        }

        @Override
        public Font getFont() {
            return delegate.getFont();
        }

        @Override
        public FontRenderContext getFontRenderContext() {
            return delegate.getFontRenderContext();
        }

        @Override
        public int getLeading() {
            return delegate.getLeading();
        }

        @Override
        public int getAscent() {
            return delegate.getAscent();
        }

        @Override
        public int getDescent() {
            return delegate.getDescent();
        }

        @Override
        public int getHeight() {
            return delegate.getHeight();
        }

        @Override
        public int getMaxAscent() {
            return delegate.getMaxAscent();
        }

        @Override
        public int getMaxDescent() {
            return delegate.getMaxDescent();
        }

        @Override
        public int getMaxDecent() {
            return delegate.getMaxDecent();
        }

        @Override
        public int getMaxAdvance() {
            return delegate.getMaxAdvance();
        }

        @Override
        public int charWidth(int codePoint) {
            return delegate.charWidth(codePoint);
        }

        @Override
        public int charWidth(char ch) {
            return delegate.charWidth(ch);
        }

        @Override
        public int stringWidth(String str) {
            return delegate.stringWidth(str);
        }

        @Override
        public int charsWidth(char[] data, int off, int len) {
            return delegate.charsWidth(data, off, len);
        }

        @Override
        public int bytesWidth(byte[] data, int off, int len) {
            return delegate.bytesWidth(data, off, len);
        }

        @Override
        public int[] getWidths() {
            return delegate.getWidths();
        }

        @Override
        public boolean hasUniformLineMetrics() {
            return delegate.hasUniformLineMetrics();
        }

        @Override
        public LineMetrics getLineMetrics(String str, Graphics context) {
            return delegate.getLineMetrics(str, context);
        }

        @Override
        public LineMetrics getLineMetrics(String str, int beginIndex, int limit, Graphics context) {
            return delegate.getLineMetrics(str, beginIndex, limit, context);
        }

        @Override
        public LineMetrics getLineMetrics(char[] chars, int beginIndex, int limit, Graphics context) {
            return delegate.getLineMetrics(chars, beginIndex, limit, context);
        }

        @Override
        public LineMetrics getLineMetrics(CharacterIterator ci, int beginIndex, int limit, Graphics context) {
            return delegate.getLineMetrics(ci, beginIndex, limit, context);
        }

        @Override
        public Rectangle2D getStringBounds(String str, Graphics context) {
            return delegate.getStringBounds(str, context);
        }

        @Override
        public Rectangle2D getStringBounds(String str, int beginIndex, int limit, Graphics context) {
            return delegate.getStringBounds(str, beginIndex, limit, context);
        }

        @Override
        public Rectangle2D getStringBounds(char[] chars, int beginIndex, int limit, Graphics context) {
            return delegate.getStringBounds(chars, beginIndex, limit, context);
        }

        @Override
        public Rectangle2D getStringBounds(CharacterIterator ci, int beginIndex, int limit, Graphics context) {
            return delegate.getStringBounds(ci, beginIndex, limit, context);
        }

        @Override
        public Rectangle2D getMaxCharBounds(Graphics context) {
            return delegate.getMaxCharBounds(context);
        }

        @Override
        public String toString() {
            return delegate.toString();
        }

        FontMetricsWrapper(FontMetrics delegate) {
            super(delegate.getFont());
            this.delegate = delegate;
        }

    }

}
