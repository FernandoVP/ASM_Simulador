
public class TokenAtributo {

	private int valorEntero;
	private float valorFlotante;
	private char valorChar;
	private boolean valorBool;
	private String valorString;
	
	// -----  CONSTRUCTORES  -----
	
	public TokenAtributo(){ /* Nada */ }
	
	public TokenAtributo(int valorEntero){ this.valorEntero = valorEntero; }
	
	public TokenAtributo(float valorFlotante){ this.valorFlotante = valorFlotante; }
	
	public TokenAtributo(char valorChar){ this.valorChar = valorChar; }
	
	public TokenAtributo(boolean valorBool){ this.valorBool = valorBool; }
	
	public TokenAtributo(String valorString){ this.valorString = valorString; }
	
	// ----- SET Y GET  -----
	
	public int getEntero(){ return valorEntero; }
	
	public void setEntero(int valorEntero){ this.valorEntero = valorEntero; }
	
	public float getFlotante(){ return valorFlotante; }
	
	public void setFlotante(float valorFlotante){ this.valorFlotante = valorFlotante; }
	
	public char getChar(){ return valorChar; }
	
	public void setChar(char valorChar){ this.valorChar = valorChar; }
	
	public boolean getBool(){ return valorBool; }
	
	public void setBool(boolean valorBool){ this.valorBool = valorBool; }
	
	public String getString(){ return valorString; }
	
	public void setString(String valorString){ this.valorString = valorString; }
}
