/**
 * Created by dremon on 12/11/15.
 */
public class Pk {
    private String nombre = null;
    private Integer identificador = null;
    private Tipo[] tipos = null;

    public Pk(){}

    public Pk(String nom, String id, String tip[], int id_tip[]){
        this.nombre = nom;
        this.identificador = Integer.parseInt(id);
        tipos = new Tipo[tip.length];
        for (int i = 0; i < tip.length; i++) {
            Tipo tp = new Tipo(id_tip[i],tip[i]);
            this.tipos[i] = tp;
        }
    }


    public Tipo[] getTipos() {
        return tipos;
    }

    public String getNombre() {
        return nombre;
    }

    public Integer getIdentificador(){
        return identificador;
    }

    public void imprimir() {

        System.out.println("-----------------");
        System.out.println("nom: "+this.nombre);
        System.out.println("national id: "+this.identificador);
        for (int i = 0; i < this.tipos.length; i++) {
            System.out.println("\t "+this.tipos[i].getTipus());
        }
        System.out.println("-----------------");

    }
}
