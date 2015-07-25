package diplomado.ccm.itesm.practicajson;

/**
 * Created by Laboratorios on 7/25/15.
 */
public class DetalleFichaTecnica {
    private String imgReverso;
    private String imgAnverso;
    private String detalle;

    public DetalleFichaTecnica(){}


    public String getImgReverso() {
        return imgReverso;
    }

    public void setImgReverso(String imgReverso) {
        this.imgReverso = imgReverso;
    }

    public String getImgAnverso() {
        return imgAnverso;
    }

    public void setImgAnverso(String imgAnverso) {
        this.imgAnverso = imgAnverso;
    }

    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }

    @Override
    public String toString() {
        return "DetalleFichaTecnica{" +
                "imgReverso='"    + imgReverso + '\'' +
                ", imgAnverso='"  + imgAnverso + '\'' +
                ", detalle='"       + detalle       + '\'' +
                '}';
    }
}
