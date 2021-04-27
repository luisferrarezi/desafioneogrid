package controller;

import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class ValidaCampo {
	public boolean validaCampoMontagem(JTextField numeroLinhas) {
		boolean isValido = true;
		
		for (int i = 0; i < numeroLinhas.getText().length(); i++) {
			if (Character.isLetter(numeroLinhas.getText().charAt(i))) {
				JOptionPane.showMessageDialog(null,"N�o � um n�mero a informa��o de linhas.","Processamento",JOptionPane.ERROR_MESSAGE);
				isValido = false;								
				break;
			}
		}
		
		return isValido;
	}
	
	public boolean validaCampoArquivo(JTextField campo) {
		boolean isValido = true;
		
		if (campo.getText().equalsIgnoreCase("")) {
			JOptionPane.showMessageDialog(null,"O local do arquivo n�o foi informado.","Processamento",JOptionPane.ERROR_MESSAGE);
			isValido = false;
		}
		
		return isValido;
	}
}
