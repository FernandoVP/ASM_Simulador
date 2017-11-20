import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;


@SuppressWarnings("serial")
public class SimuladorFrame extends JFrame{
	
	// -------- UTILIZADOS EN PARSER --------
	
	public Float uno = new Float(0.0);
	public Float dos = new Float(0.0);
	public Float op = new Float(0.0);
	
	// -------- Paneles principales de la UI --------
	
	private JPanel PanelCodigo, PanelSalida, PanelRegistros;
	
	// -------- Etiquetas en paneles --------
	
	private TitledBorder codigo, salida, registros;
	
	// -------- Barra superior para menu --------
	
	private JMenuBar Barra_Menu;
	private JMenu Archivo, Ensamblador;
	private JMenuItem Nuevo, Abrir, Guardar;
	private JMenuItem Ensamblar, Ejecutar, Ejecutar_Paso;
	private JFileChooser seleccion, guardarComo;
	
	// -------- Elementos para tabla de registros --------
	
	private JScrollPane ContenedorTabla;
	private JTable Registros;
	private String Columnas[] = {"Nombre", "Numero", "Valor"};
	public Object Datos[][] = { {"$t0", "0", "0x00000000"},
								{"$t1", "1", "0x00000000"},
								{"$t2", "2", "0x00000000"},
								{"$t3", "4", "0x00000000"},
								{"$t4", "5", "0x00000000"},
								{"$t5", "6", "0x00000000"},
								{"$t6", "7", "0x00000000"},
								{"$t7", "8", "0x00000000"},
								{"$t8", "9", "0x00000000"},
								{"$t9", "10", "0x00000000"},};
	
	public DefaultTableModel modelo;
	
	// -------- Elementos para editar codigo --------
	
	private JScrollPane ContenedorEditor;
	private JTextArea editor;
	
	// -------- Elementos para salida --------
	
	private JScrollPane ContenedorEjecucion;
	private JTextArea ejecucion;
	
	// -------- Constructor de la clase --------
	
