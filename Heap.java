package Grup19Kelimator;

import java.util.ArrayList;

/**
 * Puan tablosu işlemlerinin class yapısıdır.
 * @param <E> Generic tip üzerinde çalıştıgını gösteren parametredir.
 */
public class Heap<E extends Comparable<E>> {

    /**
     * Puan tablosu tutan heap yapısına iyi giden arrayList yapısıdır.
     */
    private ArrayList<E> tablo;

    /**
     * No parameter constructor
     */
    public Heap(){
        tablo = new ArrayList<>();
    }

    /**
     * Tablodaki(ArrayList) en yüksek puanı alır
     * @return Tablodaki en yüksek puan döner.
     */
    public E enYuksekPuaniAl(){
        return tablo.get(0);
    }

    /**
     * Puan tablosunda herhangi sıradaki puan degerini alır.
     * @param i index valuedur.
     * @return puanı döndürür.
     */
    public E puanAlIndeks(int i){
        return tablo.get(i);
    }

    // Verilen objenin puanini al metodu yazilacak. Grup19Kelimator.Oyuncu tipindeki bir objenin puani yani...

    /**
     * Puan tablosu boyutunu döndürür.
     * @return tablo size'ı döndürülür.
     */
    public int boyut(){
        return tablo.size();
    }

    /**
     * Puan tablosuna yeni bir puan ekler.
     * @param oge Generic tipde bir item'dır.
     */
    public void ekle(E oge){
        int cocuk;
        int veli;
        tablo.add(oge);
        cocuk = tablo.size()-1;
        veli = (cocuk-1)/2;
        // cocuk parent'tan buyuk.
        while(veli >= 0 && tablo.get(veli).compareTo(tablo.get(cocuk)) < 0){
            E gecici = tablo.get(veli);
            tablo.set(veli,tablo.get(cocuk));
            tablo.set(cocuk,gecici);
            cocuk = veli;
            veli = (cocuk-1)/2;
        }
    }

    /**
     * Puan tablosundan herhangi bir puan çıkarır.
     * @return Generic tipde item'dır.
     */
    public E cikar(){
        int solCocuk;
        int sagCocuk;
        int veli;
        int maksCocuk;
        // Ilk elemanin yerine en son elemani
        // (Agac yapisi olarak dusunursek en alt en sagdaki elemani)
        // koy ve sonuncu elemani listeden cikart.
        E cikarilan = tablo.get(0);
        tablo.set(0,tablo.remove(tablo.size()-1));
        veli = 0;
        while (true){
            solCocuk = 2 * veli + 1;
            sagCocuk = solCocuk + 1;
            if(solCocuk >= tablo.size())
                break;
            maksCocuk = solCocuk;
            // Sagcocuk solcocuktan daha buyuk.
            if(sagCocuk < tablo.size() && tablo.get(sagCocuk).compareTo(tablo.get(solCocuk)) > 0)
                maksCocuk = sagCocuk;
            // Buyuk olan cocuk veli'den daha buyuk degere sahip.
            if(tablo.get(maksCocuk).compareTo(tablo.get(veli)) > 0){
                E gecici = tablo.get(veli);
                tablo.set(veli,tablo.get(maksCocuk));
                tablo.set(maksCocuk,gecici);
                veli = maksCocuk;
            }
            else
                break;
        }
        return cikarilan;
    }
}
