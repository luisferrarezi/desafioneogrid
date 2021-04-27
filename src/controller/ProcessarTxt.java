package controller;

import java.io.BufferedReader;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class ProcessarTxt {
	
	private Double minutosProcesso = 0.0;
    private int minuto = 0;
    private String descricao = "";
    private int hora = 9;
		
	public boolean processar(String caminhoArquivo, ArrayList<String> lista) {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(caminhoArquivo));
	        String linha = null;	        
	        
	        while ((linha = reader.readLine()) != null) {
	            lista.add(linha);
	        }
	        reader.close();
	        
	        JOptionPane.showMessageDialog(null,"Processamento realizado com sucesso!","Processamento",JOptionPane.INFORMATION_MESSAGE);
	        
	        return true;	        
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null,"Falhar no processamento! Erro: " + e.getMessage(),"Processamento",JOptionPane.ERROR_MESSAGE);
			return false;
		}
	}
	
	private int retornaMinutos(String texto) {
		String numero = "";
		
		for (int i = 0; i < texto.length(); i++) { 
			if (Character.isDigit(texto.charAt(i))) {
				numero = numero+ texto.charAt(i);
			}
		}
		
		if ((numero == "") && (texto.toLowerCase().indexOf("maintenance") != -1)) {
			numero = "5";
		}
		
		return Integer.parseInt(numero); 
	}
	
	public void preencheGrid(JList lista, JTextField numeroLinhas, ArrayList<String> listaProcessada) {
		try {		
			DefaultListModel modelo = new DefaultListModel();
			SimpleDateFormat sdf = new SimpleDateFormat( "HH:mm" );
            Calendar cal = Calendar.getInstance();    

            int i = 1;
            while(i <= Integer.parseInt(numeroLinhas.getText())) {
            	criarLinhaMontagem(modelo, sdf, cal, listaProcessada, i);
            	i++;
            }            
        		
			lista.setModel(modelo);
		}catch (Exception e) {
			JOptionPane.showMessageDialog(null,"Falha no preenchimento da linha de montagem! Erro: " + e.getMessage(),"Preenchimento",JOptionPane.ERROR_MESSAGE);
		}
	}
	
	private void criarLinhaMontagem(DefaultListModel modelo, SimpleDateFormat sdf, Calendar cal, ArrayList<String> listaProcessada, int linha) {
		hora = 9;
		minutosProcesso = 0.0;
		
		cal.set(Calendar.HOUR_OF_DAY, hora);
		cal.set(Calendar.MINUTE, minuto);
		
		ArrayList<String> linhaMontagem = new ArrayList<>(listaProcessada);
		Collections.shuffle(linhaMontagem);
		modelo.addElement("Linha de montagem " + Integer.toString(linha) + ": ");

		while(linhaMontagem.size() > 0) {
			preencheAlmoço(modelo, sdf, cal);
			
			if ((minutosProcesso/180) != 1) {
				hora = cal.get(Calendar.HOUR_OF_DAY);		
				int index = 0;
				boolean isUltimoHorario = false;
				
				for (int i = 0; i < linhaMontagem.size(); i++) {
					if (isUltimoHorario) {
						i = index;
					}
					
					getInformacoes(linhaMontagem, i);
					
					if (hora < 12) {							
						if (validaAtividade(180.0, linhaMontagem, i)) {
							break;								
						}							
					} else {
						if (validaAtividade(480.0, linhaMontagem, i)) {						
							break;
						} else {
							if ((minutosProcesso/420) > 1) {
								linhaMontagem.remove(i);
								index = i;
								isUltimoHorario = true;
							}
						}
					}	
				}
				
				preencheAtividade(modelo, sdf, cal, linhaMontagem);					
			}	
		}
		
		modelo.addElement(" ");
	}	
	
	private boolean validaAtividade(Double totalHoras, ArrayList<String> listaProcessada, int registro) {
		boolean isValida = false;
		Double minutosRestantes = 0.0;
		
		if ((listaProcessada.size() == registro + 1) && (hora < 12)) {
			listaProcessada.remove(registro);
			isValida = true;
			
			minutosProcesso = minutosProcesso - minuto;
			minutosRestantes = totalHoras - minutosProcesso;
			minutosProcesso = minutosProcesso + minutosRestantes;
			minuto = minutosRestantes.intValue();
		} else {		
			if ((minutosProcesso/totalHoras) <= 1) {
				listaProcessada.remove(registro);
				isValida = true; 
			} else {
				minutosProcesso = minutosProcesso - minuto;
				descricao = "";
				minuto = 0;
			
				isValida = false;
			}
		}
		
		return isValida;
	}
	
	private void preencheAtividade(DefaultListModel modelo, SimpleDateFormat sdf, Calendar cal, ArrayList<String> linhaMontagem) {
		if (!descricao.equalsIgnoreCase("")) {
			modelo.addElement(sdf.format(cal.getTime()) + " " + descricao);
			cal.add(Calendar.MINUTE, minuto);
		}
		
		if (linhaMontagem.size() == 0) {
			modelo.addElement(sdf.format(cal.getTime()) + " Ginástica Laboral");
		}		
	}
	
	private void preencheAlmoço(DefaultListModel modelo, SimpleDateFormat sdf, Calendar cal) {
		if ((minutosProcesso/180) == 1) {
			modelo.addElement(sdf.format(cal.getTime()) + " Almoço");
			cal.add(Calendar.MINUTE, 60);
			minutosProcesso = minutosProcesso + 60;
		}
	}
	
	private void getInformacoes(ArrayList<String> linhaMontagem, int registro) {
		minuto = retornaMinutos(linhaMontagem.get(registro).toString());
		descricao = linhaMontagem.get(registro).toString();						
		minutosProcesso = minutosProcesso + minuto;		
	}
}
