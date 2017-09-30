package tcc.arquitetura;

import java.util.Date;

import org.apache.log4j.lf5.util.DateFormatManager;

public class Formatador {

	public static String formatarData(Date data){

		DateFormatManager dmf = new DateFormatManager("dd/MM/yyyy");
		return dmf.format(data);
		
	}
}
