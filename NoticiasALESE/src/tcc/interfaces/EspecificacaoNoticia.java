package tcc.interfaces;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

import tcc.dominio.Noticia;
import tcc.negocio.NoticiaMBean;
import tcc.arquitetura.Formatador;
import tcc.arquitetura.MapeadorDeputado;
import tcc.arquitetura.MapeadorClasses;
import javax.swing.JLabel;
import java.awt.Font;
import java.sql.SQLException;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import javax.swing.JButton;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JTextPane;
import java.awt.TextArea;

public class EspecificacaoNoticia extends JFrame {

	private Noticia noticia;
	
	private JPanel contentPane;

	/**
	 * Create the frame.
	 * @throws Exception 
	 */
	public EspecificacaoNoticia(final Noticia n) throws Exception {
		
		noticia = n;
		
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 724, 467);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(null);
		setContentPane(contentPane);
		
		JLabel labelTitulo = new JLabel(n.getTitulo());
		labelTitulo.setFont(new Font("Tahoma", Font.PLAIN, 18));
		labelTitulo.setBounds(10, 11, 688, 48);
		contentPane.add(labelTitulo);
		
		JLabel labelAutoria = new JLabel("Autoria: ");
		labelAutoria.setFont(new Font("Tahoma", Font.BOLD, 11));
		labelAutoria.setBounds(10, 70, 62, 14);
		contentPane.add(labelAutoria);
		
		JLabel labelData = new JLabel("Data: ");
		labelData.setFont(new Font("Tahoma", Font.BOLD, 11));
		labelData.setBounds(10, 95, 62, 14);
		contentPane.add(labelData);
		
		JLabel labelClasse = new JLabel("Classe: ");
		labelClasse.setFont(new Font("Tahoma", Font.BOLD, 11));
		labelClasse.setBounds(10, 120, 46, 14);
		contentPane.add(labelClasse);
		
		JLabel labelRelevancia = new JLabel("Relevância: ");
		labelRelevancia.setFont(new Font("Tahoma", Font.BOLD, 11));
		labelRelevancia.setBounds(10, 146, 79, 14);
		contentPane.add(labelRelevancia);
		
		MapeadorDeputado mapDep = new MapeadorDeputado();
		JLabel labelDescAutoria = new JLabel("Dep. "+ mapDep.getDeputado(n.getIdDeputado()));
		labelDescAutoria.setBounds(61, 70, 565, 14);
		contentPane.add(labelDescAutoria);
		
		JLabel labelDescdata = new JLabel(n.getData()==null?"Data Não Especificada":Formatador.formatarData(n.getData()));
		labelDescdata.setBounds(46, 95, 547, 14);
		contentPane.add(labelDescdata);
		
		MapeadorClasses mapClasse = new MapeadorClasses();
		JLabel labelDescClasse = new JLabel(mapClasse.getClasse(n.getClasse()));
		labelDescClasse.setBounds(56, 120, 447, 14);
		contentPane.add(labelDescClasse);
		
		TextArea textArea = new TextArea("", 4, 30, TextArea.SCROLLBARS_VERTICAL_ONLY);
		textArea.setBounds(25, 183, 635, 193);
		contentPane.add(textArea);
		textArea.setEditable(false);
		textArea.setText(n.getTexto()==null?"Sem texto":n.getTexto());
		
		
		
		final JComboBox comboRelevancia = new JComboBox();
		comboRelevancia.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				n.setRelevancia(comboRelevancia.getSelectedIndex()+1);
			}
		});
		comboRelevancia.setModel(new DefaultComboBoxModel(new String[] {"1", "2", "3", "4", "5"}));
		comboRelevancia.setMaximumRowCount(5);
		comboRelevancia.setToolTipText("");
		comboRelevancia.setBounds(85, 143, 38, 20);
		comboRelevancia.setSelectedIndex(n.getRelevancia()-1);
		contentPane.add(comboRelevancia);
		
		JButton btnSalvarNotcia = new JButton("Salvar Not\u00EDcia");
		btnSalvarNotcia.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				NoticiaMBean noticiaMBean;
				try {
					noticiaMBean = new NoticiaMBean();
					noticiaMBean.persistirNoticia(n);
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
		btnSalvarNotcia.setBounds(569, 395, 129, 23);
		contentPane.add(btnSalvarNotcia);
		
		
		
	
		
	}
}
