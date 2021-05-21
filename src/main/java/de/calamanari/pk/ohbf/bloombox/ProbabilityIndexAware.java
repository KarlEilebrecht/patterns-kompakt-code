package de.calamanari.pk.ohbf.bloombox;

import java.util.Map;

import de.calamanari.pk.ohbf.bloombox.bbq.ExpressionIdUtil;

/**
 * Interface to pass probability information downwards the query and expression hierarchy.
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 *
 */
public interface ProbabilityIndexAware {

    /**
     * To be called before execution to prepare expressions with the probability index.
     * 
     * @param probabilityIndexMap key is the {@link ExpressionIdUtil}-encoded column name, value is the index in the probability vector (aka column index)
     */
    public void prepareProbabilityIndex(Map<Long, Integer> probabilityIndexMap);
}
