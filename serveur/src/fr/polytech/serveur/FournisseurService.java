package fr.polytech.serveur;


public class FournisseurService {
	
	public FournisseurService() {
		// TODO Auto-generated constructor stub
	}
	public  static final float getPrix(String idProduit) {
		if (idProduit.equals("Pomme"))
			return 1.0f;
		else if (idProduit.equals("Orange"))
			return 0.9f;
		else if (idProduit.equals("Abricot"))
			return 0.9f;
		else if (idProduit.equals("Fraise"))
			return 2.0f;
		else
			return 999999.9f;
	}
 
	public static void main(String[] args) {
		
		System.out.println(		FournisseurService.getPrix("1")
);
	}
}
