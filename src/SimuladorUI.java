/*
 * 			Simulador de un ensamblador
 * 
 * 					 Grupo 2
 * 				   EPC 2018 - I
 * 			Villanueva Pérez Fernando
 * 			Valerio Álvarez Pablo Angel
 */

// -------- Librerias --------

import javax.swing.*; 
import java.awt.Dimension;

public class SimuladorUI {

	public static void main(String[] args) {
		
		// Ajustamos UI al sistema operativo
		
		try{
		
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			
		}catch(Exception e){ e.printStackTrace(); }
		
		// Construimos la ventana
		
		SimuladorFrame frame = new SimuladorFrame(); // Objeto SimuladorFrame
		frame.setTitle("EPC - Simulador"); // Titulo de JFrame
		frame.setVisible(true);
		frame.setSize(980, 680);
		frame.setMinimumSize(new Dimension(980, 680)); // Dimension minima
		frame.setResizable(true);
		frame.setLocationRelativeTo(null); // Centrado en la pantalla
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
	}

}
