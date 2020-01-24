package Grup19Kelimator;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.Stack;

/**
 * Oyuncunun sahip oldugu nitelikleri ve görevleri bulunduran class yapımızdır.
 */
public class Oyuncu implements Comparable<Oyuncu>{
    /**
     * Oyuncunun gerceklestirdigi sondan ilke hamle sırasıdır.
     */
    private ArrayList<String> hamleHaritasi = new ArrayList<>();

    /**
     * Belirtilen zaman sınırı miktarıdır.
     */
    private int zamanSiniri = 16;

    /**
     * Oyuncunun ismi(takma adi)
     */
    private String isim;

    /**
     * Oyuncunun oyunda yapmis oldugu puan.
     */
    private Integer puan;

    /**
     * No parameter constructor
     */
    Oyuncu(){
        isim = null;
        puan = 0;
    }
    Oyuncu(String isim){
        this.isim = isim;
        puan = 0;
    }

    /**
     * Girilen uygun kelimeyi hamle haritasına ekleyen metoddur.
     * @param kelime Oyuncu hamlesidir, alanla ilgili kelimedir.
     */
    public void kelimeKaydet(String kelime) {
        hamleHaritasi.add(kelime);
    }

    public ArrayList<String> getHamleHaritasi(){
        return hamleHaritasi;
    }

    /**
     *  Kullanıcın puanının alınması için getter metodudur.
     * @return kullanıcı puanı döndürür.
     */
    public Integer getPuan() {
        return puan;
    }

    /**
     * Mevcut puan için setter metodudur.
     * @param puan Assignment edilecek puandır.
     */
    public void setPuan(Integer puan) {
        this.puan = puan;
    }

    /**
     * Kullanıcı ismi için getter metodudur.
     * @return Kullanıcı ismini döndürür.
     */
    public String getIsim() {
        return isim;
    }

    /**
     * Kullanıcı ismi için setter metodudur.
     * @param isim Kullanıcı ismidir.
     */
    public void setIsim(String isim) {
        this.isim = isim;
    }



    public int compareTo(Oyuncu o) {
        return puan.compareTo(o.puan);
    }
}
