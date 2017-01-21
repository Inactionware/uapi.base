/*
 * Copyright (C) 2017. The UAPI Authors
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at the LICENSE file.
 *
 * You must gained the permission from the authors if you want to
 * use the project into a commercial product
 */

package uapi.common;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WordHelper {

    private static List<RuleAndReplacement> plurals = new ArrayList<>();

    private static List<RuleAndReplacement> singulars = new ArrayList<>();

    private static List<String> uncountables = new ArrayList<>();

    static {
        initialize();
    }

    private WordHelper() { }

    private static void initialize() {
        // singular to plural
        plural("$", "s");
        plural("s$", "s");
        plural("(ax|test)is$", "$1es");
        plural("(octop|vir)us$", "$1i");
        plural("(alias|status)$", "$1es");
        plural("(bu)s$", "$1es");
        plural("(buffal|tomat)o$", "$1oes");
        plural("([ti])um$", "$1a");
        plural("sis$", "ses");
        plural("(?:([^f])fe|([lr])f)$", "$1$2ves");
        plural("(hive)$", "$1s");
        plural("([^aeiouy]|qu)y$", "$1ies");
        plural("([^aeiouy]|qu)ies$", "$1y");
        plural("(x|ch|ss|sh)$", "$1es");
        plural("(matr|vert|ind)ix|ex$", "$1ices");
        plural("([m|l])ouse$", "$1ice");
        plural("^(ox)$", "$1en");
        plural("(quiz)$", "$1zes");
        // plural to singular
        singular("s$", "");
        singular("(n)ews$", "$1ews");
        singular("([ti])a$", "$1um");
        singular("((a)naly|(b)a|(d)iagno|(p)arenthe|(p)rogno|(s)ynop|(t)he)ses$", "$1$2sis");
        singular("(^analy)ses$", "$1sis");
        singular("([^f])ves$", "$1fe");
        singular("(hive)s$", "$1");
        singular("(tive)s$", "$1");
        singular("([lr])ves$", "$1f");
        singular("([^aeiouy]|qu)ies$", "$1y");
        singular("(s)eries$", "$1eries");
        singular("(m)ovies$", "$1ovie");
        singular("(x|ch|ss|sh)es$", "$1");
        singular("([m|l])ice$", "$1ouse");
        singular("(bus)es$", "$1");
        singular("(o)es$", "$1");
        singular("(shoe)s$", "$1");
        singular("(cris|ax|test)es$", "$1is");
        singular("([octop|vir])i$", "$1us");
        singular("(alias|status)es$", "$1");
        singular("^(ox)en", "$1");
        singular("(vert|ind)ices$", "$1ex");
        singular("(matr)ices$", "$1ix");
        singular("(quiz)zes$", "$1");
        // irregular
        irregular("person", "people");
        irregular("man", "men");
        irregular("child", "children");
        irregular("sex", "sexes");
        irregular("move", "moves");
        // no singular or plural
        uncountable("equipment", "information", "rice", "money", "species", "series", "fish", "sheep");
    }

    /**
     * singular to plural
     *
     * @param   word
     *          Singular word
     * @return  Plural word
     */
    public static String pluralize(String word) {
        if (uncountables.contains(word.toLowerCase())) {
            return word;
        }
        return replaceWithFirstRule(word, plurals);
    }

    /**
     * plural to singular
     *
     * @param   word
     *          Plural word
     * @return  Singular word
     */
    public static String singularize(String word) {
        if (uncountables.contains(word.toLowerCase())) {
            return word;
        }
        return replaceWithFirstRule(word, singulars);
    }

    /**
     * replace with first rule
     *
     * @param   word
     *          The word which will be replaced
     * @param   ruleAndReplacements
     *          The rule pattern list
     * @return  Result word
     */
    private static String replaceWithFirstRule(String word, List<RuleAndReplacement> ruleAndReplacements) {
        for (RuleAndReplacement rar : ruleAndReplacements) {
            String rule = rar.getRule();
            String replacement = rar.getReplacement();

            // Return if we find a match.
            Matcher matcher = Pattern.compile(rule, Pattern.CASE_INSENSITIVE).matcher(word);
            if (matcher.find()) {
                return matcher.replaceAll(replacement);
            }
        }
        return word;
    }

    /**
     * Add plural rule
     *
     * @param   rule
     *          The rule
     * @param   replacement
     *          The replacement string
     */
    private static void plural(String rule, String replacement) {
        plurals.add(0, new RuleAndReplacement(rule, replacement));
    }

    /**
     * Add singular rule
     *
     * @param   rule
     *          The rule
     * @param   replacement
     *          The replacement string
     */
    private static void singular(String rule, String replacement) {
        singulars.add(0, new RuleAndReplacement(rule, replacement));
    }

    /**
     * Add irregular rule
     *
     * @param   singular
     *          The singular word
     * @param   plural
     *          The plural word
     */
    private static void irregular(String singular, String plural) {
        plural(singular, plural);
        singular(plural, singular);
    }

    /**
     * no singular or plural
     *
     * @param   words
     *          Words will be added
     */
    private static void uncountable(String... words) {
        uncountables.addAll(Arrays.asList(words));
    }

    /**
     * rule replacement
     */
    private static class RuleAndReplacement {
        private String rule;
        private String replacement;

        private RuleAndReplacement(String rule, String replacement) {
            this.rule = rule;
            this.replacement = replacement;
        }

        private String getReplacement() {
            return replacement;
        }

        private void setReplacement(String replacement) {
            this.replacement = replacement;
        }

        private String getRule() {
            return rule;
        }

        private void setRule(String rule) {
            this.rule = rule;
        }
    }
}
