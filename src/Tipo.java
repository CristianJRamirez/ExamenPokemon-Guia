/**
 * Created by Dionis on 30/11/2015.
 */
public class Tipo {

    private Integer idty;
    private String tipus;

    public Tipo(int i, String s) {
        this.idty = i;
        this.tipus = s;
    }

    public Tipo(){}

    public Integer getIdty() {
        return idty;
    }

    public void setIdty(Integer idty) {
        this.idty = idty;
    }

    public String getTipus() {
        return tipus;
    }

    public void setTipus(String tipus) {
        this.tipus = tipus;
    }
}
