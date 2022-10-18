import java.util.Arrays;

public class PrefixExtractor {
    public static String getLongestCommonPrefix(String[] words){
        if ( words == null ) return "";
        if ( words.length == 0 ) return "";
        if ( words.length == 1 ) return words[0];

        StringBuilder PREF = new StringBuilder();
        Arrays.sort(words);

        final int WORD_ONE_SIZE = words[0].length();
        final int WORD_TWO_SIZE = words[words.length-1].length();

        for( int i = 0 ; i < WORD_ONE_SIZE && i < WORD_TWO_SIZE ; ++i){
            if(words[0].charAt(i)!=words[words.length-1].charAt(i)) break;
            PREF.append(words[0].charAt(i));
        }
           /*
        Brute force
        final int ARRAY_SIZE = words.length;
        for(int i = 0; i < WORD_SIZE; ++i) {

            for (int j = 1; j < ARRAY_SIZE; ++j) {
                if(!words[j].substring(0,PREF_SIZE).equals(PREF.toString())){
                    return PREF.substring(0,PREF_SIZE-1);
                }
            }
            PREF.append(words[0].charAt(i));
            PREF_SIZE++;
        }*/

        return PREF.toString();
    }
    public static void main(String[] args) {
        System.out.println(getLongestCommonPrefix(new String[]{"flower", "flow", "flight"}));
        System.out.println(getLongestCommonPrefix(new String[]{"dog", "racecar", "car"}));
        System.out.println(getLongestCommonPrefix(new String[]{"cat"}));

    }
}
