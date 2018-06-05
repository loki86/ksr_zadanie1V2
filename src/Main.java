import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    private static ArrayList<Artykul> listaArtykulow = new ArrayList();
    private static int wywolania = 0;
    private static Map artyBezBody = new HashMap();
    private static Map wystapieniaMap = new HashMap<>();
    private static Map mySQL_StopListHashMap = new HashMap<>();
    private static Pattern patternDFull = Pattern.compile("(<PLACES>)(<D>)(.*?)(</D>)(</PLACES>)");

    private static String mySQL_StopWordList_File = "C:\\Users\\Cinnamon\\Desktop\\KSR_zad1\\MySQL_STOPWORD_LIST.txt";

    public static void main(String[] args) {

        long startTimeMiliSec = System.currentTimeMillis();

        wystapieniaMap.put("west-germany", 0);
        wystapieniaMap.put("usa", 0);
        wystapieniaMap.put("france", 0);
        wystapieniaMap.put("canada", 0);
        wystapieniaMap.put("japan", 0);
        wystapieniaMap.put("uk", 0);

        artyBezBody.put("west-germany", 0);
        artyBezBody.put("usa", 0);
        artyBezBody.put("france", 0);
        artyBezBody.put("canada", 0);
        artyBezBody.put("japan", 0);
        artyBezBody.put("uk", 0);

        przerobPlik();

        String[] tablicaPlikowDoNauki = {
                "C:\\Users\\Cinnamon\\Desktop\\KSR_zad1\\reut2-000.sgm",
                "C:\\Users\\Cinnamon\\Desktop\\KSR_zad1\\reut2-001.sgm",
                "C:\\Users\\Cinnamon\\Desktop\\KSR_zad1\\reut2-002.sgm",
                "C:\\Users\\Cinnamon\\Desktop\\KSR_zad1\\reut2-003.sgm",
                "C:\\Users\\Cinnamon\\Desktop\\KSR_zad1\\reut2-004.sgm",
                "C:\\Users\\Cinnamon\\Desktop\\KSR_zad1\\reut2-005.sgm",
                "C:\\Users\\Cinnamon\\Desktop\\KSR_zad1\\reut2-006.sgm",
                "C:\\Users\\Cinnamon\\Desktop\\KSR_zad1\\reut2-007.sgm",
                "C:\\Users\\Cinnamon\\Desktop\\KSR_zad1\\reut2-008.sgm",
                "C:\\Users\\Cinnamon\\Desktop\\KSR_zad1\\reut2-009.sgm",
                "C:\\Users\\Cinnamon\\Desktop\\KSR_zad1\\reut2-010.sgm",
                "C:\\Users\\Cinnamon\\Desktop\\KSR_zad1\\reut2-011.sgm",
                "C:\\Users\\Cinnamon\\Desktop\\KSR_zad1\\reut2-012.sgm",
                "C:\\Users\\Cinnamon\\Desktop\\KSR_zad1\\reut2-013.sgm",
                "C:\\Users\\Cinnamon\\Desktop\\KSR_zad1\\reut2-014.sgm",
                "C:\\Users\\Cinnamon\\Desktop\\KSR_zad1\\reut2-015.sgm",
                "C:\\Users\\Cinnamon\\Desktop\\KSR_zad1\\reut2-016.sgm",
                "C:\\Users\\Cinnamon\\Desktop\\KSR_zad1\\reut2-017.sgm",
                "C:\\Users\\Cinnamon\\Desktop\\KSR_zad1\\reut2-018.sgm",
                "C:\\Users\\Cinnamon\\Desktop\\KSR_zad1\\reut2-019.sgm",
                "C:\\Users\\Cinnamon\\Desktop\\KSR_zad1\\reut2-020.sgm",
                "C:\\Users\\Cinnamon\\Desktop\\KSR_zad1\\reut2-021.sgm",
//                "C:\\Users\\Cinnamon\\Desktop\\KSR_zad1\\reut2-021-moj.sgm",
//                "C:\\Users\\Cinnamon\\Desktop\\KSR_zad1\\moj_2arty.sgm"
        };


        String[] tablicaPlikowDoTestowania = {
                "C:\\Users\\Cinnamon\\Desktop\\KSR_zad1\\reut2-000.sgm",
                "C:\\Users\\Cinnamon\\Desktop\\KSR_zad1\\reut2-001.sgm",
                "C:\\Users\\Cinnamon\\Desktop\\KSR_zad1\\reut2-002.sgm",
                "C:\\Users\\Cinnamon\\Desktop\\KSR_zad1\\reut2-003.sgm",
                "C:\\Users\\Cinnamon\\Desktop\\KSR_zad1\\reut2-004.sgm",
                "C:\\Users\\Cinnamon\\Desktop\\KSR_zad1\\reut2-005.sgm",
                "C:\\Users\\Cinnamon\\Desktop\\KSR_zad1\\reut2-006.sgm",
                "C:\\Users\\Cinnamon\\Desktop\\KSR_zad1\\reut2-007.sgm",
                "C:\\Users\\Cinnamon\\Desktop\\KSR_zad1\\reut2-008.sgm",
                "C:\\Users\\Cinnamon\\Desktop\\KSR_zad1\\reut2-009.sgm",
                "C:\\Users\\Cinnamon\\Desktop\\KSR_zad1\\reut2-010.sgm",
                "C:\\Users\\Cinnamon\\Desktop\\KSR_zad1\\reut2-011.sgm",
                "C:\\Users\\Cinnamon\\Desktop\\KSR_zad1\\reut2-012.sgm",
                "C:\\Users\\Cinnamon\\Desktop\\KSR_zad1\\reut2-013.sgm",
                "C:\\Users\\Cinnamon\\Desktop\\KSR_zad1\\reut2-014.sgm",
                "C:\\Users\\Cinnamon\\Desktop\\KSR_zad1\\reut2-015.sgm",
                "C:\\Users\\Cinnamon\\Desktop\\KSR_zad1\\reut2-016.sgm",
                "C:\\Users\\Cinnamon\\Desktop\\KSR_zad1\\reut2-017.sgm",
                "C:\\Users\\Cinnamon\\Desktop\\KSR_zad1\\reut2-018.sgm",
                "C:\\Users\\Cinnamon\\Desktop\\KSR_zad1\\reut2-019.sgm",
                "C:\\Users\\Cinnamon\\Desktop\\KSR_zad1\\reut2-020.sgm",
                "C:\\Users\\Cinnamon\\Desktop\\KSR_zad1\\reut2-021.sgm",
//                "C:\\Users\\Cinnamon\\Desktop\\KSR_zad1\\reut2-021-moj.sgm",
//                "C:\\Users\\Cinnamon\\Desktop\\KSR_zad1\\moj_2arty.sgm"
        };

        for (String plikZArtykulami : tablicaPlikowDoNauki) {
            Scanner plikArtykulow = null;
            try {
                plikArtykulow = new Scanner(new File(plikZArtykulami));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            while (plikArtykulow.hasNextLine()) {
                Scanner s2 = new Scanner(plikArtykulow.nextLine());
                while (s2.hasNext()) {
                    String s = s2.next();
                    Matcher matcherD = patternDFull.matcher(s);
                    if (matcherD.find()) {
                        switch (matcherD.group(3)) {
                            case "west-germany":
                                wystapieniaMap.put(matcherD.group(3), (int) wystapieniaMap.get(matcherD.group(3)) + 1);
                                findBody(matcherD.group(3), plikArtykulow);
                                break;
                            case "usa":
                                wystapieniaMap.put(matcherD.group(3), (int) wystapieniaMap.get(matcherD.group(3)) + 1);
                                findBody(matcherD.group(3), plikArtykulow);
                                break;
                            case "france":
                                wystapieniaMap.put(matcherD.group(3), (int) wystapieniaMap.get(matcherD.group(3)) + 1);
                                findBody(matcherD.group(3), plikArtykulow);
                                break;
                            case "uk":
                                wystapieniaMap.put(matcherD.group(3), (int) wystapieniaMap.get(matcherD.group(3)) + 1);
                                findBody(matcherD.group(3), plikArtykulow);
                                break;
                            case "canada":
                                wystapieniaMap.put(matcherD.group(3), (int) wystapieniaMap.get(matcherD.group(3)) + 1);
                                findBody(matcherD.group(3), plikArtykulow);
                                break;
                            case "japan":
                                wystapieniaMap.put(matcherD.group(3), (int) wystapieniaMap.get(matcherD.group(3)) + 1);
                                findBody(matcherD.group(3), plikArtykulow);
                                break;
                        }
                    }
                }
            }
        }

        System.out.println("Funkcje wywolano " + wywolania + " razy");
        System.out.println("<<<<<<<<<<<<<<<<<<<<================================= Ilosc artykulow w liscie listaArtykulow wynosi = " + listaArtykulow.size());
        wystapieniaMap.forEach((k, v) -> System.out.println("Kraj: " + k.toString().toUpperCase() + ", wystąpił:" + v + " razy"));
        artyBezBody.forEach((k, v) -> System.out.println("ArtyBezBody = Kraj: " + k.toString().toUpperCase() + ", wystąpił:" + v + " razy"));
        System.out.println("<<<<<<<===========================ART NUMER 1");
        listaArtykulow.get(0).mapaSlow.forEach((k,v) -> System.out.println("Slowo " + listaArtykulow.get(0).mapaSlow.size() +" "+ k.toString().toUpperCase() + " wystapiło: " + v + " razy"));
        System.out.println("<<<<<<<===========================ART NUMER 2");
        System.out.println("Kraj z artykulu nr 2 => " + listaArtykulow.get(listaArtykulow.size()-2).kraj.toString());
        listaArtykulow.get((listaArtykulow.size()-2)).mapaSlow.forEach((k,v) -> System.out.println("Slowo " + listaArtykulow.get((listaArtykulow.size()-2)).mapaSlow.size() +" "+ k.toString().toUpperCase() + " wystapiło: " + v + " razy"));
        System.out.println("<<<<<<<===========================ART NUMER 3");
        listaArtykulow.get(listaArtykulow.size()-1).mapaSlow.forEach((k,v) -> System.out.println("Slowo " + listaArtykulow.get(listaArtykulow.size()-1).mapaSlow.size() +" "+ k.toString().toUpperCase() + " wystapiło: " + v + " razy"));

        long endTimeMilliSeconds = System.currentTimeMillis();
        System.out.println("Took "+(endTimeMilliSeconds - startTimeMiliSec)/1000L + " s");

    }

    private static void findBody(String kraj, Scanner plikArtykulow) {

        boolean czyZnalezionoBody = false;
        Pattern patternBODY = Pattern.compile("(<BODY>)(.*?)");
        Pattern patternCloseBODY = Pattern.compile("</BODY>");
        Artykul nowyArtykul = new Artykul(kraj);

        while (plikArtykulow.hasNextLine()) {

            String liniaDoPodzialu = plikArtykulow.nextLine();
            liniaDoPodzialu = liniaDoPodzialu.replace("&lt;","");
            liniaDoPodzialu = liniaDoPodzialu.replaceAll("[.,0-9&#\"()]", " ");

            String[] liniaPodzielona = liniaDoPodzialu.toUpperCase().split(" ");

            for (String s: liniaPodzielona) {
                if (s.length() > 1) {
                    Matcher matcher = patternBODY.matcher(s);
                    Matcher matcherCloseBODY = patternCloseBODY.matcher(s);
                    Matcher matcherPLacesD = patternDFull.matcher(s);

                    if (matcherPLacesD.find() && (matcherPLacesD.group(3).equals("USA") || matcherPLacesD.group(3).equals("UK") ||
                            matcherPLacesD.group(3).equals("WEST-GERMANY") || matcherPLacesD.group(3).equals("FRANCE") ||
                            matcherPLacesD.group(3).equals("CANADA") || matcherPLacesD.group(3).equals("JAPAN"))) {
                        artyBezBody.put(kraj.toLowerCase(), (int) artyBezBody.get(kraj.toLowerCase()) + 1);
                        wystapieniaMap.put(matcherPLacesD.group(3).toLowerCase(), (int) wystapieniaMap.get(matcherPLacesD.group(3).toLowerCase()) + 1);

                        nowyArtykul.kraj = matcherPLacesD.group(3).toLowerCase();
                        kraj = matcherPLacesD.group(3).toLowerCase();

                    } else if (matcher.find()) {
                        String temp_substring = s.substring(s.indexOf("<BODY>")+6);
                        if (!temp_substring.isEmpty() && !mySQL_StopListHashMap.containsKey(temp_substring)) {
                            dodajSlowaDoArtykuluString(nowyArtykul, temp_substring);
                        }
                        czyZnalezionoBody = true;

                    } else if (matcherCloseBODY.find()) {
                        listaArtykulow.add(nowyArtykul);
                        return;

                    } else if (czyZnalezionoBody && !s.isEmpty() && !mySQL_StopListHashMap.containsKey(s)) {
                        dodajSlowaDoArtykuluString(nowyArtykul, s);
                    }
                }
            }
        }

    }


    private static void dodajSlowaDoArtykuluString(Artykul nowyArtykul, String linia) {

        StemmerClass stemmerWordOne = new StemmerClass();

        linia = linia.replaceAll("[.,'><]", "");
        stemmerWordOne.add(linia.toLowerCase().toCharArray(),linia.length());
        stemmerWordOne.stem();
        linia = stemmerWordOne.toString().toUpperCase();
        if (linia.length() > 1) {
            if (nowyArtykul.mapaSlow.containsKey(linia)) {
                nowyArtykul.mapaSlow.put(linia, (int) nowyArtykul.mapaSlow.get(linia) + 1);
            } else {
                nowyArtykul.mapaSlow.put(linia, 1);
            }
        }
    }

    private static void przerobPlik(){
        Scanner pliczek = null;

        try{
            pliczek = new Scanner(new File(mySQL_StopWordList_File));
        } catch (FileNotFoundException e)
        {e.printStackTrace();}

        while (pliczek.hasNextLine()){
            Scanner liniaStopLine = new Scanner(pliczek.nextLine());
            while (liniaStopLine.hasNext()){
                String stopWord = liniaStopLine.next();
                mySQL_StopListHashMap.put(stopWord.toUpperCase(),1);
            }
        }
    }

    private static void metrykaManhattan(){

    }

    private static void metrykaEuklidesowa(){

    }

    private static void metrykaCzebyszewa(){


    }

}