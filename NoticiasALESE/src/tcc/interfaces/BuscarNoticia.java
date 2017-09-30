package tcc.interfaces;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JComboBox;

import tcc.arquitetura.Formatador;
import tcc.arquitetura.MapeadorClasses;
import tcc.arquitetura.MapeadorDeputado;
import tcc.dominio.Noticia;
import tcc.negocio.NoticiaMBean;

import javax.swing.JButton;

import org.primefaces.component.calendar.Calendar;

import com.toedter.calendar.JDateChooser;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Component;
import java.awt.List;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class BuscarNoticia extends JFrame {

	private JPanel contentPane;
	private JTextField textFieldTitulo;
	private JTextField textFieldTexto;
	
	
	private NoticiaMBean noticiaMBean;
	
	/**
	 * Create the frame.
	 * @throws SQLException 
	 * @throws ClassNotFoundException 
	 */
	public BuscarNoticia() throws ClassNotFoundException, SQLException {
		
		noticiaMBean = new NoticiaMBean();
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 795, 502);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(null);
		setContentPane(contentPane);
		
		
		JLabel lblTtulo = new JLabel("T\u00EDtulo:");
		lblTtulo.setBounds(94, 21, 46, 14);
		contentPane.add(lblTtulo);
		
		JLabel lblTexto = new JLabel("Texto:");
		lblTexto.setBounds(94, 54, 46, 14);
		contentPane.add(lblTexto);
		
		JLabel lblNomeDoParlamentar = new JLabel("Parlamentar:");
		lblNomeDoParlamentar.setBounds(50, 87, 83, 14);
		contentPane.add(lblNomeDoParlamentar);
		
		JLabel lblClasse = new JLabel("Classe:");
		lblClasse.setBounds(87, 118, 46, 14);
		contentPane.add(lblClasse);
		
		JLabel lblEntreAsDatas = new JLabel("Entre as datas:");
		lblEntreAsDatas.setBounds(45, 155, 94, 14);
		contentPane.add(lblEntreAsDatas);
		
		JLabel lblEntreAsRelavncias = new JLabel("Entre as relav\u00E2ncias:");
		lblEntreAsRelavncias.setBounds(10, 185, 123, 14);
		contentPane.add(lblEntreAsRelavncias);
		
		textFieldTitulo = new JTextField();
		textFieldTitulo.setBounds(132, 18, 463, 20);
		contentPane.add(textFieldTitulo);
		textFieldTitulo.setColumns(10);
		
		textFieldTexto = new JTextField();
		textFieldTexto.setBounds(132, 51, 463, 20);
		contentPane.add(textFieldTexto);
		textFieldTexto.setColumns(10);
		
		final JComboBox comboBoxParlamentar = new JComboBox();
		comboBoxParlamentar.setBounds(132, 84, 209, 20);
		contentPane.add(comboBoxParlamentar);
		MapeadorDeputado mapDep = new MapeadorDeputado();
		Collection<String> deputados = new ArrayList<String>();
		deputados.add("Qualquer");
		deputados.addAll(mapDep.getDeputados());
		comboBoxParlamentar.setModel(new DefaultComboBoxModel(deputados.toArray()));
		
		final JComboBox comboBoxClasse = new JComboBox();
		comboBoxClasse.setBounds(132, 118, 209, 20);
		contentPane.add(comboBoxClasse);
		MapeadorClasses mapClasses = new MapeadorClasses();
		Collection<String> classes = new ArrayList<String>();
		classes.add("Qualquer");
		classes.addAll(mapClasses.getClasses());
		comboBoxClasse.setModel(new DefaultComboBoxModel(classes.toArray()));
		
		final JDateChooser campoDataInicio = new JDateChooser();
		campoDataInicio.setBounds(132, 152, 123, 20);
		contentPane.add(campoDataInicio);
		
		final JDateChooser campoDataFim = new JDateChooser();
		campoDataFim.setBounds(274, 152, 123, 20);
		contentPane.add(campoDataFim);
		
		
		final JComboBox comboBoxRelevancia2 = new JComboBox();
		comboBoxRelevancia2.setBounds(191, 182, 40, 20);
		comboBoxRelevancia2.setSelectedIndex(-1);
		contentPane.add(comboBoxRelevancia2);
				
		final JComboBox comboBoxRelevancia1 = new JComboBox();
		comboBoxRelevancia1.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				if (comboBoxRelevancia1.getSelectedIndex()>=0){
					Collection<Integer> valores = new ArrayList<Integer>();
					for (int i = comboBoxRelevancia1.getSelectedIndex()+1; i <= 5; i++)
						valores.add(i);
					comboBoxRelevancia2.setModel(new DefaultComboBoxModel(valores.toArray()));
			    }
			}
		});
		comboBoxRelevancia1.setModel(new DefaultComboBoxModel(new String[] {"1", "2", "3", "4", "5"}));
		comboBoxRelevancia1.setBounds(132, 182, 40, 20);
		comboBoxRelevancia1.setSelectedIndex(-1);
		contentPane.add(comboBoxRelevancia1);
		
		JLabel lblE = new JLabel("e");
		lblE.setBounds(178, 185, 11, 14);
		contentPane.add(lblE);
		
		final List listaNoticias = new List();
		listaNoticias.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				
				try {
					noticiaMBean.selecionarNoticia(listaNoticias.getSelectedIndex());
					EspecificacaoNoticia espNot;
					espNot = new EspecificacaoNoticia(noticiaMBean.getNoticia());
					espNot.setVisible(true);
					espNot.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		});
		listaNoticias.setBounds(30, 254, 728, 171);
		contentPane.add(listaNoticias);
		
		JButton btnBuscar = new JButton("Buscar");
		btnBuscar.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				
				listaNoticias.clear();
				
				Integer idDeputado = (comboBoxParlamentar.getSelectedIndex() <= 0 ? null : comboBoxParlamentar.getSelectedIndex()-1);
				String titulo = textFieldTitulo.getText().length() == 0 ? null : textFieldTitulo.getText();
				String texto = textFieldTexto.getText().length() == 0 ? null : textFieldTexto.getText();
				Integer idClasse =  (comboBoxClasse.getSelectedIndex() <= 0 ? null : comboBoxClasse.getSelectedIndex());
				
				boolean pesquisarPorRelavancia = ( comboBoxRelevancia1.getSelectedIndex() >= 0 ) && ( comboBoxRelevancia2.getSelectedIndex() >= 0 );
				Integer relevanciaInicio = (pesquisarPorRelavancia ? Integer.getInteger(comboBoxRelevancia1.getSelectedItem().toString()) : null);
				Integer relevanciaFim =  (pesquisarPorRelavancia ? Integer.getInteger(comboBoxRelevancia2.getSelectedItem().toString()) : null);
				
				boolean pesquisarPorData = (campoDataInicio.getDate() != null) && (campoDataFim.getDate() != null);
				if (pesquisarPorData && (campoDataInicio.getDate().compareTo(campoDataFim.getDate()) > 0)){
					JOptionPane.showMessageDialog(null, "Intervalo de datas inválido. Datas não serão consideradas");
				    pesquisarPorData = false;
				}
				Date dataInicio = pesquisarPorData ? campoDataInicio.getDate() : null; 
				Date dataFim = pesquisarPorData ? campoDataFim.getDate() : null; 				
				
				//try {
					//noticiaMBean.buscar(idDeputado, titulo, texto, idClasse, null, dataInicio, dataFim, relevanciaInicio, relevanciaFim);
				//} catch (SQLException e) {
				//	// TODO Auto-generated catch block
				//	e.printStackTrace();
				//}
				
				Collection<Noticia> noticias = noticiaMBean.getNoticias();
				
				if (noticias.isEmpty()){
					
					JOptionPane.showMessageDialog(null, "Nenhuma notícia encontrada");
					
				} else
					for (Noticia n : noticias){
						listaNoticias.add(Formatador.formatarData(n.getData())+" - "+n.getTitulo());
			}
		}});
		btnBuscar.setBounds(324, 215, 89, 23);
		contentPane.add(btnBuscar);
		
		JLabel lblE_1 = new JLabel("e");
		lblE_1.setBounds(264, 155, 22, 14);
		contentPane.add(lblE_1);
		
		
			
		
	}
}
