package Model;

        import java.io.IOException;
        import java.util.*;
        import java.util.regex.Matcher;
        import java.util.regex.Pattern;

/**
 * This class get one doc each iteration, split to separated words and doing parser on each word.
 */
public class Parse {
    private Pattern pattern, patternShever;
    private Matcher m, mShever;
    private HashMap<String, String> dates;
    private HashMap<String, Term> terms;
    private HashSet<Character> delimiters;
    private HashSet<String> billionMillion;
    private HashMap<String, String> units;
    private Map<String, String> replacements;
    private HashMap<String,Integer> termsPerDoc;
    private Stemmer stemmer;
    private int termsInOneDoc = 0;
    private int docLength = 0;
    private int testLength = 0;
    private int place = 0;
    private int maxTf = 1;

    public void setPlace(int place) {
        this.place = place;
    }

    public HashMap<String, Integer> getTermsPerDoc() {
        return termsPerDoc;
    }

    public HashMap<String, Term> getTerms() {
        return terms;
    }

    public Parse() {
        dates = new HashMap<>();
        terms = new HashMap<>();
        delimiters = new HashSet<>();
        billionMillion = new HashSet<>();
        units = new HashMap<>();
        termsPerDoc=new HashMap<>();
        replacements = new HashMap<>();
        stemmer = new Stemmer();
        String letter = "([a-z]+)" + "|" + "([A-Z]+)" + "|" + "([A-Z][a-z]+)";
        String shever = "(\\d+)" + "/" + "(\\d+)";
        pattern = Pattern.compile(letter);
        patternShever = Pattern.compile(shever);

        fillDates();
        fillUnits();
        fillDelimiters();
        fillReplacements();
        fillMB();
    }
    //Fill in the billionMillion dictionary
    private void fillMB() {
        billionMillion.add("Trillion");
        billionMillion.add("Billion");
        billionMillion.add("Thousand");
        billionMillion.add("Million");
    }
    //Fill in the replacments dictionary
    private void fillReplacements() {
        replacements.put(",", "");
        replacements.put("th", "");
        replacements.put("$", "");
        replacements.put("%", "")
        ;
        replacements.put(":", "");
    }
    //Fill in the delimiters dictionary
    private void fillDelimiters() {
        delimiters.add('.');
        delimiters.add(',');
        delimiters.add(':');
        delimiters.add('!');
        delimiters.add('\"');
        delimiters.add('(');
        delimiters.add(')');
        delimiters.add('[');
        delimiters.add(']');
        delimiters.add('|');
        delimiters.add(';');
        delimiters.add('?');
        delimiters.add('\'');
        delimiters.add('*');
        delimiters.add('-');
        delimiters.add('/');
        delimiters.add(' ');
        delimiters.add('\n');
    }
    //Fill in the units of measure dictionary
    private void fillUnits() {
        units.put("Milligram", "mg");
        units.put("Kilogram", "kg");
        units.put("Gram", "g");
        units.put("milligram", "mg");
        units.put("kilogram", "kg");
        units.put("gram", "g");
        units.put("Decimeter", "dc");
        units.put("Centimeter", "cm");
        units.put("Millimeter", "mm");
        units.put("Meter", "m");
        units.put("Kilometer", "km");
        units.put("decimeter", "dc");
        units.put("centimeter", "cm");
        units.put("millimeter", "mm");
        units.put("meter", "m");
        units.put("kilometer", "km");
        units.put("Inch", "in");
        units.put("Foot", "ft");
        units.put("Yard", "yd");
        units.put("Mile", "mile");
        units.put("inch", "in");
        units.put("foot", "ft");
        units.put("mile", "mile");
        units.put("yard", "yd");
        units.put("yards", "yd");
        units.put("Milligrams", "mg");
        units.put("Kilograms", "kg");
        units.put("Grams", "g");
        units.put("milligrams", "mg");
        units.put("kilograms", "kg");
        units.put("grams", "g");
        units.put("Decimeters", "dc");
        units.put("Centimeters", "cm");
        units.put("Millimeters", "mm");
        units.put("Meters", "m");
        units.put("Kilometers", "km");
        units.put("decimeters", "dc");
        units.put("centimeters", "cm");
        units.put("millimeters", "mm");
        units.put("meters", "m");
        units.put("kilometers", "km");
        units.put("Inchs", "in");
        units.put("Foots", "ft");
        units.put("Yards", "yd");
        units.put("Miles", "mile");
        units.put("inchs", "in");
        units.put("foots", "ft");
        units.put("miles", "mile");
    }
    //Fill in the date dictionary
    private void fillDates() {
        this.dates.put("January", "01");
        this.dates.put("February", "02");
        this.dates.put("March", "03");
        this.dates.put("April", "04");
        this.dates.put("May", "05");
        this.dates.put("June", "06");
        this.dates.put("July", "07");
        this.dates.put("August", "08");
        this.dates.put("September", "09");
        this.dates.put("October", "10");
        this.dates.put("November", "11");
        this.dates.put("December", "12");
        this.dates.put("JANUARY", "01");
        this.dates.put("FEBRUARY", "02");
        this.dates.put("MARCH", "03");
        this.dates.put("APRIL", "04");
        this.dates.put("MAY", "05");
        this.dates.put("JUNE", "06");
        this.dates.put("JULY", "07");
        this.dates.put("AUGUST", "08");
        this.dates.put("SEPTEMBER", "09");
        this.dates.put("OCTOBER", "10");
        this.dates.put("NOVEMBER", "11");
        this.dates.put("DECEMBER", "12");
        this.dates.put("Jan", "01");
        this.dates.put("Feb", "02");
        this.dates.put("Mar", "03");
        this.dates.put("Apr", "04");
        this.dates.put("Jun", "06");
        this.dates.put("Jul", "07");
        this.dates.put("Aug", "08");
        this.dates.put("Sep", "09");
        this.dates.put("Oct", "10");
        this.dates.put("Nov", "11");
        this.dates.put("Dec", "12");
        this.dates.put("JAN", "01");
        this.dates.put("FEB", "02");
        this.dates.put("MAR", "03");
        this.dates.put("APR", "04");
        this.dates.put("JUN", "06");
        this.dates.put("JUL", "07");
        this.dates.put("AUG", "08");
        this.dates.put("SEP", "09");
        this.dates.put("OCT", "10");
        this.dates.put("NOV", "11");
        this.dates.put("DEC", "12");
        this.dates.put("january", "01");
        this.dates.put("february", "02");
        this.dates.put("march", "03");
        this.dates.put("april", "04");
        this.dates.put("may", "05");
        this.dates.put("june", "06");
        this.dates.put("july", "07");
        this.dates.put("august", "08");
        this.dates.put("september", "09");
        this.dates.put("october", "10");
        this.dates.put("november", "11");
        this.dates.put("december", "12");
        this.dates.put("jan", "01");
        this.dates.put("feb", "02");
        this.dates.put("mar", "03");
        this.dates.put("apr", "04");
        this.dates.put("jun", "06");
        this.dates.put("jul", "07");
        this.dates.put("aug", "08");
        this.dates.put("sep", "09");
        this.dates.put("oct", "10");
        this.dates.put("nov", "11");
        this.dates.put("dec", "12");
    }
    //Go through the document, perform a parsing of all the words in the document by checking on each
    // word to whom it corresponds and sending to a function that will handle each word accordingly
    //At the end, fields in the document will be updated (max_tf, number of different terms and doc_length
    public void parsing(Document document, String text, HashSet<String> sw, boolean stem) {
        if (text.equals(""))
            return;
        String doc = document.getDocName();
        testLength = text.length();
        termsInOneDoc = 0;
        docLength = 0;
        int place=0;
        String[] tokens = text.split("\\s");
        for (int j = 0; j < tokens.length; j++) {
            while (j < tokens.length && tokens[j].equals("")) {
                j = j + 1;
                if (j == tokens.length)
                    return;
            }
            if (tokens[j].equals(""))
                continue;
            tokens[j] = cutOffDelimiters(tokens[j]);
            if (tokens[j].equals(""))
                continue;

            int counter=1;
            String toSplit="";
            try{
                if(tokens[j].contains(".")){
                    toSplit=toSplit+".";
                    int index=tokens[j].indexOf(".");
                    while (index+1<tokens[j].length() && delimiters.contains(tokens[j].charAt(index+1))) {
                        counter++;
                        toSplit = toSplit + tokens[j].charAt(index + 1);
                        index++;
                    }
                }
                if(counter>1) {
                    String[] array = tokens[j].replace(toSplit, " ").split(" ");
                    tokens[j] = array[0];
                }
            }
            catch (Exception e){
                continue;
            }

            if (sw.contains(tokens[j].toLowerCase())) {
                if (((tokens[j].equals("Between") || tokens[j].equals("between")) && j + 2 < tokens.length && tokens[j + 2].equals("and")) || (tokens[j].equals("and") && j - 2 > 0
                        && (tokens[j - 2].equals("Between") || tokens[j - 2].equals("Between")))) {
                } else if (dates.containsKey(tokens[j]) && j + 1 < tokens.length && isNumber(tokens[j + 1])) {

                } else {
                    continue;
                }
            }

            if (tokens[j].contains("(") && tokens[j].contains(")") || tokens[j].contains("#") || tokens[j].contains(".\"") || tokens[j].contains("=")) {
                continue;
            }

            if (tokens[j].equals("$") || tokens[j].equals("%") || tokens[j].equals(":") || tokens[j].equals("-"))
                continue;

            Boolean checkNumber = isNumber(replaceFromMap(tokens[j], replacements));
            Boolean checkLetter = tokens[j].matches("([a-z]+)" + "|" + "([A-Z]+)" + "|" + "([A-Z][a-z]+)");

            if (checkNumber && j + 1 < tokens.length) {
                mShever = patternShever.matcher(tokens[j + 1]);
                if (mShever.find() && Double.valueOf(replaceFromMap(tokens[j], replacements)) < 1000 && !tokens[j].contains(".")) {
                    tokens[j + 1] = tokens[j] + " " + tokens[j + 1];
                    j = j + 1;
                    if (j + 1 == tokens.length || (j + 1 < tokens.length && !(tokens[j + 1].equals("")) && !cutOffDelimiters(tokens[j + 1]).equals("Dollars"))) {
                        if (terms.containsKey(tokens[j])) {
                            Term t = terms.get(tokens[j]);
                            if (!t.getDocs().containsKey(doc))
                                termsInOneDoc++;
                            t.setTerm(doc, "" + place);
                            terms.put(t.term, t);
                            docLength++;
                            int temp = t.getDocs().get(doc).getKey();
                            if (temp > maxTf) {
                                maxTf = temp;
                            }
                        } else {
                            Term t = new Term(tokens[j], doc, 1, "" + place);
                            terms.put(tokens[j], t);
                            termsInOneDoc++;
                            docLength++;
                        }
                        place++;
                        continue;
                    } else if ((j + 1 == tokens.length || (j + 1 < tokens.length && !(tokens[j + 1].equals("")) && cutOffDelimiters(tokens[j + 1]).equals("Dollars")))) {
                        if (terms.containsKey(tokens[j] + " Dollars")) {
                            Term t = terms.get(tokens[j] + " Dollars");
                            if (!t.getDocs().containsKey(doc))
                                termsInOneDoc++;
                            t.setTerm(doc, "" + place);
                            terms.put(t.term, t);
                            docLength++;
                            int temp = t.getDocs().get(doc).getKey();
                            if (temp > maxTf)
                                maxTf = temp;

                        } else {
                            Term t = new Term(tokens[j] + " Dollars", doc, 1, "" + place);
                            terms.put(t.term, t);
                            docLength++;
                            termsInOneDoc++;

                        }
                        j++;
                        place++;
                        continue;
                    }
                }
            }
            if (checkNumber && !checkLetter) {
                if (j + 3 < tokens.length && !(tokens[j + 3].equals("")) && cutOffDelimiters(tokens[j + 3]).equals("dollars")
                        && !(tokens[j + 2].equals("")) && tokens[j + 2].equals("U.S.") && !(tokens[j + 1].equals(""))
                        && (tokens[j + 1].equals("billion") || tokens[j + 1].equals("million") || tokens[j + 1].equals("trillion"))) {
                    dollars(tokens[j] + " " + tokens[j + 1] + " " + tokens[j + 2] + " " + cutOffDelimiters(tokens[j + 3]), doc, "" + place);
                    place++;
                    j = j + 3;
                    continue;

                } else if (j + 2 < tokens.length && !(tokens[j + 2].equals("")) && cutOffDelimiters(tokens[j + 2]).equals("Dollars")
                        && !(tokens[j + 1].equals("")) && (tokens[j + 1].equals("bn") || tokens[j + 1].equals("m"))) {
                    dollars(tokens[j] + " " + tokens[j + 1] + " " + cutOffDelimiters(tokens[j + 2]), doc, "" + place);
                    place++;
                    j = j + 2;
                    continue;

                } else if (j + 1 < tokens.length && !(tokens[j + 1].equals("")) && tokens[j].charAt(0) == '$' && (cutOffDelimiters(tokens[j + 1]).equals("trillion") || cutOffDelimiters(tokens[j + 1]).equals("billion") || cutOffDelimiters(tokens[j + 1]).equals("million"))) {
                    if (tokens[j].length() > 1)
                        dollars(tokens[j] + " " + cutOffDelimiters(tokens[j + 1]), doc, "" + place);
                    place++;
                    j = j + 1;
                    continue;

                } else if (j + 1 < tokens.length && !(tokens[j + 1].equals("")) && cutOffDelimiters(tokens[j + 1]).equals("Dollars") && tokens[j].charAt(0) != '$' && !tokens[j].contains("-")) {
                    tokens[j + 1] = cutOffDelimiters(tokens[j + 1]);
                    if (!tokens[j].contains("%")) {
                        dollars(tokens[j] + " " + tokens[j + 1], doc, "" + place);
                        place++;
                    } else {
                        if (terms.containsKey(tokens[j] + " " + tokens[j + 1])) {
                            Term t = terms.get(tokens[j] + " " + tokens[j + 1]);
                            if (!t.getDocs().containsKey(doc))
                                termsInOneDoc++;
                            t.setTerm(doc, "" + place);
                            terms.put(t.term, t);
                            docLength++;
                            int temp = t.getDocs().get(doc).getKey();
                            if (temp > maxTf)
                                maxTf = temp;

                        } else {
                            Term t = new Term(tokens[j] + " " + cutOffDelimiters(tokens[j + 1]), doc, 1, "" + place);
                            terms.put(t.term, t);
                            docLength++;
                            termsInOneDoc++;

                        }
                    }
                    place++;
                    j++;
                    continue;

                } else if (tokens[j].charAt(0) == '$') {
                    tokens[j] = cutOffDelimiters(tokens[j]);
                    if (tokens[j].length() > 1 && tokens[j].charAt(tokens[j].length() - 1) != '$' && !tokens[j].contains("-") && !tokens[j].contains("%") && !tokens[j].contains(":")) {
                        if (tokens[j].charAt(1) == '$')//in case of $$17
                            dollars(tokens[j].substring(1), doc, "" + place);
                        else
                            dollars(tokens[j], doc, "" + place);
                        place++;
                        continue;
                    }

                }
                if (tokens[j].contains("%")) {
                    percent(cutOffDelimiters(tokens[j]), doc, "" + place);
                    place++;
                    continue;

                } else if (j + 1 < tokens.length && !tokens[j + 1].equals("") && (cutOffDelimiters(tokens[j + 1]).equals("percentage") || cutOffDelimiters(tokens[j + 1]).equals("percent"))) {
                    percent(tokens[j] + " " + cutOffDelimiters(tokens[j + 1]), doc, "" + place);
                    place++;
                    j = j + 1;
                    continue;
                } else if (j + 1 < tokens.length && !(tokens[j + 1].equals("")) && !tokens[j].contains(".")) {
                    String temp = cutOffDelimiters(tokens[j + 1]);
                    String word = "";
                    if (dates.containsKey(temp)) {
                        word = tokens[j] + " " + temp;
                        convertDates(word, doc, "" + place); //DATES
                        place++;
                        j++;
                        continue;
                    }
                }

                //dates or units
                else if (j + 1 < tokens.length && !(tokens[j + 1].equals("")) && units.containsKey(tokens[j + 1])) {
                    String word = tokens[j] + " " + tokens[j + 1];
                    unitsOfMeasure(word, doc, "" + place); //UNITS OF MEASURE
                    place++;
                    j++;
                    continue;
                } else { //Hours
                    if (tokens[j].contains(":")) {
                        if (j + 1 < tokens.length && !(tokens[j + 1].equals("")) && (tokens[j + 1].contains("p.m")
                                || tokens[j + 1].contains("a.m") || tokens[j + 1].contains("AM") || tokens[j + 1].contains("PM"))) {
                            hours(tokens[j] + " " + tokens[j + 1], doc, "" + place);
                            place++;
                            j++;
                            continue;
                        } else if (tokens[j].contains(":")) {
                            int index = tokens[j].indexOf(":");
                            String first = tokens[j].substring(0, index);
                            String second = tokens[j].substring(index + 1);
                            if (!first.contains(".") && !second.contains(".") && !second.contains(":")) {
                                if (Integer.valueOf(first) < 24 && Integer.valueOf(second) < 60)
                                    hours((tokens[j]), doc, "" + place);
                                place++;
                            }
                            continue;
                        }
                    }
                }

                if (j + 1 < tokens.length && billionMillion.contains(cutOffDelimiters(tokens[j + 1]))) {
                    numbers(tokens[j] + " " + tokens[j + 1], doc, "" + place);
                    j++;
                } else
                    numbers(tokens[j], doc, "" + place);
                place++;
                continue;

            } else if (j + 1 < tokens.length && !(tokens[j + 1].equals("")) && dates.containsKey(tokens[j])
                    && !tokens[j + 1].contains("-")) { //MM-DD or MM-YYYY
                tokens[j + 1] = cutOffDelimiters(tokens[j + 1]);
                if (isNumber(tokens[j + 1]) && !tokens[j + 1].contains(".")) {
                    String word = tokens[j] + " " + tokens[j + 1];
                    convertDates(word, doc, "" + place); //DATES
                    j++;
                    place++;
                    continue;
                }
            }
            //between and
            if (j + 3 < tokens.length && (tokens[j].equals("Between") || tokens[j].equals("between")) && tokens[j + 2].equals("and")) {
                String word = tokens[j].toLowerCase() + " " + tokens[j + 1] + " " + tokens[j + 2].toLowerCase() + " " + tokens[j + 3];
                convertExpressionOrRanges(word, doc, sw, "" + place, stem,termsPerDoc);
                j = j + 3;
                place++;
                continue;
            }
            if(tokens[j].contains("--"))
                tokens[j]=tokens[j].replace("--","-");

            if (tokens[j].contains("-") && (!tokens[j].contains("[") && !tokens[j].contains("]") && !tokens[j].contains("(") && !tokens[j].contains(")") && !tokens[j].contains("{") && !tokens[j].contains("}"))) {
                if (tokens[j].contains(":")) {
                    tokens[j] = tokens[j].substring(0, tokens[j].indexOf(':'));
                }
                String[]toadd=tokens[j].split("-");
                convertExpressionOrRanges(tokens[j], doc, sw, "" + place, stem,termsPerDoc); //EXPRESSION
                place=place+toadd.length+1;
                continue;
            }
            if (!checkNumber && checkLetter) {
                letters(cutOffDelimiters(tokens[j]), doc, sw, "" + place, stem,termsPerDoc);
                place++;
                continue;
            }
            if (terms.containsKey(tokens[j])) {
                Term t = terms.get(tokens[j]);
                if (!t.getDocs().containsKey(doc))
                    termsInOneDoc++;
                t.setTerm(doc, "" + place);
                place++;
                terms.put(t.term, t);
                docLength++;
                int temp = t.getDocs().get(doc).getKey();
                if (temp >= maxTf)
                    maxTf = temp;

            } else {
                Term t = new Term(tokens[j], doc, 1, "" + place);
                place++;
                terms.put(tokens[j], t);
                docLength++;
                termsInOneDoc++;

            }
        }
        document.setNumOfDifferentTerms(termsInOneDoc);
        document.setMax_tf(maxTf);
        document.setDocumentLength(docLength);
    }
    //Handling the law of hours
    public void hours(String text, String docName, String j) {
        try {
            String[] array = text.split(" ");
            String hourTOsave = "";
            if (array.length == 2) {
                array[1] = cutOffDelimiters(array[1]);
                if (array[1].equals("AM"))
                    array[1] = "a.m";
                else if (array[1].equals("PM"))
                    array[1] = "p.m";
                hourTOsave = array[0] + " " + array[1];
            } else {
                String[] hour = array[0].split(":");
                if (Integer.valueOf(hour[0]) < 12) {
                    hourTOsave = array[0] + " " + "a.m";
                } else
                    hourTOsave = array[0] + " " + "p.m";
            }
            if (terms.containsKey(hourTOsave)) {
                Term t = terms.get(hourTOsave);
                if (!t.getDocs().containsKey(docName))
                    termsInOneDoc++;
                t.setTerm(docName, j);
                terms.put(t.term, t);
                docLength++;
                int temp = t.getDocs().get(docName).getKey();
                if (temp >= maxTf)
                    maxTf = temp;
            } else {
                Term t = new Term(hourTOsave, docName, 1, j);
                terms.put(t.term, t);
                docLength++;
                termsInOneDoc++;

            }
        } catch (Exception e) {
            return;
        }
    }
    //Handling the law of dates as required
    public void convertDates(String date, String docName, String j) {
        try {
            String[] temp = date.split(" ");
            if (temp.length < 2)
                return;
            temp[0] = replaceFromMap(temp[0], replacements);
            temp[1] = replaceFromMap(temp[1], replacements);
            if (delimiters.contains(temp[1].charAt(temp[1].length() - 1)))
                temp[1] = temp[1].substring(0, temp[1].length() - 1);
            if (dates.containsKey(temp[0])) { //begin with month
                if (Float.valueOf(temp[1]) > 1000 && Float.valueOf(temp[1]) < 9999) { //year
                    if (terms.containsKey(temp[1] + "-" + dates.get(temp[0]))) {
                        Term t = terms.get(temp[1] + "-" + dates.get(temp[0]));
                        if (!t.getDocs().containsKey(docName))
                            termsInOneDoc++;
                        t.setTerm(docName, j);
                        terms.put(t.term, t);
                        docLength++;
                        int tempo = t.getDocs().get(docName).getKey();
                        if (tempo > maxTf)
                            maxTf = tempo;

                    } else {
                        Term t = new Term(temp[1] + "-" + dates.get(temp[0]), docName, 1, j);
                        terms.put(t.term, t);
                        docLength++;
                        termsInOneDoc++;

                    }
                } else { //month
                    if (Float.valueOf(temp[1]) < 10 && Float.valueOf(temp[1]) > 0) {
                        if (terms.containsKey(dates.get(temp[0]) + "-" + "0" + temp[1])) {
                            Term t = terms.get(dates.get(temp[0]) + "-" + "0" + temp[1]);
                            if (!t.getDocs().containsKey(docName))
                                termsInOneDoc++;
                            t.setTerm(docName, j);
                            terms.put(t.term, t);
                            docLength++;
                            int tempo = t.getDocs().get(docName).getKey();
                            if (tempo > maxTf)
                                maxTf = tempo;
                        } else {
                            Term t = new Term(dates.get(temp[0]) + "-" + "0" + temp[1], docName, 1, j);
                            terms.put(t.term, t);
                            docLength++;
                            termsInOneDoc++;
                        }
                    } else {
                        if (terms.containsKey(dates.get(temp[0]) + "-" + temp[1])) {
                            Term t = terms.get(dates.get(temp[0]) + "-" + temp[1]);
                            if (!t.getDocs().containsKey(docName))
                                termsInOneDoc++;
                            t.setTerm(docName, j);
                            terms.put(t.term, t);
                            docLength++;
                            int tempo = t.getDocs().get(docName).getKey();
                            if (tempo > maxTf)
                                maxTf = tempo;
                        } else {
                            Term t = new Term(dates.get(temp[0]) + "-" + temp[1], docName, 1, j);
                            terms.put(t.term, t);
                            docLength++;
                            termsInOneDoc++;
                        }
                    }
                }
            } else { //begin with number
                if (Float.valueOf(temp[0]) > 1000 && Float.valueOf(temp[0]) < 9999) {//year
                    if (terms.containsKey(temp[0] + "-" + dates.get(temp[1]))) {
                        Term t = terms.get(temp[0] + "-" + dates.get(temp[1]));
                        if (!t.getDocs().containsKey(docName))
                            termsInOneDoc++;
                        t.setTerm(docName, j);
                        terms.put(t.term, t);
                        docLength++;
                        int tempo = t.getDocs().get(docName).getKey();
                        if (tempo > maxTf)
                            maxTf = tempo;
                    } else {
                        Term t = new Term(temp[0] + "-" + dates.get(temp[1]), docName, 1, j);
                        terms.put(t.term, t);
                        docLength++;
                        termsInOneDoc++;
                    }
                } else { //month
                    if (Float.valueOf(temp[0]) > 0 && Float.valueOf(temp[0]) < 10) {
                        if (terms.containsKey(dates.get(temp[1]) + "-" + "0" + temp[0])) {
                            Term t = terms.get(dates.get(temp[1]) + "-" + "0" + temp[0]);
                            if (!t.getDocs().containsKey(docName))
                                termsInOneDoc++;
                            t.setTerm(docName, j);
                            terms.put(t.term, t);
                            docLength++;
                            int tempo = t.getDocs().get(docName).getKey();
                            if (tempo > maxTf)
                                maxTf = tempo;
                        } else {
                            Term t = new Term(dates.get(temp[1]) + "-" + "0" + temp[0], docName, 1, j);
                            terms.put(t.term, t);
                            docLength++;
                            termsInOneDoc++;
                        }
                    } else {
                        if (terms.containsKey(dates.get(temp[1]) + "-" + temp[0])) {
                            Term t = terms.get(dates.get(temp[1]) + "-" + temp[0]);
                            if (!t.getDocs().containsKey(docName))
                                termsInOneDoc++;
                            t.setTerm(docName, j);
                            terms.put(t.term, t);
                            docLength++;
                            int tempo = t.getDocs().get(docName).getKey();
                            if (tempo > maxTf)
                                maxTf = tempo;
                        } else {
                            Term t = new Term(dates.get(temp[1]) + "-" + temp[0], docName, 1, j);
                            terms.put(t.term, t);
                            docLength++;
                            termsInOneDoc++;
                        }
                    }
                }
            }
        } catch (Exception e) {
            return;
        }
    }
    /**
     * This function check if term is a number
     *
     * @param text
     * @return true if number, false otherwise
     */
    public boolean isNumber(String text) {
        boolean thereIsPoint = false;
        text = replaceFromMap(text, replacements); //its ok with ,
        if (text.equals("") || (!text.equals("") && text.charAt(0) != '-' && !Character.isDigit(text.charAt(0))))
            return false;
        int point = text.indexOf('.');
        for (int i = 1; i < text.length(); i++) {
            if (!Character.isDigit(text.charAt(i)) && text.charAt(i) != '.') {
                return false;
            }
            if (point != i && text.charAt(i) == '.')
                return false;
        }
        return true;
    }
    //Handling the law of letters(big letters in a word, small letters in a words, integrated letters in a word) as required
    public void letters(String word, String docName, HashSet<String> sw, String j, boolean stem, HashMap<String,Integer> termsPerDoc) {
        if (!word.equals("") && delimiters.contains(word.charAt(0)))
            word = word.substring(1);
        if (!word.equals("") && delimiters.contains(word.charAt(word.length() - 1)))
            word = word.substring(0, word.length() - 1);
        if (sw.contains(word.toLowerCase()))
            return;
        if (stem) {
            stemmer.getStemmer().setCurrent(word);
            if (stemmer.getStemmer().stem())
                word = stemmer.getStemmer().getCurrent();
        }
        String littleLetters = "^([a-z]+$)";
        pattern = Pattern.compile(littleLetters);
        m = pattern.matcher(word);
        if (word.matches("^([a-z]+$)")) {
            if (terms.containsKey(word.toLowerCase())) {
                Term t = terms.get(word.toLowerCase());
                if (!t.getDocs().containsKey(docName))
                    termsInOneDoc++;
                t.setTerm(docName, j);
                terms.put(word.toLowerCase(), t);
                docLength++;
                int tempo = t.getDocs().get(docName).getKey();
                if (tempo > maxTf)
                    maxTf = tempo;
            } else if (terms.containsKey(word.toUpperCase())) {
                Term t = terms.get(word.toUpperCase());
                terms.remove(word.toUpperCase());
                if (!t.getDocs().containsKey(docName))
                    termsInOneDoc++;
                t.setTerm(docName, j);
                terms.put(word.toLowerCase(), t);
                docLength++;
                int tempo = t.getDocs().get(docName).getKey();
                if (tempo > maxTf)
                    maxTf = tempo;
            } else if (!terms.containsKey(word.toUpperCase()) && !terms.containsKey(word.toLowerCase())) {
                Term t = new Term(word.toLowerCase(), docName, 1, j);
                terms.put(word.toLowerCase(), t);
                docLength++;
                termsInOneDoc++;
            }
            if(termsPerDoc.containsKey(word.toUpperCase()))
                termsPerDoc.remove(word.toUpperCase());
            return;
        }
        //convert to all big letters
        String capitalLetter = "(([A-Z])([a-z]+))";
        pattern = Pattern.compile(capitalLetter);
        m = pattern.matcher(word);
        if (word.matches("(([A-Z])([a-z]+))")) {
            if (!terms.containsKey(word.toUpperCase()) && !terms.containsKey(word.toLowerCase())) {
                Term t = new Term(word.toUpperCase(), docName, 1, j);
                terms.put(word.toUpperCase(), t);
                docLength++;
                termsInOneDoc++;
                //termsPerDoc.put(word.toUpperCase(),1);
            } else if (terms.containsKey(word.toLowerCase())) {
                Term t = terms.get(word.toLowerCase());
                if (!t.getDocs().containsKey(docName))
                    termsInOneDoc++;
                t.setTerm(docName, j + "");
                terms.put(word.toLowerCase(), t);
                docLength++;
                int tempo = t.getDocs().get(docName).getKey();
                if (tempo > maxTf)
                    maxTf = tempo;
//                if(termsPerDoc.containsKey(word.toUpperCase()))
//                    termsPerDoc.remove(word.toUpperCase());
            } else if (terms.containsKey(word.toUpperCase())) {
                Term t = terms.get(word.toUpperCase());
                if (!t.getDocs().containsKey(docName))
                    termsInOneDoc++;
                t.setTerm(docName, j);
                terms.put(word.toUpperCase(), t);
                docLength++;
                int tempo = t.getDocs().get(docName).getKey();
                if (tempo > maxTf)
                    maxTf = tempo;
//                if(termsPerDoc.containsKey(word.toUpperCase()))
//                    termsPerDoc.put(word.toUpperCase(),termsPerDoc.get(word.toUpperCase())+1);
//                else
//                    termsPerDoc.put(word.toUpperCase(),1);
            }
            if(termsPerDoc.containsKey(word.toUpperCase()))
                termsPerDoc.put(word.toUpperCase(),termsPerDoc.get(word.toUpperCase())+1);
            else
                termsPerDoc.put(word.toUpperCase(),1);
            return;
        }
        //all big letter
        String bigLetters = "(([A-Z])([A-Z]+))";
        pattern = Pattern.compile(bigLetters);
        m = pattern.matcher(word);
        if (word.matches("(([A-Z])([A-Z]+))")) {
            if (!terms.containsKey(word.toLowerCase()) && !terms.containsKey(word.toUpperCase())) {
                Term t = new Term(word, docName, 1, j);
                terms.put(word, t);
                docLength++;
                termsInOneDoc++;
                //termsPerDoc.put(word.toUpperCase(),1);
            } else if (terms.containsKey(word.toLowerCase())) {
                Term t = terms.get(word.toLowerCase());
                if (!t.getDocs().containsKey(docName))
                    termsInOneDoc++;
                t.setTerm(docName, j);
                terms.put(word.toLowerCase(), t);
                docLength++;
                int tempo = t.getDocs().get(docName).getKey();
                if (tempo >= maxTf)
                    maxTf = tempo;
//                if(termsPerDoc.containsKey(word.toUpperCase()))
//                    termsPerDoc.remove(word.toUpperCase());
            } else if (terms.containsKey(word.toUpperCase())) {
                Term t = terms.get(word.toUpperCase());
                if (!t.getDocs().containsKey(docName))
                    termsInOneDoc++;
                t.setTerm(docName, j);
                terms.put(word.toUpperCase(), t);
                docLength++;
                int tempo = t.getDocs().get(docName).getKey();
                if (tempo >= maxTf)
                    maxTf = tempo;
//                if(termsPerDoc.containsKey(word.toUpperCase()))
//                    termsPerDoc.put(word.toUpperCase(),termsPerDoc.get(word.toUpperCase())+1);
//                else
//                    termsPerDoc.put(word.toUpperCase(),1);
            }
            if(termsPerDoc.containsKey(word.toUpperCase()))
                termsPerDoc.put(word.toUpperCase(),termsPerDoc.get(word.toUpperCase())+1);
            else
                termsPerDoc.put(word.toUpperCase(),1);
            return;
        }
    }
    //Handling the law of units of expressions&ranges as required
    public void convertExpressionOrRanges(String word, String docName, HashSet<String> sw, String j, boolean stem, HashMap<String,Integer> termsPerDoc) {
        word = cutOffDelimiters(word);
        String expression = "((\\w+)(\\-)(\\w+)(((\\-)(\\w+))*))";
        String ranges = "((\\-)(\\d+)(\\-)(\\d+)(((\\-)(\\d+))*))";
        String toCompile = expression + "|" + ranges;
        pattern = Pattern.compile(toCompile);
        m = pattern.matcher(word);
        if (m.find()) {
            //put all expression
            if (!terms.containsKey(word)) {
                Term t = new Term(word, docName, 1, j);
                terms.put(word, t);
                docLength++;
                termsInOneDoc++;
            } else {
                Term t = terms.get(word);
                if (!t.getDocs().containsKey(docName))
                    termsInOneDoc++;
                t.setTerm(docName, j);
                terms.put(word, t);
                docLength++;
                int tempo = t.getDocs().get(docName).getKey();
                if (!(tempo < maxTf))
                    maxTf = tempo;
            }
            //range (numbers)
            String[] subWords = word.split("-");
            if (word.charAt(0) == '-') {
                if (isNumber(subWords[1]))
                    subWords[1] = "-" + subWords[1];
            }
            int place=Integer.valueOf(j)+1;
            for (int i = 0; i < subWords.length; i++) {
                if (subWords[i].length() >= 1 && subWords[i].charAt(0) == '-') {
                    if (isNumber(subWords[i].substring(1)))
                        numbers(replaceFromMap(subWords[i], replacements), docName, place+"");
                } else if (isNumber(subWords[i]))
                    numbers(replaceFromMap(subWords[i], replacements), docName, place+"");
                else
                    letters(subWords[i], docName, sw, place+"", stem,termsPerDoc);
                place++;
            }
            return;
        }
        //between and
        String betweenAnd = "((between )(\\d+)( and )(\\d+))";
        pattern = Pattern.compile(betweenAnd);
        m = pattern.matcher(word);
        if (m.find()) {
            if (!terms.containsKey(word)) {
                Term t = new Term(word + "", docName, 1, j);
                terms.put(word, t);
                docLength++;
                termsInOneDoc++;
            } else {
                Term t = terms.get(word);
                if (!t.getDocs().containsKey(docName))
                    termsInOneDoc++;
                t.setTerm(docName, j);
                terms.put(word, t);
                docLength++;
                int tempo = t.getDocs().get(docName).getKey();
                if (tempo > maxTf)
                    maxTf = tempo;
            }
            String[] subWord = word.split(" ");
            if (subWord[1].matches("-?\\d+(\\.\\d+)?"))
                numbers(replaceFromMap(subWord[1], replacements), docName, j);
            else
                letters(subWord[1], docName, sw, j, stem,termsPerDoc);
            if (subWord[1].matches("-?\\d+(\\.\\d+)?"))
                numbers(replaceFromMap(subWord[3], replacements), docName, j);
            else
                letters(subWord[3], docName, sw, j, stem,termsPerDoc);
        }
    }


