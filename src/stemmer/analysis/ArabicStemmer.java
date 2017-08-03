/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stemmer.analysis;

import static stemmer.analysis.StemmerUtil.deleteN;

/**
 *
 * @author amina
 */
public class ArabicStemmer {
  public static final char ALEF = '\u0627';
  public static final char BEH = '\u0628';
  public static final char TEH_MARBUTA = '\u0629';
  public static final char TEH = '\u062A';
  public static final char FEH = '\u0641';
  public static final char KAF = '\u0643';
  public static final char LAM = '\u0644';
  public static final char NOON = '\u0646';
  public static final char HEH = '\u0647';
  public static final char WAW = '\u0648';
  public static final char YEH = '\u064A';
  
  public static final char prefixes[][] = {
      ("" + ALEF + LAM).toCharArray(), 
      ("" + WAW + ALEF + LAM).toCharArray(), 
      ("" + BEH + ALEF + LAM).toCharArray(),
      ("" + KAF + ALEF + LAM).toCharArray(),
      ("" + FEH + ALEF + LAM).toCharArray(),
      ("" + LAM + LAM).toCharArray(),
      ("" + WAW).toCharArray(),
  };
  
  public static final char suffixes[][] = {
    ("" + HEH + ALEF).toCharArray(), 
    ("" + ALEF + NOON).toCharArray(), 
    ("" + ALEF + TEH).toCharArray(), 
    ("" + WAW + NOON).toCharArray(), 
    ("" + YEH + NOON).toCharArray(), 
    ("" + YEH + HEH).toCharArray(),
    ("" + YEH + TEH_MARBUTA).toCharArray(),
    ("" + HEH).toCharArray(),
    ("" + TEH_MARBUTA).toCharArray(),
    ("" + YEH).toCharArray(),
};
  
  /**
   * Stem an input buffer of Arabic text.
   * 
   * @param s input buffer
   * @param len length of input buffer
   * @return length of input buffer after normalization
   */
  public int stem(char s[], int len) {
    len = stemPrefix(s, len);
    len = stemSuffix(s, len);
    
    return len;
  }
  
  /**
   * Stem a prefix off an Arabic word.
   * @param s input buffer
   * @param len length of input buffer
   * @return new length of input buffer after stemming.
   */
  public int stemPrefix(char s[], int len) {
    for (int i = 0; i < prefixes.length; i++) 
      if (startsWithCheckLength(s, len, prefixes[i]))
        return deleteN(s, 0, len, prefixes[i].length);
    return len;
  }

  /**
   * Stem suffix(es) off an Arabic word.
   * @param s input buffer
   * @param len length of input buffer
   * @return new length of input buffer after stemming
   */
  public int stemSuffix(char s[], int len) {
    for (int i = 0; i < suffixes.length; i++) 
      if (endsWithCheckLength(s, len, suffixes[i]))
        len = deleteN(s, len - suffixes[i].length, len, suffixes[i].length);
    return len;
  }
  
  /**
   * Returns true if the prefix matches and can be stemmed
   * @param s input buffer
   * @param len length of input buffer
   * @param prefix prefix to check
   * @return true if the prefix matches and can be stemmed
   */
  boolean startsWithCheckLength(char s[], int len, char prefix[]) {
    if (prefix.length == 1 && len < 4) { // wa- prefix requires at least 3 characters
      return false;
    } else if (len < prefix.length + 2) { // other prefixes require only 2.
      return false;
    } else {
      for (int i = 0; i < prefix.length; i++)
        if (s[i] != prefix[i])
          return false;
        
      return true;
    }
  }
  
  /**
   * Returns true if the suffix matches and can be stemmed
   * @param s input buffer
   * @param len length of input buffer
   * @param suffix suffix to check
   * @return true if the suffix matches and can be stemmed
   */
  boolean endsWithCheckLength(char s[], int len, char suffix[]) {
    if (len < suffix.length + 2) { // all suffixes require at least 2 characters after stemming
      return false;
    } else {
      for (int i = 0; i < suffix.length; i++)
        if (s[len - suffix.length + i] != suffix[i])
          return false;
        
      return true;
    }
  }  
}
