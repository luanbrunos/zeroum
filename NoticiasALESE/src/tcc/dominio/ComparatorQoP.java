package tcc.dominio;

import java.util.Comparator;

public class ComparatorQoP implements Comparator<QoP>{

	int campo;
	
	public ComparatorQoP(int campo){
		this.campo = campo;
	}
	
	@Override
	public int compare(QoP arg0, QoP arg1) {
		
		Float f1 = (float) 0;
		Float f2 = (float) 0;
		
		switch(campo){
			case 0:{
				f1 = arg0.getQopFinal();
				f2 = arg1.getQopFinal();
				break;
			}
			case 1:{
				f1 = arg0.getQopClasse1();
				f2 = arg1.getQopClasse1();
				break;
			}
			case 2:{
				f1 = arg0.getQopClasse2();
				f2 = arg1.getQopClasse2();
				break;
			}
			case 3:{
				f1 = arg0.getQopClasse3();
				f2 = arg1.getQopClasse3();
				break;
			}
		}
		//invertido para gerar ordem decrescente
		return f2.compareTo(f1);
		
	}


}
