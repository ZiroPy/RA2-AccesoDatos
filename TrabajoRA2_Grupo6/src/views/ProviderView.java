package views;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import models.Provider;
import services.ProviderServices;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.Font;

public class ProviderView extends JFrame {

	private static final long serialVersionUID = 1L;
	private JLabel lblTitle;
	private JTextField textName;
	private JTextField textDescription;
	private JTextField textAddress;
	private JTextField textPhone;
	private JButton btnBack;
	private JButton btnEdit;
	private JButton btnSave;
	private Listener l=new Listener();
	private Provider provider = null;
	private int id = 0;

	public ProviderView(int id) {
		
		setBounds(100, 100, 422, 318);
		getContentPane().setLayout(null);
		InterfaceModel.FrameModel(this, "New provider");
		this.id = id;
		
		lblTitle = new JLabel("New provider");
		lblTitle.setFont(new Font("Arial", Font.BOLD, 14));
		lblTitle.setBounds(157, 31, 107, 20);
		getContentPane().add(lblTitle);
		
		JLabel lblName = new JLabel("Name");
		lblName.setFont(new Font("Arial", Font.PLAIN, 14));
		lblName.setBounds(82, 75, 46, 14);
		getContentPane().add(lblName);
		
		JLabel lblDescription = new JLabel("Description");
		lblDescription.setFont(new Font("Arial", Font.PLAIN, 14));
		lblDescription.setBounds(82, 109, 78, 14);
		getContentPane().add(lblDescription);
		
		JLabel lblAddress = new JLabel("Address");
		lblAddress.setFont(new Font("Arial", Font.PLAIN, 14));
		lblAddress.setBounds(82, 145, 78, 14);
		getContentPane().add(lblAddress);
		
		JLabel lblPhone = new JLabel("Phone");
		lblPhone.setFont(new Font("Arial", Font.PLAIN, 14));
		lblPhone.setBounds(82, 180, 78, 14);
		getContentPane().add(lblPhone);
		
		textName = new JTextField();
		textName.setBounds(170, 73, 138, 20);
		getContentPane().add(textName);
		textName.setColumns(10);
		
		textDescription = new JTextField();
		textDescription.setColumns(10);
		textDescription.setBounds(170, 107, 138, 20);
		getContentPane().add(textDescription);
		
		textAddress = new JTextField();
		textAddress.setColumns(10);
		textAddress.setBounds(170, 143, 138, 20);
		getContentPane().add(textAddress);
		
		textPhone = new JTextField();
		textPhone.setColumns(10);
		textPhone.setBounds(170, 178, 138, 20);
		getContentPane().add(textPhone);
		
		ImageIcon back=new ImageIcon("resources/icon/back.png");
		btnBack = new JButton(back);
		btnBack.setToolTipText("Back");
		btnBack.setBounds(50, 233, 35, 35);
		getContentPane().add(btnBack);
		btnBack.addActionListener(l);
		
		ImageIcon edit=new ImageIcon("resources/icon/update.png");
		btnEdit = new JButton(edit);
		btnEdit.setToolTipText("Edit");
		btnEdit.setBounds(261, 218, 47, 47);
		getContentPane().add(btnEdit);
		btnEdit.addActionListener(l);
		btnEdit.setVisible(false);
		
		ImageIcon save=new ImageIcon("resources/icon/save.png");
		btnSave = new JButton(save);
		btnSave.setToolTipText("Save");
		btnSave.setBounds(261, 218, 47, 47);
		getContentPane().add(btnSave);
		btnSave.addActionListener(l);
		
		if(id!=-1) { //if we are updating a provider we get it from the database
			provider = ProviderServices.selectProvider("id", id).get(0);
			setProviderValues(ProviderServices.selectProvider("id", id).get(0));
		}
		
	}
	
