
public enum TokenTipo {
	
	ID("ID"), // ETIQUETA 
	OPERACION("OPERACION"), // PALABRAS RESERVADAS (DIRECTIVAS TAMBIEN)
	OPERADOR("OPERADOR"), // REGISTROS
	REFID("REFID"), // REFERENCIAS A ETIQUETAS
	NUMERO("NUMERO"), // ENTERO O FLOTANTE
	CADENA("CADENA"), // CADENAS
	COMENTARIOS("COMENTARIOS"), // COMENTARIOS
	DIRECTIVA("DIRECTIVA"), // DIRECTIVAS
	DESCONOCIDO("DESCONOCIDO"), // DESCONOCIDO
	SUM("SUM"),
	RES("RES"),
	MUL("MUL"),
	DIV("DIV"),
	IMP("IMP"),
	CNU("CNU"),
	CST("CST"),
	SAL("SAL"),
	SMS("SMS"), //SI MAYOR, SALTA 
	
	// -----  PUNTUACION  -----
	
	COMA("COMA"), // ,
	
	// -----  FIN DEL ARCHIVO  -----
	EOF("EOF");
	
	private String nombre;
	
	private TokenTipo(String valor){ nombre = valor; }
	
	public String toString(){return nombre;}
}
