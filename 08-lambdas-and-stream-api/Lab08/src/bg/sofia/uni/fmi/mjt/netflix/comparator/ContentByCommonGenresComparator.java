package bg.sofia.uni.fmi.mjt.netflix.comparator;

import bg.sofia.uni.fmi.mjt.netflix.Content;

import java.util.Comparator;

public class ContentByCommonGenresComparator implements Comparator<Content> {

    public static Content anchor = null;

    @Override
    public int compare(Content o1, Content o2) {
        return Integer.compare(o1.calculateAmountOfCommonGenres(anchor), o2.calculateAmountOfCommonGenres(anchor));
    }
}