	private void setProviderValues(Provider p) { //if we are updating a provider we set the camps and buttons for it
		setTitle(p.getName());
		lblTitle.setText(p.getName());
		btnEdit.setVisible(true);
		btnSave.setVisible(false);
		textName.setText(p.getName());
		textName.setEditable(false);
		textDescription.setText(p.getDescription());
		textDescription.setEditable(false);
		textAddress.setText(p.getAddress());
		textAddress.setEditable(false);
		textPhone.setText(p.getPhone());
		textPhone.setEditable(false);
	}

	private class Listener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			Object o=e.getSource();
			
			if(o.equals(btnBack)) {
				goBack();
				
			}else if(o.equals(btnEdit)) {
				
				btnSave.setVisible(true);
				btnEdit.setVisible(false);
				textName.setEditable(true);
				textDescription.setEditable(true);
				textAddress.setEditable(true);
				textPhone.setEditable(true);
			}else if(o.equals(btnSave)) {
				
				if(matchesProviderFields()) { //we see if the camps are the formats we need
					if(id!=-1) {//if we are updating a provider we update it form the database directly
						if(ProviderServices.updateProvider(id, textName.getText(), textDescription.getText(), textAddress.getText(), textPhone.getText(),1)) {
							JOptionPane.showMessageDialog(ProviderView.this, "Provider updated");
						}else {
							JOptionPane.showMessageDialog(ProviderView.this, "Error");
						}
						goBack();
							
					}else {//if we are creating a provider we first check it it exists in the database with the select below
						
						if(ProviderServices.selectProvider("name", textName.getText()).isEmpty())
							provider = null;
						else
							provider=ProviderServices.selectProvider("name", textName.getText()).get(0);
						
						int op = operationToDo(provider); //now we set the operation we need based on the state of that provider in the database
						if(op==-1) { //if it not exists we create a new one
							if(ProviderServices.insertProvider(new Provider(textName.getText(), textDescription.getText(), textAddress.getText(), textPhone.getText()))) {
								JOptionPane.showMessageDialog(ProviderView.this, "Provider inserted");
							}else {
								JOptionPane.showMessageDialog(ProviderView.this, "Error inserting provider");
							}
						}else if(op==0){ //if it exists and it deactivated we update it
							if(ProviderServices.updateProvider(provider.getId(), textName.getText(), textDescription.getText(), textAddress.getText(), textPhone.getText(), 1))
								JOptionPane.showMessageDialog(ProviderView.this, "Provider inserted");
							else
								JOptionPane.showMessageDialog(ProviderView.this, "Error inserting provider");
						}else { //if it exists and is activated we cant create it again
							JOptionPane.showMessageDialog(ProviderView.this, "That provider already exists");
						}
						textName.setText("");
						textDescription.setText("");
						textAddress.setText("");
						textPhone.setText("");
						
					}
				}
			}
			
		}
		private void goBack() {
			
			dispose();
			ListProvidersView lpv=new ListProvidersView();
			lpv.setVisible(true);
		}
		private boolean matchesProviderFields() { //matches for the camp we introduce according to the db
			if(!textName.getText().matches("^.{1,30}$")) {
				JOptionPane.showMessageDialog(ProviderView.this, "Name length should be between 1 and 30 characters");
				return false;
			}else if(!textDescription.getText().matches("^.{1,80}$")) {
				JOptionPane.showMessageDialog(ProviderView.this, "Description length should be between 1 and 80 characters");
				return false;
			}else if(!textAddress.getText().matches("^.{1,70}$")) {
				JOptionPane.showMessageDialog(ProviderView.this, "Address length should be between 1 and 70 characters");
				return false;
			}else if(!textPhone.getText().matches("^[1-9]\\d{8}$")) {
				JOptionPane.showMessageDialog(ProviderView.this, "The phone has to be 9 digits long and it cant start with 0");
				return false;
			}
			return true;
		}
		
		private int operationToDo(Provider provider){ // short method to know which operation we have to do when creating a provider
			
			if(provider!=null) {
				if(provider.getActive()==1)
					return 1;
				else
					return 0;
			}else
				return -1;
			
		}
	}
	
}
