import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import javax.swing.JOptionPane;

class AppClient extends Frame implements ActionListener, Runnable {
	Frame f = new Frame("MODBUS PROTOCOL IMPLEMENTATION");
	byte buff[] = new byte[12];
	String str = "";
	Button b1, b2, b3, b4, b5, b6, b7;
	TextField tf1, tf2, tf3, tf4, tf5, tf6, tf7, tf8, tf9;
	TextArea ta;
	Label n, l2, l3, l4, l5, l6, l7, l8, l9, l10, l11, l12;
	BufferedReader br;
	PrintWriter pw;
	OutputStream outStream;
	InputStream instream;
	Socket S;
	Thread th;

	AppClient() {
		f.setBackground(new Color(120, 100, 200));
		f.setLayout(new GridLayout(9, 4, 20, 10));
		b1 = new Button("Connect");
		Font myFont1 = new Font("Jokerman", Font.PLAIN, 18);
		b1.setFont(myFont1);
		b1.setBackground(Color.green);
		b2 = new Button("Read1");
		Font myFont2 = new Font("Jokerman", Font.PLAIN, 15);
		b2.setFont(myFont2);
		b2.setBackground(Color.yellow);
		b3 = new Button("Read2");
		Font myFont3 = new Font("Jokerman", Font.PLAIN, 15);
		b3.setFont(myFont3);
		b3.setBackground(Color.yellow);
		b4 = new Button("Read3");
		Font myFont4 = new Font("Jokerman", Font.PLAIN, 15);
		b4.setFont(myFont4);
		b4.setBackground(Color.yellow);
		b5 = new Button("Read4");
		Font myFont5 = new Font("Jokerman", Font.PLAIN, 15);
		b5.setFont(myFont5);
		b5.setBackground(Color.yellow);
		b6 = new Button("Clear");
		Font myFont6 = new Font("Jokerman", Font.PLAIN, 18);
		b6.setFont(myFont6);
		b6.setBackground(Color.magenta);
		b7 = new Button("Exit");
		Font myFont7 = new Font("Jokerman", Font.PLAIN, 18);
		b7.setFont(myFont7);
		b7.setBackground(Color.red);
		tf1 = new TextField(20);
		tf2 = new TextField(20);
		tf3 = new TextField(20);
		tf4 = new TextField(20);
		tf5 = new TextField(20);
		tf6 = new TextField(20);
		tf7 = new TextField(20);
		tf8 = new TextField(20);
		tf9 = new TextField(20);
		ta = new TextArea(20, 49);
		l2 = new Label("Ip-Address:", Label.CENTER);
		l2.setFont(new Font("Jokerman", Font.PLAIN, 22));
		l3 = new Label("Default Port:502", Label.CENTER);
		l3.setFont(new Font("Jokerman", Font.BOLD, 22));
		l3.setBackground(Color.cyan);
		l4 = new Label("READ", Label.CENTER);
		l4.setFont(new Font("Jokerman", Font.BOLD, 22));
		l4.setBackground(Color.orange);
		l5 = new Label("INPUT", Label.CENTER);
		l5.setFont(new Font("Jokerman", Font.BOLD, 22));
		l5.setBackground(Color.orange);
		l6 = new Label("START", Label.CENTER);
		l6.setFont(new Font("Jokerman", Font.BOLD, 22));
		l6.setBackground(Color.orange);
		l7 = new Label("LENGTH", Label.CENTER);
		l7.setFont(new Font("Jokerman", Font.BOLD, 22));
		l7.setBackground(Color.orange);
		l8 = new Label("Status Register:", Label.CENTER);
		l8.setFont(new Font("Jokerman", Font.PLAIN, 22));
		l9 = new Label("Coil Register:", Label.CENTER);
		l9.setFont(new Font("Jokerman", Font.PLAIN, 22));
		l10 = new Label("Input Register:", Label.CENTER);
		l10.setFont(new Font("Jokerman", Font.PLAIN, 22));
		l11 = new Label("Holding Register:", Label.CENTER);
		l11.setFont(new Font("Jokerman", Font.PLAIN, 22));
		l12 = new Label("TEXT AREA:", Label.CENTER);
		l12.setFont(new Font("Jokerman", Font.BOLD, 22));
		l12.setBackground(Color.orange);
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
		Date date = new Date();
		n = new Label(dateFormat.format(date));
		n.setFont(new Font("Jokerman", Font.BOLD, 18));
		b1.addActionListener(this);
		b2.addActionListener(this);
		b3.addActionListener(this);
		b4.addActionListener(this);
		b5.addActionListener(this);
		b6.addActionListener(this);
		b7.addActionListener(this);
		f.addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(java.awt.event.WindowEvent windowEvent) {
				if (JOptionPane.showConfirmDialog(f, "Are you sure to close this window?", "Really Closing?",
						JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
					System.exit(0);
				}
			}
		});
		f.add(l2);
		f.add(tf1);
		f.add(l3);
		f.add(b1);
		f.add(l5);
		f.add(l6);
		f.add(l7);
		f.add(l4);
		f.add(l8);
		f.add(tf2);
		f.add(tf3);
		f.add(b2);
		f.add(l9);
		f.add(tf4);
		f.add(tf5);
		f.add(b3);
		f.add(l10);
		f.add(tf6);
		f.add(tf7);
		f.add(b4);
		f.add(l11);
		f.add(tf8);
		f.add(tf9);
		f.add(b5);
		f.add(l12);
		f.add(ta);
		f.add(b6);
		f.add(b7);
		f.add(n);
		th = new Thread(this);
		th.setDaemon(true);
		th.start();
		f.setSize(1000, 400);
		f.setLocation(100, 100);
		f.setVisible(true);
		f.validate();
		f.setResizable(false);// Removes Maximise Button
	}

	public void actionPerformed(ActionEvent ae) {
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();
		n.setText(dateFormat.format(date));
		String str = ae.getActionCommand();
		if (str.equals("Connect")) {
			if (validateIPAddress(tf1.getText())) {
				try {
					ta.setText("Connected");
					S = new Socket(InetAddress.getByName(tf1.getText()), 502);
					instream = S.getInputStream();
					outStream = S.getOutputStream();
				} catch (Exception e) {
				}
				b1.setLabel("Disconnect");
				b1.setBackground(Color.red);
			} else {
				JOptionPane.showMessageDialog(f, "Please Enter A Valid IP-Address", "Inane warning",
						JOptionPane.WARNING_MESSAGE);
			}
		} else if (str.equals("Disconnect")) {
			try {
				ta.setText("Connection closed");
				tf1.setText("");
				S.close();
			} catch (Exception e) {
			}
			b1.setLabel("Connect");
			b1.setBackground(Color.green);
		} else if (str.equals("Read1")) {
			if (b1.getLabel().equals("Disconnect")) {
				if (validateTF(tf2.getText()) && validateTF(tf3.getText())) {
					for (int i = 0; i < 5; i++)
						buff[i] = 0;
					buff[5] = 6;
					buff[6] = 0;
					buff[7] = 2;
					buff[8] = (byte) MSB(tf2.getText());
					buff[9] = (byte) (LSB(tf2.getText()) - 1);
					buff[10] = (byte) MSB(tf3.getText());
					buff[11] = (byte) LSB(tf3.getText());
					try {
						outStream.write(buff);
					} catch (Exception ex) {
					}
				} else {
					JOptionPane.showMessageDialog(f, "Please Enter A Valid Start/Length Value", "Inane warning",
							JOptionPane.WARNING_MESSAGE);
				}
			} else {
				JOptionPane.showMessageDialog(f, "Please Connect To A Server", "Inane warning",
						JOptionPane.WARNING_MESSAGE);
			}
		} else if (str.equals("Read2")) {
			if (b1.getLabel().equals("Disconnect")) {
				if (validateTF(tf4.getText()) && validateTF(tf5.getText())) {
					for (int i = 0; i < 5; i++)
						buff[i] = 0;
					buff[5] = 6;
					buff[6] = 0;
					buff[7] = 1;
					buff[8] = (byte) MSB(tf4.getText());
					buff[9] = (byte) (LSB(tf4.getText()) - 1);
					buff[10] = (byte) MSB(tf5.getText());
					buff[11] = (byte) LSB(tf5.getText());
					try {
						outStream.write(buff);
					} catch (Exception ex) {
					}
				} else {
					JOptionPane.showMessageDialog(f, "Please Enter A Valid Start/Length Value", "Inane warning",
							JOptionPane.WARNING_MESSAGE);
				}
			} else {
				JOptionPane.showMessageDialog(f, "Please Connect To A Server", "Inane warning",
						JOptionPane.WARNING_MESSAGE);
			}
		} else if (str.equals("Read3")) {
			if (b1.getLabel().equals("Disconnect")) {
				if (validateTF(tf6.getText()) && validateTF(tf7.getText())) {
					for (int i = 0; i < 5; i++)
						buff[i] = 0;
					buff[5] = 6;
					buff[6] = 0;
					buff[7] = 4;
					buff[8] = (byte) MSB(tf6.getText());
					buff[9] = (byte) (LSB(tf6.getText()) - 1);
					buff[10] = (byte) MSB(tf7.getText());
					buff[11] = (byte) LSB(tf7.getText());
					try {
						outStream.write(buff);
					} catch (Exception ex) {
					}
				} else {
					JOptionPane.showMessageDialog(f, "Please Enter A Valid Start/Length Value", "Inane warning",
							JOptionPane.WARNING_MESSAGE);
				}
			} else {
				JOptionPane.showMessageDialog(f, "Please Connect To A Server", "Inane warning",
						JOptionPane.WARNING_MESSAGE);
			}
		} else if (str.equals("Read4")) {
			if (b1.getLabel().equals("Disconnect")) {
				if (validateTF(tf8.getText()) && validateTF(tf9.getText())) {
					for (int i = 0; i < 5; i++)
						buff[i] = 0;
					buff[5] = 6;
					buff[6] = 0;
					buff[7] = 3;
					buff[8] = (byte) MSB(tf8.getText());
					buff[9] = (byte) (LSB(tf8.getText()) - 1);
					buff[10] = (byte) MSB(tf9.getText());
					buff[11] = (byte) LSB(tf9.getText());
					try {
						outStream.write(buff);
					} catch (Exception ex) {
					}
				} else {
					JOptionPane.showMessageDialog(f, "Please Enter A Valid Start/Length Value", "Inane warning",
							JOptionPane.WARNING_MESSAGE);
				}
			} else {
				JOptionPane.showMessageDialog(f, "Please Connect To A Server", "Inane warning",
						JOptionPane.WARNING_MESSAGE);
			}
		} else if (str.equals("Clear")) {
			ta.setText(" ");
		} else if (str.equals("Exit")) {
			if (JOptionPane.showConfirmDialog(f, "Are you sure to close this window?", "Really Closing?",
					JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION)
				System.exit(0);
		}
	}

	public void run() {
		while (true) {
			try {
				byte[] buffer = new byte[100];
				int[] b1 = new int[100];
				int noofBytes = instream.read(buffer);
				int[] b2 = new int[100];
				if ((noofBytes > 0)) {
					ta.setText("");
					for (int i = 0; i < noofBytes; i++)
						ta.append(buffer[i] + ",");
					int k = 0;
					if (buff[7] == 2 || buff[7] == 1) {
						for (int buffindex = 9; buffindex < noofBytes; buffindex++) {
							b1[k] = buffer[buffindex];
							int x = b1[k];
							int y = 0, z = 0;
							int binary[] = new int[8];
							if (x == 0) {
								for (y = 0; y < 8; y++)
									binary[y] = 0;
							}
							while (x > 0) {
								int rem = x % 2;
								binary[y] = rem;
								x = x / 2;
								y++;
							}
							for (z = 0; z < 8; z++)
								ta.append(binary[z] + ",");
							k++;
						}
					} else if (buff[7] == 4 || buff[7] == 3) {
						k = 0;
						int l = 0;
						for (int buffindex = 9; buffindex < noofBytes; buffindex++) {
							b1[k] = buffer[buffindex];
							k++;
						}
						int i = 0;
						while (i < k) {
							b2[l] = (b1[i] * 256) + b1[i + 1];
							i = i + 2;
							ta.append(b2[l] + ",");
							l++;
						}
					}
				}
			} catch (Exception e) {
			}
		}
	}

	public boolean validateIPAddress(String ipAddress) {
		String tokens[] = ipAddress.split("\\.");
		if (tokens.length != 4)
			return false;
		for (int i = 0; i < 4; i++) {
			for (char c : tokens[i].toCharArray()) {
				int value = (int) c;
				if ((value >= 65 && value <= 90) || (value >= 97 && value <= 122)) {
					return false;
				}
			}
		}
		if (ipAddress == null)
			return false;
		for (String str : tokens) {
			int i = Integer.parseInt(str);
			if ((i < 0) || (i > 255))
				return false;
		}
		return true;
	}

	public boolean validateTF(String text) {
		for (char c : text.toCharArray()) {
			int value = (int) c;
			if ((value >= 65 && value <= 90) || (value >= 97 && value <= 122)) {
				return false;
			}
		}
		if (text.trim().isEmpty())
			return false;
		int i = Integer.parseInt(text);
		if ((i < 1) || (i > 256))
			return false;
		return true;
	}

	public int MSB(String tf) {
		int n = Integer.parseInt(tf);
		int i, k = 0, sum = 0;
		int binary[] = new int[16];
		int msb[] = new int[8];
		int val[] = new int[8];
		if (n == 0) {
			for (i = 0; i < 16; i++)
				binary[i] = 0;
		}
		i = 15;
		while (n > 0) {
			int rem = n % 2;
			binary[i] = rem;
			n = n / 2;
			i--;
		}
		for (i = 0; i < 8; i++)
			msb[i] = binary[i];
		for (int j = 7; j >= 0; j--) {
			val[k] = (int) Math.pow(2, j);
			k++;
		}
		for (i = 0; i < 8; i++)
			if (msb[i] == 1)
				sum = sum + val[i];
		return sum;
	}

	public int LSB(String tf) {
		int n = Integer.parseInt(tf);
		int i, k = 0, sum = 0;
		int binary[] = new int[16];
		int lsb[] = new int[8];
		int val[] = new int[8];
		if (n == 0) {
			for (i = 0; i < 16; i++)
				binary[i] = 0;
		}
		i = 15;
		while (n > 0) {
			int rem = n % 2;
			binary[i] = rem;
			n = n / 2;
			i--;
		}
		for (i = 8; i < 16; i++) {
			lsb[k] = binary[i];
			k++;
		}
		k = 0;
		for (int j = 7; j >= 0; j--) {
			val[k] = (int) Math.pow(2, j);
			k++;
		}
		for (i = 0; i < 8; i++)
			if (lsb[i] == 1)
				sum = sum + val[i];
		return sum;
	}

	public static void main(String args[]) throws IOException {
		new AppClient();
	}
}