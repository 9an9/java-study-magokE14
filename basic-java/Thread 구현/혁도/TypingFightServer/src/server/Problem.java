package server;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Problem {
	private List<String> problemList;
	private int currentIndex;
	
	public Problem() {
//		String[] str = {
//			"��������", "��������",	"��������", "��������",
//			"��������", "��������", "��������", "��������",
//			"��������", "��������", "��������", "��������"
//		};
		
		String[] str = {
			"ȣ����", "���", "��", "�Ȱ�", "����", "����", "�����ٶ� ���ٻ�", "����������������������"
		};
		
		problemList = new ArrayList<>(Arrays.asList(str));
		Collections.shuffle(problemList);
		
		currentIndex = 0;
	}
	
	public List<String> getProblemList(){
		return problemList;
	}
	
	public String getProblem() {
		return problemList.get(currentIndex);
	}
	
	public int getNumber() {
		return currentIndex + 1;
	}
	
	public void answer() {
		currentIndex++;
	}
	
	public boolean isFinish() {
		return (problemList.size() <= currentIndex);
	}
}
