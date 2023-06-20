package it.unisa.c07.biblionet.gestioneclubdellibro.repository;

import it.unisa.c07.biblionet.gestioneclubdellibro.repository.Genere;

import java.util.HashMap;
import java.util.Map;

public class GenereSpecifico {
    private static Map<GenereS, String> genereDescrizioneMap;
    public enum GenereS{
        ROMANZO,
        FANTASCIENZA,
        FANTASY,
        THRILLER,
        GIALLO,
        HORROR,
        POESIA,
        DRAMMA,
        BIOGRAFIA,
        SAGGIO,
        STORICO;
    }
    static {
        genereDescrizioneMap = new HashMap<>();
        genereDescrizioneMap.put(GenereS.ROMANZO, "Un lungo racconto di finzione che segue le vicende di personaggi in situazioni complesse.");
        genereDescrizioneMap.put(GenereS.FANTASCIENZA, "Un genere che combina elementi scientifici e immaginazione per creare storie ambientate in universi alternativi.");
        genereDescrizioneMap.put(GenereS.FANTASY, "Un genere che coinvolge elementi magici, creature mitiche e spesso si svolge in un mondo immaginario.");
        genereDescrizioneMap.put(GenereS.THRILLER, "Un genere caratterizzato da suspense, tensione e spesso da una trama ad alta velocità.");
        genereDescrizioneMap.put(GenereS.GIALLO, "Un genere incentrato sulla risoluzione di un crimine o di un mistero, solitamente attraverso l'indagine di un detective.");
        genereDescrizioneMap.put(GenereS.HORROR, "Un genere che cerca di provocare paura, terrore o disgusto nel lettore attraverso l'uso di elementi soprannaturali o macabri.");
        genereDescrizioneMap.put(GenereS.POESIA, "Una forma di espressione artistica che utilizza il linguaggio per suscitare emozioni ed esplorare temi profondi.");
        genereDescrizioneMap.put(GenereS.DRAMMA, "Un genere che presenta conflitti emotivi e tensioni tra i personaggi, spesso con una trama intensa e coinvolgente.");
        genereDescrizioneMap.put(GenereS.BIOGRAFIA, "La narrazione della vita di una persona reale, spesso scritta da un'altra persona.");
        genereDescrizioneMap.put(GenereS.SAGGIO, "Un'opera che esplora un argomento specifico e offre un'analisi e un punto di vista dell'autore.");
        genereDescrizioneMap.put(GenereS.STORICO, "Un genere che si basa su eventi storici reali, spesso con l'intento di educare o intrattenere.");
    }

    public static String getDescrizioneByGenere(Genere genere) {
        return genereDescrizioneMap.get(genere);
    }
}
