package de.calamanari.pk.ohbf.bloombox;

public interface DataPointOccurrenceAware extends DataPointDictionaryAware {

    public void registerDataPointOccurrences(DataPointOccurrenceCollector collector);
}