    //Handling the law of units of percents as required
    public void percent(String text, String docName, String j) {
        try {
            String[] array = text.split(" ");
            String percent = "";
            if (array.length == 3) {
                percent = array[0] + " " + array[1] + "%";
            } else if (array.length == 2) {
                if (array[1].equals("percent") || array[1].equals("percentage"))
                    percent = array[0] + "%";
                else
                    percent = array[0] + array[1] + "%";
            } else {
                percent = array[0];
            }
            if (terms.containsKey(percent)) {
                Term t = terms.get(percent);
                if (!t.getDocs().containsKey(docName))
                    termsInOneDoc++;
                t.setTerm(docName, j);
                terms.put(t.term, t);
                docLength++;
                int tempo = t.getDocs().get(docName).getKey();
                if (tempo > maxTf)
                    maxTf = tempo;
            } else if (!percent.equals("")) {
                Term t = new Term(percent, docName, 1, j);
                terms.put(t.term, t);
                docLength++;
                termsInOneDoc++;
            }
        } catch (Exception e) {
            return;
        }
    }

    //Handling the law of prices as required
    public void dollars(String text, String docName, String j) {
        try {
            String[] array = text.split(" ");
            double num;
            String price = "";
            if (array.length == 4) {
                if (array[0].charAt(0) == '$')
                    array[0] = array[0].substring(1);
                if (array[0].contains(","))
                    array[0] = replaceFromMap(array[0], replacements);
                if (array[1].equals("million")) {
                    price = array[0] + " M" + " Dollars";
                } else if (array[1].equals("billion")) {
                    num = Float.valueOf(array[0]);
                    num = num * 1000;
                    if (num % 1 != 0)
                        price = num + " M Dollars";
                    else
                        price = Math.round(num) + " M Dollars";
                } else if (array[1].equals("trillion")) {
                    num = Double.valueOf(array[0]);
                    num = num * 1000000;
                    if (num % 1 != 0) {
                        price = num + " M Dollars";
                    } else
                        price = Math.round(num) + " M Dollars";
                }
            } else if (array.length == 3) {
                if (array[0].contains(","))
                    array[0] = replaceFromMap(array[0], replacements);
                if (array[1].equals("m")) {
                    price = array[0] + " M" + " Dollars";
                } else if (array[1].equals("bn")) {
                    num = Double.valueOf(array[0]);
                    num = num * 1000;
                    if (num % 1 != 0)
                        price = num + " M Dollars";
                    else
                        price = Math.round(num) + " M Dollars";
                } else if (array[2].equals("Dollars")) {
                    num = Double.valueOf(array[0]);
                    if (num < 1000000)
                        price = array[0] + " " + array[1] + " Dollars";
                }
            } else if (array.length == 2) {
                if (array[1].equals("Dollars")) {
                    if (array[0].contains(","))
                        array[0] = replaceFromMap(array[0], replacements);
                    num = Double.valueOf(array[0]);
                    if (num < 1000000) {
                        price = array[0] + " Dollars";
                    } else if (num < 1000000000) {
                        num = num / 1000000;
                        if (num % 1 != 0)
                            price = num + " M Dollars";
                        else
                            price = Math.round(num) + " M Dollars";
                    }
                } else if ((array[1].contains("million") || array[1].contains("billion") || array[1].contains("trillion"))) {
                    String numbers = (array[0].substring(1));//without the $
                    if (numbers.contains(","))
                        numbers = replaceFromMap(numbers, replacements);
                    num = Double.valueOf(numbers);
                    numbers = num + "";
                    if (array[1].equals("million")) {
                        if (num % 1 == 0)
                            numbers = Math.round(num) + "";
                    } else if (array[1].equals("billion")) {
                        num = num * 1000;
                        if (num % 1 == 0)
                            numbers = Math.round(num) + "";
                    } else if (array[1].equals("trillion")) {
                        num = num * 1000000;
                        if (num % 1 == 0)
                            numbers = Math.round(num) + "";
                    }
                    price = numbers + " M Dollars";
                } else {
                    price = array[0].substring(1) + " " + array[1] + " Dollars";
                }
            } else if (array.length == 1) {
                String numbersFunc = (array[0].substring(1));//without the $
                if (numbersFunc.contains(","))
                    numbersFunc = replaceFromMap(numbersFunc, replacements);
                num = Double.valueOf(numbersFunc);
                if (num < 1000000) {
                    price = numbersFunc + " Dollars";
                } else if (num < 1000000000) {
                    num = num / 1000000;
                    if (num % 1 != 0)
                        price = num + " M Dollars";
                    else
                        price = Math.round(num) + " M Dollars";
                } else {
                    num = num * 1000000;
                    if (num % 1 != 0)
                        price = num + " " + " M Dollars";
                    else
                        price = Math.round(num) + " M Dollars";
                }
            }
            if (terms.containsKey(price)) {
                Term t = terms.get(price);
                if (!t.getDocs().containsKey(docName))
                    termsInOneDoc++;
                t.setTerm(docName, j);
                terms.put(price, t);
                docLength++;
                int tempo = t.getDocs().get(docName).getKey();
                if (!(tempo <= maxTf))
                    maxTf = tempo;
            } else if (!price.equals("")) {
                Term t = new Term(price, docName, 1, j);
                terms.put(price, t);
                docLength++;
                termsInOneDoc++;
            }
        } catch (Exception e) {
            return;
        }
    }

