package Grup19Kelimator;

import java.util.Scanner;

/**
 * Algoritmayı performanslı kılan ikili agac yapısının classıdır.
 * @param <E> generic tip oldugunu belirten parametredir.
 */
public class IkiliAgac<E> {
    /**
     * Ikili agac Inner classıdır.
     * @param <E> generic tip oldugunu belirten parametredir.
     */
    protected static class Dugum<E> {
        /**
         * Ikili agac yapısındaki her bir node icin ayrılan veri degiskenidir.
         */
        protected E veri;
        /**
         * Ikili agac yapısının sol dugumunu point eder.
         */
        protected Dugum<E> sol;
        /**
         * Ikili agac yapısının sag dugumunu point eder.
         */
        protected Dugum<E> sag;
        /**
         * One parameter constructor
         * @param veri Generic tipde degisken alır.Bunu node'un veri'sine  atar.
         */
        Dugum(E veri) {
            this.veri = veri;
            sol = null;
            sag = null;
        }
        /**
         * Node yapısının veri üyesini print eden methoddur.
         * @return Node'un string representini return eder.
         */
        public String toSting() {
            return veri.toString();
        }
    }

    /**
     * Ikili agacın root node tutulur.
     */
    protected Dugum<E> kok;

    /**
     * Ikili agac no parameter constructor
     */
    IkiliAgac() {
        kok = null;
    }

    /**
     * Ikili agac one parameter constructor
     * @param kok paramater root initialise'ı icin gereklidir.
     */
    protected IkiliAgac(Dugum<E> kok) {
        this.kok = kok;
    }

    /**
     * Ikili agac three parameter constructor
     * @param veri Generic veri tipidir.
     * @param solAgac Sol agac icin root value'dur.
     * @param sagAgac Sag agac icin root value'dur.
     */
    public IkiliAgac(E veri, IkiliAgac<E> solAgac, IkiliAgac<E> sagAgac) {
        kok = new Dugum<>(veri);
        if (solAgac.kok != null)
            kok.sol = solAgac.kok;
        else
            kok.sol = null;

        if (sagAgac.kok != null)
            kok.sag = sagAgac.kok;
        else
            kok.sag = null;
    }

    /**
     * Ikili agacın sol Altagacı referansıdır.
     * @return Sol alt agac root referansıdır.
     */
    public IkiliAgac<E> solAltAgaciAl() {
        if (kok != null && kok.sol != null)
            return new IkiliAgac<>(kok.sol);
        else
            return null;
    }

    /**
     * Ikili agacın sag Altagacı referansıdır.
     * @return Sag alt agac root referansıdır.
     */
    public IkiliAgac<E> sagAltAgaciAl() {
        if (kok != null && kok.sag != null)
            return new IkiliAgac<>(kok.sag);
        else
            return null;
    }

    /**
     * Leaf node durumunu  kontrol eder.
     * @return " Leaf mi degil mi? " sonucunu döndürür.
     */
    public boolean yaprakDugumMu() {
        return (kok.sol == null && kok.sag == null);
    }

    /**
     * Ikili agacın print gösterimidir. Aynı zamanda wrapper methoddur.
     * @return Ikılı agac görüntüsünün bir cesit string gösterimini döndürür.
     */
    public String toString() {
        StringBuilder sb = new StringBuilder();
        toString(kok, 1, sb);
        return sb.toString();
    }

    /**
     * Ikıli agac printi için gerekli recursive methoddur.
     * @param dugum Baslangıc node'udur.
     * @param derinlik Leveli update etmeye yarar.
     * @param sb String represented tipidir.
     */
    private void toString(Dugum<E> dugum, int derinlik, StringBuilder sb) {
        for (int i = 1; i < derinlik; i++) {
            sb.append("  ");
        }
        if (dugum == null) {
            sb.append("bos\n");
        } else {
            sb.append(dugum.veri);
            sb.append("\n");
            toString(dugum.sol, derinlik + 1, sb);
            toString(dugum.sag, derinlik + 1, sb);
        }
    }

    /**
     * Ikılı agacı datalarını input olarak alır.
     * @param scan Scanner classının bir objesidir.
     * @return Datalardan oluşturdugu ikili agac yapısı return eder.
     */
    public static IkiliAgac<String> ikiliAgacOku(Scanner scan) {
        String veri = scan.nextLine().trim();
        if (veri.equals("null"))
            return null;
        else {
            IkiliAgac<String> solAgac = ikiliAgacOku(scan);
            IkiliAgac<String> sagAgac = ikiliAgacOku(scan);
            return new IkiliAgac<>(veri, solAgac, sagAgac);
        }
    }

}