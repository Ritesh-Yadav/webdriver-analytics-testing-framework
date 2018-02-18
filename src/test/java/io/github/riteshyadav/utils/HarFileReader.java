package io.github.riteshyadav.utils;

import de.sstoehr.harreader.HarReader;
import de.sstoehr.harreader.HarReaderException;
import de.sstoehr.harreader.model.Har;
import de.sstoehr.harreader.model.HarEntry;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

public class HarFileReader {

    private Har har;
    public HarFileReader(File harFile){

        HarReader harReader = new HarReader();
        try {
            har = harReader.readFromFile(harFile);
        } catch (HarReaderException e) {
            e.printStackTrace();
        }

    }

    public List<HarEntry> getHarEntries(){
        return har.getLog().getEntries();
    }

    public List<HarEntry> getHarEntriesFor(List<HarEntry> harEntries, String urlPattern){
        return harEntries.stream()
                .filter(harEntry -> {
                    String url = harEntry.getRequest().getUrl();
                    if(url.contains(urlPattern)) {
                        return true;
                    }
                    return false;
                }).collect(Collectors.toList());

    }

}
