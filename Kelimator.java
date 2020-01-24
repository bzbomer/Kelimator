package Grup19Kelimator;

import java.io.*;
import java.util.*;

/**
 * Oyunumuz temel işlevlerini bu classdan sağlar.
 */
public abstract class Kelimator implements KelimeOyunu {

    static boolean oyunKurulu = false; //kurulu bir oyun var ise yeni oyun kurulamaz
    protected IkiliAramaAgaci<String> terimler;
    protected HashMap<String, String> sozluk;
    protected HashMap<String, String> puanlar;
    protected ArrayList<String> kelimelerDizisi;

    /**
     * No parameter constructor
     */
    Kelimator(){
        oyunKurulu = true;
        sozluk = new HashMap<>();
        kelimelerDizisi = new ArrayList<>();
        puanlar = new HashMap<>();
    }

    /**
     * Joker durumuna  göre puan hesabının ayarlamasını yapar.
     * @param jokerDurumu   Joker olup olmadığının bilgisini tutar.
     * @return  Joker durumuna göre puan degeri döndürürüz.
     */
    int puaniHesapla(boolean jokerDurumu){
        if(jokerDurumu)
            return 5;
        else
            return 10;
    }

    /**
     * Random bir harf ile baslayan kelime uretir ve o kelimenin ilk harfini return eder.
     * @param alan Oyun icinde gecerli ilgili alandır.
     * @return  Random üretilen kelimenin ilk harfi degerini döndürür.
     */
    char kelimeJokeri(String alan){
        // agacı 10 kez dolasip rastgele kelime bulup ilk harfini döndürecek

        Random rand = new Random();
        int sayi = rand.nextInt(100);
        IkiliAgac<String> gecici = null;

        // Oyun hangi alanda oynaniyor karar ver. Ona gore alana ait
        // veri tabanini isleme sok.

        gecici = terimler;

        while(0 < sayi){
            if(rand.nextInt(2) == 1) {
                if(Objects.requireNonNull(gecici).solAltAgaciAl() != null) {
                    gecici = gecici.solAltAgaciAl();
                }
                else
                    break;
            }
            else {
                if(Objects.requireNonNull(gecici).sagAltAgaciAl() != null) {
                    gecici = gecici.sagAltAgaciAl();
                }
                else
                    break;
            }
            --sayi;
        }

        return Objects.requireNonNull(gecici).kok.veri.charAt(0);
    }

    /**
     * İlgili alana ait legal bir kelime mi kontrolü yapacak.
     * @param kelime    Girilen kelime
     * @param ilkHarf   Kelimenin ilk harfi kontrolü için gereklidir.
     * @param alan      Alan bilgisidir.
     * @return          Kelime bulunursa true bulunmazsa false döner.
     */
    @Override
    public boolean kelimeDogrumu(String kelime, char ilkHarf, String alan) {

        if(ilkHarf == kelime.toLowerCase().charAt(0)) {
            String temp = terimler.bul(kelime);
            return temp != null;
        }
        else
            return false;
    }

    /**
     * İlgili alana ait kelimeleri dosyadan okuduktan sonra onlara ait olan agaclara atıyor.
     * @param alan  Kullanıcı tarafından belirlenen alandır.
     */
    void kelimeleriEsitle(String alan) {
        String dosyaAdi="";

        dosyaAdi = alan +".csv";
        terimleriDosyadanAl(dosyaAdi, kelimelerDizisi, sozluk);
        String[] dizi =(String[])kelimelerDizisi.toArray(new String[kelimelerDizisi.size()]);
        MergeSort.mergeSort(dizi);
        kelimelerDizisi =new ArrayList<>(Arrays.asList(dizi));
        terimler = agaciOlustur(kelimelerDizisi);
    }

    /**
     *  Kelimeleri dosyadan okuma islemlerini gercekleştiren private methoddur.
     * @param dosyaAdi  alana ait csv file'dır
     * @param kelimeler kelimeleri tutacağımız yapıdır.
     * @param sozluk    kelime ve anlamını sakladıgımız map yapısıdır.
     */
    private void terimleriDosyadanAl(String dosyaAdi, List<String> kelimeler, HashMap<String,String> sozluk) {
        File file = new File(dosyaAdi);
        Scanner scan;
        String terim;
        String anlam;
        try {
            if (file.exists() && !file.isDirectory()) {
                scan = new Scanner(file);
                while(scan.hasNextLine()){
                    String line = scan.nextLine();
                    String[] parts = line.split("-->");
                    terim = parts[0];
                    anlam = parts[1];

                    // Terimlerdeki Turkce karakterler, terimler arasinda String karsilastirmasinin
                    // uygun yapilabilmesi icin Ingilizce karaktere cevrilir.
                    terim = karakterCevirme(terim.toLowerCase());
                    kelimeler.add(terim);

                    sozluk.put(terim,anlam);
                }
                scan.close();
            }
        } catch (IOException ioe) {
            System.out.println("Exception bulundu: ");
            ioe.printStackTrace();
        }
    }

    void puanlariDosyadanAl(String dosyaAdi, HashMap<String,String> puanlar){
        File file = new File(dosyaAdi);
        Scanner scan;
        String isim;
        String puan;
        try {
            if (file.exists() && !file.isDirectory()) {
                scan = new Scanner(file);
                while(scan.hasNextLine()){
                    String line = scan.nextLine();
                    String[] parts = line.split("-->");
                    isim = parts[0];
                    puan = parts[1];
                    puanlar.put(isim,puan);
                }
                scan.close();
            }
        } catch (IOException ioe) {
            System.out.println("Exception bulundu: ");
            ioe.printStackTrace();
        }
    }

