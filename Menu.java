package Grup19Kelimator;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;

public class Menu{
    private JPanel panel1;
    private JButton tekKişilikButton;
    private JButton ikiKişilikButton;
    private JButton kapatButton;
    private JLabel arkaPlan;
    private JButton müziğiKapatButton;
    private JComboBox AlanSecComboBox;
    private JLabel AlanSecLabel;
    private JButton kelimeÖnerButton;
    private JButton enYüksekSkorButton;
    private Heap<Oyuncu> puanTablosu;
    Map<String, String> isimVeSifre = new HashMap<>();

    BufferedImage img;
    boolean muzikCaliyorMu=true;

    Menu(){
        //------------Buton renklendirme---------
        tekKişilikButton.setBackground(Color.PINK);
        ikiKişilikButton.setBackground(Color.PINK);
        müziğiKapatButton.setBackground(Color.YELLOW);
        kapatButton.setBackground(Color.CYAN);
        kelimeÖnerButton.setBackground(Color.LIGHT_GRAY);
        enYüksekSkorButton.setBackground(Color.LIGHT_GRAY);

        //--oyuncular.csv dosyasindaki isim ve sifre bilgisini map'e ekler.
        dosyadanIdAl("oyuncular.csv");

        //--Alanlar.csv'deki verileri combobox'a ekleme
        String fileName = "alanlar.csv";
        File file = new File(fileName);
        try{
            Scanner inputStream = new Scanner(file);
            LinkedList<String> list = new LinkedList<>();
            while (inputStream.hasNext()) {
                list.add(inputStream.nextLine().toLowerCase());
            }
            String[] data =new String[list.size()];
            list.toArray(data);
            MergeSort.mergeSort(data);
            for(int i=0;i<data.length;++i){
                AlanSecComboBox.addItem(data[i]);
            }
            inputStream.close();
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }

        //----ON CLICK LISTENER  tum butonlar icin----
        kapatButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        müziğiKapatButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(muzikCaliyorMu) {
                    Muzik.durdur();
                    müziğiKapatButton.setText("Müziği Oynat");
                }
                else {
                    Muzik.oynat("kelimatorMuzik.wav");
                    müziğiKapatButton.setText("Müziği Kapat");
                }
                muzikCaliyorMu = !muzikCaliyorMu;
            }
        });

        ikiKişilikButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!Kelimator.oyunKurulu) {
                    String oyuncu1Adi, oyuncu2Adi;

                    oyuncu1Adi = oyuncuGiris("Oyuncu1 Adi Giriniz.");

                    if (oyuncu1Adi != null ) {//OK for p1
                        oyuncu2Adi = oyuncuGiris("Oyuncu2 Adi Giriniz.");
                        if (oyuncu2Adi != null ) {
                            if(!oyuncu1Adi.equals(oyuncu2Adi)) {
                                if (muzikCaliyorMu) {
                                    Muzik.durdur();
                                    müziğiKapatButton.setText("Müziği Oynat");
                                }
                                Kelimator oyun = new IkiKisilik(oyuncu1Adi, oyuncu2Adi, AlanSecComboBox.getSelectedItem().toString());
                                oyun.oyunuBaslat();
                            }else {
                                JFrame frame = new JFrame("HATA");
                                JOptionPane.showMessageDialog(frame, "oyuncu1 ve oyuncu2 ayni hesap olamaz");
                            }
                        }
                    }
                }
            }
        });

        tekKişilikButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if(!Kelimator.oyunKurulu) {
                    String oyuncuAdi = oyuncuGiris("Oyuncu Adi Giriniz.");
                    //if OK is pushed then (if not strDialogResponse is null)
                    if (oyuncuAdi != null) {//cancel
                        if (muzikCaliyorMu) {
                            Muzik.durdur();
                            müziğiKapatButton.setText("Müziği Oynat");
                        }
                        Kelimator oyun = new TekKisilik(oyuncuAdi, AlanSecComboBox.getSelectedItem().toString());
                        oyun.oyunuBaslat();
                    }
                }else {
                    JFrame frame = new JFrame("OYUN BİLGİ.");
                    JOptionPane.showMessageDialog(frame, "Zaten bir oyun kurulu");
                }
            }
        });

        kelimeÖnerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String alan = AlanSecComboBox.getSelectedItem().toString();
                String kelime = isimAl("Kelime Giriniz.");
                if(kelime != null && !kelime.equals("")){
                    String anlam = isimAl("Anlamini Giriniz.");
                    if(anlam != null && !anlam.equals(""))
                        oneriAl(alan,kelime,anlam);
                }
            }
        });
        enYüksekSkorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String alan = AlanSecComboBox.getSelectedItem().toString();
                Oyuncu rekortmen = puanTablosuOlustur(alan);
                if(rekortmen==null){
                    JFrame frame = new JFrame("Max skor");
                    JOptionPane.showMessageDialog(frame, alan+" BU ALAN DAHA ONCE HİÇ OYNANMAMIŞ");
                }
                else {
                    System.out.println(alan + " Max skor : " + rekortmen.getIsim() + " " + rekortmen.getPuan());
                    JFrame frame = new JFrame("Max skor");
                    JOptionPane.showMessageDialog(frame, alan + " : " + rekortmen.getIsim() + " " + rekortmen.getPuan());
                }
            }
        });
    }

    public static void main(String []s){
        JFrame jFrame = new JFrame("KELIMATOR");
        jFrame.setSize(500,1000);
        jFrame.setContentPane(new Menu().panel1);
        jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        jFrame.pack();
        jFrame.setVisible(true);
        Muzik.oynat("kelimatorMuzik.wav");
    }

    private String parolaAl(String oyuncuadi){
        JPasswordField parola = new JPasswordField();
        JFrame frame = new JFrame("ŞİFRE EKRANI");
        int returnVal =JOptionPane.showConfirmDialog( frame, parola, oyuncuadi.toUpperCase()+" PAROLA", JOptionPane.OK_CANCEL_OPTION );


        if ( returnVal == JOptionPane.OK_OPTION )
        {
            return new String( parola.getPassword( ) );
        }
        else
        {
            return null;
        }
    }

    private void oneriAl(String alan,String kelime,String anlami){
        String fileName = "onerilenler.csv";
        File file = new File(fileName);
        fileName =alan+".csv";
        File alanFile =new File(fileName);
        ArrayList<String> kelimeler =new ArrayList<>();
        ArrayList<String> onerilenler =new ArrayList<>();
        String[] temp;
        try{
            Scanner scan= new Scanner(alanFile);
            while(scan.hasNext()){
                temp =scan.nextLine().split("-->");
                kelimeler.add(temp[0].toLowerCase());

            }
            scan.close();
        }
        catch(FileNotFoundException exp){

        }
        try{
            Scanner scan= new Scanner(file);
            while(scan.hasNext()){
                temp =scan.nextLine().split("-->");
                onerilenler.add(temp[0].toLowerCase());

            }
            scan.close();
        }
        catch(FileNotFoundException exp){

        }

        try{
            FileWriter writer =new FileWriter(file,true);
            if(!kelimeler.contains(kelime.toLowerCase()) && !onerilenler.contains(alan.toLowerCase()+":"+kelime.toLowerCase())) {
                writer.write(alan + ":" + kelime + "-->" + anlami + "\n");
            }
            writer.close();

        }catch (FileNotFoundException e){
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Oyuncu puanTablosuOlustur(String alan){
        puanTablosu = new Heap<>();
        String fileName = alan+"_puanlari.csv";
        File file = new File(fileName);
        try{
            Scanner inputStream = new Scanner(file);
            Oyuncu oyuncu =null;
            String line;
            String[] data;
            while (inputStream.hasNext()) {
                line =inputStream.nextLine();
                data =line.split("-->");
                oyuncu =new Oyuncu();
                oyuncu.setIsim(data[0]);
                oyuncu.setPuan(Integer.parseInt(data[1]));
                puanTablosu.ekle(oyuncu);
                System.out.println("Okunan : "+oyuncu.getIsim()+" "+oyuncu.getPuan());
            }
            inputStream.close();
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }
        if(puanTablosu.boyut()==0)
            return null;
        else
            return puanTablosu.enYuksekPuaniAl();
    }

    private String oyuncuGiris(String bildiri){
        boolean giris=false;

        String oyuncuAdi = isimAl(bildiri);
        //if OK is pushed then (if not strDialogResponse is null)
        if (oyuncuAdi != null){//cancel
            if (!oyuncuAdi.equals("")){//bos isim

                while(!giris){ //sifre dogru girilene kadar veya cancel denilir ise
                    String sifre = parolaAl(oyuncuAdi);
                    if(sifre==null)
                        return null;
                    if(idUygunMu(oyuncuAdi, sifre)){
                        return oyuncuAdi;
                    }else{
                        JFrame frame = new JFrame("HATA");
                        JOptionPane.showMessageDialog(frame, "HATALI ŞİFRE");
                    }
                }
            }else {
                System.out.println("Isim yeri bos olamaz.");
                return null;
            }
        }
        return null;
    }


    private String isimAl(String bildiri){
        //inputdialog
        JOptionPane inpOption = new JOptionPane();

        //Shows a inputdialog
        String yeniIsim = inpOption.showInputDialog(bildiri);
        return yeniIsim;
    }

    private String sifreAl(String bildiri){
        //inputdialog
        JOptionPane inpOption = new JOptionPane();

        //Shows a inputdialog
        String sifre = inpOption.showInputDialog(bildiri);


        return sifre;
    }

    private void isimKaydet(String isim, String sifre, String dosyaIsmi) {
        try(FileWriter fw = new FileWriter(dosyaIsmi, true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter out = new PrintWriter(bw);) {

            out.println(isim + "-->" + sifre);
        }
        catch (IOException e) {
            System.out.println("Dosya bulunamadı.");
        }
    }

    private void dosyadanIdAl(String dosyaIsmi) {
        File f = new File(dosyaIsmi);
        if(f.exists() && !f.isDirectory()) {
            Scanner scanner = null;
            try {
                scanner = new Scanner(f);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            for(int i = 0; scanner.hasNextLine(); i++){
                String line = scanner.nextLine();
                String[] fields = line.split("-->");
                isimVeSifre.put(fields[0], fields[1]);
            }
            scanner.close();
        }
    }

    private boolean idUygunMu(String isim, String sifre) {
        if(isim.compareTo("") == 0 || sifre.compareTo("") == 0)
            return false;

        if(isimVeSifre.containsKey(isim)) {
            if (isimVeSifre.get(isim).compareTo(sifre) == 0)
                return true;
        }

        if(!isimVeSifre.containsKey(isim)) {
            isimVeSifre.put(isim, sifre);
            isimKaydet(isim, sifre, "oyuncular.csv");
            return true;
        }
        return false;
    }

    private void createUIComponents() {
       tekKişilikButton = new JButton();
       ikiKişilikButton = new JButton();
       kapatButton = new JButton();
       müziğiKapatButton = new JButton();
       kelimeÖnerButton = new JButton();
       enYüksekSkorButton = new JButton();
       panel1 = new JPanel();
       AlanSecComboBox  = new JComboBox();
       arkaPlan = new JLabel(new ImageIcon("kelimatorMenuArkaPlan.jpg"));
    }

}
