package tcc.dominio;

public class QoP {

	private String nomeDeputado;
	private float qopClasse1;
	private float qopClasse2;
	private float qopClasse3;
	private float qopFinal;
	
	public QoP(String nomeDeputado, float qopClasse1, float qopClasse2, float qopClasse3, float qopFinal){
		this.nomeDeputado = nomeDeputado;
		this.qopClasse1 = qopClasse1;
		this.qopClasse2 = qopClasse2;
		this.qopClasse3 = qopClasse3;
		this.qopFinal = qopFinal;
	}
	
	public String getNomeDeputado() {
		return nomeDeputado;
	}
	public void setNomeDeputado(String nomeDeputado) {
		this.nomeDeputado = nomeDeputado;
	}
	public float getQopClasse1() {
		return qopClasse1;
	}
	public void setQopClasse1(float qopClasse1) {
		this.qopClasse1 = qopClasse1;
	}
	public float getQopClasse2() {
		return qopClasse2;
	}
	public void setQopClasse2(float qopClasse2) {
		this.qopClasse2 = qopClasse2;
	}
	public float getQopClasse3() {
		return qopClasse3;
	}
	public void setQopClasse3(float qopClasse3) {
		this.qopClasse3 = qopClasse3;
	}
	public float getQopFinal() {
		return qopFinal;
	}
	public void setQopFinal(float qopFinal) {
		this.qopFinal = qopFinal;
	}
	
}