    //Handling the law of units of measure
    public void unitsOfMeasure(String word, String docName, String j) {
        if (delimiters.contains(word.charAt(word.length() - 1)))
            word = word.substring(0, word.length() - 1);
        String[] subWords = word.split(" ");
        if (units.containsKey(subWords[1])) {
            if (terms.containsKey(subWords[0] + " " + units.get(subWords[1]))) {
                Term t = terms.get(subWords[0] + " " + units.get(subWords[1]));
                if (!t.getDocs().containsKey(docName))
                    termsInOneDoc++;
                t.setTerm(docName, j);
                terms.put(t.term, t);
                docLength++;
                int tempo = t.getDocs().get(docName).getKey();
                if (tempo > maxTf)
                    maxTf = tempo;
            } else {
                Term t = new Term(subWords[0] + " " + units.get(subWords[1]), docName, 1, j);
                terms.put(t.term, t);
                docLength++;
                termsInOneDoc++;
            }
        }
    }

    //Check whether the number is already in the dictionary or not, and treatment accordingly
    private void numbers(String word, String docName, String j) {
        try {
            String num = convertNumbers(word);
            if (terms.containsKey(num)) {
                Term t = terms.get(num + "");
                if (!t.getDocs().containsKey(docName))
                    termsInOneDoc++;
                t.setTerm(docName, j);
                terms.put(t.term, t);
                docLength++;
                int tempo = t.getDocs().get(docName).getKey();
                if (tempo > maxTf)
                    maxTf = tempo;
            } else if (!num.equals("")) {
                Term t = new Term(num, docName, 1, j);
                termsInOneDoc++;
                terms.put(t.term, t);
                docLength++;
            }
        } catch (Exception e) {
            return;
        }
    }
    //Handling the law of numbers as required
    public String convertNumbers(String number) {
        try {
            String[] array = number.split(" ");
            String num = "";
            if (array.length == 1) {
                Double cast;
                array[0] = cutOffDelimiters(array[0]);
                array[0] = replaceFromMap(array[0], replacements);

                if (array[0].contains("\'"))
                    return "";
                cast = Double.valueOf(array[0]);

                if (cast >= 1000 && cast < 1000000) {
                    cast = cast / 1000;
                    if (cast % 1 != 0)
                        num = String.format("%.2f", cast) + "K";
                    else
                        num = String.format("%.2f", Math.round(cast)) + "K";
                } else if (cast >= 1000000 && cast < 1000000000) {
                    cast = cast / 1000000;
                    if (cast % 1 != 0)
                        num = String.format("%.2f", cast) + "M";
                    else
                        num = String.format("%.2f", Math.round(cast)) + "M";

                } else if (cast >= 1000000000) {
                    cast = cast / 1000000000;
                    if (cast % 1 != 0)
                        num = String.format("%.2f", cast) + "B";

                    else
                        num = String.format("%.2f", Math.round(cast)) + "B";
                } else {
                    if (cast % 1 != 0)
                        num = String.format("%.2f", cast);
                    else
                        num = String.format("%.2f", Math.round(cast));
                }
            } else {
                String left = cutOffDelimiters(array[0]);
                String right = cutOffDelimiters(array[1]);
                left = replaceFromMap(left, replacements);
                double cast = Double.valueOf(left);
                if (right.equals("Thousand")) {
                    if (cast % 1 != 0)
                        num = String.format("%.2f", cast) + "K";
                    else
                        num = String.format("%.2f", Math.round(cast)) + "K";
                } else if (right.equals("Million")) {
                    if (cast % 1 != 0)
                        num = String.format("%.2f", cast) + "M";
                    else
                        num = String.format("%.2f", Math.round(cast)) + "M";
                } else if (right.equals("Billion")) {
                    if (cast % 1 != 0)
                        num = String.format("%.2f", cast) + "B";
                    else
                        num = String.format("%.2f", Math.round(cast)) + "B";
                } else if (right.equals("Trillion")) {
                    cast = cast * 1000;
                    if (cast % 1 != 0)
                        num = String.format("%.2f", cast) + "B";
                    else
                        num = String.format("%.2f", Math.round(cast)) + "B";
                } else if (right.contains("/")) {
                    if (cast % 1 != 0)
                        num = String.format("%.2f", cast) + " " + right;
                    else
                        num = String.format("%.2f", Math.round(cast)) + " " + right;
                }
            }
            return num;
        } catch (Exception e) {
            String[] array = number.split(" ");
            String num = "";
            if (array.length == 1) {
                Double cast;
                array[0] = cutOffDelimiters(array[0]);
                array[0] = replaceFromMap(array[0], replacements);

                if (array[0].contains("\'"))
                    return "";
                cast = Double.valueOf(array[0]);

                if (cast >= 1000 && cast < 1000000) {
                    cast = cast / 1000;
                    if (cast % 1 != 0)
                        num = cast + "K";
                    else
                        num = Math.round(cast) + "K";
                } else if (cast >= 1000000 && cast < 1000000000) {
                    cast = cast / 1000000;
                    if (cast % 1 != 0)
                        num = cast + "M";
                    else
                        num = Math.round(cast) + "M";

                } else if (cast >= 1000000000) {
                    cast = cast / 1000000000;
                    if (cast % 1 != 0)
                        num = cast + "B";

                    else
                        num = Math.round(cast) + "B";
                } else {
                    if (cast % 1 != 0)
                        num = cast + "";
                    else
                        num = Math.round(cast) + "";
                }
            } else {
                String left = cutOffDelimiters(array[0]);
                String right = cutOffDelimiters(array[1]);
                left = replaceFromMap(left, replacements);
                double cast = Double.valueOf(left);
                if (right.equals("Thousand")) {
                    if (cast % 1 != 0)
                        num = cast + "K";
                    else
                        num = Math.round(cast) + "K";
                } else if (right.equals("Million")) {
                    if (cast % 1 != 0)
                        num = cast + "M";
                    else
                        num = Math.round(cast) + "M";
                } else if (right.equals("Billion")) {
                    if (cast % 1 != 0)
                        num = cast + "B";
                    else
                        num = Math.round(cast) + "B";
                } else if (right.equals("Trillion")) {
                    cast = cast * 1000;
                    if (cast % 1 != 0)
                        num = cast + "B";
                    else
                        num = Math.round(cast) + "B";
                } else if (right.contains("/")) {
                    if (cast % 1 != 0)
                        num = cast + " " + right;
                    else
                        num = Math.round(cast) + " " + right;
                }
            }
            return num;
        }
    }

