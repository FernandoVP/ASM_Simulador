
public class Token {
	
	private TokenTipo tipo;
	private TokenAtributo atributo;
	
	public Token(TokenTipo tipo, TokenAtributo atributo){
		
		this.tipo = tipo;
		this.atributo = atributo;
		
	}
	
	public TokenTipo getTipo(){ return tipo; }
	
	public TokenAtributo getAtributo(){ return atributo; }
	
}
