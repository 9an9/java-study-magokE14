package main;

import city.London;
import city.NewYork;
import city.Seoul;

public class Main {
	public static void main(String[] args) {
		// =================================================
		// ����
		// =================================================
		StringBuffer winSb = new StringBuffer();
		StringBuffer loseSb = new StringBuffer();
		int civilianCount = 20000;
		int villainCount = 100;
		winSb.append("==============================\n");
		winSb.append("���۸��� ������ ���߽��ϴ�.\n");
		winSb.append("==============================\n");
		loseSb.append("==============================\n");
		loseSb.append("�Ǵ翡 ���� ���￡ ȥ���� ã�ƿԽ���.\n");
		loseSb.append("==============================\n");
		Seoul seoul = new Seoul(civilianCount, villainCount, winSb.toString(), loseSb.toString());
		seoul.run();
		
		
		// =================================================
		// ����
		// =================================================
		winSb = new StringBuffer();
		loseSb = new StringBuffer();
		civilianCount = 20000;
		villainCount = 100;
		winSb.append("==============================\n");
		winSb.append("���۸��� ������ ���߽��ϴ�.\n");
		winSb.append("==============================\n");
		loseSb.append("==============================\n");
		loseSb.append("�Ǵ翡 ���� ������ ȥ���� ã�ƿԽ���.\n");
		loseSb.append("==============================\n");
		NewYork newYork = new NewYork(civilianCount, villainCount, winSb.toString(), loseSb.toString());
		newYork.run();
		
		
		// =================================================
		// ����
		// =================================================
		winSb = new StringBuffer();
		loseSb = new StringBuffer();
		civilianCount = 20000;
		villainCount = 100;
		winSb.append("==============================\n");
		winSb.append("���۸��� ������ ���߽��ϴ�.\n");
		winSb.append("==============================\n");
		loseSb.append("==============================\n");
		loseSb.append("�Ǵ翡 ���� ������ ȥ���� ã�ƿԽ���.\n");
		loseSb.append("==============================\n");
		London london = new London(civilianCount, villainCount, winSb.toString(), loseSb.toString());
		london.run();
		
	}
}
