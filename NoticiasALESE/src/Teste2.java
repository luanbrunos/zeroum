
public class Teste2 {

	private static boolean estouVivo = true;

	private static int ano = 0;
	
	public static void main(String args[]){
		adicionarMaisAmor();
	}
	
	public static void adicionarMaisAmor(){
		
		if (estouVivo){
			
			System.out.println(ano+"º ano");
			ano++;
			adicionarMaisAmor();
			
		}
		
	}
	
	
}
