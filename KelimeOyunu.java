package Grup19Kelimator;
/**
 * Kelime oyununun ana metodlarını içeren interface yapımızdır.
 */
public interface KelimeOyunu {
    /**
     * Oyuna başlamak için gerekli oyun tipine göre override edilecek methoddur
     */
    void oyunuBaslat();
    /**
     *  Oyunun bitiminde puan ve hamle haritasının print edileceği metoddur.
     */
    void oyunuBitir();
    /**
     * İlgili alana ait legal bir kelime mi kontrolü yapacak.
     * @param Kelime Girilen kelime
     * @param ilkHarf Kelimenin ilk harfi kontrolü için gereklidir.
     * @param alan Alan bilgisidir.
     * @return Kelime bulunursa true bulunmazsa false döner.
     */
    boolean kelimeDogrumu(String Kelime, char ilkHarf, String alan);
}
