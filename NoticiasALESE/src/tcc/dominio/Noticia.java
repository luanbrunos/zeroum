package tcc.dominio;

import java.io.Serializable;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import tcc.arquitetura.MapeadorClasses;
import tcc.arquitetura.MapeadorDeputado;
import tcc.dados.QopDAO;


public class Noticia implements Serializable{

	
	private int idNoticia;
	
	private String titulo;
	
	private Date data;
	
	private String link;
	
	private String texto;
	
	private int idDeputado;
	
	private int classe;
	
	private int relevancia;
	
	private boolean lida;
	
	private boolean ativo;
	
	public String getTitulo() {
		return titulo;
	}
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	public Date getData() {
		return data;
	}
	public void setData(Date data) {
		this.data = data;
	}
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	public String getTexto() {
		return texto;
	}
	public void setTexto(String texto) {
		this.texto = texto;
	}
	public int getIdNoticia() {
		return idNoticia;
	}
	public void setIdNoticia(int idNoticia) {
		this.idNoticia = idNoticia;
	}
	public int getIdDeputado() {
		return idDeputado;
	}
	public void setIdDeputado(int idDeputado) {
		this.idDeputado = idDeputado;
	}
	public int getClasse() {
		return classe;
	}
	public void setClasse(int classe) {
		this.classe = classe;
	}
	public int getRelevancia() {
		return relevancia;
	}
	public void setRelevancia(int relevancia) {
		this.relevancia = relevancia;
	}
	public boolean isLida() {
		return lida;
	}
	public void setLida(boolean lida) {
		this.lida = lida;
	}
	public boolean isAtivo() {
		return ativo;
	}
	public void setAtivo(boolean ativo) {
		this.ativo = ativo;
	}
	
	public float getRelevanciaMedia() throws ClassNotFoundException, SQLException{
		
		QopDAO qopDao = new QopDAO();
		DecimalFormat aproximador = new DecimalFormat("00.00");
		float media = qopDao.getRelevanciaMediaNoticia(idNoticia);
		
		return Float.parseFloat(aproximador.format(media).replace(',', '.'));
				
	}
	
	public String getNomeDeputado() throws ClassNotFoundException, SQLException{
		MapeadorDeputado mapDep = new MapeadorDeputado();
		return mapDep.getDeputado(idDeputado);
	}

	public String getNomeClasse() throws SQLException, ClassNotFoundException{
		MapeadorClasses mapCla = new MapeadorClasses();
		return mapCla.getClasse(classe);
	}
	
}
