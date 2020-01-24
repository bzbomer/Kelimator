package Grup19Kelimator;

/**
 * Arama agacının genel özelliklerinin arayüzüdür.
 * @param <E> Generic tip argumanıdır.
 */
public interface AramaAgaci<E> {

    /**
     * Veri yapısına item ekler
     * @param oge Itemdır.
     * @return Veri yapısına eklenip eklenmedigi döner.
     */
    boolean ekle(E oge);

    /**
     * Veri yapısında item bulunur mu?
     * @param hedef Veri yapısında aranacak olan item'dır.
     * @return Veri yapısında icerip icermediği bilgisini döner.
     */
    boolean icerir(E hedef);

    /**
     * Veri yapısında itemı arar?
     * @param hedef Aradıgı item'dır.
     * @return Bulunan hedefi bulursa döner veya null döner.
     */
    E bul(E hedef);

    /**
     * Veri yapısından item silinir.
     * @param hedef Silinecek olan item'dır.
     * @return Silinen item'ı döner veya null döner.
     */
    E sil(E hedef);

    /**
     * Veri yapısından item cıkarmada cıkarılıp çıkarılmadıgını bildirir.
     * @param hedef Cıkarılacak olan item'dır.
     * @return Cıkarılma durumunu döner.
     */
    boolean cikar(E hedef);

}
