package Grup19Kelimator;

public class HeapTest {
    public static void main(String[] args) {
        // Grup19Kelimator.Heap'in testi...
        Heap<Integer> felsefePuanTablosu = new Heap<>();
        felsefePuanTablosu.ekle(1);
        System.out.println("En yuksek puan: " + felsefePuanTablosu.enYuksekPuaniAl());
        felsefePuanTablosu.ekle(22);
        System.out.println("En yuksek puan: " + felsefePuanTablosu.enYuksekPuaniAl());
        felsefePuanTablosu.ekle(0);
        System.out.println("En yuksek puan: " + felsefePuanTablosu.enYuksekPuaniAl());
        felsefePuanTablosu.ekle(25);
        System.out.println("En yuksek puan: " + felsefePuanTablosu.enYuksekPuaniAl());
        felsefePuanTablosu.ekle(56);
        System.out.println("En yuksek puan: " + felsefePuanTablosu.enYuksekPuaniAl());
        System.out.println("Suan heap boyutu: " + felsefePuanTablosu.boyut());

        int cikarilan = felsefePuanTablosu.cikar();
        System.out.println("Cikarilan en yuksek puan: " + cikarilan);
        System.out.println("Artik en yuksek puan: " + felsefePuanTablosu.enYuksekPuaniAl());
        System.out.println("Cikarma isleminden sonra heap boyutu: " + felsefePuanTablosu.boyut());
    }

}
