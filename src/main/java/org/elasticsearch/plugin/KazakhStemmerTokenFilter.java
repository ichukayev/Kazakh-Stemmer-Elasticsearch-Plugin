package org.elasticsearch.plugin;

import java.io.IOException;
import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.KeywordAttribute;

// Код практически скопипастен отсюда
// https://github.com/vgrichina/ukrainian-stemmer/blob/master/src/main/groovy/com/componentix/nlp/stemmer/uk/Stemmer.groovy

public class KazakhStemmerTokenFilter extends TokenFilter {

    /**
     * Construct a token stream filtering the given input.
     *
     * @param input
     */
    public KazakhStemmerTokenFilter(TokenStream input) {
        super(input);
    }

    private final CharTermAttribute termAttribute = addAttribute(CharTermAttribute.class);
    private final KeywordAttribute keywordAttr = addAttribute(KeywordAttribute.class);

    @Override
    public final boolean incrementToken() throws IOException {

        if (!input.incrementToken()) return false;

        if (!keywordAttr.isKeyword()) {
            String result = stem(new String(termAttribute.buffer(), 0, termAttribute.length()));
            termAttribute.setEmpty().append(result);
        }
        return true;
    }

    public String stem(String word)
    {
        // don't change short words
        if (word.length() <= 2 ) return word;

        // try simple trim
        for (String suffix : suffixes) {
            if (word.endsWith(suffix)) {
                String trimmed = word.substring(0, word.length() - suffix.length());
                if (trimmed.length() > 2) {
                    return trimmed;
                }
            }
        }

        return word;
    }

    private final String suffixes[] = new String[] {
            "мын", "мiн", "бын", "бiн", "пын", "пiн", "мыз", "мiз", "быз", "бiз", "пыз", "пiз", "сың", "сiң",
            "сыңдар", "сiңдер", "сыз", "сiз", "сыздар", "сiздер", "ңдар", "ңдер", "ңыз",
            "ңiз", "ңыздар", "ңiздер", "сы", "сi", "дан", "ден", "тан", "тен", "нан", "нен", "да",
            "де", "та", "те", "нда", "нде", "ға", "ге", "қа", "ке", "на", "не", "дың", "дiң", "тың",
            "тiң", "ның", "нiң", "ды", "дi", "ты", "тi", "ны", "нi", "н", "дiкi", "тiкi", "нiкi", "дар", "дер",
            "тар", "тер", "лар", "лер", "ба", "бе", "па", "пе", "ма", "ме", "бен", "пен", "мен", "лы", "лi",
            "ғы", "гi", "қы", "кi", "дай", "дей", "тай", "тей", "дық", "дiк", "тық", "тiк", "лық", "лiк", "паз",
            "ғыш", "гiш", "қыш", "кiш", "шек", "шақ", "шыл", "шiл", "ншi", "ншы", "ау", "еу", "дап", "деп",
            "тап", "теп", "лап", "леп", "даған", "деген", "таған", "теген", "лаған", "леген", "ла", "ле", "даc",
            "деc", "таc", "теc", "лаc", "леc", "ар", "ер", "ғар", "гер", "қар", "кер", "дыр", "дiр", "тыр",
            "тiр", "ғыз", "гiз", "қыз", "кiз", "ған", "ген", "қан", "кен", "атын", "етiн",
            "йтын", "йтiн", "ушы", "ушi", "р", "п", "ып", "iп", "й", "ғалы", "гелi", "қалы", "келi", "ша", "ше",
            "лай", "лей", "дайын", "дейін", "тайын", "тейін", "шама", "шеме", "шалық", "шелік", "сын", "сiн", "са",
            "се", "бақ", "бек", "пақ", "пек", "мақ", "мек", "йын", "йiн", "йық", "йiк"
    };
}