	public SimuladorFrame(){
		
		setLayout(new BorderLayout()); // Layout de JFrame
		
		Barra_Menu = new JMenuBar(); // MENU
		
		// ---------- MENU -> Archivo ----------
		
		Archivo = new JMenu("Archivo"); 
		
		Nuevo = new JMenuItem("Nuevo", new ImageIcon(this.getClass().getResource("/nuevo.png"))); // MENU -> Archivo -> Nuevo
		
		// ****************************************
		//  EVENTO AL HACER CLIC EN 'NUEVO'
		// ****************************************
		
		Nuevo.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				
				String s = editor.getText(); // Texto del area de codigo
				
				// Si no hay nada en el area de codigo, continuar
				if( s.equals("") )
					editor.setText("");
				
				// Si hay algo escrito en el area de codigo, preguntar si esta seguro
				// de abrir un archivo nuevo
				else{
					
					int n = JOptionPane.showOptionDialog(null,
													"Hay un archivo abierto.\n\n¿Continuar?", 
													"Aviso",
													JOptionPane.YES_NO_OPTION,
													JOptionPane.QUESTION_MESSAGE,
													null,
													null, 
													null);
					
					if( n == JOptionPane.YES_OPTION)
						editor.setText("");
					
				}
				
			}
			
		});
		
		
		Archivo.add(Nuevo);
		
		Abrir = new JMenuItem("Abrir", new ImageIcon(this.getClass().getResource("/abrir.png"))); // MENU -> Archivo -> Abrir
		
		// ****************************************
		//  EVENTO AL HACER CLIC EN 'NUEVO'
		// ****************************************
		
		Abrir.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				
				if( seleccion == null ){
					
					seleccion = new JFileChooser();
					seleccion.setFileSelectionMode(JFileChooser.FILES_ONLY);
					seleccion.setAcceptAllFileFilterUsed(false);
					seleccion.addChoosableFileFilter(new FileNameExtensionFilter("Archivos", "txt"));
					
				}
				
				switch(seleccion.showOpenDialog(SimuladorFrame.this)){
				
				case JFileChooser.APPROVE_OPTION:
					
					try(BufferedReader br = new BufferedReader(new FileReader(seleccion.getSelectedFile()))){
						
						editor.setText(null);
						String texto = null;
						
						while((texto = br.readLine()) != null){
							editor.append(texto);
							editor.append("\n");
						}
						editor.setCaretPosition(0);
					}catch(IOException ex){
						
						ex.printStackTrace();
					}
					break;
				
				}
				
			}
			
		});
		
		Archivo.add(Abrir);
		
		Guardar = new JMenuItem("Guardar", new ImageIcon(this.getClass().getResource("/guardar.png"))); // MENU -> Archivo -> Abrir
		
		Guardar.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				
				guardarComo = new JFileChooser();
				guardarComo.setApproveButtonText("Guardar");
				
				int dialogo = guardarComo.showOpenDialog(SimuladorFrame.this);
				
				if(dialogo == JFileChooser.APPROVE_OPTION){
					
					File doc = new File(guardarComo.getSelectedFile() + ".txt");
					BufferedWriter salida = null; // 'Escritor'
					
					try{
						
						salida = new BufferedWriter(new FileWriter(doc)); // 'Escritor'
						editor.write(salida); // Escribimos archivo
						
					}catch(IOException ex){ ex.printStackTrace(); }
					finally{
						
						if(salida != null){
							
							try{ salida.close(); } // Finalizamos escritor
							catch(IOException e){ e.printStackTrace(); }
							
						}
						
					}
					
				}
				
			}
			
		});
				
		Archivo.add(Guardar);
		
		Barra_Menu.add(Archivo);
		
		// ---------- MENU -> Ensamblador ----------
		
		Ensamblador = new JMenu("Ensamblador"); // MENU -> Ensamblador
		
		Ensamblar = new JMenuItem("Ensamblar y ejecutar", new ImageIcon(this.getClass().getResource("/ensamblar.png"))); // MENU -> Ensamblador -> Ensamblar
		
		Ensamblar.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				
				//COPIA DE SEGURIDAD DEL ARCHIVO A ENSAMBLAR
				Writer escritor = null;
				
				try{
					
					escritor = new BufferedWriter(new OutputStreamWriter(
							new FileOutputStream("ScopiaS.txt"), "utf-8"));
					
					escritor.write(editor.getText());
					
				}catch(Exception ez){ JOptionPane.showMessageDialog(null, "Algo salió mal..."); }
				finally{ try{ escritor.close(); }catch(Exception ex){ /* NADA */} }
				
				// CODIGO ESCRITO EN EL ARCHIVO
				String codigo = null;
				
				try{
					
					codigo = leerArchivo("ScopiaS.txt", StandardCharsets.UTF_8);
					
				}catch(IOException ez){ JOptionPane.showMessageDialog(null, "Algo salió mal..."); }
				
				Lexer lexer = new Lexer(codigo);
				
				List<Token> tokens = new ArrayList<Token>();
				
				// LEXER.
				tokens = lexer.getTokens();
				
				// LE FALTA BASTANTE AL 'PARSER' ACTUAL PARA FUNCIONAR ADECUADAMENTE
				// IMPLEMENTAREMOS UNO PROVICIONAL PARA QUE EJECUTE ALGUNAS INSTRUCCIONES
				
				int i = 0;
				int j = 0;
				int z = 0;
				
				Map<String, Float> numeros = new HashMap<String, Float>();
				Map<String, String> cadenas = new HashMap<String, String>();
				
				/*
				 * AQUI ESTA LA EJECUCION.
				 */
				
				int errorLexico = 0;
				
				ERROR:
				while( z < tokens.size() ){
					
					if( tokens.get(z).getTipo() == TokenTipo.DESCONOCIDO ){
						
						errorLexico = 1;
						break ERROR;
						
					}
						
					
					z++;
					
				}
				
				
				if( errorLexico == 0){
				
				PARSER:
				while( i < tokens.size() ){
					
					// FIN DE TOKENS
					if( tokens.get(i).getTipo() == TokenTipo.EOF ){
						
						break PARSER;
						
					}
					
					// -----  ASIGNACIONES  -----
					
					// NUMEROS
					if( tokens.get(i).getTipo() == TokenTipo.CNU ){
						
						if( tokens.get(i+1).getTipo() == TokenTipo.OPERADOR ){
							
							if(tokens.get(i+2).getTipo() == TokenTipo.COMA){
								
								if(tokens.get(i+3).getTipo() == TokenTipo.NUMERO){
									
									/* AQUI LLEGA LA EJECUCION */
									numeros.put(tokens.get(i+1).getAtributo().getString(), tokens.get(i+3).getAtributo().getFlotante());
									
									for(j = 0; j < 10; j++){
										
										if( Datos[j][0].equals(tokens.get(i+1).getAtributo().getString()) )
											Datos[j][2] = tokens.get(i+3).getAtributo().getFlotante() ;
									}
									
									try{
										
										modelo = (DefaultTableModel)Registros.getModel();
										modelo.fireTableDataChanged();
										
									}catch(Exception o){ /* Ignorar */ }
									
									i += 3;
								}
								
							}
							
						}
						
					}
					
					// CADENAS
					
					if( tokens.get(i).getTipo() == TokenTipo.CST ){
						
						if( tokens.get(i+1).getTipo() == TokenTipo.OPERADOR ){
							
							if( tokens.get(i+2).getTipo() == TokenTipo.COMA ){
								
								if(tokens.get(i+3).getTipo() == TokenTipo.CADENA){
									
									/* AQUI LLEGA LA EJECUCION */
									cadenas.put(tokens.get(i+1).getAtributo().getString(), tokens.get(i+3).getAtributo().getString());
									
									for(j = 0; j < 10; j++){
										
										if( Datos[j][0].equals(tokens.get(i+1).getAtributo().getString()) )
											Datos[j][2] = tokens.get(i+3).getAtributo().getString() ;
									}
									
									try{
										
										modelo = (DefaultTableModel)Registros.getModel();
										modelo.fireTableDataChanged();
										
									}catch(Exception o){ /* Ignorar */ }
									
									i += 4;
								}
								
							}
							
						}
						
					}
					
					// IMPRESION EN CONSOLA
					
					if( tokens.get(i).getTipo() == TokenTipo.IMP ){
						
						if(tokens.get(i+1).getTipo() == TokenTipo.CADENA){
							
							ejecucion.append(tokens.get(i+1).getAtributo().getString() + "\n");
							i += 1;
						}
						
					}
					
					// IMPRESION DE NUMEROS EN CONSOLA
					
					if( tokens.get(i).getTipo() == TokenTipo.IMP ){
						
						if(tokens.get(i+1).getTipo() == TokenTipo.NUMERO){
							
							ejecucion.append(Float.toString(tokens.get(i+1).getAtributo().getFlotante()) + "\n");
							i += 1;
						}
						
					}
					
					// IMPRESION DE VARIABLES EN CONSOLA
					
					if( tokens.get(i).getTipo() == TokenTipo.IMP ){
						
						if( tokens.get(i+1).getTipo() == TokenTipo.OPERADOR ){
							
							if( numeros.keySet().contains(tokens.get(i+1).getAtributo().getString() )){
								
								ejecucion.append(Float.toString(numeros.get(tokens.get(i+1).getAtributo().getString())) + "\n");
								i += 1;
								
							}else if( cadenas.keySet().contains(tokens.get(i+1).getAtributo().getString()) ){
								
								ejecucion.append(cadenas.get(tokens.get(i+1).getAtributo().getString()) + "\n");
								i += 1;
							}
							
						}
						
					}
					
					// SUMA SOLO CON REGISTROS
					
					if( tokens.get(i).getTipo() == TokenTipo.SUM ){
						
						if( tokens.get(i+1).getTipo() == TokenTipo.OPERADOR ){
							
							if( tokens.get(i+2).getTipo() == TokenTipo.COMA){
								
								if( tokens.get(i+3).getTipo() == TokenTipo.OPERADOR ){
									
									if( tokens.get(i+4).getTipo() == TokenTipo.COMA ){
										
										if( tokens.get(i+5).getTipo() == TokenTipo.OPERADOR ){
											
											uno = numeros.get(tokens.get(i+1).getAtributo().getString());
											dos = numeros.get(tokens.get(i+3).getAtributo().getString());
											
											op = uno + dos;
											
											numeros.put(tokens.get(i+5).getAtributo().getString(), op);
											
											for(j = 0; j < 10; j++){
												
												if( Datos[j][0].equals(tokens.get(i+5).getAtributo().getString()) )
													Datos[j][2] = op ;
											}
											
											try{
												
												modelo = (DefaultTableModel)Registros.getModel();
												modelo.fireTableDataChanged();
												
											}catch(Exception o){ /* Ignorar */ }
											
											i += 5;
											
										}
										
									}
									
								}
								
							}
							
						}
						
					}
					
					// RESTA CON SOLO REGISTROS
					
					if( tokens.get(i).getTipo() == TokenTipo.RES ){
						
						if( tokens.get(i+1).getTipo() == TokenTipo.OPERADOR ){
							
							if( tokens.get(i+2).getTipo() == TokenTipo.COMA){
								
								if( tokens.get(i+3).getTipo() == TokenTipo.OPERADOR ){
									
									if( tokens.get(i+4).getTipo() == TokenTipo.COMA ){
										
										if( tokens.get(i+5).getTipo() == TokenTipo.OPERADOR ){
											
											uno = numeros.get(tokens.get(i+1).getAtributo().getString());
											dos = numeros.get(tokens.get(i+3).getAtributo().getString());
											
											op = uno - dos;
											
											numeros.put(tokens.get(i+5).getAtributo().getString(), op);
											
											for(j = 0; j < 10; j++){
												
												if( Datos[j][0].equals(tokens.get(i+5).getAtributo().getString()) )
													Datos[j][2] = op ;
											}
											
											try{
												
												modelo = (DefaultTableModel)Registros.getModel();
												modelo.fireTableDataChanged();
												
											}catch(Exception o){ /* Ignorar */ }
											
											i += 5;
											
										}
										
									}
									
								}
								
							}
							
						}
						
					}
					
					// MULTIPLICACION CON SOLO REGISTROS
					
					if( tokens.get(i).getTipo() == TokenTipo.MUL ){
						
						if( tokens.get(i+1).getTipo() == TokenTipo.OPERADOR ){
							
							if( tokens.get(i+2).getTipo() == TokenTipo.COMA){
								
								if( tokens.get(i+3).getTipo() == TokenTipo.OPERADOR ){
									
									if( tokens.get(i+4).getTipo() == TokenTipo.COMA ){
										
										if( tokens.get(i+5).getTipo() == TokenTipo.OPERADOR ){
											
											uno = numeros.get(tokens.get(i+1).getAtributo().getString());
											dos = numeros.get(tokens.get(i+3).getAtributo().getString());
											
											op = uno * dos;
											
											numeros.put(tokens.get(i+5).getAtributo().getString(), op);
											
											for(j = 0; j < 10; j++){
												
												if( Datos[j][0].equals(tokens.get(i+5).getAtributo().getString()) )
													Datos[j][2] = op ;
											}
											
											try{
												
												modelo = (DefaultTableModel)Registros.getModel();
												modelo.fireTableDataChanged();
												
											}catch(Exception o){ /* Ignorar */ }
											
											i += 5;
											
										}
										
									}
									
								}
								
							}
							
						}
						
					}
					
					// DIVISION CON SOLO REGISTROS
					
					if( tokens.get(i).getTipo() == TokenTipo.DIV ){
						
						if( tokens.get(i+1).getTipo() == TokenTipo.OPERADOR ){
							
							if( tokens.get(i+2).getTipo() == TokenTipo.COMA){
								
								if( tokens.get(i+3).getTipo() == TokenTipo.OPERADOR ){
									
									if( tokens.get(i+4).getTipo() == TokenTipo.COMA ){
										
										if( tokens.get(i+5).getTipo() == TokenTipo.OPERADOR ){
											
											uno = numeros.get(tokens.get(i+1).getAtributo().getString());
											dos = numeros.get(tokens.get(i+3).getAtributo().getString());
											
											op = uno / dos;
											
											numeros.put(tokens.get(i+5).getAtributo().getString(), op);
											
											for(j = 0; j < 10; j++){
												
												if( Datos[j][0].equals(tokens.get(i+5).getAtributo().getString()) )
													Datos[j][2] = op ;
											}
											
											try{
												
												modelo = (DefaultTableModel)Registros.getModel();
												modelo.fireTableDataChanged();
												
											}catch(Exception o){ /* Ignorar */ }
											
											i += 5;
											
										}
										
									}
									
								}
								
							}
							
						}
						
					}
					
					// SALTOS
					
					if( tokens.get(i).getTipo() == TokenTipo.SAL ){
						
						if( tokens.get(i+1).getTipo() == TokenTipo.REFID ){
							
							int r = 0;
							String et = tokens.get(i+1).getAtributo().getString();
							
							SALTAR:
							while( r < tokens.size() ){
								
								if( tokens.get(r).getTipo() == TokenTipo.ID ){
									
									if( tokens.get(r).getAtributo().getString().equals(et) ){
										
										i = r;
										break SALTAR;
										
									}
									
								}
								
								r++;
								
							}
							
							if( r == tokens.size() ){
								
								ejecucion.append("Referencia a etiqueta no existente. \n"); 
								break PARSER;
								
							}
							
						}
						
					}
					
					// SI ES MAYOR, SALTA
					
					if( tokens.get(i).getTipo() == TokenTipo.SMS ){
						
						if( tokens.get(i+1).getTipo() == TokenTipo.OPERADOR ){
							
							if( tokens.get(i+2).getTipo() == TokenTipo.COMA ){
								
								if( tokens.get(i+3).getTipo() == TokenTipo.OPERADOR ){
									
									if( tokens.get(i+4).getTipo() == TokenTipo.COMA){
										
										if( tokens.get(i+5).getTipo() == TokenTipo.REFID ){
											
											uno = numeros.get(tokens.get(i+1).getAtributo().getString());
											dos = numeros.get(tokens.get(i+3).getAtributo().getString());
											
											
											
											if( uno > dos ){
												
												
												int k = 0;
												String et = tokens.get(i+5).getAtributo().getString();
												
												SALTAR:
												while( k < tokens.size() ){
													
													if( tokens.get(k).getTipo() == TokenTipo.ID ){
														
														if( tokens.get(k).getAtributo().getString().equals(et) ){
															
															i = k;
															break SALTAR;
															
														}
														
													}
													
													k++;
													
												}
												
												if( k == tokens.size() ){
													
													ejecucion.append("Referencia a etiqueta no existente. \n"); 
													break PARSER;
													
												}
												
												
											}/* EN CASO CONTRARIO, NO HAGAS NADA */
											
										}
										
									}
									
								}
								
							}
							
						}
						
					}
					
					i++;
					
				}
				
				}else{ ejecucion.append("Se detectó un error léxico"); }
				
			
		}});
		
		Ensamblador.add(Ensamblar);
		
		Ejecutar = new JMenuItem("Ejecutar", new ImageIcon(this.getClass().getResource("/ejecutar.png"))); // MENU -> Ensamblar -> Ejecutar
		Ejecutar.setEnabled(false); // Desactivado. Primero ensamblar
		//Ensamblador.add(Ejecutar);
		
		Ejecutar_Paso = new JMenuItem("Ejecutar paso a paso", new ImageIcon(this.getClass().getResource("/ejecutar.png"))); // MENU -> Ensamblar -> Ejecutar paso a paso
		Ejecutar_Paso.setEnabled(false); // Desactivado. Primero ensamblar
		Ensamblador.add(Ejecutar_Paso);
		
		Barra_Menu.add(Ensamblador);
		
		setJMenuBar(Barra_Menu); // Agregamos barra a JFrame
		
		
		// -------------------- FIN BARRA DE MENU --------------------
		
		//*************************************************************
		//* 		Construccion de los contenedores
		//*************************************************************
		
		// -------------------- CONTENEDOR DE CODIGO --------------------
	    
		
		PanelCodigo = new JPanel();
		
		// Layout del editor. Conviene usar BorderLayout para que
		// se autoajuste la caja de texto
		
		PanelCodigo.setLayout(new BorderLayout()); 
		
		codigo = new TitledBorder("Editar");
		codigo.setTitleJustification(TitledBorder.LEFT);
		codigo.setTitlePosition(TitledBorder.TOP);
		
		editor = new JTextArea(23, 84); // Base x Altura
		editor.setEditable(true);
		
		ContenedorEditor = new JScrollPane(editor);
		
		PanelCodigo.setBorder(codigo);
		PanelCodigo.add(ContenedorEditor);
		
		add(PanelCodigo, BorderLayout.CENTER);
		
		// -------------------- CONTENEDOR DE REGISTROS --------------------
		
		PanelRegistros = new JPanel();
		//PanelRegistros.setLayout(new BorderLayout());
		
		registros = new TitledBorder("Registros");
		registros.setTitleJustification(TitledBorder.LEFT);
		registros.setTitlePosition(TitledBorder.TOP);
		
		Registros = new JTable(Datos, Columnas);
		
		ContenedorTabla = new JScrollPane(Registros);
		Registros.setPreferredScrollableViewportSize(Registros.getPreferredSize());
		Registros.setFillsViewportHeight(true);
		
		PanelRegistros.setBorder(registros);
		PanelRegistros.add(ContenedorTabla);
		//PanelRegistros.add(southButton);
		
		add(PanelRegistros, BorderLayout.LINE_END);
		
		// -------------------- CONTENEDOR DE EJECUCION --------------------
		
		PanelSalida = new JPanel();
		
		// Layout del editor. Conviene usar BorderLayout para que
		// se autoajuste la caja de texto
		
		PanelSalida.setLayout(new BorderLayout());
		
		salida = new TitledBorder("Ejecución");
		salida.setTitleJustification(TitledBorder.LEFT);
		salida.setTitlePosition(TitledBorder.TOP);
		
		ejecucion = new JTextArea(7, 115); // Base x Altura
		ejecucion.setEditable(false);
		
		ContenedorEjecucion = new JScrollPane(ejecucion);
		
		PanelSalida.setBorder(salida);
		PanelSalida.add(ContenedorEjecucion);
		
		add(PanelSalida, BorderLayout.PAGE_END);
		
		
		// -------------------- FIN INTERFAZ GRAFICA --------------------
		
	}
	 
	// -------------------- METODO PARA LEER ARCHIVOS --------------------
	
	static String leerArchivo(String ruta, Charset encoding) throws IOException {
		
		byte[] encoded = Files.readAllBytes(Paths.get(ruta));
		return new String(encoded, encoding);
		
	}
	
}
