package tcc.interfaces;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import tcc.arquitetura.Formatador;
import tcc.dominio.Noticia;
import tcc.negocio.NoticiaMBean;

import java.awt.List;
import java.sql.SQLException;
import java.util.Collection;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JLabel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class TelaPrincipal extends JFrame {

	private JPanel contentPane;
	private NoticiaMBean noticiaMBean;

	private final List list;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					TelaPrincipal frame = new TelaPrincipal();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 * @throws SQLException 
	 * @throws ClassNotFoundException 
	 */
	public TelaPrincipal() throws ClassNotFoundException, SQLException {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 687, 429);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(null);
		setContentPane(contentPane);
		
		try {
			noticiaMBean = new NoticiaMBean();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		list = new List();
		list.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				
				try {
					noticiaMBean.selecionarNoticia(list.getSelectedIndex());
					EspecificacaoNoticia espNot;
					noticiaMBean.getNoticia().setLida(true);
					noticiaMBean.persistirNoticia(noticiaMBean.getNoticia());
					espNot = new EspecificacaoNoticia(noticiaMBean.getNoticia());
					espNot.show();
					espNot.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
					buscarNaoLidas();
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
		list.setBounds(21,100,473,182);
		contentPane.add(list, BorderLayout.CENTER);
		
		JLabel lblNotciasNoLidas = new JLabel("Not\u00EDcias n\u00E3o lidas:");
		lblNotciasNoLidas.setBounds(21, 80, 473, 14);
		contentPane.add(lblNotciasNoLidas);
		
		JButton btnImportarNovasNotcias = new JButton("Importar Novas");
		btnImportarNovasNotcias.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				try {
					//noticiaMBean.buscarESalvarNoticiasMaisRecentes();
					buscarNaoLidas();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		btnImportarNovasNotcias.setBounds(511, 100, 140, 23);
		contentPane.add(btnImportarNovasNotcias);
		
		JButton btnBuscaNoticias = new JButton("Busca de Noticias");
		btnBuscaNoticias.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				BuscarNoticia buscarNoticia;
				try {
					buscarNoticia = new BuscarNoticia();
					buscarNoticia.setVisible(true);
					buscarNoticia.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		btnBuscaNoticias.setBounds(510, 127, 141, 23);
		contentPane.add(btnBuscaNoticias);
		
		JButton btnRelatorio = new JButton("Relatórios");
		btnRelatorio.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Relatorio relatorio;
				relatorio = new Relatorio();
				relatorio.setVisible(true);
				relatorio.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			}
		});
		btnRelatorio.setBounds(511, 154, 140, 23);
		contentPane.add(btnRelatorio);
		
		buscarNaoLidas();		
	    
	}
	
	private void buscarNaoLidas() throws SQLException{
		
		list.clear();
		//noticiaMBean.buscarNaoLidas();
	    Collection<Noticia> noticiasNaoLidas = noticiaMBean.getNoticias();
	    for (Noticia n : noticiasNaoLidas){
	    	list.add(Formatador.formatarData(n.getData())+" - "+n.getTitulo());
	    }
		
	}
}
