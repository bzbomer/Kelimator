package Grup19Kelimator;
/**
 * Kelime searching  için  gerekli, performanlı yapı olan Ikili Arama Agacı classıdır.
 * @param <E>
 */
public class IkiliAramaAgaci<E extends Comparable<E>> extends IkiliAgac<E>
        implements AramaAgaci<E> {
    /**
     * Add isleminin wrapper methodu icin gerekli olacak return value'dur.
     */
    protected boolean ekleDondurulen;

    /**
     * Delete isleminde wrapper methodu icin gerekli olacak return value'dur.
     */
    protected E silDondurulen;


    /**
     * Aranan kelimeyi ikili agacta bulacak methoddur.
     * @param hedef Aranacak kelimedir.
     * @return Bulunan nesneyi veya null döndürür.
     */
    public E bul(E hedef) {
        return bul(kok, hedef);
    }

    /**
     * One parameter constructor
     * @param veri rootdaki veriye  atanacak veridir.
     */
    public IkiliAramaAgaci(E veri){
        kok =new Dugum<>(veri);

    }

    /**
     * Sol agacı agacdaki kökün soluna atar.
     * @param sol Sol agac yapısının referance'ıdır.
     */
    public void solAgaciKur(IkiliAramaAgaci<E> sol){
        if(sol !=null)
            kok.sol =sol.kok;
    }

    /**
     * Sag agacı agacdaki kökün sagına atar.
     * @param sag Sag agac yapısının referance'ıdır.
     */
    public void sagAgaciKur(IkiliAramaAgaci<E> sag){

        if(sag !=null)
            kok.sag =sag.kok;
    }

    /**
     * Ikili Agacı ekrana yazdıran methoddur. Wrapper methoddur.
     */
    public void agaciYazdir(){
        yazdir(kok);
    }

    /**
     * Ikili Agacın datasını ekrana yazan recursive methoddur.
     * @param dugum Baslancı nood'udur. Her recursion call'da  update edilir.
     */
    private void yazdir(Dugum<E> dugum){
        if(dugum !=null){

            System.out.println(dugum.veri);
            yazdir(dugum.sol);
            yazdir(dugum.sag);
        }

    }

    /**
     * Hedefi local root'dan aramaya başlayan methoddur.
     * @param lokalkok Methodda Root olarak kabul edilir.
     * @param hedef Aranan hedef kelimedir.
     * @return Bulunan hedef item'ı döner
     */
    private E bul(Dugum<E> lokalkok, E hedef) {
        if (lokalkok == null)
            return null;

        int karsilastirma = hedef.compareTo(lokalkok.veri);
        if (karsilastirma == 0)
            return lokalkok.veri;
        else if (karsilastirma < 0)
            return bul(lokalkok.sol, hedef);
        else
            return bul(lokalkok.sag, hedef);
    }

    /**
     * Ikılı agaca item ekler.
     * @param oge Eklenecek olan item variable'ıdır.
     * @return Private variable'dır,recursive method da degerlendirilir,wrapper method da return edilir.
     */
    public boolean ekle(E oge) {
        kok = ekle(kok, oge);
        return ekleDondurulen;
    }

    /**
     * Ikili agaca item ekleyen private  recursion methoddur.
     * @param lokalkok  Recursion method da root olarak sayılır.
     * @param oge  Eklenecek itemdır.
     * @return  Yeni node tipi döndürür.
     */
    private Dugum<E> ekle(Dugum<E> lokalkok, E oge) {
        if (lokalkok == null) {
            ekleDondurulen = true;
            return new Dugum<>(oge);
        }
        else if (oge.compareTo(lokalkok.veri) == 0) {
            ekleDondurulen = false;
            return lokalkok;
        }
        else if (oge.compareTo(lokalkok.veri) < 0) {
            lokalkok.sol = ekle(lokalkok.sol, oge);
            return lokalkok;
        }
        else {
            lokalkok.sag = ekle(lokalkok.sag, oge);
            return lokalkok;
        }
    }

    /**
     * Ikili agacdan item silen wrapper methoddur.
     * @param hedef Silinecek olan item'dır.
     * @return Silen item'ı döndürür.
     */
    public E sil(E hedef) {
        kok = sil(kok, hedef);
        return silDondurulen;
    }

    /**
     * Silme islemi için gerekli private recursive methodudur.
     * @param lokalkok Recursion method da root olarak sayılır.
     * @param oge Silinecek olan item'dır.
     * @return Yeni node tipi döner.
     */
    private Dugum<E> sil(Dugum<E> lokalkok, E oge) {
        if (lokalkok == null) {
            silDondurulen = null;
            return lokalkok;
        }
        int karsilastirma = oge.compareTo(lokalkok.veri);

        if (karsilastirma < 0) {
            lokalkok.sol = sil(lokalkok.sol, oge);
            return lokalkok;
        }
        else if (karsilastirma > 0) {
            lokalkok.sag = sil(lokalkok.sag, oge);
            return lokalkok;
        }
        else {
            silDondurulen = lokalkok.veri;
            if (lokalkok.sol == null) {
                return lokalkok.sag;
            }
            else if (lokalkok.sag == null) {
                return lokalkok.sol;
            }
            else {
                if (lokalkok.sol.sag == null) {
                    lokalkok.veri = lokalkok.sol.veri;
                    lokalkok.sol = lokalkok.sol.sol;
                    return lokalkok;
                }
                else {
                    lokalkok.veri = enBuyukCocuguBul(lokalkok.sol);
                    return lokalkok;
                }
            }
        }
    }

    /**
     * Ikili agacdaki en büyük child'ı bulacak olan private recursive methoddur.
     * @param veli Node tipi bir elemandır. Her recursive call da update edilir.
     * @return En büyük verinin degerini  döndürür.
     */
    private E enBuyukCocuguBul(Dugum<E> veli) {
        if (veli.sag.sag == null) {
            E dondurulenDeger = veli.sag.veri;
            veli.sag = veli.sag.sol;
            return dondurulenDeger;

        } else {
            return enBuyukCocuguBul(veli.sag);
        }
    }

    /**
     * Bu asamada ihtiyac duyulmadigi icin implement edilmedi.
     *
     * @param hedef aranacak eleman
     * @return true: eger eleman agacta var ise false: eleman agacta yok
     */
    public boolean icerir(E hedef){
        return true;
    }

    /**
     * Bu asamada ihtiyac duyulmadigi icin implement edilmedi.
     *
     * @param hedef agactan cikarilacak eleman
     * @return true: basarili, false: basarisiz islem
     */
    public boolean cikar(E hedef){
        return true;
    }
}