    void puaniDegistir(String isim, Integer puan, HashMap<String,String> puanlar){
        // Oyuncu yeni mi?
        if(!puanlar.containsKey(isim))
            puanlar.put(isim,Integer.toString(puan));
        // Eski oyuncudur. Yeni yapmis oldugu puan eski puanindan buyukse buyuk olani
        // eskisi ile degistir.
        else{
            if(puan > (Integer.parseInt(puanlar.get(isim))))
                puanlar.put(isim,Integer.toString(puan));
        }
    }

    /**
     * Turkce- Ingilizce karakter formatını ayarlayan oyunumuz için son derece kullanışlı bir methoddur.
     * @param oge   İlgili kelime yapısıdır.
     * @return  Kelimenin ayarlanmıs haini return ederiz.
     */
    String karakterCevirme(String oge){
        int i = 0;
        boolean degisti = false;
        String duzeltilen = oge;
        while (i < oge.length()){
            char karakter = oge.charAt(i);
            switch (karakter){
                case 'ü' : duzeltilen = duzeltilen.replace('ü','u');
                    degisti = true;
                    break;
                case 'ö' : duzeltilen = duzeltilen.replace('ö','o');
                    degisti = true;
                    break;
                case 'ş' : duzeltilen = duzeltilen.replace('ş','s');
                    degisti = true;
                    break;
                case 'ç' : duzeltilen = duzeltilen.replace('ç','c');
                    degisti = true;
                    break;
                case 'ı' : duzeltilen = duzeltilen.replace('ı','i');
                    degisti = true;
                    break;
                case 'ğ' : duzeltilen = duzeltilen.replace('ğ','g');
                    degisti = true;
                    break;
            }
            i++;
        }
        if(!degisti)
            return oge;
        return duzeltilen;
    }

    /**
     * Yapılarımız arasında gerekli ve önemli bir performans sağlayan ikili arama agacı oluşturan methoddur.
     * @param list  Sakladığımız kelimeleri listesidir.
     * @return  Olusturulan agac yapısı return edilir.
     */
    private IkiliAramaAgaci<String> agaciOlustur(List<String> list){

        if(!list.isEmpty()){

            IkiliAramaAgaci<String> node = new IkiliAramaAgaci<>(list.get(list.size()/2));
            node.solAgaciKur(agaciOlustur(list.subList(0, list.size() / 2)));
            node.sagAgaciKur(agaciOlustur(list.subList(list.size() / 2+1, list.size())));
            return node;
        }
        return null;
    }

    /**
     * Iki kisilik oyunda oyuncularin yapmis oldugu puani ilgili dosyalara kaydeder.
     * Ornegin; felsefe oyunu oynandiysa felsefe_puanlari.csv'ye selcuk-->50 formatinda
     * bir satir yazilacak.
     */
    void puaniKaydet(String dosyaAdi, HashMap<String,String> puanlar){
        FileWriter fw = null;
        BufferedWriter bw = null;
        PrintWriter pw = null;
        try{
            File file = new File(dosyaAdi);
            fw = new FileWriter(file);
            bw = new BufferedWriter(fw);
            pw = new PrintWriter(bw);

            // Map'ten isimleri al. (Key(Anahtar) elemanlar)
            Iterator iterKey = puanlar.keySet().iterator();
            // Map'ten degerleri al. (ValueSet)
            Iterator iterValue = puanlar.values().iterator();

            while (iterKey.hasNext() && iterValue.hasNext()) {
                String temp = (String) iterKey.next();
                // Oyuncu ismi
                pw.print(temp);
                pw.print("-->");
                // Oyuncunun puani
                temp = (String) iterValue.next();
                pw.print(temp);
                pw.print("\n");
            }
            pw.close();
        } catch (IOException ioe) {
            System.out.println("Exception bulundu");
            ioe.printStackTrace();
        } finally {
            try {
                if (pw != null)
                    pw.close();
                if (bw != null)
                    bw.close();
                if (fw != null)
                    fw.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * Oyuncunun yapmis "olmadigi" herhangi bir kelimenin anlami gosterilir..
     * @param hamleHaritasi Hamleleri saklayan arraylist yapımızdır.
     */
    protected String ornekKelimeGoster(ArrayList<String> hamleHaritasi, char ilkHarf) {
        boolean bulundu = false;
        String terim = "";
        int sinir = 10000; // Istenilen harf icin 1000 arama siniri var.
        // Bu sinir ilkHarf ile baslayan ve oyunda girilmemis olan kelime kalmama durumu icin
        int i = 0;
        ArrayList<Integer> indexList = new ArrayList<>();
        Random rand = new Random();
        int size = kelimelerDizisi.size();

        for (i=0; i<size;++i){
            terim = kelimelerDizisi.get(i);
            terim = karakterCevirme(terim);
            if(terim.toLowerCase().charAt(0) == ilkHarf && !hamleHaritasi.contains(terim)) {
                indexList.add(i);
            }
        }
        if(indexList.size()!=0) {
            int sayi = indexList.get(rand.nextInt(indexList.size()));
            return kelimelerDizisi.get(sayi);
        }

        if(sozluk.isEmpty())
            return "Kelimeler okunamadi";
        i=0;
        int  n = rand.nextInt(sozluk.size()-1);
        while(!bulundu) {
            terim = kelimelerDizisi.get(n);
            terim = karakterCevirme(terim);
            if(!hamleHaritasi.contains(terim)) {
                return terim;
            }
        }
        return terim;
    }

    /**
     * Ikikisilik ve TekKisilik siniflarin ayri olarak uyguladigi abstract metotdur.
     */
    public abstract void oyunuOynat();
}