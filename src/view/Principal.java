package view;

import java.awt.event.*;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.filechooser.FileFilter;
import javax.swing.*;
import controller.ProcessarTxt;
import controller.ValidaCampo;

public class Principal extends JFrame {

	public static void main(String[] args) {
		Principal exemplo = new Principal();
        exemplo.setVisible(true); 
	}
 	
        private JButton botaoBuscar;
	    private JButton botaoProcessar;	    
	    private JTextField campo;
	    private JLabel tituloCampo;
	    private JTextField numeroLinhas;
	    private JLabel tituloLinhas;	    
	    private JList lista;
	    private ArrayList<String> listaProcess;

	    public Principal() {
	        this.setTitle("Desafio NeoGrid");
	        this.setBounds(0, 0, 565, 600);
	        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
	        this.getContentPane().setLayout(null);	        
	        
	        tituloCampo = new JLabel();
	        tituloCampo.setText("Selecione o arquivo que deseja importar:");
	        tituloCampo.setBounds(10, 20, 300, 30);
	        this.add(tituloCampo);        
	        
	        campo = new JTextField();
	        campo.setBounds(10, 50, 500, 30);
	        campo.setEditable(false);
	        this.add(campo);
	        
	        botaoBuscar = new JButton();
	        botaoBuscar.setText("...");
	        botaoBuscar.setBounds(510, 50, 30, 29);
	        this.add(botaoBuscar);        

	        botaoBuscar.addActionListener(new ActionListener() {
	            public void actionPerformed(ActionEvent evt) {
	                JFileChooser abrir = new JFileChooser();
	                abrir.setFileFilter(new FileFilter() {
						
						@Override
						public String getDescription() {
							return "input.txt";
						}
						
						@Override
						public boolean accept(File f) {						
							return (f.getName().endsWith("input.txt")) || f.isDirectory();
						}
					});
	                int retorno = abrir.showOpenDialog(null);  
	                           if (retorno==JFileChooser.APPROVE_OPTION)  
	                        	   campo.setText(abrir.getSelectedFile().getAbsolutePath());                
	            }
	        });
	        
	        lista = new JList();
	        JScrollPane painelLista = new JScrollPane(lista);
	        painelLista.setBounds(10, 150, 530, 400);
	        this.add(painelLista);
	        
	        tituloLinhas = new JLabel();
	        tituloLinhas.setText("Qtd Linha Montagem:");
	        tituloLinhas.setBounds(10, 75, 300, 30);
	        this.add(tituloLinhas);        
	        
	        numeroLinhas = new JTextField();
	        numeroLinhas.setBounds(10, 100, 120, 30);	
	        numeroLinhas.setText("1");
	        this.add(numeroLinhas);	        
	        
	        botaoProcessar = new JButton();
	        botaoProcessar.setText("Processar arquivo");
	        botaoProcessar.setBounds(150, 100, 160, 30);
	        this.add(botaoProcessar);	        
	        
	        listaProcess = new ArrayList<>();
	        
	        botaoProcessar.addActionListener(new ActionListener() {
	            public void actionPerformed(ActionEvent evt) {
	            	ValidaCampo validaCampo = new ValidaCampo();
	            	
	            	if (validaCampo.validaCampoMontagem(numeroLinhas) &&
	            		validaCampo.validaCampoArquivo(campo)) {	            	
	            		boolean processado = false;
	            	
	            		botaoBuscar.setEnabled(processado);
	            		botaoProcessar.setEnabled(processado);
	            		numeroLinhas.setEnabled(processado);
	            	
	            		listaProcess.clear();
	            	
	            		ProcessarTxt importar = new ProcessarTxt();
	            		processado = importar.processar(campo.getText(), listaProcess);
	            		importar.preencheGrid(lista, numeroLinhas, listaProcess);
	            	
	            		botaoBuscar.setEnabled(processado);
	            		botaoProcessar.setEnabled(processado);
	            		numeroLinhas.setEnabled(processado);
	            	}
	            }
	        });                

	    } 	

}