    /**
     * This function cutting if there is word with any delimiters attached
     *
     * @param word
     * @return word without delimiters
     */
    public String cutOffDelimiters(String word) {
        int i = 0;
        if ((isNumber(word) && word.length() > 2 && (word.substring(word.length() - 2).equals("th"))) || (word.length() > 2 && word.substring(word.length() - 2).equals("'s")) || (word.length() > 2 && word.substring(word.length() - 2).equals("'S")))
            word = word.substring(0, word.length() - 2);
        while ((word.length() > 0 && delimiters.contains(word.charAt(i)))) {
            word = word.substring(1);
        }
        i = word.length() - 1;
        while (word.length() > 0 && delimiters.contains(word.charAt(i))) {
            word = word.substring(0, word.length() - 1);
            i--;
        }
        if ((isNumber(word) && word.length() > 2 && (word.substring(word.length() - 2).equals("th"))) || (word.length() > 2 && word.substring(word.length() - 2).equals("'s")) || (word.length() > 2 && word.substring(word.length() - 2).equals("'S")))
            word = word.substring(0, word.length() - 2);
        return word;
    }

    /**
     * This function replace delimiters in word with empty string
     *
     * @param string
     * @param replacements
     * @return
     */
    private String replaceFromMap(String string, Map<String, String> replacements) {
        StringBuilder sb = new StringBuilder(string);
        for (Map.Entry<String, String> entry : replacements.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();

            int start = sb.indexOf(key, 0);
            while (start > -1) {
                int end = start + key.length();
                int nextSearchStart = start + value.length();
                sb.replace(start, end, value);
                start = sb.indexOf(key, nextSearchStart);
            }
        }
        return sb.toString();
    }
    //Reset the terms field of the Parser object
    public void startAgain() {
        terms = new HashMap<>();
    }



}