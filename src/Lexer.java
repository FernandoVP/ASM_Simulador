import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Lexer {
	
	// TEXTO CON EL QUE SE VA A TRABAJAR
	private String codigo;
	
	private static Map <String, TokenTipo> palabrasReservadas;
	private static Map <Character, TokenTipo> puntuacion;
	
	static{
		
		palabrasReservadas = new HashMap<String, TokenTipo>();
		palabrasReservadas.put(".datos", TokenTipo.DIRECTIVA);
		palabrasReservadas.put(".texto", TokenTipo.DIRECTIVA);
		palabrasReservadas.put(".globl", TokenTipo.DIRECTIVA);
		palabrasReservadas.put(".entero", TokenTipo.DIRECTIVA);
		palabrasReservadas.put(".cadena", TokenTipo.DIRECTIVA);
		palabrasReservadas.put(".flotante", TokenTipo.DIRECTIVA);
		
		// CONJUNTO DE INSTRUCCIONES
		
		palabrasReservadas.put("sum", TokenTipo.OPERACION); // SUMA
		palabrasReservadas.put("res", TokenTipo.OPERACION); // RESTA
		palabrasReservadas.put("mul", TokenTipo.OPERACION); // MULT
		palabrasReservadas.put("div", TokenTipo.OPERACION); // DIV
		palabrasReservadas.put("sal", TokenTipo.OPERACION); // GOTO
		palabrasReservadas.put("imp", TokenTipo.OPERACION); // IMPRIMIR
		palabrasReservadas.put("cnu", TokenTipo.OPERACION); // CARGAR NUMERO
		palabrasReservadas.put("cst", TokenTipo.OPERACION); // CARGAR STRING
		palabrasReservadas.put("sms", TokenTipo.OPERACION); // SI MAYOR, SALTA
		
		puntuacion = new HashMap<Character, TokenTipo>();
		puntuacion.put(',', TokenTipo.COMA); // COMA
		
	}
	
	// CONSTRUCTOR DE LA CLASE
	public Lexer(String codigo){ this.codigo = codigo; };
	
	public List<Token> getTokens(){
		
		// LISTA CON TOKENS QUE SE ENTREGARA
		List<Token> tokens = new ArrayList<Token>();
		
		int i = 0; // CONTADOR PARA AVANZAR EN LA CADENA
		
		/*
		 * 	AQUI VAMOS A PROCESAR EL CODIGO QUE SE PASO POR EL CONSTRUCTOR
		 */
		
		TOKENIZER:
			
		while(i < codigo.length()){
			
			// FIN DEL CADENA
			// PALABBRA RESERVADA DEL LENGUAJE. FACILITA EL TRABAJO EN EL LEXER
			// NO NOS PREOCUPAMOS POR LLEGAR A EXCEDER EL NUMERO DE ELEMENTOS EN CADENA
			if( codigo.charAt(i) == 'F' ){
				
				if(codigo.charAt(i+1) == 'I'){
					
					if(codigo.charAt(i+2) == 'N'){ 
						
						tokens.add( new Token(TokenTipo.EOF, new TokenAtributo()) );
						break TOKENIZER;
						
					}
					
				}
				
			}
			
			
			/*
			 * 	REVISAMOS TODOS LOS POSIBLES CASOS
			 */
			
			// SI EL CARACTER ES UN ESPACIO EN BLANCO
			if(Character.isWhitespace( codigo.charAt(i) )){ /* DEJALO PASAR */ i++; }
			
			
			/*
			 *  DIRECTIVA: .[a-zA-z]+
			 */
			
			
			// SI EL CARACTER ES UN PUNTO, PODRIA SER DIRECTIVA
			if( codigo.charAt(i) == '.' ){
				
				// SI ES UN PUNTO, VAMOS BIEN
				//String actual = Character.toString( codigo.charAt(i) );
				
				String actual = Character.toString( codigo.charAt(i) );
				i++;
				int j = i;
				
				while( !Character.isWhitespace( codigo.charAt(j) ) ){
					
					if( Character.isLetterOrDigit( codigo.charAt(j) )){
						
						actual += codigo.charAt(j);
						j++;
						
					}else if( !Character.isLetterOrDigit( codigo.charAt(j) )){
						
						tokens.add( new Token(TokenTipo.DESCONOCIDO, new TokenAtributo()) );
						break TOKENIZER;
						
					}
					
				} // FIN CICLO DIRECTIVA
				
				TokenTipo t = palabrasReservadas.get(actual);	
				
				// SI SU VALOR SEMANTICO CORRESPONDE A UNA DIRECTIVA, AGREGALA
				if( t != null ){ 
					
					tokens.add( new Token(TokenTipo.DIRECTIVA, new TokenAtributo()) ); 
					i = j;
					
				}else{ tokens.add(new Token(TokenTipo.DESCONOCIDO, new TokenAtributo()) ); break TOKENIZER; }
				
			}
			
			/*
			 * ETIQUETA: [A-Z]+: 
			 */
			
			
			if( Character.isUpperCase(codigo.charAt(i)) ){
				
				String actual = Character.toString(codigo.charAt(i));
				i++;
				
				int j = i;
				while( Character.isUpperCase(codigo.charAt(j)) ){
					
					actual += codigo.charAt(j);
					j++;
					
					if(actual.equals("FIN")){
						
						tokens.add( new Token(TokenTipo.EOF, new TokenAtributo()) );
						break TOKENIZER;
						
					}
					
				}
				
				if( Character.isWhitespace(codigo.charAt(j)) ){
					
					tokens.add(new Token(TokenTipo.REFID, new TokenAtributo(actual)));
					i = j; // DESPLAZAMOS i
					
				}else if( codigo.charAt(j) == ':' ){
					
					tokens.add(new Token(TokenTipo.ID, new TokenAtributo(actual)));
					i = j+1;
					
				}else{ tokens.add(new Token(TokenTipo.DESCONOCIDO, new TokenAtributo()) ); break TOKENIZER; }
				
			}
			
			/*
			 *  REGISTROS: [$][t | a][0-9]
			 */
			
			if( codigo.charAt(i) == '$' ){
				
				String actual = Character.toString(codigo.charAt(i));
				
				if( codigo.charAt(i+1) == 't' || codigo.charAt(i+1) == 'a' ){
					
					actual += codigo.charAt(i+1);
					
					if( Character.isDigit(codigo.charAt(i+2)) ){
						
						actual += codigo.charAt(i+2);
						tokens.add( new Token(TokenTipo.OPERADOR, new TokenAtributo(actual)) );
						i = i + 3;
						
					}else{ tokens.add(new Token(TokenTipo.DESCONOCIDO, new TokenAtributo()) ); break TOKENIZER; }
					
				}else{ tokens.add(new Token(TokenTipo.DESCONOCIDO, new TokenAtributo()) ); break TOKENIZER; }
				
			}
			
			
			/*
			 *  OPERACION: [a-z][a-z][a-z]
			 */
			
			if( Character.isLowerCase(codigo.charAt(i)) ){
				
				String actual = Character.toString(codigo.charAt(i));
				
				if( Character.isLowerCase(codigo.charAt(i+1)) ){
					
					actual += codigo.charAt(i+1);
					
					if( Character.isLowerCase(codigo.charAt(i+2)) ){
						
						actual += codigo.charAt(i+2);
						
					}else{ tokens.add(new Token(TokenTipo.DESCONOCIDO, new TokenAtributo()) ); break TOKENIZER; }
					
				}else{ tokens.add(new Token(TokenTipo.DESCONOCIDO, new TokenAtributo()) ); break TOKENIZER; }
				
				TokenTipo t = palabrasReservadas.get(actual);
				
				if(t != null){
					
					if( actual.equals("sum") )
						tokens.add(new Token(TokenTipo.SUM, new TokenAtributo()));
					
					else if( actual.equals("res") )
						tokens.add(new Token(TokenTipo.RES, new TokenAtributo()));
					
					else if( actual.equals("mul") )
						tokens.add(new Token(TokenTipo.MUL, new TokenAtributo()));
					
					else if( actual.equals("div") )
						tokens.add(new Token(TokenTipo.DIV, new TokenAtributo()));
					
					else if( actual.equals("imp") )
						tokens.add(new Token(TokenTipo.IMP, new TokenAtributo()));
					
					else if( actual.equals("cnu") )
						tokens.add(new Token(TokenTipo.CNU, new TokenAtributo()));
					
					else if( actual.equals("cst") )
						tokens.add(new Token(TokenTipo.CST, new TokenAtributo()));
					
					else if( actual.equals("sal") )
						tokens.add(new Token(TokenTipo.SAL, new TokenAtributo()));
					
					else if( actual.equals("sms") )
						tokens.add(new Token(TokenTipo.SMS, new TokenAtributo()));
					
					else
						tokens.add(new Token(TokenTipo.DIRECTIVA, new TokenAtributo()));
					i = i + 3; // DESPLAZAMOS i
					
				}else{ tokens.add(new Token(TokenTipo.DESCONOCIDO, new TokenAtributo()) ); break TOKENIZER; }
				
			}
			
			/*
			 *  NUMEROS (ENTEROS O FLOTANTES)
			 *  TANTO ENTEROS COMO FLOTANTES LOS PASO COMO FLOTANTES
			 *  PARA AHORRARME EL TRABAJO DE DIFERENTES INSTRUCCIONES
			 *  PAA CADA TIPO. ES MI ENSAMBLADOR 
			 */
			
			if( Character.isDigit(codigo.charAt(i)) || codigo.charAt(i) == '-' ){
				
				String numero = Character.toString(codigo.charAt(i));
				i++;
				
				int j = i;
				while( Character.isDigit(codigo.charAt(j)) ){
					
					numero += codigo.charAt(j);
					j++;
				}
				
				if( codigo.charAt(j) == '.' ){
					
					numero += codigo.charAt(j);
					j++;
					
					if( !Character.isDigit(codigo.charAt(j)) ){
						
						tokens.add(new Token(TokenTipo.DESCONOCIDO, new TokenAtributo()) ); break TOKENIZER;
						
					}
					
					// CONCATENAR LO QUE RESTA DEL NUMERO
					
					while( Character.isDigit(codigo.charAt(j)) ){
						
						numero += codigo.charAt(j);
						j++;
						
					}	
					
					tokens.add( new Token(TokenTipo.NUMERO, new TokenAtributo(Float.parseFloat(numero))) );
					i = j;
					
				}else{
					
					tokens.add( new Token(TokenTipo.NUMERO, new TokenAtributo(Float.parseFloat(numero))) );
					i = j;
					
				}
				
				
			}
			
			/*
			 *  CADENAS: "[LO QUE SEA]"
			 */
			
			if( codigo.charAt(i) == '"'){
				
				i++;
				String actual = Character.toString(codigo.charAt(i));
				i++;
				
				while( codigo.charAt(i) != '"' ){
					
					actual += codigo.charAt(i);
					i++;
					
				}
				
				tokens.add(new Token(TokenTipo.CADENA, new TokenAtributo(actual)));
				i++;
			}
			
			/*
			 *  COMENTARIOS: #[LO QUE SEA]#
			 */
			
			if( codigo.charAt(i) == '#' ){
				
				i++;
				
				while( codigo.charAt(i) != '#' ){ i++; }
				
				tokens.add(new Token(TokenTipo.COMENTARIOS, new TokenAtributo()));
				i++;
			}
			
			/*
			 *  PUNTUACION: LA UNICA PUNTUACION QUE NOS INTERESA ES LA COMA
			 */
			
			if( codigo.charAt(i) == ',' ){
				
				tokens.add(new Token(TokenTipo.COMA, new TokenAtributo()));
				i++;
				
			}
			
		}
		
		return tokens;
		
	}
}
