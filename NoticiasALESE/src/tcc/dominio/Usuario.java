package tcc.dominio;

import java.util.ArrayList;
import java.util.Collection;

import javax.faces.model.SelectItem;

import tcc.arquitetura.TCCUtils;

public class Usuario {

	private int id;
	private String nome;
	private String email;
	private String senha;
	private float pesoClasse1;
	private float pesoClasse2;
	private float pesoClasse3;
	//Indica se usuário deseja receber emails sobre novas notícias cadastradas
	private boolean receberEmails;
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getNome() {
		return nome;
	}
	
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public float getPesoClasse1() {
		return pesoClasse1*10;
	}
	
	public void setPesoClasse1(float pesoClasse1) {
		this.pesoClasse1 = pesoClasse1/10;
	}
	
	public float getPesoClasse2() {
		return pesoClasse2*10;
	}
	
	public void setPesoClasse2(float pesoClasse2) {
		this.pesoClasse2 = pesoClasse2/10;
	}
	
	public float getPesoClasse3() {
		return 10-getPesoClasse1()-getPesoClasse2();
	}
	
	public void setPesoClasse3(float pesoClasse3) {
		this.pesoClasse3 = 10-getPesoClasse1()-getPesoClasse2();
	}
	
	public Collection<SelectItem> getPesos1(){
		Collection<SelectItem> items = new ArrayList<SelectItem>();
		for (int i = 0; i <= 10; i++)
			items.add(new SelectItem(i));
		return items;
	}
	
	public Collection<SelectItem> getPesos2(){
		Collection<SelectItem> items = new ArrayList<SelectItem>();
		for (int i = 0; i <= 10-getPesoClasse1(); i++)
			items.add(new SelectItem(i));
		return items;
	}
	
	public boolean isReceberEmails() {
		return receberEmails;
	}

	public void setReceberEmails(boolean receberEmails) {
		this.receberEmails = receberEmails;
	}

	public String validarCampos(){
		
		if (TCCUtils.isEmpty(nome))
			return "Nome";
		
		if (TCCUtils.isEmpty(email))
			return "Email";
		
		return null;
	}
	
		
}
