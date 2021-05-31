//@formatter:off
/*
 * BbxProtocol
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

package de.calamanari.pk.ohbf.bloombox;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.UUID;

import de.calamanari.pk.ohbf.bloombox.bbq.ExpressionIdUtil;

/**
 * Protocol data that can be optionally attached to a {@link BloomBoxQueryResult}
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 *
 */
public class BbxProtocol implements Serializable {

    private static final long serialVersionUID = -2182263529340552457L;

    /**
     * UUID for the running process, no intrinsic meaning, just to distinguish messages from different processes.
     */
    private static final String BBX_PROCESS_ID = Long.toHexString(ExpressionIdUtil.createExpressionId(UUID.randomUUID().toString()));

    /**
     * protocol entries
     */
    private List<String> entries = new ArrayList<>();

    public BbxProtocol() {
        // default
    }

    /**
     * @return entries recorded so far (the protocol)
     */
    public List<String> getEntries() {
        return entries;
    }

    /**
     * @param entries the protocol, can be sorted naturally to group by executor ordered by time
     */
    public void setEntries(List<String> entries) {
        this.entries = entries;
    }

    /**
     * Adds the given message with a prepended executor id and a time stamp to the protocol.
     * 
     * @param message arbitrary information
     */
    public void logEntry(long executionId, String message) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        this.entries.add(String.format("%d %s %s@%s: %s", executionId, df.format(new Date()), Long.toHexString(Thread.currentThread().getId()), BBX_PROCESS_ID,
                message));
    }

    /**
     * Appends the messages from the other protocol to <b>this</b> protocol
     * 
     * @param otherProtocol entries to be appended
     */
    public void addProtocol(BbxProtocol otherProtocol) {
        if (otherProtocol != null) {
            this.entries.addAll(otherProtocol.entries);
        }
    }

    @Override
    public String toString() {
        return "BbxProtocol [" + (entries == null ? "<INVALID>" : entries.size()) + "]";
    }

}
